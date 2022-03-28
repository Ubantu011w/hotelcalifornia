package javaproject;

import java.lang.Thread;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Class that contains stage that shows up to confirm certain succesful tasks.
 * 
 */
public class Confirmation {
  
  /**
   * Shows a notification in the window that confirms certain successful tasks
   * and closes down after a timer goes off.
   *
   * @param text  to be sent
   */
  public void run(String text) {
    Label label = new Label(text);
    label.setWrapText(true);
    label.setPrefSize(290, 64);
    label.setAlignment(Pos.CENTER);
    label.setFont(Font.font(16));
    label.setTextFill(Color.WHITE);

    ProgressBar progressbar = new ProgressBar();
    progressbar.getStyleClass().add("notisProgress-bar");
    progressbar.setPrefSize(325, 5);
    progressbar.setProgress(0.0);

    VBox vbox = new VBox(label, progressbar);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPrefSize(335, 80);
    vbox.getStyleClass().add("notisContainer");
    vbox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

    FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), vbox);
    fadeInTransition.setFromValue(0.0);
    fadeInTransition.setToValue(1);
    fadeInTransition.play();
    Scene scene = new Scene(vbox);
    Stage stage = new Stage();
    stage.setX(Screen.getPrimary().getBounds().getWidth() - vbox.getPrefWidth() - 5);
    stage.setY(Screen.getPrimary().getBounds().getHeight() - 80);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setAlwaysOnTop(true);
    stage.setScene(scene);
    stage.getScene().setFill(Color.TRANSPARENT);

    Runnable runnable = () -> { 
      Timeline progressbarTimeline = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(progressbar.progressProperty(), 0)),
          new KeyFrame(Duration.seconds(3), e -> {
            stage.close();
          }, new KeyValue(progressbar.progressProperty(), 1))    
      );
      progressbarTimeline.play();
    };

    Thread thread = new Thread(runnable);
    AnimationTimer t = new AnimationTimer() {
      @Override
      public void handle(long now) {
          stage.setY(stage.getY() - 3.5);
          if (stage.getY() < Screen.getPrimary().getBounds().getHeight() - vbox.getPrefHeight() - 10 * 5) {
              this.stop();
              thread.start();
          }
      }
    };

    t.start();
    stage.initOwner((Stage) Stage.getWindows().get(0));
    stage.setTitle("Confirmation");
    stage.show();
  }
}