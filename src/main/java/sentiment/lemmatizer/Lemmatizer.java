package sentiment.lemmatizer;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class Lemmatizer {

    private Map<String, String> dictionary;

    public void createDictionary(File input) throws IOException {
        try {
            dictionary = new HashMap<>();

            InputStream dictInputStream = new FileInputStream(input);
            Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
            BufferedReader dictReader = new BufferedReader(inputStreamReader);

            while(dictReader.ready()) {
                String line = dictReader.readLine();
                String[] parts = line.split("\t");
                dictionary.put(parts[0], parts[1]);
            }
        } catch(IOException e) {
            dictionary = null;
            throw e;
        }
    }

    public boolean isReady() {
        return dictionary != null;
    }

    public String getLemma(final String word) {
        return dictionary.get(word);
    }
}
