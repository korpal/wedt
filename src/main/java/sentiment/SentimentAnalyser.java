package sentiment;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sentiment.lemmatizer.Lemmatizer;
import sentiment.tokenizer.WordSplitter;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Service
public class SentimentAnalyser {

    private static final String EMOTION_DICTIONARY_FILE_NAME = "EmotionLookupTable.txt";
    private static final String BOOSTER_DICTIONARY_FILE_NAME = "BoosterWordList.txt";
    private static final String NEGATING_DICTIONARY_FILE_NAME = "NegatingWordList.txt";

    private Logger logger = LoggerFactory.getLogger(SentimentAnalyser.class);
    private Map<String, Integer> emotionDictionary = new HashMap<>();
    private Map<String, Integer> boosterDictionary = new HashMap<>();
    private Set<String> negatingDictionary = new HashSet<>();
    private Lemmatizer lemmatizer;

    @Autowired
    public SentimentAnalyser(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
        try {
            fillDictionary(EMOTION_DICTIONARY_FILE_NAME, emotionDictionary);
            fillDictionary(BOOSTER_DICTIONARY_FILE_NAME, boosterDictionary);
            fillDictionary(NEGATING_DICTIONARY_FILE_NAME, negatingDictionary);
        } catch (URISyntaxException | IOException e) {
            logger.error("Error while creating Sentiment dictionaries", e);
        }
    }

    public Integer calcSentiment(String sentence) {
        WordSplitter wordSplitter = new WordSplitter();
        List<String> words = wordSplitter.getWords(sentence);

        Integer emotionSum = 0;
        Integer boostSum = 1;
        for(String word : words) {
            String lemma = lemmatizer.getLemma(word);
            if(lemma == null) {
                continue;
            }

            Integer emotion = emotionDictionary.get(lemma);
            Integer boost = boosterDictionary.get(lemma);

            emotionSum += emotion != null ? emotion : 0;
            boostSum *= boost != null ? boost : 1;
        }

        return emotionSum*boostSum;
    }

    private void fillDictionary(final String fileName, Map<String, Integer> dictionary) throws IOException, URISyntaxException {
        URL dictURL = Resources.getResource(this.getClass(), fileName);
        InputStream dictInputStream = new FileInputStream(dictURL.toURI().getPath());
        Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
        BufferedReader dictReader = new BufferedReader(inputStreamReader);

        while(dictReader.ready()) {
            String line = dictReader.readLine();
            String[] parts = line.split("\t");
            dictionary.put(parts[0], Integer.valueOf(parts[1]));
        }

    }

    private void fillDictionary(final String fileName, Set<String> dictionary) throws IOException, URISyntaxException {
        URL dictURL = Resources.getResource(this.getClass(), fileName);
        InputStream dictInputStream = new FileInputStream(dictURL.toURI().getPath());
        Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
        BufferedReader dictReader = new BufferedReader(inputStreamReader);

        while(dictReader.ready()) {
            String line = dictReader.readLine();
            dictionary.add(line);
        }

    }
}
