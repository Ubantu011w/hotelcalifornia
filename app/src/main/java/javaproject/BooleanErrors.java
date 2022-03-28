package javaproject;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class that creates buttons and boxes.
 */
public class BooleanErrors {

  /**
  * Creates buttons and a box.
  */
  public Button start(Text text) {
    Stage errorDialog = new Stage(); // Error popup as a stage in order to block background program.
    final Double width = (Screen.getPrimary().getBounds().getMaxX());
    final Double height = (Screen.getPrimary().getBounds().getMaxY());
    errorDialog.setX(width / 2 - 20);
    errorDialog.setY(height / 2 - 10);
 
    Button yes = new Button("YES"); // Button inside of Hbox.
    yes.setMinWidth(60);
    yes.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    yes.getStyleClass().add("errorButton");
       
    
    Button no = new Button("NO"); // Button inside of Hbox.
    no.setMinWidth(60);
    no.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    no.getStyleClass().add("errorButton");
    no.setOnAction(f -> {  // When OK button is clicked, dialogwindow is closed and mainstage becomes available.
      errorDialog.close();
    });
    HBox confirmBox = new HBox();
    confirmBox.setSpacing(10);
    confirmBox.setAlignment(Pos.CENTER);
    confirmBox.setStyle(
        "-fx-padding: 15;");
    confirmBox.getChildren().addAll(yes, no); // Hbox for confirmation encapsulates button.
    Errors error = new Errors(text);
    VBox errorBox = error.basicError();
    errorBox.getChildren().add(confirmBox);
    errorDialog.initStyle(StageStyle.UNDECORATED); // No title bar on error prompt.
    Scene stageScene = new Scene(errorBox, 200, 120); // Dialog window width and height.
    errorDialog.setScene(stageScene);
    errorDialog.initModality(Modality.APPLICATION_MODAL); // This disables the mainstage while prompt is active.
    errorDialog.show();
    return yes;
  }
  
}
