import client.MenuController;
import common.SpringUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        SpringUtils.setApplicationContext(context);
        String[] beans = context.getBeanDefinitionNames();

        Application.launch(App.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Analiza wydźwięku");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);

        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> showAppError());

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("client/menu.fxml"));
            BorderPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            MenuController controller = loader.getController();
            controller.setStage(primaryStage);

        } catch (IOException e) {
            showAppError();
            e.printStackTrace();
        }
    }

    private void showAppError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("");
        alert.setContentText("Wystąpił błąd aplikacji.");

        alert.showAndWait();
    }
}
