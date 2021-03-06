package sentiment.tokenizer;

import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.testng.Assert.assertEquals;

@Test
public class WordSplitterTest {

    public void shouldDetectWords() {

        final String input = "Ala ma kota, ale nie ma psa. Czy to prawda?";
        final List<String> expectedWords = newArrayList("Ala", "ma", "kota", "ale", "nie", "ma", "psa", "Czy", "to", "prawda");
        WordSplitter wordSplitter = new WordSplitter();

        List<String> words = wordSplitter.getWords(input);

        assertEquals(words, expectedWords);
    }
}
