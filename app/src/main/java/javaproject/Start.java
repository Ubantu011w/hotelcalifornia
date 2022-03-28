package javaproject;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Class that creates the login screen.
 */
public class Start extends Application {

  /**
   * Starts the program by setting upp the startscreen as primarystage.
   */
  public void start(Stage primaryStage) {

    // Setting the width and height of the scene to be always /2 and /1.4 of the
    // screen resolution.
    final Double width = (Screen.getPrimary().getBounds().getMaxX() / 2);
    final Double height = (Screen.getPrimary().getBounds().getMaxY() / 1.4);

    // Cutting the background picture into palmtree, clouds and an exit button.
    Image background = new Image("file:img/background.png");
    ImageView palmtree = new ImageView(background);
    ImageView clouds = new ImageView(background);
    ImageView exit = new ImageView(background);
    Rectangle2D cutTree = new Rectangle2D(0, 0, 400, 531);
    Rectangle2D cutClouds = new Rectangle2D(400, 0, 741, 470);
    Rectangle2D cutExit = new Rectangle2D(676, 470, 716, 514); // W:38 H:41
    palmtree.setViewport(cutTree);
    clouds.setViewport(cutClouds);
    exit.setViewport(cutExit);

    // Setting the location for the palmtree, clouds and exit button.
    palmtree.setTranslateY(height / 5);
    clouds.setTranslateY(-130);
    exit.setTranslateX((width - 45));
    exit.setTranslateY(-3);
    exit.setPickOnBounds(true); // This is used to detect mouse click on the transparent area of the "exit" button
    exit.setOnMouseClicked(e -> {
      primaryStage.close();
    });

    // animating the palmtree
    FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(4), palmtree);
    fadeInTransition.setFromValue(0.0);
    fadeInTransition.setToValue(1.0);
    fadeInTransition.play();

    // Creating the logo
    Image logo = new Image("file:img/logoWhite.png");
    ImageView logoView = new ImageView(logo);

    // putting the logo into one VBox.
    VBox logoBox = new VBox(logoView);
    logoBox.setSpacing(20);
    logoBox.setTranslateX(width / 2 - 250);

    // Animating the 3 figures in background.png using keyframes
    KeyValue initText = new KeyValue(logoBox.translateYProperty(), height / 1.5);
    KeyValue initclouds = new KeyValue(clouds.translateXProperty(), width);

    KeyFrame initFrame = new KeyFrame(Duration.seconds(0), initText);
    KeyFrame initFrameclouds = new KeyFrame(Duration.seconds(0), initclouds);

    KeyValue endKeyValue = new KeyValue(logoBox.translateYProperty(), (height / 2) - 240);
    KeyValue endkeyclouds = new KeyValue(clouds.translateXProperty(), width / 2 * -1); // -400

    KeyFrame endFrame = new KeyFrame(Duration.seconds(2), endKeyValue);
    KeyFrame endFrameClouds = new KeyFrame(Duration.seconds(25), endkeyclouds);
    Timeline timeline = new Timeline(initFrame, endFrame);
    Timeline timelineclouds = new Timeline(initFrameclouds, endFrameClouds);

    timelineclouds.setCycleCount(Animation.INDEFINITE); // set the clouds animation to loop for ever.
    timelineclouds.play();
    timeline.play();

