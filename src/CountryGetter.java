import javafx.util.Pair;
import org.json.JSONArray;
import org.w3c.dom.css.CSSRule;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

public class CountryGetter {
    private boolean isTxtFileExists;
    private static final String DOWNLOAD = "https://restcountries.eu/rest/v2/all";
    private static final String FILE = "countries_input.txt";
    private Map<String, Pair<String, ImageIcon>> countriesMap;

    public CountryGetter() throws IOException {
        isTxtFileExists = (new File(FILE)).exists();
        if (!isTxtFileExists) {
            JSONArray json = downloadCountries();
            createInputFile(json);
        }
        initFromTxt();
    }

    public Map<String, Pair<String, ImageIcon>> getCountries()
    {
        return countriesMap;
    }

    private JSONArray downloadCountries() throws IOException {
        InputStream is = new URL(DOWNLOAD).openStream();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            JSONArray json = new JSONArray(readAll(br));
            isTxtFileExists = true;
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private void createInputFile(JSONArray json) throws IOException {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> iterator = json.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (!(o instanceof JSONObject))
                continue;
            JSONObject jsonObject = (JSONObject) o;
            sb.append(jsonObject.get("name")).append(";").append(jsonObject.get("capital")).
                    append(";").append(jsonObject.get("alpha2Code")).append("\n");
        }

        File out = new File(FILE);
        boolean status = out.createNewFile();
        if (!status)
            throw new IOException("Cannot create file");
        FileWriter writer = new FileWriter(out);
        writer.write(sb.toString());
        writer.flush();
    }

    private void initFromTxt() throws FileNotFoundException {
        countriesMap = new HashMap<>();
        Scanner scanner = new Scanner(new File(FILE));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(";");
            if (line.length != 3)
                continue;
            String iconPath = "flags-mini\\" + line[2].toLowerCase() + ".png";
            if (new File(iconPath).exists())
                countriesMap.put(line[0], new Pair<>(line[1], new ImageIcon(iconPath)));
        }
    }

    public  Map<String, ImageIcon> getFlagsByCode() throws FileNotFoundException {
        Map<String, ImageIcon> dictionary = new HashMap<>();
        Scanner in = new Scanner(new File(FILE));
        while(in.hasNextLine())
        {
            String[] lines = in.nextLine().split(";");
            if (lines.length != 3)
                continue;
            String iconPath = "flags-mini\\" + lines[2].toLowerCase() + ".png";
            if (new File(iconPath).exists())
                dictionary.put(lines[2].toLowerCase(), new ImageIcon(iconPath));
        }
        return dictionary;
    }
}
