import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class CountryEntity implements Comparable<CountryEntity>
{
    private String name, capital;
    private ImageIcon flag;
    private String code;

    public CountryEntity(String name, String capital, ImageIcon flag)
    {
        this.capital = capital;
        this.name = name;
        this.flag = flag;
    }

    public String getName()
    {
        return name;
    }

    public String getCapital()
    {
        return capital;
    }

    public ImageIcon getFlag()
    {
        return flag;
    }

    public void setName(String name)
    {
        this.name= name;
    }

    public void setCapital(String capital)
    {
        this.capital= capital;
    }

    public void setFlag(ImageIcon flag)
    {
        this.flag = flag;
    }

    @Override
    public String toString()
    {
        return "Name : " +  name + ", capital :  " + capital;
    }

    @Override
    public int compareTo(CountryEntity o) {
        return name.compareTo(o.getName());
    }
}

class CountryRenderer extends JLabel implements ListCellRenderer<CountryEntity>
{
    private JLabel detailed;
    public CountryRenderer(JLabel label) {
        setOpaque(true);
        detailed = label;
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends CountryEntity> list, CountryEntity value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.getName());
        setIcon(value.getFlag());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            detailed.setText(value.toString());
            detailed.setIcon(value.getFlag());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}


public class ListPanel extends JPanel {
    private CountryGetter getter;
    private DefaultListModel<CountryEntity> listModel;
    private JList<CountryEntity> list;
    private JScrollPane scroll;

    private JLabel detailed;

    public ListPanel()
    {
        try {
            getter = new CountryGetter();
            listModel = new DefaultListModel<>();
            detailed = new JLabel();
            detailed.setSize(getWidth(), getHeight()/4);

            CountryAdapter.convertToCountryEntityList(getter.getCountries()).stream()
                    .sorted(CountryEntity::compareTo).forEach(listModel::addElement);
            list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setCellRenderer(new CountryRenderer(detailed));
            scroll = new JScrollPane(list);

            setLayout(new BorderLayout());
            add(detailed, BorderLayout.SOUTH);
            add(scroll, BorderLayout.CENTER);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


    }

}
