import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountryAdapter {
    public static List<CountryEntity> convertToList(Map<String, Pair<String , ImageIcon>> map)
    {
        List<CountryEntity> entities = new ArrayList<>(map.size());
        map.forEach((x,y)->{
            entities.add(new CountryEntity(x, y.getKey(), y.getValue()));
        });
        return entities;
    }
}
