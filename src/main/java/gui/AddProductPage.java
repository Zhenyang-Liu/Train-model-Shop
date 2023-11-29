package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import model.*;
import service.ProductService;
import DAO.*;

public class AddProductPage extends JDialog {
    private JTextField productNameField, productCodeField, brandNameField, retailPriceField, stockQuantityField;
    private JTextArea descriptionField;
    private JComboBox<String> productTypeComboBox, gaugeComboBox, dccComboBox, compartmentComboBox, digitalComboBox;
    private JPanel inputPanel;
    private JLabel errorLabel;
    private List<Integer> eraList;
    

    public AddProductPage(JFrame parent) {
        super(parent, "Add Product", true);
        this.eraList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
    
        setMinimumSize(new Dimension(540, 600));

        // Error Message
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        add(errorLabel, gbc);

        // Product Type Selection
        String[] productTypes = {"Select Product Type", "Track", "Locomotive", "Rolling Stock", "Controller","Train Set", "Train Pack"};
        productTypeComboBox = new JComboBox<>(productTypes);
        productTypeComboBox.addActionListener(e -> updateInputFields());
        add(new JLabel("Product Type:"), gbc);
        add(productTypeComboBox, gbc);

        // Input Panel for Additional Inputs
        inputPanel = new JPanel(new GridBagLayout());
        add(inputPanel, gbc);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitProduct());
        add(submitButton, gbc);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void updateInputFields() {
        inputPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        String selectedType = (String) productTypeComboBox.getSelectedItem();
        if (selectedType == null || selectedType.equals("Select Product Type")) {
            inputPanel.revalidate();
            inputPanel.repaint();
            return;
        }

        // Common input fields

        productNameField = new JTextField(20);
        productCodeField = new JTextField(20);
        brandNameField = new JTextField(20);
        retailPriceField = new JTextField(20);
        stockQuantityField = new JTextField(20);
        descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JButton eraSelectButton = new JButton("Select Era");
        eraSelectButton.addActionListener(e -> openEraSelectDialog(eraList));

        addInputField("Product Name:", productNameField, gbc);
        addInputField("Product Code:", productCodeField, gbc);
        addInputField("Brand:", brandNameField, gbc);
        addInputField("Retail Price:", retailPriceField, gbc);
        addInputField("Stock Quantity:", stockQuantityField, gbc);
        addInputField("Description:", descriptionScrollPane, gbc);

        gaugeComboBox = new JComboBox<>(new String[]{"OO", "TT", "N"});
        dccComboBox = new JComboBox<>(new String[]{"Analogue", "Ready", "Fitted", "Sound"});
        compartmentComboBox = new JComboBox<>(new String[]{"Wagon", "Carriage"});
        digitalComboBox = new JComboBox<>(new String[]{"Digital", "Analogue"});
        

        // Additional type-specific input fields
        switch (selectedType) {
            case "Track":
                addInputField("Gauge:", gaugeComboBox, gbc);
                break;
            case "Locomotive":
                addInputField("Gauge:", gaugeComboBox, gbc);
                addInputField("DCC Type:", dccComboBox, gbc);
                addInputField("", eraSelectButton, gbc);
                break;
            case "Rolling Stock":
                addInputField("Compartment Type:", compartmentComboBox, gbc);
                addInputField("Gauge:", gaugeComboBox, gbc);
                addInputField("", eraSelectButton, gbc);
                break;
            case "Controller":
                addInputField("Digital Type:", digitalComboBox, gbc);
                break;
            case "Train Set":
                trainSetPanel(gbc);
                break;
            case "Track Pack":
                trackPackPanel(gbc);
                break;
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void trainSetPanel(GridBagConstraints gbc){
        JButton locoSelectButton = createButton("Select Locomotives");
        JButton rollSelectButton = createButton("Select Rollingstocks");
        JButton traPaSelectButton = createButton("Select Track Packs");
        JButton ctrlSelectButton = createButton("Select Controller");

        locoSelectButton.addActionListener(e -> openProductSelectDialog("Locomotive"));
        rollSelectButton.addActionListener(e -> openProductSelectDialog("Rolling Stock"));
        traPaSelectButton.addActionListener(e -> openProductSelectDialog("Track Pack"));
        ctrlSelectButton.addActionListener(e -> openProductSelectDialog("Controller"));

        addInputField("", locoSelectButton, gbc);
        addInputField("", rollSelectButton, gbc);
        addInputField("", traPaSelectButton, gbc);
        addInputField("", ctrlSelectButton, gbc);
        
    }

    private void trackPackPanel(GridBagConstraints gbc){
        JButton trackSelectButton = createButton("Select Tracks");
        trackSelectButton.addActionListener(e -> openProductSelectDialog("Track"));
        addInputField("", trackSelectButton, gbc);
    }

    private void addInputField(String label, JComponent component, GridBagConstraints gbc) {
        inputPanel.add(new JLabel(label), gbc);
        inputPanel.add(component, gbc);
    }

    private JButton createButton(String text){
        JButton button = new JButton(text);
        button.setBackground(new Color(0x204688));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void openEraSelectDialog(List<Integer> eras) {
        EraSelect eraSelect = new EraSelect(this, eras);
        eraSelect.setVisible(true);
        List<Integer> selectedEras = eraSelect.getSelectedEras();
        setSelectedEra(selectedEras);
    }

    private void openProductSelectDialog(String type) {
        ProductSelectPage productSelect = new ProductSelectPage(this, type);
        productSelect.setVisible(true);
    }

    private void setSelectedEra(List<Integer> eraList) {
        this.eraList = eraList;
    }

    private void submitProduct() {
        String selectedType = (String) productTypeComboBox.getSelectedItem();
        if (selectedType == null || selectedType.equals("Select Product Type")) {
            errorLabel.setText("Please select a product type.");
            errorLabel.setVisible(true);
            return;
        }

        // Validate and submit product details
        String productName = productNameField.getText();
        String productCode = productCodeField.getText().toUpperCase();
        String brandName = brandNameField.getText();
        String retailPrice = retailPriceField.getText();
        String stockQuantity = stockQuantityField.getText();
        String description = descriptionField.getText();

        String validationResult = ProductService.validateProductInput(productName, productCode, brandName, retailPrice, stockQuantity);
        if (validationResult != null) {
            errorLabel.setText(validationResult);
            errorLabel.setVisible(true);
            return;
        }

        Product product = new Product(brandName, productName, productCode, Double.parseDouble(retailPrice), description, Integer.parseInt(stockQuantity), "");
        if (!product.getProductType().equalsIgnoreCase(selectedType)){
            errorLabel.setText("Your select type does no match the Product Code you Enter");
            errorLabel.setVisible(true);
            return;
        }
        try{
            switch (selectedType) {
                case "Track":
                    String selectedGauge = gaugeComboBox.getSelectedItem().toString();
                    Track track = new Track(product, selectedGauge);
                    TrackDAO.insertTrack(track);
                    break;
                case "Controller":
                    boolean selectedDigitalType = digitalComboBox.getSelectedItem().equals("Digital");
                    Controller controller = new Controller(product, selectedDigitalType);
                    ControllerDAO.insertController(controller);
                    break;
                case "Locomotive":
                    String selectedDccType = dccComboBox.getSelectedItem().toString();
                    String selectedGaugeForLoco = gaugeComboBox.getSelectedItem().toString();
                    int[] era = eraList.stream().mapToInt(i -> i).toArray();
                    Locomotive locomotive = new Locomotive(product, selectedGaugeForLoco,selectedDccType,era);
                    LocomotiveDAO.insertLocomotive(locomotive);
                    break;
                case "Rolling Stock":
                    String selectedCompartmentType = compartmentComboBox.getSelectedItem().toString();
                    String selectedGaugeForRoll = gaugeComboBox.getSelectedItem().toString();
                    int[] era1 = eraList.stream().mapToInt(i -> i).toArray();
                    RollingStock rollingStock = new RollingStock(product,selectedCompartmentType,selectedGaugeForRoll,era1);
                    RollingStockDAO.insertRollingStock(rollingStock);
                    break;
                case "Train Set":
                    // TODO:
                    break;
                case "Track Pack":
                    // TODO:
                    break;
                }
            } catch (Exception ex) {
                errorLabel.setText("Your select type does no match the Product Code you Enter");
                errorLabel.setVisible(true);
                return;
            }

        errorLabel.setVisible(false);
        JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        setVisible(false);
    }
}
