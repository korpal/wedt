package sentiment.tokenizer;

import net.sourceforge.segment.TextIterator;
import net.sourceforge.segment.srx.SrxDocument;
import net.sourceforge.segment.srx.SrxParser;
import net.sourceforge.segment.srx.io.Srx2Parser;
import net.sourceforge.segment.srx.legacy.AccurateSrxTextIterator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;

import static net.sourceforge.segment.util.Util.getReader;

@Service
public class SentenceSplitter {

    private static final String LANGUAGE_CODE = "pl";

    private SrxDocument document;

    public void configure(File file) throws IOException {
        try {
            Reader srxReader = getReader(new FileInputStream(file));
            SrxParser srxParser = new Srx2Parser();
            document = srxParser.parse(srxReader);
            srxReader.close();
        } catch(IOException e) {
            document = null;
            throw e;
        }
    }

    public boolean isReady() {
        return document != null;
    }

    public TextIterator getTextIterator(final String input) {
        return new AccurateSrxTextIterator(document, LANGUAGE_CODE, input);
    }
}
