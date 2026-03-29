package com.shoppingcart.controller;

import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.ShoppingCart;
import com.shoppingcart.util.LocaleManager;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
public class ShoppingCartController implements Initializable {

    // ── Top bar
    @FXML private Label  lblTitle;
    @FXML private Label  lblSelectLanguage;
    @FXML private ComboBox<String> cbLanguage;

    // ── Item count
    @FXML private Label     lblNumItems;
    @FXML private Spinner<Integer> spnNumItems;
    @FXML private Button    btnSetItems;

    // ── Dynamic item grid 
    @FXML private ScrollPane scrollPane;
    @FXML private VBox       itemsContainer;

    // ── Results 
    @FXML private Button btnCalculate;
    @FXML private Button btnClear;
    @FXML private Label  lblTotalCost;
    @FXML private Label  lblTotalValue;
    @FXML private VBox   resultsBox;

    // ── Internal state
    private final ShoppingCart cart = new ShoppingCart();

    private final List<TextField> priceFields    = new ArrayList<>();
    private final List<TextField> quantityFields = new ArrayList<>();
    private final List<Label>     itemLabels     = new ArrayList<>();
    private final List<Label>     priceLabelsList = new ArrayList<>();
    private final List<Label>     qtyLabelsList   = new ArrayList<>();

    private static final String[] LANGUAGE_DISPLAY = {
            "English", "Suomi (Finnish)", "Svenska (Swedish)",
            "日本語 (Japanese)", "العربية (Arabic)"
    };
    private static final Locale[] LOCALES = {
            LocaleManager.ENGLISH, LocaleManager.FINNISH, LocaleManager.SWEDISH,
            LocaleManager.JAPANESE, LocaleManager.ARABIC
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate language ComboBox
        cbLanguage.getItems().addAll(LANGUAGE_DISPLAY);
        cbLanguage.getSelectionModel().selectFirst();
        cbLanguage.setOnAction(e -> onLanguageChange());

        // Spinner: 1–20 items, default 3
        SpinnerValueFactory<Integer> factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 3);
        spnNumItems.setValueFactory(factory);

        applyLanguage();
        buildItemRows(spnNumItems.getValue());
    }

    //  Language switching
    private void onLanguageChange() {
        int idx = cbLanguage.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < LOCALES.length) {
            LocaleManager.setLocale(LOCALES[idx]);
            applyLanguage();
            refreshItemRowLabels();
            clearResults();
        }
    }

    private void applyLanguage() {
        lblTitle.setText(LocaleManager.getString("app.title"));
        lblSelectLanguage.setText(LocaleManager.getString("label.selectLanguage"));
        lblNumItems.setText(LocaleManager.getString("label.numItems"));
        btnSetItems.setText(LocaleManager.getString("button.setItems"));
        btnCalculate.setText(LocaleManager.getString("button.calculate"));
        btnClear.setText(LocaleManager.getString("button.clear"));
        lblTotalCost.setText(LocaleManager.getString("label.totalCost"));

        Platform.runLater(this::applyOrientation);
    }

    private void applyOrientation() {
        if (lblTitle.getScene() != null) {
            NodeOrientation orientation = LocaleManager.isRTL()
                    ? NodeOrientation.RIGHT_TO_LEFT
                    : NodeOrientation.LEFT_TO_RIGHT;
            lblTitle.getScene().getRoot().setNodeOrientation(orientation);
        }
    }

    @FXML
    private void onSetItems() {
        int count = spnNumItems.getValue();
        buildItemRows(count);
        clearResults();
    }

    private void buildItemRows(int count) {
        itemsContainer.getChildren().clear();
        priceFields.clear();
        quantityFields.clear();
        itemLabels.clear();
        priceLabelsList.clear();
        qtyLabelsList.clear();

        for (int i = 1; i <= count; i++) {
            HBox row = createItemRow(i);
            itemsContainer.getChildren().add(row);
        }
    }

    private HBox createItemRow(int index) {
        HBox row = new HBox(12);
        row.getStyleClass().add("item-row");

        // Item number label
        Label lblItem = new Label(
                LocaleManager.getString("label.item") + " " + index + ":");
        lblItem.getStyleClass().add("item-label");
        lblItem.setPrefWidth(80);
        itemLabels.add(lblItem);

        // Price label + field
        Label lblPrice = new Label(LocaleManager.getString("label.price"));
        lblPrice.getStyleClass().add("field-label");
        priceLabelsList.add(lblPrice);

        TextField tfPrice = new TextField("0.00");
        tfPrice.getStyleClass().add("price-field");
        tfPrice.setPrefWidth(100);
        priceFields.add(tfPrice);

        // Quantity label + field
        Label lblQty = new Label(LocaleManager.getString("label.quantity"));
        lblQty.getStyleClass().add("field-label");
        qtyLabelsList.add(lblQty);

        TextField tfQty = new TextField("1");
        tfQty.getStyleClass().add("qty-field");
        tfQty.setPrefWidth(80);
        quantityFields.add(tfQty);

        row.getChildren().addAll(lblItem, lblPrice, tfPrice, lblQty, tfQty);
        return row;
    }

    private void refreshItemRowLabels() {
        for (int i = 0; i < itemLabels.size(); i++) {
            itemLabels.get(i).setText(
                    LocaleManager.getString("label.item") + " " + (i + 1) + ":");
        }
        for (Label l : priceLabelsList) {
            l.setText(LocaleManager.getString("label.price"));
        }
        for (Label l : qtyLabelsList) {
            l.setText(LocaleManager.getString("label.quantity"));
        }
    }
    
    //  Calculation
    @FXML
    private void onCalculate() {
        cart.clear();
        resultsBox.getChildren().clear();

        boolean hasError = false;

        for (int i = 0; i < priceFields.size(); i++) {
            String priceText = priceFields.get(i).getText().trim();
            String qtyText   = quantityFields.get(i).getText().trim();

            try {
                double price = Double.parseDouble(priceText);
                int    qty   = Integer.parseInt(qtyText);

                if (price < 0 || qty < 0) {
                    showAlert(LocaleManager.getString("error.negativeValue"));
                    hasError = true;
                    break;
                }

                CartItem item = new CartItem(
                        LocaleManager.getString("label.item") + " " + (i + 1),
                        price, qty);
                cart.addItem(item);

                // Per-item result row
                HBox resultRow = new HBox(10);
                resultRow.getStyleClass().add("result-row");

                Label nameLabel = new Label(item.getName() + ":");
                nameLabel.getStyleClass().add("result-item-name");

                Label calcLabel = new Label(
                        String.format("%.2f × %d = %.2f", price, qty, item.getItemTotal()));
                calcLabel.getStyleClass().add("result-calc");

                resultRow.getChildren().addAll(nameLabel, calcLabel);
                resultsBox.getChildren().add(resultRow);

            } catch (NumberFormatException ex) {
                showAlert(LocaleManager.getString("error.invalidInput") + " (Item " + (i + 1) + ")");
                hasError = true;
                break;
            }
        }

        if (!hasError) {
            // Divider
            Separator sep = new Separator();
            sep.getStyleClass().add("result-separator");
            resultsBox.getChildren().add(sep);

            // Total
            lblTotalValue.setText(String.format("%.2f €", cart.getTotalCost()));
        }
    }

    @FXML
    private void onClear() {
        for (TextField tf : priceFields)    tf.setText("0.00");
        for (TextField tf : quantityFields) tf.setText("1");
        clearResults();
    }

    private void clearResults() {
        resultsBox.getChildren().clear();
        lblTotalValue.setText("—");
    }

    //  Helpers

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LocaleManager.getString("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
