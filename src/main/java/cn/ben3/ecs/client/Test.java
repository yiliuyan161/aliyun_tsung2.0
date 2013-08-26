package cn.ben3.ecs.client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
       final WebView view = new WebView();
        WebEngine engine = view.getEngine();
        engine.load("http://www.baidu.com");
        root.getChildren().add(view);
        engine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov,
                                        Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {

                            snapshot(view);

                        }
                    }
                });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
    public void snapshot(Node view) {
        System.out.println(System.currentTimeMillis());
        Image image = view.snapshot(null, null);
        System.out.println(System.currentTimeMillis());

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png",
                    new File("f:\\" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}