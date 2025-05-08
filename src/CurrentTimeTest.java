/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author bidid
 */
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CurrentTimeTest extends VBox {
    
    public List<Label> Labels;
    public Label dateValueLabel;
    public Label timeValueLabel;
    public Label dateLabel;
    public Label timeLabel;

    public CurrentTimeTest() {
        initUI();
    }

    private void initUI() {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10));
        
        dateLabel = new Label("Date: ");
        timeLabel = new Label("Time: ");

        dateValueLabel = new Label();
        timeValueLabel = new Label();

        hbox.getChildren().addAll(dateLabel, dateValueLabel, timeLabel, timeValueLabel);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateDateTime(dateValueLabel, timeValueLabel))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        getChildren().add(hbox);
    }

    private void updateDateTime(Label dateLabel, Label timeLabel) {
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(dateFormatter);

        dateLabel.setText(formattedDate);

        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(timeFormatter);

        timeLabel.setText(formattedTime);
    }
    
    public List<Label> getLabels(){
        
        Labels = new ArrayList<>();
        Labels.add(dateLabel);
        Labels.add(dateValueLabel);
        Labels.add(timeLabel);
        Labels.add(timeValueLabel);
        
        return Labels;
    }
}
