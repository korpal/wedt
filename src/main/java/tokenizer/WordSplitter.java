package tokenizer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WordSplitter {

    public List<String> getWords(final String sentence) {
        return newArrayList(sentence.replace(".", "").replace(",", "").split(" "));
    }
}
