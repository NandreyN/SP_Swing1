import javafx.util.Pair;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VacationGetter {

    private List<VacationCountryEntity> countryData;
    private final String PATH = "vacation.txt";

    public VacationGetter() throws IOException {
        CountryGetter cGetter = new CountryGetter();
        List<CountryEntity> countryFlags = CountryAdapter.convertToCountryEntityList(cGetter.getCountries());
        Map<String, ImageIcon> icons = cGetter.getFlagsByCode();
        readVacationData(icons);
        mergeWithFlags(icons);
    }

    private void readVacationData(Map<String, ImageIcon> icons) throws FileNotFoundException {
        Scanner in = new Scanner(new File(PATH));
        countryData = new ArrayList<>();
        // country code , description , price
        while (in.hasNextLine()) {
            String line[] = in.nextLine().split("\\s+");
            if (line.length != 3)
                continue;
            String code = line[0].toLowerCase();
            ImageIcon flag = icons.getOrDefault(code, null);
            String desc = line[1];
            Integer price = Integer.parseInt(line[2]);
            countryData.add(new VacationCountryEntity(code, flag, desc, price, false));
        }
    }

    private void mergeWithFlags(Map<String, ImageIcon> icons)
    {
        countryData.forEach(x->{
            if (icons.containsKey(x.getCode().toLowerCase()))
                x.setFlag(icons.get(x.getCode().toLowerCase()));
        });
    }

    public List<VacationCountryEntity> getVacationData()
    {
        return countryData;
    }
}
