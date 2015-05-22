package sentiment.tokenizer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WordSplitter {

    public List<String> getWords(final String sentence) {
        String raw = sentence.replaceAll(",|\\.|\\?|!|-|\\(|\\)", "");
        return newArrayList(raw.toLowerCase().split(" "));
    }
}
