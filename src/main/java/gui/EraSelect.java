package gui;

import javax.swing.*;

import service.ProductService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EraSelect extends JDialog {
    private List<JCheckBox> eraCheckBoxes;
    private List<Integer> selectedEras;

    public EraSelect(Dialog parent, List<Integer> selectedEras) {
        super(parent, "Select Era", true);
        this.selectedEras = selectedEras;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(0, 1));
        eraCheckBoxes = new ArrayList<>();
        ArrayList<String> eraDescriptions = ProductService.findAllEra();
        for (int i = 0; i < eraDescriptions.size(); i++) {
            String description = eraDescriptions.get(i);
            JCheckBox checkBox = new JCheckBox(description);
            if (selectedEras != null && selectedEras.contains(i)) {
                checkBox.setSelected(true);
            }
            eraCheckBoxes.add(checkBox);
            add(checkBox);
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> setVisible(false));
        add(okButton);

        pack();
        setLocationRelativeTo(getParent());
    }

    public List<Integer> getSelectedEras() {
        List<Integer> selectedEras = new ArrayList<>();
        for (int i = 0; i < eraCheckBoxes.size(); i++) {
            if (eraCheckBoxes.get(i).isSelected()) {
                selectedEras.add(i + 1);
            }
        }
        return selectedEras;
    }
}

