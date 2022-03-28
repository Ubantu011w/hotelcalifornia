package javaproject;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class that creates the different boxes.
 */
public class Errors {
  Text text;

  /**
   * Constructor that initialize text field.
   */
  public Errors(Text text) {
    this.text = text;
  }

  /**
   * Creates a VBox.
   */
  public VBox basicError() {

    text.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16)); // Font for string argument.
    text.setFill(Color.WHITE); // Fill for string argument.

    VBox errorBox = new VBox(); // VBox that contains an HBox, and an Hbox that contains a button.
    errorBox.setAlignment(Pos.CENTER);
    errorBox.getChildren().add(text);
    errorBox.setStyle(
        "-fx-padding: 10;"
        + "-fx-border-color: #212529;" // Darkgray colour.
        + "-fx-border-width: 2;" 
        + "-fx-background-radius: 0;" 
        + "-fx-background-color: #343a40;" // Lightgray colour.
        + "-fx-text-fill: white");

    HBox buttonBox = new HBox(); // Hbox inside of Vbox.
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setStyle(
        "-fx-padding: 15;");

    HBox confirmBox = new HBox(10); // Hbox inside of Vbox.
    confirmBox.setAlignment(Pos.CENTER);
    confirmBox.setStyle(
        "-fx-padding: 20;");
    errorBox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    return errorBox;
  }
}
