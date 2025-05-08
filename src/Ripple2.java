/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import java.time.Clock;
import java.time.DayOfWeek;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;
//import java.time.;

/**
 *
 * @author David Bidi
 */
public class Ripple2 extends Application {
    File soundFile1 = new File("barneySong.mp3");
        Media media1 = new Media(soundFile1.toURI().toString());
        MediaPlayer player1 = new MediaPlayer(media1);
    
    File soundFile2 = new File("sirenSound.mp3");
        Media media2 = new Media(soundFile2.toURI().toString());
        MediaPlayer player2 = new MediaPlayer(media2);
    private static final int WIDTH = 800;
    private static final int HEIGHT = 750;
    private static final int OBSERVER_X = WIDTH / 4;
    private static final int SOURCE_X = 3 * WIDTH / 4;
    private static final int Y_POSITION = HEIGHT / 2;
    private static final int WAVE_INITIAL_RADIUS = 10;
    private static final int WAVE_GROWTH_SPEED = 1; // Adjust growth speed
    private double initialObserverX = OBSERVER_X;
    private double initialSourceX = SOURCE_X;
    private static int WAVE_FREQUENCY = 25; // Adjust frequency of waves
    private static final double SPEED_OF_SOUND_340 = 340.0;
    private static final double SPEED_OF_SOUND_343 = 343.0;
    private double speedOfSound = SPEED_OF_SOUND_340;
    private static int WAVE_FREQUENCY_VALUE;

    private double observerSpeed = 0.0;
    private double sourceSpeed = 0.0;

    private Circle observer;
    private Circle source;
    private Group waveGroup;

    private int frameCount = 0;
    private Image image1;
    private Image image2;

    private Label observerSpeedLabel;
    private Label sourceSpeedLabel;
    private Label sourceFrequencyLabel;
    private Label resultLabel;

    private Slider observerSpeedSlider;
    private Slider sourceSpeedSlider;
    private Slider sourceFrequencySlider;

    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button clearButton;

    private AnimationTimer timer;

    private Canvas cartesianPlane;

    private StackPane animationPane;

    private static final int MAX_WAVES = 10;

    private LinkedList<Circle> waves = new LinkedList<>();

