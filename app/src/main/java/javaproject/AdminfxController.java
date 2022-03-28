package javaproject;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the controller class for the admin interface.
 * It handles all the user interactions.
 */
public class AdminfxController implements Initializable {
  DbHandler dbHandler = new DbHandler("root", "root");
  ErrorHandler errorDialog = new ErrorHandler();
  Stage stage;
  Stage loginStage; // Login-logout stage earlier called mainStage
  DbManager dbManager;
  Confirmation confirmation = new Confirmation();
  Room selectedEditRoom;

  private ObservableList<Room> roomTable = FXCollections.observableArrayList();
  private FilteredList<Room> filteredRooms = new FilteredList<>(roomTable, p -> true);
  private SortedList<Room> sortedRooms = new SortedList<>(filteredRooms);

  private ObservableList<User> userTable = FXCollections.observableArrayList();
  private FilteredList<User> filteredUser = new FilteredList<>(userTable, p -> true);
  private SortedList<User> sortedUser = new SortedList<>(filteredUser);

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Label usernameLabel;
  @FXML
  private VBox mainVbox;
  @FXML
  private VBox receptionistsVbox;
  @FXML
  private VBox roomsVbox;
  @FXML
  private VBox earningButtonBox;
  @FXML
  private VBox earningsVbox;
  @FXML
  private VBox settingsVbox;
  @FXML
  private HBox earningHbox;
  @FXML
  private VBox receptionistButtonBox;
  @FXML
  private VBox roomButtonbox;
  @FXML
  private VBox settingsButtonBox;
  @FXML
  private TableView<User> receptionistView;
  @FXML
  private TableView<Room> roomtableView;
  @FXML
  private TableColumn<User, String> firstnameColumn;
  @FXML
  private TableColumn<User, String> lastnameColumn;
  @FXML
  private TableColumn<User, String> passwordColumn;
  @FXML
  private TableColumn<User, String> usernameColumn;
  @FXML
  private TableColumn<User, Integer> phoneColumn;
  @FXML
  private TableColumn<User, String> emailColumn;
  @FXML
  private TableColumn<User, String> roleColumn;
  @FXML
  private TableColumn<Room, Integer> idColumn;
  @FXML
  private TableColumn<Room, String> capacityColumn;
  @FXML
  private TableColumn<Room, String> typeColumn;
  @FXML
  private TableColumn<Room, Integer> priceColumn;
  @FXML
  private TableColumn<Room, String> detailsColumn;
  @FXML
  private TableColumn<Room, Integer> locationColumn;
  @FXML
  private TextField firstnameField;
  @FXML
  private TextField lastnameField;
  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;
  @FXML
  private TextField phoneField;
  @FXML
  private TextField emailField;
  @FXML
  private TextField searchField;
  @FXML
  private TextField searchFieldStaff;
  @FXML
  private ComboBox<String> staffTypeCombox;
  @FXML
  private TextField roomPrice;
  @FXML
  private ComboBox<String> locationCombox;
  @FXML
  private ComboBox<String> capacityCombox;
  @FXML
  private ComboBox<String> typeCombox;
  @FXML
  private TextArea adminAnnoText;
  @FXML
  private TextArea adminDayText;
  @FXML
  private TextArea adminToDoText;
  @FXML
  private Button triggerUpdateAnno;
  @FXML
  private Button triggerSaveAnno;
  @FXML
  private VBox postItVbox;
  @FXML
  private CheckBox wifiCheckbox;
  @FXML
  private CheckBox tvCheckbox;
  @FXML
  private CheckBox minibarCheckbox;
  @FXML
  private CheckBox airconCheckbox;
  @FXML
  private CheckBox balconyCheckbox;
  @FXML
  private CheckBox phoneCheckbox;
  @FXML
  private CheckBox hairDryerCheckbox;
  @FXML
  private Label floorLabel;
  @FXML
  private TextField roomnoTextfield;
  @FXML
  private Label resultLabel;
  @FXML
  private CheckBox autoincrement;
  @FXML
  private GridPane typesParent;
  @FXML
  private GridPane capacityParent;
  @FXML
  private ComboBox<String> capacityCombo;
  @FXML
  private ComboBox<String> typesCombo;
  @FXML
  private ComboBox<String> locationCombo;
  @FXML
  private FlowPane roomtypesParent;
  @FXML
  private FlowPane roomcapacityParent;
  @FXML
  private FlowPane roomlocationParent;

