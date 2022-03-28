package javaproject;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class that is called where error handling is required.
 * It creates error dialog popups.
 */
public class ErrorHandler {

  /**
   * Function that creates error dialogs when called. 
   * Args consist of error message and dialogbox coordinates.
   */
  public void start(Text text) { // Arguments for errormessage and coordinates.
    Stage errorDialog = new Stage(); // Error popup as a stage in order to block background program.
    final Double width = (Screen.getPrimary().getBounds().getMaxX());
    final Double height = (Screen.getPrimary().getBounds().getMaxY());
    errorDialog.setX(width / 2 - 20);
    errorDialog.setY(height / 2 - 10);
    
    text.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16)); // Font for string argument.
    text.setFill(Color.WHITE); // Fill for string argument.

    HBox buttonBox = new HBox(); // Hbox inside of Vbox.
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setStyle(
        "-fx-padding: 15;");

    Button ok = new Button("OK"); // Button inside of Hbox.
    ok.setMinWidth(100);
    ok.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    ok.getStyleClass().add("errorButton");
    ok.setOnAction(f -> { // When OK button is clicked, dialogwindow is closed and mainstage becomes available.
      errorDialog.close();
    });
    
    Errors error = new Errors(text);
    VBox errorBox = error.basicError();
    buttonBox.getChildren().add(ok); // Hbox for warning encapsulates button.
    errorBox.getChildren().add(buttonBox);
    errorDialog.initStyle(StageStyle.UNDECORATED); // No title bar on error prompt.
    Scene stageScene = new Scene(errorBox, 200, 120); // Dialog window width and height.
    errorDialog.setScene(stageScene);
    errorDialog.initModality(Modality.APPLICATION_MODAL); // This disables the mainstage while prompt is active.
    errorDialog.show();
  }

}