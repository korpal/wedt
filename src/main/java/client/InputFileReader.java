package client;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.util.Scanner;

public class InputFileReader {

    public String readFile(File file) throws IOException {
        if(file == null) {
            return null;
        }

        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.indexOf(".") + 1, file.getName().length());

        switch (fileExtension) {
            case "doc":
                return readDOCFile(file);
            case "docx":
                return readDOCXFile(file);
            case "rtf":
                return readRTFFile(file);
            default:
                return readTextFile(file);
        }
    }

    private String readRTFFile(File file) throws IOException {
        RTFEditorKit rtfParser = new RTFEditorKit();
        Document document = rtfParser.createDefaultDocument();
        try {
            rtfParser.read(new FileInputStream(file), document, 0);
            return document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

    private String readDOCXFile(File file) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));
        XWPFWordExtractor wordExtractor = new XWPFWordExtractor(document);
        return wordExtractor.getText();
    }

    private String readDOCFile(final File file) throws IOException {
        WordExtractor wordExtractor = new WordExtractor(new FileInputStream(file));
        return wordExtractor.getText();
    }

    private String readTextFile(final File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

}