  ArrayList<String> roomTypes = dbHandler.getSettings(1);
  ArrayList<String> roomCapacity = dbHandler.getSettings(2);
  ArrayList<String> roomLocation = dbHandler.getSettings(3);

  /**
   * Start screen of the admin scene. Reads receptionists from the database
   * and presents them in a tableview.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
    updateAnno();
    receptionistView.setPlaceholder(new Label(""));
    roomtableView.setPlaceholder(new Label(""));
    mainVbox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("usertype"));

    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

    staffTypeCombox.getItems().addAll("Receptionist", "Administrator");

    //First version of a search
    //Creates a filteredList class with the observablelist data

    //Adds a listener to the searchField and defines a predicate for the value entered.
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredRooms.setPredicate(room -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String searchValue = newValue.toLowerCase();

        if (String.valueOf(room.getId()).toLowerCase().contains(searchValue)) {
          return true;
        } else if (String.valueOf(room.getPrice()).toLowerCase().contains(searchValue)) {
          return true;
        } else if (room.getLocation().toLowerCase().contains(searchValue)) {
          return true;
        } else if (room.getCapacity().toLowerCase().contains(searchValue)) {
          return true;
        } else if (room.getType().toLowerCase().contains(searchValue)) {
          return true;
        }
        return false;
      });
    });

    sortedRooms.comparatorProperty().bind(roomtableView.comparatorProperty());
    roomtableView.setItems(sortedRooms);

    roomtableView.setOnMouseClicked((MouseEvent event) -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        try {
          selectedEditRoom = roomtableView.getSelectionModel().getSelectedItem();
          typeCombox.setValue(selectedEditRoom.getType());
          capacityCombox.setValue(selectedEditRoom.getCapacity());
          roomPrice.setText(String.valueOf(selectedEditRoom.getPrice()));
          locationCombox.setValue(selectedEditRoom.getLocation());
          String details = selectedEditRoom.getDetails();
          List<String> detailList = Arrays.asList(details.split(", "));
          fillCheckboxes(detailList);
        } catch (NullPointerException e) {
          System.out.println("NullPointerException when mouseclick on empty cell");
        }
      }
    });

    searchFieldStaff.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredUser.setPredicate(user -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String searchValue = newValue.toLowerCase();

        if (String.valueOf(user.getPhone()).toLowerCase().contains(searchValue)) {
          return true;
        } else if (user.getFirstname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (user.getLastname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (user.getEmail().toLowerCase().contains(searchValue)) {
          return true;
        }
        return false;
      });
    });

    //Binds the tableview column sorting to the sortedList.
    sortedUser.comparatorProperty().bind(receptionistView.comparatorProperty());
    receptionistView.setItems(sortedUser);

    autoincrement.setOnAction(e -> {
      Parent parent = autoincrement.getParent();
      for (Node node : ((HBox) parent).getChildren()) {
        if (!node.equals(autoincrement) && !node.equals(resultLabel)) {
          node.setDisable(autoincrement.isSelected());
        }
      }
      if (autoincrement.isSelected()) {
        resultLabel.setText("" + (roomTable.get(roomTable.size() - 1).getId() + 1));
      } else {
        resultLabel.setText("");
        roomnoTextfield.clear();
      }
    });

    roomnoTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != "") {
        try {
          Integer.parseInt(newValue);
          if (roomnoTextfield.getText().length() < 3) {
            resultLabel.setText(floorLabel.getText() + newValue);
          } else {
            roomnoTextfield.setText(oldValue);
          }
          
        } catch (Exception e) {
          roomnoTextfield.setText(oldValue);
        } 
      }
    });
  }

  /**
   * Adds a receptionist to the database.
   */
  @FXML
  private void addReceptionist(ActionEvent e) {
    if (firstnameField.getText().trim().isEmpty() || lastnameField.getText().trim().isEmpty()
        || usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()
        || emailField.getText().trim().isEmpty() || staffTypeCombox.getSelectionModel().isEmpty()) {
      errorDialog.start(new Text("Fields cannot be empty!"));
      firstnameField.clear();
      lastnameField.clear();
      usernameField.clear();
      passwordField.clear();
      phoneField.clear();
      emailField.clear();
      staffTypeCombox.getSelectionModel().clearSelection();
      return;
    }

    int id = dbHandler.createReceptionist((staffTypeCombox.getValue().equals("Receptionist") ? 0 : 1),
        firstnameField.getText(), lastnameField.getText(),
        usernameField.getText(), passwordField.getText(),
        Integer.parseInt(phoneField.getText()), emailField.getText());
    if (id != -1) {
      User receptionist = new User(id, staffTypeCombox.getValue(), firstnameField.getText(),
          lastnameField.getText(), usernameField.getText(),
          passwordField.getText(), Integer.parseInt(phoneField.getText()), emailField.getText());
      userTable.add(receptionist);
      firstnameField.clear();
      lastnameField.clear();
      usernameField.clear();
      passwordField.clear();
      phoneField.clear();
      emailField.clear();
      staffTypeCombox.getSelectionModel().clearSelection();
      confirmation.run("Successfully added a user.");
    } else {
      errorDialog.start(new Text("Employee not added!"));
    }
  }

