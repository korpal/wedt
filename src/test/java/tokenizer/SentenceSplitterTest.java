package tokenizer;

import net.sourceforge.segment.TextIterator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

@Test
public class SentenceSplitterTest {

    public void shouldDetectSentences() {

        final String input = "Ala ma kota, ale nie ma psa. Tomek ma rower. Bla, bla, bla...";
        TextIterator textIterator = SentenceSplitter.getInstance().getTextIterator(input);

        assertEquals(textIterator.next(), "Ala ma kota, ale nie ma psa. ");
        assertEquals(textIterator.next(), "Tomek ma rower. ");
        assertEquals(textIterator.next(), "Bla, bla, bla...");
        assertFalse(textIterator.hasNext());
    }
}
