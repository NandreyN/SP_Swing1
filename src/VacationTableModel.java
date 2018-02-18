import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

class VacationCountryEntity {
    private String desc;
    private Integer price;
    private String code;
    private boolean isSelected;
    private ImageIcon flag;
    private static CountryGetter getter = null;

    static {
        try {
            getter = new CountryGetter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VacationCountryEntity(String code, ImageIcon flag, String desc, Integer price, boolean isSelected) {
        this.desc = desc;
        this.code = code;
        this.flag = flag;
        this.price = price;
        this.isSelected = isSelected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.toLowerCase();
        try {
            setFlag(getter.getFlagsByCode().get(code));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean state) {
        this.isSelected = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ImageIcon getFlag()
    {
        return flag;
    }

    public void setFlag(ImageIcon flag)
    {
        this.flag = flag;
    }
}

public class VacationTableModel extends DefaultTableModel {
    private List<VacationCountryEntity> countries;
    private static final String[] HEADERS = {"Flag", "Code", "Description", "Price", "Select"};

    public VacationTableModel(List<VacationCountryEntity> data) throws IOException {
        this.countries = data;
    }

    private int getSum()
    {
        return (int) countries.stream().filter(VacationCountryEntity::getSelected).mapToLong(VacationCountryEntity::getPrice).sum();
    }

    @Override
    public int getRowCount() {
        return (countries != null) ? countries.size() + 1 : 0;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            default:
                return Boolean.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public String getColumnName(int index) {
        return HEADERS[index];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        VacationCountryEntity e = countries.get(row);
        switch (col) {
            case 0:

                break;
            case 1:
                // code
                e.setCode((String) value);
                fireTableCellUpdated(row, 0);
                break;
            case 2:
                e.setDesc((String) value);
                break;
            case 3:
                e.setPrice((Integer) value);
                fireTableCellUpdated(countries.size(), 3);
                break;
            case 4:
                e.setSelected((Boolean) value);
                fireTableCellUpdated(countries.size(), 3);
                break;
        }
        fireTableCellUpdated(row, col);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == countries.size())
        {
            if (columnIndex == 3)
            {
                return getSum();
            }
            else
                return null;
        }
        switch (columnIndex) {
            case 0:
                return countries.get(rowIndex).getFlag();
            case 1:
                return countries.get(rowIndex).getCode();
            case 2:
                return countries.get(rowIndex).getDesc();
            case 3:
                return countries.get(rowIndex).getPrice();
            case 4:
                return countries.get(rowIndex).getSelected();
            default:
                return "???";
        }
    }
}
