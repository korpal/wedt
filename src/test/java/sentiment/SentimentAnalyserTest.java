package sentiment;

import net.sourceforge.segment.TextIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import sentiment.tokenizer.SentenceSplitter;

@Test
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class SentimentAnalyserTest extends AbstractTestNGSpringContextTests {

    @Autowired
    SentenceSplitter sentenceSplitter;

    @Autowired
    SentimentAnalyser sentimentAnalyser;

    public void shouldDetectSentences() {

        //final String document = "Ala ma bardzo ładnego kota. Ty głupi gnoju, po co to ruszasz.";
        final String document = "Ja nazywam się Justyna Wiśniewska i poprowadzę dzisiejsze spotkanie, moja rola polega na tym, żeby zapewnić uczestnikom spotkania jednakową szansę wyrażenia opinii i przypilnować, żebyśmy zdążyli porozmawiać na wszystkie zaplanowane na dziś tematy. Są to po pierwsze plagiat, po drugie metody kształcenia i po trzecie odpłatność za drugi kierunek studiów. Jak już wspomniałam na tablicy widzicie harmonogram dzisiejszego spotkania, zwróćcie uwagę ile mamy czasu na poszczególne tematy, Karol ciągle walczy o głos, jeszcze zostało nam 3 minuty. Pod koniec dyskusji na każdy z wymienionych tematów postaramy się ustalić pytanie do ekspertów, możemy myśleć o tych pytaniach już podczas dyskusji, będziemy zapisywać propozycje na tablicy. Pamelu chciałabyś coś powiedzieć? \n" +
                "P: Nie, w sumie nie.\n" +
                "J: To możecie jeśli nie chcecie nic mówić to opuście proszę ręce, czyli naciskając na ludziki jeszcze raz. Tak Pamelo już cię wyłączam. Nie, nie, nie, właśnie niestety nie automatycznie, Aniu czy chcesz powiedzieć? Tak Aniu masz głos. Okej, nie chcesz. \n";
        TextIterator sentencesIterator = sentenceSplitter.getTextIterator(document);

        while(sentencesIterator.hasNext()) {
            String sentence = sentencesIterator.next();
            Double sentiment = sentimentAnalyser.calcSentiment(sentence);

            System.out.println(sentiment + " " + sentence);
        }
    }
}
