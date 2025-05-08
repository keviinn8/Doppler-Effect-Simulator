/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        Group group1 = new Group();
        group1.setLayoutX(screenSize.getWidth() / 4);
        group1.setLayoutY(screenSize.getHeight() / 4);
        Scene Scene1 = new Scene(group1, screenSize.getWidth(), screenSize.getHeight());
        //Scene1.setFill(Color.web("#8A2BE2"));

        Button allMovingButton = new Button();
        allMovingButton.setText("Start of the Simulation");
        allMovingButton.setStyle("-fx-font: bold italic 15pt \"Arial\";" + "-fx-pref-height: 80px;"
                + "-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 );" + "-fx-background-color: red;");
        allMovingButton.setOnAction(e -> {
            Ripple2 ripple2 = new Ripple2();
            ripple2.start(primaryStage);
        });

        Label titleLabel = new Label("  Physics Simulation Doppler \n                   Effect");
        titleLabel.setStyle("-fx-font-size: 57px;" + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
                + "-fx-alignment: center");

        HBox hbox = new HBox(10, allMovingButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));
        

        CurrentTimeTest timeDate = new CurrentTimeTest();
        List<Label> time = timeDate.getLabels(); 
        for (Label label : time) {
            label.setStyle("-fx-text-fill: white;");
        }

        VBox vbox = new VBox(10, timeDate, titleLabel, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Image background = new Image("file:soundBack.gif");
        ImageView background1 = new ImageView(background);
        background1.setFitWidth(screenSize.getWidth());
        background1.setFitHeight(screenSize.getHeight());
        background1.setLayoutX(-384);
        background1.setLayoutY(-200);

        group1.getChildren().addAll(background1, vbox);
        

        Image icon = new Image("file:PSODE.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(Scene1);
        primaryStage.setTitle("Physics Simulation On Doppler Effect");
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