  /**
   * Deletes a receptionist from the database.
   * Confirmation box appears to ensure deletion.
   */
  @FXML
  void deleteReceptionist(ActionEvent event) {
    User user = receptionistView.getSelectionModel().getSelectedItem();
    if (user != null) {
      BooleanErrors booleanError = new BooleanErrors();
      Button yes = booleanError.start(new Text("Are you sure?"));
      yes.setOnAction(f -> {
        clicked(true, user, "remove");
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
      });
    } else {
      errorDialog.start(new Text("Please select a user!"));
      return;
    }
  }

  /**
   * Checks if the buttons are clicked.
   */
  public void clicked(boolean answer, Object object, String action) {
    if (answer) {
      if (object instanceof User) {
        User temp = (User) object;
        int userIndex = receptionistView.getSelectionModel().getSelectedIndex();
        int sourceUserIndex = sortedUser.getSourceIndexFor(userTable, userIndex);
        userTable.remove(sourceUserIndex);
        dbHandler.remove("user", temp.getId());
      } else if (object instanceof Room) {
        Room temp = (Room) object;
        if (action.equals("remove")) {
          int roomIndex = roomtableView.getSelectionModel().getSelectedIndex();
          int sourceRoomIndex = sortedRooms.getSourceIndexFor(roomTable, roomIndex);
          roomTable.remove(sourceRoomIndex);
          dbHandler.remove("room", temp.getId());
          dbHandler.remove("room_details", temp.getId());
        } else if (action.equals("editRoom")) {
          dbHandler.editRoom(temp.getId(), capacityCombox.getSelectionModel().getSelectedIndex() + 1,
              typeCombox.getSelectionModel().getSelectedIndex() + 1,
              Integer.parseInt(roomPrice.getText()),
              locationCombox.getSelectionModel().getSelectedItem());
          return;
        }
      }
      confirmation.run("Successfully deleted a " + object.getClass().getSimpleName().toLowerCase() + ".");
    } else {
      return;
    }
  }

