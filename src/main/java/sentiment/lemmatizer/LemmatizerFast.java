package sentiment.lemmatizer;

import com.google.common.io.Resources;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class LemmatizerFast {

    private static final String DICTIONARY_FILE_NAME = "lemmatizer.txt";

    private Logger logger = LoggerFactory.getLogger(LemmatizerFast.class);

    private HashObjObjMap<String, String> dictionary;

    public LemmatizerFast() {
        dictionary = HashObjObjMaps.newMutableMap();
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
            logger.error("Error while creating LemmatizerFast", e);
        }
    }

    public String getLemma(final String word) {
        return dictionary.get(word);
    }
}
