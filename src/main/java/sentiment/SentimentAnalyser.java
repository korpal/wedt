package sentiment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sentiment.lemmatizer.Lemmatizer;
import sentiment.tokenizer.WordSplitter;

import java.io.*;
import java.util.*;

@Service
public class SentimentAnalyser {

    private Logger logger = LoggerFactory.getLogger(SentimentAnalyser.class);
    private Map<String, Double> emotionDictionary;
    private Map<String, Double> boosterDictionary;
    private Set<String> negatingDictionary;

    private Lemmatizer lemmatizer;

    @Autowired
    public SentimentAnalyser(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    public void createEmotionDictionary(File input) throws IOException {
        try {
            emotionDictionary = new HashMap<>();

            InputStream dictInputStream = new FileInputStream(input);
            Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
            BufferedReader dictReader = new BufferedReader(inputStreamReader);

            while(dictReader.ready()) {
                String line = dictReader.readLine();
                String[] parts = line.split("\t");

                String word = parts[0];
                Double emotion = Double.valueOf(parts[1]);

                emotionDictionary.put(word, emotion);
            }
        } catch(IOException | ArrayIndexOutOfBoundsException e) {
            emotionDictionary = null;
            throw e;
        }
    }

    public void createBoosterDictionary(File input) throws IOException {
        try {
            boosterDictionary = new HashMap<>();

            InputStream dictInputStream = new FileInputStream(input);
            Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
            BufferedReader dictReader = new BufferedReader(inputStreamReader);

            while(dictReader.ready()) {
                String line = dictReader.readLine();
                String[] parts = line.split("\t");
                boosterDictionary.put(parts[0], Double.valueOf(parts[1]));
            }
        } catch(IOException | ArrayIndexOutOfBoundsException e) {
            boosterDictionary = null;
            throw e;
        }
    }

    public void createNegatingDictionary(File input) throws IOException {
        try {
            negatingDictionary = new HashSet<>();

            InputStream dictInputStream = new FileInputStream(input);
            Reader inputStreamReader = new InputStreamReader(dictInputStream, "utf-8");
            BufferedReader dictReader = new BufferedReader(inputStreamReader);

            while(dictReader.ready()) {
                String line = dictReader.readLine();
                negatingDictionary.add(line);
            }
        } catch(IOException | ArrayIndexOutOfBoundsException e) {
            negatingDictionary = null;
            throw e;
        }
    }

    public boolean isReady() {
        return emotionDictionary != null && boosterDictionary != null && negatingDictionary != null;
    }

    public Double calcSentiment(String sentence) {
        WordSplitter wordSplitter = new WordSplitter();
        List<String> words = wordSplitter.getWords(sentence);

        Double emotionSum = 0.0;
        Double boostSum = 1.0;
        Integer negation = 1;
        for(String word : words) {
            String lemma = lemmatizer.getLemma(word);
            if(lemma == null) {
                continue;
            }

            Double emotion = emotionDictionary.get(lemma);
            Double boost = boosterDictionary.get(lemma);

            emotionSum += emotion != null ? emotion : 0.0;
            boostSum *= boost != null ? boost : 1.0;

            if(negatingDictionary.contains(lemma)) {
                negation *= -1;
            }
        }

        if (negation < 0) {
            boostSum = 1/boostSum;
        }

        return emotionSum*boostSum*negation;
    }
}