    Text user = new Text("Username");
    user.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 18));
    user.setFill(Color.WHITE);
    Text pass = new Text("Password");
    pass.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 18));
    pass.setFill(Color.WHITE);

    TextField username = new TextField();
    username.setStyle(
        "-fx-border-color: white;"
            + "-fx-border-width: 1;"
            + "-fx-border-radius: 4;"
            + "-fx-background-radius: 0;"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: white");
    username.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 14));
    HBox userBox = new HBox(user, username);
    userBox.setSpacing(10);

    PasswordField password = new PasswordField();
    password.setStyle(
        "-fx-border-color: white;"
            + "-fx-border-width: 1;"
            + "-fx-border-radius: 4;"
            + "-fx-background-radius: 0;"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: white");
    password.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 14));
    HBox passBox = new HBox(pass, password);
    passBox.setSpacing(14.5);

    Button login = new Button("Login"); // Login button design.
    login.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    login.setStyle(
        "-fx-border-color: white;"
            + "-fx-border-width: 1;"
            + "-fx-border-radius: 4;"
            + "-fx-cursor: hand;"
            + "-fx-background-radius: 0;"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: white");

    Button ok = new Button("OK"); // Ok button design.
    ok.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    ok.setStyle(
        "-fx-border-color: white;"
            + "-fx-border-width: 2;"
            + "-fx-border-radius: 4;"
            + "-fx-background-radius: 0;"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: white");

    Text answer = new Text("Incorrect login credentials"); // Warning in the form of a text prompt.
    answer.setFont(Font.font("Berlin Sans FB", FontWeight.NORMAL, 16));
    answer.setFill(Color.WHITE);

    VBox errorBox = new VBox(); // Errormessage design, functions and location.
    errorBox.setMaxWidth(1000);
    errorBox.setMaxHeight(300);
    errorBox.setSpacing(10);
    errorBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    errorBox.setStyle(
        "-fx-padding: 10;"
            + "-fx-border-style: solid inside;"
            + "-fx-border-width: 2;"
            + "-fx-border-radius: 4;"
            + "-fx-border-color: white;");

    ok.setMaxWidth(1000);
    ok.setMaxHeight(300);
    errorBox.setTranslateX(width / 2.7);
    errorBox.setTranslateY(height / 1.5);
    errorBox.getChildren().addAll(answer, ok);
    errorBox.setVisible(false); // Visibility set to false until user enters wrong credentials.

    HBox loginBox = new HBox(login);
    loginBox.setSpacing(10);

    VBox inputs = new VBox(userBox, passBox, loginBox); // Login design, functions and location.
    inputs.setTranslateX(width / 2.7);
    inputs.setTranslateY(height / 1.5);
    inputs.setSpacing(15);

    DbHandler dbHandler = new DbHandler("root", "root"); // Creating an instance of dbHandler
    login.setOnAction(e -> {
      int usertype = dbHandler.login(username.getText(), password.getText());
      if (usertype == 0) {
        try {
          DbManager dbManager = new DbManager(username.getText(), usertype);
          FXMLLoader loader = new FXMLLoader(getClass().getResource("Receptionist.fxml"));
          Stage mainStage = new Stage();
          Parent root = loader.load();
          RecepfxController controller = loader.getController();
          controller.set_stages(primaryStage, mainStage);
          controller.set_info(dbManager);
          Scene scene = new Scene(root);
          primaryStage.close();
          mainStage.initStyle(StageStyle.DECORATED);
          mainStage.setScene(scene);
          mainStage.setTitle("Receptionist");
          mainStage.getIcons().add(new Image("file:img/iconBlack.png"));
          mainStage.setMaximized(true);
          mainStage.show();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else if (usertype == 1) { // Here we start the adminstrator fxml
        try {
          DbManager dbManager = new DbManager(username.getText(), usertype);
          FXMLLoader loader = new FXMLLoader(getClass().getResource("Administrator.fxml"));
          Stage mainStage = new Stage();
          Parent root = loader.load();
          AdminfxController controller = loader.getController();
          Stage stage = new Stage();
          controller.setObjects(stage, primaryStage, dbManager);
          Scene scene = new Scene(root);
          primaryStage.close();
          mainStage.initStyle(StageStyle.DECORATED);
          mainStage.setScene(scene);
          mainStage.setTitle("Administrator");
          mainStage.getIcons().add(new Image("file:img/iconBlack.png"));
          mainStage.setMaximized(true);
          mainStage.show();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else {
        errorBox.setVisible(true); // Error popup replaces the login screen.
        inputs.setVisible(false);

        ok.setOnAction(f -> { // When OK button is pressed, the receptionist returns to the login screen.
          errorBox.setVisible(false);
          inputs.setVisible(true);
        });
      }
    });

    Pane root = new Pane(clouds, palmtree, inputs, errorBox, logoBox, exit);
    // Using css script to color the background of our main Pane
    root.setStyle("-fx-background-color: #495057ff");
    Scene scene = new Scene(root, width, height);
    primaryStage.initStyle(StageStyle.UNDECORATED); // Hide the title bar
    primaryStage.setScene(scene);
    primaryStage.setTitle("Välkomstfönster");
    primaryStage.show();
  }
}