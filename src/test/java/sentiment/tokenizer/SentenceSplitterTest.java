package sentiment.tokenizer;

import net.sourceforge.segment.TextIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

@Test
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class SentenceSplitterTest extends AbstractTestNGSpringContextTests {

    @Autowired
    SentenceSplitter sentenceSplitter;

    public void shouldDetectSentences() {

        final String input = "Ala ma kota, ale nie ma psa. Tomek ma rower. Bla, bla, bla...";
        TextIterator textIterator = sentenceSplitter.getTextIterator(input);

        assertEquals(textIterator.next(), "Ala ma kota, ale nie ma psa. ");
        assertEquals(textIterator.next(), "Tomek ma rower. ");
        assertEquals(textIterator.next(), "Bla, bla, bla...");
        assertFalse(textIterator.hasNext());
    }
}
