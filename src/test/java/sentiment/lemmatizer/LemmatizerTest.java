package sentiment.lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class LemmatizerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    Lemmatizer lemmatizer;

    @Test(invocationCount = 10000)
    public void shouldFindLemmasProperly() {

        String word = "niebieska";
        String lemma = "niebieski";

        String found = lemmatizer.getLemma(word);

        assertEquals(found, lemma);
    }
}
