package client;

import common.SpringUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sourceforge.segment.TextIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sentiment.SentimentAnalyser;
import sentiment.lemmatizer.Lemmatizer;
import sentiment.tokenizer.SentenceSplitter;

import java.io.File;
import java.io.IOException;

public class MenuController {

    @FXML
    private Label lemmaDictReadLabel;

    @FXML
    private Label sentenceSplitterConfReadLabel;

    @FXML
    private Label emotionDictReadLabel;

    @FXML
    private Label boosterDictReadLabel;

    @FXML
    private Label negatingDictReadLabel;

    @FXML
    private TableView emotionTable;

    @FXML
    private TableColumn<RatedSentence, Number> emotionColumn;

    @FXML
    private TableColumn<RatedSentence, String> sentenceColumn;

    @Value( "${defaultLemmatizerInputName}" )
    private String defaultLemmatizerInputName;

    @Value("${defaultSentenceSplitterConfName}")
    private String defaultSentenceSplitterConfName;

    @Value( "${defaultBoosterDictInputName}" )
    private String defaultBoosterDictInputName;

    @Value( "${defaultEmotionDictInputName}" )
    private String defaultEmotionDictInputName;

    @Value( "${defaultNegatingDictInputName}" )
    private String defaultNegatingDictInputName;

    @Autowired
    Lemmatizer lemmatizer;

    @Autowired
    SentenceSplitter sentenceSplitter;

    @Autowired
    SentimentAnalyser sentimentAnalyser;

    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    private Stage stage;
    private ObservableList<RatedSentence> ratedSentences = FXCollections.observableArrayList();

    public MenuController() {
        SpringUtils.autowireBean(this);
    }

    @FXML
    public void initialize() {
        emotionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sentenceColumn.setCellFactory(new Callback<TableColumn<RatedSentence, String>, TableCell<RatedSentence, String>>() {
            @Override
            public TableCell<RatedSentence, String> call( TableColumn<RatedSentence, String> param) {
                TableCell<RatedSentence, String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(cell.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });

        emotionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RatedSentence, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<RatedSentence, Number> p) {
                RatedSentence ratedSentence = p.getValue();
                Double sentiment = ratedSentence.getSentiment();

                return new SimpleDoubleProperty(sentiment);
            }
        });

        sentenceColumn.setCellValueFactory(p -> {
            RatedSentence ratedSentence = p.getValue();
            String sentence = ratedSentence.getSentence();
            return new SimpleStringProperty(sentence);
        });


        File file = new File(defaultLemmatizerInputName);
        readLemmatizerInput(file);

        file = new File(defaultSentenceSplitterConfName);
        readSentenceSplitterInput(file);

        file = new File(defaultBoosterDictInputName);
        readBoosterInput(file);

        file = new File(defaultEmotionDictInputName);
        readEmotionInput(file);

        file = new File(defaultNegatingDictInputName);
        readNegatingInput(file);
    }

    private void readLemmatizerInput(File input) {
        try {
            lemmatizer.createDictionary(input);
            setReadStateLabel(lemmaDictReadLabel);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.warn("Error while reading Lemmatizer input file.", e);
            setUnreadStateLabel(lemmaDictReadLabel);
        }
    }

    private void readSentenceSplitterInput(File input) {
        try {
            sentenceSplitter.configure(input);
            setReadStateLabel(sentenceSplitterConfReadLabel);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.warn("Error while reading SentenceSplitter input file.", e);
            setUnreadStateLabel(sentenceSplitterConfReadLabel);
        }
    }

    private void readEmotionInput(File input) {
        try {
            sentimentAnalyser.createEmotionDictionary(input);
            setReadStateLabel(emotionDictReadLabel);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.warn("Error while reading emotion dictionary input file.", e);
            setUnreadStateLabel(emotionDictReadLabel);
        }
    }

    private void readBoosterInput(File input) {
        try {
            sentimentAnalyser.createBoosterDictionary(input);
            setReadStateLabel(boosterDictReadLabel);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.warn("Error while reading booster dictionary input file.", e);
            setUnreadStateLabel(boosterDictReadLabel);
        }
    }

    private void readNegatingInput(File input) {
        try {
            sentimentAnalyser.createNegatingDictionary(input);
            setReadStateLabel(negatingDictReadLabel);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.warn("Error while reading negating dictionary input file.", e);
            setUnreadStateLabel(negatingDictReadLabel);
        }
    }

    @FXML
    public void handleReadInputButton() {
        if(!lemmatizer.isReady() || !sentenceSplitter.isReady() || !sentimentAnalyser.isReady()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("");
            alert.setContentText("Nie wszystkie pliki konfiguracyjne zostały wczytane poprawnie.");

            alert.showAndWait();
            return;
        }
        File file = readFile();
        if(file == null) {
            return;
        }

        InputFileReader inputFileReader = new InputFileReader();
        String content;
        try {
            content = inputFileReader.readFile(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("");
            alert.setContentText("Błędny format pliku wejściowego.");

            alert.showAndWait();
            return;
        } catch(Exception e) {
            logger.info("Error while loading file.", e);
            throw e;
        }

        fillSentimentTable(content);
    }

    @FXML
    public void handleReadLemmaDictButton() {
        File file = readFile();
        if(file == null) {
            return;
        }
        readLemmatizerInput(file);
    }

    @FXML
    public void handleReadSentenceSplitterConfButton() {
        File file = readFile();
        if(file == null) {
            return;
        }
        readSentenceSplitterInput(file);
    }

    @FXML
    public void handleReadEmotionDictButton() {
        File file = readFile();
        if(file == null) {
            return;
        }
        readEmotionInput(file);
    }

    @FXML
    public void handleReadBoosterDictButton() {
        File file = readFile();
        if(file == null) {
            return;
        }
        readBoosterInput(file);
    }

    @FXML
    public void handleReadNegatingDictButton() {
        File file = readFile();
        if(file == null) {
            return;
        }
        readNegatingInput(file);
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }
    
    private File readFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otwórz plik");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        return fileChooser.showOpenDialog(stage);
    }
    
    private void setReadStateLabel(Label label) {
        label.setText("Wczytano");
        label.setTextFill(Color.GREEN);
    }

    private void setUnreadStateLabel(Label label) {
        label.setText("Nie wczytano");
        label.setTextFill(Color.RED);
    }

    private void fillSentimentTable(String text) {
        TextIterator sentencesIterator = sentenceSplitter.getTextIterator(text);
        ratedSentences.clear();

        while(sentencesIterator.hasNext()) {
            String sentence = sentencesIterator.next();
            Double sentiment = sentimentAnalyser.calcSentiment(sentence);

            ratedSentences.add(new RatedSentence(sentiment, sentence));
        }

        emotionTable.setItems(ratedSentences);
    }

    private class RatedSentence {
        private Double sentiment;
        private String sentence;

        public RatedSentence(Double sentiment, String sentence) {
            this.sentiment = sentiment;
            this.sentence = sentence;
        }

        public Double getSentiment() {
            return sentiment;
        }

        public String getSentence() {
            return sentence;
        }
    }
}