    @Override
    public void start(Stage primaryStage) {
        Group animationRoot = new Group();

        RadioButton radio340 = new RadioButton("Speed of Sound: 340 m/s");
        RadioButton radio343 = new RadioButton("Speed of Sound: 343 m/s");

        ToggleGroup soundSpeedGroup = new ToggleGroup();
        radio340.setToggleGroup(soundSpeedGroup);
        radio343.setToggleGroup(soundSpeedGroup);

        radio340.setSelected(true);

        radio340.setOnAction(e -> speedOfSound = SPEED_OF_SOUND_340);
        radio343.setOnAction(e -> speedOfSound = SPEED_OF_SOUND_343);

        VBox radioButtonsVBox = new VBox(5);
        radioButtonsVBox.getChildren().addAll(radio340, radio343);

        cartesianPlane = createCartesianPlane(WIDTH, HEIGHT);
        animationRoot.getChildren().add(cartesianPlane);

        observerSpeedSlider = createSpeedSlider(-0.99, 0.99, observerSpeed);
        sourceSpeedSlider = createSpeedSlider(-0.99, 0.99, sourceSpeed);
        sourceFrequencySlider = createFreqSlider(25, 50, 37.5);

        observerSpeedLabel = createLabel("Observer Speed: " + observerSpeed + " m/s");
        sourceSpeedLabel = createLabel("Source Speed: " + sourceSpeed + " m/s");
        sourceFrequencyLabel = createLabel("Frequency: " + " Medium" + " (75 Hz)");

        observerSpeedSlider.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            observerSpeed = observerSpeedSlider.getValue();
            observerSpeedLabel.setText("Observer Speed: " + String.format("%.0f %s", observerSpeed * 340 / 0.841633, "m/s"));
            updateResultLabel(calculateObservedFrequency());
        });
        sourceSpeedSlider.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            sourceSpeed = sourceSpeedSlider.getValue();
            sourceSpeedLabel.setText("Source Speed: " + String.format("%.0f %s", sourceSpeed * 340 / 0.841633, "m/s")); //* 340 / 1.63
            updateResultLabel(calculateObservedFrequency());
            if(sourceSpeedSlider.getValue() < 0 && sourceSpeedSlider.getValue() > -150){
                player1.setRate(sourceSpeedSlider.getValue()* -3);
                player1.seek(Duration.seconds(0));
                
            }
            if(sourceSpeedSlider.getValue() < -150){
                player1.setRate(sourceSpeedSlider.getValue()* -0.98);
                player1.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 0 && sourceSpeedSlider.getValue() < 150){
                player1.setRate(sourceSpeedSlider.getValue()* 4);
                player1.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 150){
                player1.setRate(sourceSpeedSlider.getValue()* 1.01);
                player1.seek(Duration.seconds(0));
               
            }
             if(sourceSpeedSlider.getValue() == 0){
                player1.setRate((sourceSpeedSlider.getValue() + 1) * 0.25);
                
                
            }
        });
        sourceFrequencySlider.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            double sliderValue = newvalue.doubleValue();

            // Check if the value is one of the allowed frequencies
            if (sliderValue == 25 || sliderValue == 37.5 || sliderValue == 50) {
                WAVE_FREQUENCY = (int) sliderValue;

                switch (WAVE_FREQUENCY) {
                    case 25:
                        sourceFrequencyLabel.setText("Frequency: " + String.format("%s", "High (100 Hz)"));
                        WAVE_FREQUENCY_VALUE = 100;
                        break;
                    case (int) 37.5:
                        sourceFrequencyLabel.setText("Frequency: " + String.format("%s", "Medium (75 Hz)"));
                        WAVE_FREQUENCY_VALUE = 75;
                        break;
                    case 50:
                        sourceFrequencyLabel.setText("Frequency: " + String.format("%s", "Low (50 Hz)"));
                        WAVE_FREQUENCY_VALUE = 50;
                        break;
                }

                updateResultLabel(calculateObservedFrequency());
            }
        });

        startButton = new Button("Start Animation");
        startButton.setOnAction(e -> startAnimation());

        stopButton = new Button("Stop Animation");
        stopButton.setOnAction(e -> stopAnimation());

        resetButton = new Button("Reset Animation");
        resetButton.setOnAction(e
                -> {
            resetAnimation();

            observerSpeedSlider.setValue(0);
            sourceSpeedSlider.setValue(0);
            sourceFrequencySlider.setValue(37.5);
        });

        clearButton = new Button("Clear waves");
        clearButton.setOnAction(e -> clearWaves());

        Button buttonBack = new Button();
        buttonBack.setAlignment(Pos.TOP_LEFT);

        buttonBack.setText("Back to Home");
        Main m1 = new Main();
        buttonBack.setOnAction(e
                -> {
            m1.start(primaryStage);
        });
        
       

        HBox controlButtons = new HBox(10);
        controlButtons.getChildren().addAll(startButton, stopButton, resetButton, clearButton);

        resultLabel = createLabel("Observed Frequency: " + "75.00 Hz");
        
        image1 = new Image("file:barney.png");
        image2 = new Image("file:sanic.png");

        observer = createCircle(OBSERVER_X, Y_POSITION, 20, Color.BLUE);
        observer.setFill(new ImagePattern(image2));

        source = createCircle(SOURCE_X, Y_POSITION, 20, Color.LIME);
        source.setFill(new ImagePattern(image1));
        waveGroup = new Group();

        animationRoot.getChildren().addAll(observer, source, waveGroup);

        BorderPane animationPane = new BorderPane();
        animationPane.setPrefWidth(WIDTH);
        animationPane.setPrefHeight(HEIGHT);
        animationPane.setCenter(animationRoot);
        animationPane.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Sanic And Barney", "thief and cop");
        comboBox.setOnAction(event-> {
            if(comboBox.getSelectionModel().getSelectedIndex() == 0){
                image1 = new Image("file:barney.png");
                image2 = new Image("file:sanic.png");
                
                 observer.setFill(new ImagePattern(image2));
                source.setFill(new ImagePattern(image1));
                
                sourceSpeedSlider.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            sourceSpeed = sourceSpeedSlider.getValue();
            sourceSpeedLabel.setText("Source Speed: " + String.format("%.0f %s", sourceSpeed * 340 / 0.841633, "m/s")); //* 340 / 1.63
            updateResultLabel(calculateObservedFrequency());
            if(sourceSpeedSlider.getValue() < 0 && sourceSpeedSlider.getValue() > -150){
                player1.setRate(sourceSpeedSlider.getValue()* -3);
                player1.seek(Duration.seconds(0));
                
            }
            if(sourceSpeedSlider.getValue() < -150){
                player1.setRate(sourceSpeedSlider.getValue()* -0.98);
                player1.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 0 && sourceSpeedSlider.getValue() < 150){
                player1.setRate(sourceSpeedSlider.getValue()* 4);
                player1.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 150){
                player1.setRate(sourceSpeedSlider.getValue()* 1.01);
                player1.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() == 0){
                player1.setRate((sourceSpeedSlider.getValue() + 1) * 0.25);
                
                
                
            }
        });
            }
            if(comboBox.getSelectionModel().getSelectedIndex() == 1){
                image1 = new Image("file:car.jpg");
                image2 = new Image("file:theif.jpg");
                
                observer.setFill(new ImagePattern(image2));
                source.setFill(new ImagePattern(image1));
                
                sourceSpeedSlider.valueProperty().addListener((observable, oldvalue, newvalue)
                -> {
            sourceSpeed = sourceSpeedSlider.getValue();
            sourceSpeedLabel.setText("Source Speed: " + String.format("%.0f %s", sourceSpeed * 340 / 0.841633, "m/s")); //* 340 / 1.63
            updateResultLabel(calculateObservedFrequency());
            if(sourceSpeedSlider.getValue() < 0 && sourceSpeedSlider.getValue() > -150){
                player2.setRate(sourceSpeedSlider.getValue()* -3);
                player2.seek(Duration.seconds(0));
                
            }
            if(sourceSpeedSlider.getValue() < -150){
                player2.setRate(sourceSpeedSlider.getValue()* -0.98);
                player2.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 0 && sourceSpeedSlider.getValue() < 150){
                player2.setRate(sourceSpeedSlider.getValue()* 4);
                player2.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() > 150){
                player2.setRate(sourceSpeedSlider.getValue()* 1.01);
                player2.seek(Duration.seconds(0));
                
            }
             if(sourceSpeedSlider.getValue() == 0){
                player2.setRate((sourceSpeedSlider.getValue() + 1) * 0.25);
                
                
                
            }
        });
            }
        });
        
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(20));
        controls.getChildren().addAll(buttonBack, observerSpeedLabel,observerSpeedSlider, 
                sourceSpeedLabel,sourceSpeedSlider, sourceFrequencyLabel,
                sourceFrequencySlider, radioButtonsVBox, 
                controlButtons, resultLabel, comboBox, new CurrentTimeTest());
        HBox mainCont = new HBox(20, controls, animationPane);
        mainCont.setPadding(new Insets(20));
        mainCont.setAlignment(Pos.CENTER);
        
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(mainCont, screenSize.getWidth(), screenSize.getHeight());
        scene.setFill(Color.BEIGE);

        primaryStage.setTitle("Physics Simulation On Doppler Effect");
        primaryStage.getIcons().add(new Image("file:PSODE.png.png"));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Slider createSpeedSlider(double min, double max, double initialValue) {
        Slider slider = new Slider(min, max, initialValue);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(10);
        slider.setBlockIncrement(0.01);
        slider.showTickMarksProperty().set(true);

        return slider;
    }

    private Slider createFreqSlider(double min, double max, double initialValue) {
        Slider slider = new Slider(min, max, initialValue);
        slider.setMajorTickUnit((max - min) / 2);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement((max - min) / 2);
        slider.setSnapToTicks(true);
        slider.showTickMarksProperty().set(true);

        return slider;
    }

    private void startAnimation() {
        if(image1.getUrl().contains("file:barney.png")){
        player1.seek(Duration.seconds(0));
        player1.play();
        }
        else {
            player2.seek(Duration.seconds(0));
            player2.play();
        }
        
        
        if (timer == null) {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    updatePositions();
                    frameCount++;

                    if (frameCount % WAVE_FREQUENCY == 0) {
                        createWave(source.getCenterX(), source.getCenterY());
                    }
                }
            };
            timer.start();
        }
    }

    private void stopAnimation() {
        if(image1.getUrl().contains("file:barney.png")){
            player1.stop();
        }
        else{
            player2.stop();
        }
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void resetAnimation() {
        stopAnimation();
        frameCount = 0;
        waveGroup.getChildren().clear();
        observer.setCenterX(OBSERVER_X);
        source.setCenterX(SOURCE_X);
    }

    private void clearWaves() {
        frameCount = 0;
        waveGroup.getChildren().clear();
    }

    private Circle createCircle(double x, double y, double radius, Color color) {
        Circle circle = new Circle(x, y, radius);
        circle.setFill(color);
        return circle;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    private void createWave(double x, double y) {
        Circle wave = new Circle(x, y, WAVE_INITIAL_RADIUS);
        wave.setFill(Color.TRANSPARENT);
        wave.setStroke(Color.RED);
        wave.setStrokeWidth(2);
        waveGroup.getChildren().add(wave);
        waves.addLast(wave);

        while (waves.size() > MAX_WAVES) {
            Circle oldestWave = waves.removeFirst();
            waveGroup.getChildren().remove(oldestWave);
        }

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(javafx.util.Duration.millis(10), e -> {
            double newRadius = wave.getRadius() + (WAVE_GROWTH_SPEED * 0.5);
            wave.setRadius(newRadius);

            if (wave.getCenterX() - newRadius < 0 || wave.getCenterX() + newRadius > WIDTH) {
                timeline.stop();
                waveGroup.getChildren().remove(wave);
                waves.remove(wave);
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updatePositions() {
        double observerMovement = adjustSpeedOfSound(observerSpeed);
        double sourceMovement = adjustSpeedOfSound(sourceSpeed);

        observer.setCenterX(observer.getCenterX() + observerMovement);
        source.setCenterX(source.getCenterX() + sourceMovement);

        // Check if observer hits the boundary with a 15-pixel margin
        if (observer.getCenterX() < 20 || observer.getCenterX() > WIDTH - 20) {
            // Reset positions upon hitting the boundary
            observer.setCenterX(initialObserverX);
            source.setCenterX(initialSourceX);
            // Clear old waves
            clearWaves();
        }

        // Check if source hits the boundary with a 15-pixel margin
        if (source.getCenterX() < 20 || source.getCenterX() > WIDTH - 20) {
            // Reset positions upon hitting the boundary
            observer.setCenterX(initialObserverX);
            source.setCenterX(initialSourceX);
            // Clear old waves
            clearWaves();
        }

        // Check for collision
        if (Math.abs(observer.getCenterX() - source.getCenterX()) < observer.getRadius() + source.getRadius()) {
            // Reset positions upon collision
            observer.setCenterX(initialObserverX);
            source.setCenterX(initialSourceX);
            // Clear old waves upon collision
            clearWaves();
        }

        // Remove waves that are 5 pixels before the boundary
        Iterator<Circle> iterator = waves.iterator();
        while (iterator.hasNext()) {
            Circle wave = iterator.next();
            double waveCenterX = wave.getCenterX();
            double waveRadius = wave.getRadius();

            if (waveCenterX - waveRadius < 25 || waveCenterX + waveRadius > WIDTH - 25) {
                waveGroup.getChildren().remove(wave);
                iterator.remove();
            }
        }
    }

    private Canvas createCartesianPlane(double width, double height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double xAxisStart = 0;
        double xAxisEnd = width;

        double gridSize = 20;
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        double gridCountX = halfWidth / gridSize;
        double gridCountY = height / gridSize;

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        for (int i = 1; i < gridCountX; i++) {
            double x = i * gridSize;
            gc.strokeLine(halfWidth + x, 0, halfWidth + x, height);
            gc.strokeLine(halfWidth - x, 0, halfWidth - x, height);
        }

        for (int i = 1; i < gridCountY; i++) {
            double y = i * gridSize;
            gc.strokeLine(0, halfHeight + y, width, halfHeight + y);
            gc.strokeLine(0, halfHeight - y, width, halfHeight - y);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine(xAxisStart, halfHeight, xAxisEnd, halfHeight);
        for (int i = 1; i < gridCountX; i += 2) {
            double x = i * gridSize;
            gc.strokeLine(halfWidth + x, halfHeight - 5, halfWidth + x, halfHeight + 5);
            gc.strokeLine(halfWidth - x, halfHeight - 5, halfWidth - x, halfHeight + 5);

            gc.setFill(Color.BLACK);
            gc.fillText(Integer.toString(i), halfWidth + x - 8, halfHeight + 20);
            gc.fillText(Integer.toString(-i), halfWidth - x - 8, halfHeight + 20);
        }

        gc.strokeLine(halfWidth, 0, halfWidth, height);
        for (int i = 1; i < gridCountY; i += 2) {
            double y = i * gridSize;
            gc.strokeLine(halfWidth - 5, halfHeight + y, halfWidth + 5, halfHeight + y);
            gc.strokeLine(halfWidth - 5, halfHeight - y, halfWidth + 5, halfHeight - y);

            gc.setFill(Color.BLACK);
            gc.fillText(Integer.toString(-i), halfWidth + 5, halfHeight + y + 4);
            gc.fillText(Integer.toString(i), halfWidth + 5, halfHeight - y + 4);
        }

        return canvas;
    }

    private double adjustSpeedOfSound(double value) {
        return speedOfSound == SPEED_OF_SOUND_340 ? value : value * (SPEED_OF_SOUND_343 / SPEED_OF_SOUND_340);
    }

    private double observedFrequencyObserverAwaySrcTo() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340 - (observerSpeed * 340 / 0.841633)) / (340 - (sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencyObserverToSrcAway() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340 + (observerSpeed * 340 / 0.841633)) / (340 + (sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencyBothApproaching() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340 + Math.abs(observerSpeed * 340 / 0.841633)) / (340 - Math.abs(sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencyBothReceding() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340 - Math.abs(observerSpeed * 340 / 0.841633)) / (340 + Math.abs(sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencySrcFromStationaryObserver() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340) / (340 - Math.abs(sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencySrcToStationaryObserver() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * (340) / (340 + Math.abs(sourceSpeed * 340 / 0.841633)));
        return observedFrequency;
    }

    private double observedFrequencyObsMovingAwayFromStationarySrc() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * ((340 - Math.abs(observerSpeed * 340 / 0.841633)) / (340)));
        return observedFrequency;
    }

    private double observedFrequencyObsMovingToStationarySrc() {
        double observedFrequency = Math.abs(WAVE_FREQUENCY_VALUE * ((340 + Math.abs(observerSpeed * 340 / 0.841633)) / (340)));
        return observedFrequency;
    }

    private double observedFrequencyStationaryBoth() {
        return WAVE_FREQUENCY_VALUE;
    }

    private void updateResultLabel(double observedFrequency) {
        resultLabel.setText("Observed Frequency: " + String.format("%.2f", observedFrequency) + " Hz");
    }

    private int determineDopplerEffectSituation() {
        if (observerSpeed > 0 && sourceSpeed == 0) { // -> -
            return 2;
        } else if (observerSpeed < 0 && sourceSpeed == 0) { // <- -
            return 3;
        } else if (observerSpeed == 0 && sourceSpeed > 0) { // - ->
            return 4;
        } else if (observerSpeed == 0 && sourceSpeed < 0) { // - <-
            return 5;
        } else if (observerSpeed > 0 && sourceSpeed < 0) { // -> <-
            return 6;
        } else if (observerSpeed < 0 && sourceSpeed > 0) { // <- ->
            return 7;
        } else if (observerSpeed < 0 && sourceSpeed < 0) { // <- <-
            return 8;
        } else if (observerSpeed < 0 && sourceSpeed < 0) { // -> ->
            return 9;
        } else {
            return 1;
        }
    }

    private double calculateObservedFrequency() {
        int situation = determineDopplerEffectSituation();
        double observedFrequency = WAVE_FREQUENCY_VALUE; // Default value

        switch (situation) {
            case 1:
                observedFrequency = observedFrequencyStationaryBoth();
                break;
            case 2:
                observedFrequency = observedFrequencyObsMovingToStationarySrc();
                break;
            case 3:
                observedFrequency = observedFrequencyObsMovingAwayFromStationarySrc();
                break;
            case 4:
                observedFrequency = observedFrequencySrcToStationaryObserver();
                break;
            case 5:
                observedFrequency = observedFrequencySrcFromStationaryObserver();
                break;
            case 6:
                observedFrequency = observedFrequencyBothApproaching();
                break;
            case 7:
                observedFrequency = observedFrequencyBothReceding();
                break;
            case 8:
                observedFrequency = observedFrequencyObserverAwaySrcTo();
                break;
            case 9:
                observedFrequency = observedFrequencyObserverToSrcAway();
                break;
            default:
                observedFrequency = WAVE_FREQUENCY_VALUE;
                break;
        }

        return observedFrequency;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
