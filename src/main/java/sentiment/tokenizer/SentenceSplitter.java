package sentiment.tokenizer;

import com.google.common.io.Resources;
import net.sourceforge.segment.TextIterator;
import net.sourceforge.segment.srx.SrxDocument;
import net.sourceforge.segment.srx.SrxParser;
import net.sourceforge.segment.srx.io.Srx2Parser;
import net.sourceforge.segment.srx.legacy.AccurateSrxTextIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;

import static net.sourceforge.segment.util.Util.getFileInputStream;
import static net.sourceforge.segment.util.Util.getReader;

@Service
public class SentenceSplitter {

    private static final String SRX_FILE_NAME = "segment.srx";
    private static final String LANGUAGE_CODE = "pl";

    private Logger logger = LoggerFactory.getLogger(SentenceSplitter.class);
    private SrxDocument document;

    public SentenceSplitter() {
        URL srcURL = Resources.getResource(this.getClass(), SRX_FILE_NAME);

        try {
            Reader srxReader = getReader(getFileInputStream(srcURL.toURI().getPath()));
            SrxParser srxParser = new Srx2Parser();
            document = srxParser.parse(srxReader);
            srxReader.close();
        } catch (URISyntaxException | IOException e) {
            logger.error("Error while creating SentenceSplitter", e);
        }
    }

    public TextIterator getTextIterator(final String input) {
        return new AccurateSrxTextIterator(document, LANGUAGE_CODE, input);
    }
}
