import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

class AddPanel extends JPanel {
    private Map<String, ImageIcon> supportedCountries;
    private List<VacationCountryEntity> vacationData;
    private JButton addButton;
    private JTextField description, price;
    private JComboBox<String> codes;
    private JTable table;

    public AddPanel(Map<String, ImageIcon> list, JTable table, List<VacationCountryEntity> vacationData) {
        this.table = table;
        this.vacationData = vacationData;
        supportedCountries = list;
        codes = new JComboBox<>();
        addButton = new JButton("Add");
        description = new JTextField("Description");
        price = new JTextField("Price");

        add(codes);
        add(description);
        add(price);
        add(addButton);
        supportedCountries.forEach((x, y) -> codes.addItem(x.toLowerCase()));
        addButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon flag = supportedCountries.getOrDefault(codes.getSelectedItem(), null);
                if (flag == null) {
                    JOptionPane.showMessageDialog(null, "Unknown country code");
                    return;
                }
                int intValue;
                try{
                    intValue = Integer.parseInt(price.getText());
                }
                catch (NumberFormatException e1)
                {
                    return;
                }

                VacationCountryEntity entity = new VacationCountryEntity(codes.getSelectedItem().toString(), flag,description.getText(),
                        intValue, false);
                vacationData.add(entity);
                ((DefaultTableModel) table.getModel()).fireTableDataChanged();
            }
        });
    }
}

public class VacationPanel extends JPanel {
    private VacationTableModel tableModel;
    private JTable table;
    private JPanel addPanel;
    private JScrollPane scroll;
    private List<VacationCountryEntity> vacationData;

    public VacationPanel() throws IOException {
        VacationGetter vGetter = new VacationGetter();
        vacationData = vGetter.getVacationData();
        tableModel = new VacationTableModel(vacationData);
        table = new JTable(tableModel);

        addPanel = new AddPanel(new CountryGetter().getFlagsByCode(), table, vacationData);

        scroll = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(addPanel, BorderLayout.NORTH);
    }
}