  /**
   * This method is called from "triggerUpdateAnno" which is a
   * actionevent when user presses "update" on the post-it.
   */
  void updateAnno() {
    ResultSet updatedAnno = dbHandler.select("postit", "*");
    try {
      while (updatedAnno.next()) {
        Postit postit = new Postit(updatedAnno.getString(2), updatedAnno.getString(3),
            updatedAnno.getString(4), updatedAnno.getString(5));

        adminAnnoText.setText(postit.getAnnouncement());
        adminDayText.setText(postit.getDaily());
        adminToDoText.setText(postit.getAdminTodo());
      }
      updatedAnno.close();
      dbHandler.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void triggerUpdateAnno(ActionEvent event) {
    updateAnno();
  }

  /**
   * This method is called from "initialize()" or if the save-button is pressed
   * in method below "triggerSaveAnno".
   */
  public void saveAnno() {
    String theTextAnno = adminAnnoText.getText();
    String theTextDaily = adminDayText.getText();
    String theTextTodo = adminToDoText.getText();
    dbHandler.insert("postit", "announce", theTextAnno, String.valueOf(1));
    dbHandler.insert("postit", "daily", theTextDaily, String.valueOf(1));
    dbHandler.insert("postit", "admintodo", theTextTodo, String.valueOf(1));
  }

  @FXML
  private void triggerSaveAnno(ActionEvent event) {
    saveAnno();
  }

  /**
   * Logout the admin and returns to start screen.
   */
  @FXML
  private void logout(ActionEvent e) {
    saveAnno();
    Stage stage = (Stage) receptionistView.getScene().getWindow();
    stage.close();
    loginStage.show();
  }

  /**
   * Sets Objects.
   *
   * @param stage     Stage of the gui.
   * @param dbManager Db manager.
   */

  public void setObjects(Stage stage, Stage loginStage, DbManager dbManager) {
    this.stage = stage;
    this.loginStage = loginStage;
    this.dbManager = dbManager;
    usernameLabel.setText(dbManager.get_username());
    stage.setOnHiding(e -> saveAnno());
  }

  @FXML
  private void showReceptionists(MouseEvent event) {
    ResultSet users = dbHandler.select("user", "*");
    userTable.clear();
    try {
      while (users.next()) {
        User user = new User(users.getInt(1), users.getInt(2) == 0 ? "Receptionist" : "Administrator",
            users.getString(4), users.getString(5),
            users.getString(3), users.getString(6),
            users.getInt(7), users.getString(8));

        userTable.add(user);
      }
      users.close();
      dbHandler.close();
    } catch (SQLException e) {
      System.out.println("SQL Error while iterating through guests. Line 305-313");
      e.printStackTrace();
    }
    vboxPressed(receptionistButtonBox, receptionistsVbox);
  }

  /**
   * Changes the background color of the background of the vboxes when pressed.
   *
   *
   * @param pressed vbox to be shown
   */
  void vboxPressed(VBox pressed, Parent selectedPanel) {
    VBox[] vboxList = { earningButtonBox, receptionistButtonBox, roomButtonbox, settingsButtonBox };
    Parent[] panelsList = {roomsVbox, receptionistsVbox, settingsVbox, earningsVbox};
    for (VBox vbox : vboxList) {
      if (vbox.equals(pressed)) {
        vbox.setStyle("-fx-background-color: #495057;");
      } else {
        vbox.setStyle("");
      }
    }
    for (Parent panel : panelsList) {
      if (!panel.equals(selectedPanel)) {
        panel.setVisible(false);
      } else {
        panel.setVisible(true);
      }
    }
  }

  @FXML
  private void showEarnings(MouseEvent event) {
    earningsVbox.setVisible(true);
    settingsVbox.setVisible(false);
    roomsVbox.setVisible(false);
    receptionistsVbox.setVisible(false);
  }

  @FXML
  private void showRooms(MouseEvent event) {
    updateComboBox(capacityCombox);
    updateComboBox(typeCombox);
    updateComboBox(locationCombox);
    ResultSet rooms = dbHandler.select("room", "*");
    roomTable.clear();
    try {
      while (rooms.next()) {
        Room room = new Room(rooms.getInt(1), roomCapacity.get(rooms.getInt(2) - 1),
            roomTypes.get(rooms.getInt(3) - 1), rooms.getInt(4), rooms.getString(5));
        roomTable.add(room);
      }
      rooms.close();
      dbHandler.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (Room room : roomTable) {
      room.setDetails(dbHandler.getRoomDetails(room.getId()));
    }
    vboxPressed(roomButtonbox, roomsVbox);
  }

  @FXML
  private void editRoom(ActionEvent event) {
    Room room = roomtableView.getSelectionModel().getSelectedItem();
    if (room != null) {
      BooleanErrors booleanError = new BooleanErrors();
      Button yes = booleanError.start(new Text("Are you sure?"));
      yes.setOnAction(f -> {
        clicked(true, room, "editRoom");
        ArrayList<Integer> selectedDetails = new ArrayList<Integer>();
        String details = getSelectedDetails(selectedDetails);
        dbHandler.remove("room_details", room.getId());
        for (int detail : selectedDetails) {
          dbHandler.insertDetailRelation(room.getId(), detail);
        }
        room.setDetails(details);
        room.setCapacity(capacityCombox.getSelectionModel().getSelectedItem());
        room.setLocation(locationCombox.getSelectionModel().getSelectedItem());
        room.setPrice(Integer.parseInt(roomPrice.getText()));
        room.setType(typeCombox.getSelectionModel().getSelectedItem());
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
        roomPrice.clear();
        roomtableView.refresh();
        confirmation.run("Successfully edited a room.");
      });
    } else {
      errorDialog.start(new Text("Please select a room!"));
      return;
    }

  }

  @FXML
  private void addRoom(ActionEvent event) {
    if (capacityCombox.getSelectionModel().isEmpty() || typeCombox.getSelectionModel().isEmpty()
        || locationCombox.getSelectionModel().isEmpty() || Integer.parseInt(resultLabel.getText()) / 100 == 0) {
      errorDialog.start(new Text("Fields cannot be empty!"));
      return;
    }
    int id = dbHandler.createRoom(resultLabel.getText(), capacityCombox.getSelectionModel().getSelectedIndex() + 1,
        typeCombox.getSelectionModel().getSelectedIndex() + 1, Integer.parseInt(roomPrice.getText()),
        locationCombox.getSelectionModel().getSelectedItem());
    if (id * -1 < 0) {
      Room room = new Room(id, capacityCombox.getSelectionModel().getSelectedItem(),
          typeCombox.getSelectionModel().getSelectedItem(),
          Integer.parseInt(roomPrice.getText()), locationCombox.getSelectionModel().getSelectedItem());
      ArrayList<Integer> selectedDetails = new ArrayList<Integer>();
      String details = getSelectedDetails(selectedDetails);
      for (int detail : selectedDetails) {
        dbHandler.insertDetailRelation(id, detail);
      }
      if (autoincrement.isSelected()) {
        resultLabel.setText("" + (id + 1));
      }
      room.setDetails(details);
      roomTable.add(room);
      roomPrice.clear();

      confirmation.run("Successfully added a room.");
    } else if (id == -2) {
      errorDialog.start(new Text("Room " + resultLabel.getText() + " already exists!"));
    } else {
      errorDialog.start(new Text("Room not added!"));
    }
  }

  private void fillCheckboxes(List<String> details) {
    wifiCheckbox.selectedProperty().set(false);
    tvCheckbox.selectedProperty().set(false);
    minibarCheckbox.selectedProperty().set(false);
    airconCheckbox.selectedProperty().set(false);
    balconyCheckbox.selectedProperty().set(false);
    phoneCheckbox.selectedProperty().set(false);
    hairDryerCheckbox.selectedProperty().set(false);
    for (String detail : details) {
      if (detail.equals("WiFi")) {
        wifiCheckbox.selectedProperty().set(true);
      } else if (detail.equals("TV")) {
        tvCheckbox.selectedProperty().set(true);
      } else if (detail.equals("Minibar")) {
        minibarCheckbox.selectedProperty().set(true);
      } else if (detail.equals("Air Conditioner")) {
        airconCheckbox.selectedProperty().set(true);
      } else if (detail.equals("Balcony")) {
        balconyCheckbox.selectedProperty().set(true);
      } else if (detail.equals("Phone")) {
        phoneCheckbox.selectedProperty().set(true);
      } else if (detail.equals("Allergy friendly")) {
        hairDryerCheckbox.selectedProperty().set(true);
      }
    }
  }

  private String getSelectedDetails(ArrayList<Integer> selectedDetailsId) {
    String selectedDetailsString = "";
    if (wifiCheckbox.isSelected()) {
      selectedDetailsId.add(1);
      selectedDetailsString += wifiCheckbox.getText() + ", ";
    }
    if (tvCheckbox.isSelected()) {
      selectedDetailsId.add(2);
      selectedDetailsString += tvCheckbox.getText() + ", ";
    }
    if (minibarCheckbox.isSelected()) {
      selectedDetailsId.add(3);
      selectedDetailsString += minibarCheckbox.getText() + ", ";
    }
    if (airconCheckbox.isSelected()) {
      selectedDetailsId.add(4);
      selectedDetailsString += airconCheckbox.getText() + ", ";
    }
    if (balconyCheckbox.isSelected()) {
      selectedDetailsId.add(5);
      selectedDetailsString += balconyCheckbox.getText() + ", ";
    }
    if (hairDryerCheckbox.isSelected()) {
      selectedDetailsId.add(6);
      selectedDetailsString += hairDryerCheckbox.getText() + ", ";
    }
    if (phoneCheckbox.isSelected()) {
      selectedDetailsId.add(7);
      selectedDetailsString += phoneCheckbox.getText() + ", ";
    }
    selectedDetailsString = selectedDetailsString.substring(0, selectedDetailsString.length() - 2);
    return selectedDetailsString;
  }

  @FXML
  private void deleteRoom(ActionEvent event) {
    Room room = roomtableView.getSelectionModel().getSelectedItem();
    if (room != null) {
      BooleanErrors booleanError = new BooleanErrors();
      Button yes = booleanError.start(new Text("Are you sure?"));
      yes.setOnAction(f -> {
        clicked(true, room, "remove");
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
      });
    } else {
      errorDialog.start(new Text("Please select a room!"));
      return;
    }
  }

  @FXML
  private void floor(MouseEvent event) {
    Label label = (Label) event.getSource();
    if (label.getText().equals("+")) {
      int temp = Integer.parseInt(floorLabel.getText()) + 1;
      if (temp >= 10) {
        return;
      }
      floorLabel.setText("" + temp);
    } else {
      int temp = Integer.parseInt(floorLabel.getText()) - 1;
      if (temp <= 0) {
        return;
      }
      floorLabel.setText("" + temp);
    }
    resultLabel.setText(floorLabel.getText() + roomnoTextfield.getText());
  }

  @FXML
  private void showSettings(MouseEvent event) {
    settingsHandler(event);
    vboxPressed(settingsButtonBox, settingsVbox);
  }

  @FXML
  private void settingsHandler(MouseEvent event) {
    FlowPane[] parentsinSettings = {roomtypesParent, roomcapacityParent, roomlocationParent};
    Node button = (Node) event.getSource();
    int i = 0;
    if (button.getId().equals("Save")) { // Save
      for (FlowPane parent : parentsinSettings) {
        i++;
        StringBuilder stringBuilder = new StringBuilder();
        for (Node textfield : parent.getChildren()) {
          if (!((TextField) textfield).getText().isEmpty()) {
            stringBuilder.append(((TextField) textfield).getText() + "/");
          }
        }
        dbHandler.insert("settings", "data", stringBuilder.toString(), Integer.toString(i));
        if (i == 3) {
          confirmation.run("Successfully saved new settings.");
        }
      }
    } else { // Undo
      for (FlowPane parent : parentsinSettings) {
        i++;
        ArrayList<String> arraylist = dbHandler.getSettings(i);
        for (int j = 0; j < parent.getChildren().size(); j++) {
          if (!arraylist.get(j).equals("Deleted")) {
            ((TextField) parent.getChildren().get(j)).setText(arraylist.get(j));
          } else {
            ((TextField) parent.getChildren().get(j)).setText("");
          }
        }
      }
    }
    
    roomTypes = dbHandler.getSettings(1);
    roomCapacity = dbHandler.getSettings(2);
    roomLocation = dbHandler.getSettings(3);

    updateComboBox(typesCombo);
    updateComboBox(capacityCombo);
    updateComboBox(locationCombo);
  }

  /**
   * Deletes string 'deleted' from arrayList.
   * 
   */
  private void updateComboBox(ComboBox<String> comboBox) {
    comboBox.getItems().clear();
    comboBox.getSelectionModel().clearSelection();
    switch (comboBox.getPromptText()) {
      case "Type":
        roomTypes.forEach(e -> {
          if (!e.equals("Deleted")) {
            comboBox.getItems().add(e);
          }
        });
        break;
      case "Capacity":
        roomCapacity.forEach(e -> {
          if (!e.equals("Deleted")) {
            comboBox.getItems().add(e);
          }
        });
        break;
      case "Location":
        roomLocation.forEach(e -> {
          if (!e.equals("Deleted")) {
            comboBox.getItems().add(e);
          }
        });
        break;
      default:
        break;
    }
  }
}