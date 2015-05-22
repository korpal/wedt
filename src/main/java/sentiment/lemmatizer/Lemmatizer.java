package sentiment.lemmatizer;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class Lemmatizer {

    private static final String DICTIONARY_FILE_NAME = "lemmatizer.txt";

    private Logger logger = LoggerFactory.getLogger(Lemmatizer.class);
    private Map<String, String> dictionary = new HashMap<>();

    public Lemmatizer() {
        URL dictURL = Resources.getResource(this.getClass(), DICTIONARY_FILE_NAME);

        try {
            InputStream dictInputStream = new FileInputStream(dictURL.toURI().getPath());
            Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
            BufferedReader dictReader = new BufferedReader(inputStreamReader);

            while(dictReader.ready()) {
                String line = dictReader.readLine();
                String[] parts = line.split("\t");
                dictionary.put(parts[0], parts[1]);
            }

        } catch (URISyntaxException | IOException e) {
            logger.error("Error while creating Lemmatizer", e);
        }
    }

    public String getLemma(final String word) {
        return dictionary.get(word);
    }
}
