package client;

import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            case "docx":
            case "rtf":
                return readWordFile(file);
            default:
                return readTextFile(file);
        }
    }

    private String readWordFile(final File file) throws IOException {
        WordExtractor wordExtractor = new WordExtractor(new FileInputStream(file));
        return wordExtractor.getText();
    }

    private String readTextFile(final File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

}
