package javaproject;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class handles the receptionist startpage gui.
 *
 */
public class RecepfxController implements Initializable {
  DbManager dbManager;
  DbHandler dbHandler = new DbHandler("root", "root");
  ErrorHandler errorDialog = new ErrorHandler();
  Confirmation confirmation = new Confirmation();
  BookingfxController bookingfxController;
  Guest selectedEdiGuest;
  Stage stage;
  Stage loginStage;
  Stage secondaryStage; // Either calendar or new booking
  Button maximizeButton; // Button shows up when a secondaryStage is showing and mainstage is not maximized

  
  private ObservableList<Arrival> arrivalTable = FXCollections.observableArrayList();

  private ObservableList<Arrival> departuresTable = FXCollections.observableArrayList();

  private ObservableList<Reservation> reservationTable = FXCollections.observableArrayList();

  private ObservableList<Guest> guestTable = FXCollections.observableArrayList();

  private ObservableList<User> staffTable = FXCollections.observableArrayList();

  //Wraps the observableList in a filteredList, used for searchfiltering. predicate set to true initially
  // meaning all data shows.
  private FilteredList<Guest> filteredGuests = new FilteredList<>(guestTable, p -> true);
  //Wrapping to a sortedList to allow for the sorting when column are pressed.
  private SortedList<Guest> sortedGuests = new SortedList<>(filteredGuests);

  //Wraps the observableList in a filteredList, used for searchfiltering. predicate set to true initially
  //meaning all data shows.
  private FilteredList<User> filteredStaff = new FilteredList<>(staffTable, p -> true);
  //Wrapping to a sortedList to allow for theh sorting when column are pressed.
  private SortedList<User> sortedStaff = new SortedList<>(filteredStaff);

  //Wraps the observableList in a filteredList, used for searchfiltering. predicate set to true initially
  //meaning all data shows.
  private FilteredList<Reservation> filteredReservations = new FilteredList<>(reservationTable, p -> true);
  //Wrapping to a sortedList to allow for theh sorting when column are pressed.
  private SortedList<Reservation> sortedReservations = new SortedList<>(filteredReservations);

  @FXML
  private VBox mainVbox;
  @FXML
  private AnchorPane anchorPane;
  @FXML
  private VBox newBookingButtonBox;
  @FXML
  private VBox reservationButtonBox;
  @FXML
  private VBox arrivalsButtonBox;
  @FXML
  private VBox guestsButtonBox;
  @FXML
  private VBox calendarButtonBox;
  @FXML
  private VBox staffButtonBox;
  @FXML
  private VBox housekeepingButtonBox;
  @FXML
  private Button arrivalButton;
  @FXML
  private VBox arrivaldeparturesVbox;
  @FXML
  private HBox arrivalHbox;
  @FXML
  private Text arrivalText;
  @FXML
  private TableView<Arrival> arrivalsView;
  @FXML
  private VBox reservationVbox;
  @FXML
  private TableView<Arrival> departureView;
  @FXML
  private TableView<Reservation> reservationView;
  @FXML
  private TableColumn<Reservation, String> guestIdReservationColumn;
  @FXML
  private TableColumn<Reservation, String> firstnameReservationColumn;
  @FXML
  private TableColumn<Reservation, String> lastnameReservationColumn;
  @FXML
  private TableColumn<Reservation, String> dateStartReservationColumn;
  @FXML
  private TableColumn<Reservation, String> dateEndReservationColumn;
  @FXML
  private TableColumn<Arrival, String> roomReservationColumn;
  @FXML
  private TableColumn<Reservation, String> costReservationColumn;
  @FXML
  private TableColumn<Arrival, String> lastnameArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> checkedInArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> emailArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> firstnameArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> phoneArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> reservationidArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> roomArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> costArrivalsColumn;
  @FXML
  private TableColumn<Arrival, String> roomDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> firstnameDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> lastnameDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> emailDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> phoneDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> checkedInDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> reservationidDepartureColumn;
  @FXML
  private TableColumn<Arrival, String> costDepartureColumn;
  @FXML
  private VBox guestsVbox;
  @FXML
  private TableView<Guest> guestsView;
  @FXML
  private TableColumn<Guest, String> idGuestsColumn;
  @FXML
  private TableColumn<Guest, String> firstnameGuestsColumn;
  @FXML
  private TableColumn<Guest, String> lastnameGuestsColumn;
  @FXML
  private TableColumn<Guest, String> emailGuestsColumn;
  @FXML
  private TableColumn<Guest, String> phoneGuestsColumn;
  @FXML
  private TableColumn<Guest, Integer> pointsGuestsColumn;
  @FXML
  private VBox staffVbox;
  @FXML
  private TableView<User> staffView;
  @FXML
  private TableColumn<User, String> firstnameStaffColumn;
  @FXML
  private TableColumn<User, String> lastnameStaffColumn;
  @FXML
  private TableColumn<User, String> phoneStaffColumn;
  @FXML
  private TableColumn<User, String> emailStaffColumn;
  @FXML
  private TableColumn<User, String> typeStaffColumn;
  @FXML
  private TableColumn<User, String> roleColumn;
  @FXML
  private Label usernameLabel;
  @FXML
  private Button logout;
  @FXML
  private TextField guestFirstname;
  @FXML
  private TextField guestLastname;
  @FXML
  private TextField guestEmail;
  @FXML
  private TextField guestPhone;
  @FXML
  private TextField guestSearchField;
  @FXML
  private TextField staffSearchField;
  @FXML
  private TextField reservationSearchField;
  @FXML
  private TextArea recepAnnoText;
  @FXML
  private TextArea recepDayText;
  @FXML
  private TextArea recepToDoText;
  @FXML
  private VBox receptionistPostIt;
  @FXML
  private Button triggerUpdateAnno;
  @FXML
  private Button triggerSaveAnno;
  
  /**
   * A listener used to keep track of width property of the main stage
   * Case changed: hide secondary and show button maximized.
   * Listener is removed when we show other panels
   */
  ChangeListener<? super Number> listener = new ChangeListener<>() {
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
      if (stage.isMaximized()) {
        if (secondaryStage.getTitle().equals("Calendar")) {
          showCalendar(null);
        } else {
          showHousekeeping(null);
        }
        secondaryStage.show();
        if (maximizeButton != null) {
          anchorPane.getChildren().remove(maximizeButton);
        }
      } else {
        if (secondaryStage.isShowing()) {
          secondaryStage.hide();
          maximizeButton = new Button();
          anchorPane.getChildren().add(maximizeButton);
          maximizeButton.setLayoutX(stage.getWidth() / 2);
          maximizeButton.setLayoutY(stage.getHeight() / 2 - 125);
          maximizeButton.getStyleClass().add("maximize");
          maximizeButton.setPrefSize(250, 250);
          maximizeButton.setOnAction(e -> maximize(e));
        }
      }
    }
  };
  
  /**
   * Do stuff upon initializing.
   *
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    updateAnno();
    arrivalsView.setPlaceholder(new Label(""));
    departureView.setPlaceholder(new Label(""));
    reservationView.setPlaceholder(new Label(""));
    guestsView.setPlaceholder(new Label(""));
    mainVbox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    guestIdReservationColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));
    firstnameReservationColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameReservationColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    dateStartReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
    dateEndReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
    roomReservationColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
    costReservationColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

    roomArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
    firstnameArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    emailArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    phoneArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    reservationidArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
    checkedInArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("checkin"));
    costArrivalsColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

    idGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    firstnameGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    emailGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    phoneGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    pointsGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

    firstnameStaffColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameStaffColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    phoneStaffColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    emailStaffColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    typeStaffColumn.setCellValueFactory(new PropertyValueFactory<>("usertype"));

    roomDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
    firstnameDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastnameDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    emailDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    phoneDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    reservationidDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
    checkedInDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("checkin"));
    costDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));


    guestsView.setOnMouseClicked((MouseEvent event) -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        selectedEdiGuest = guestsView.getSelectionModel().getSelectedItem();
        guestFirstname.setText(selectedEdiGuest.getFirstname());
        guestLastname.setText(selectedEdiGuest.getLastname());
        guestEmail.setText(selectedEdiGuest.getEmail());
        guestPhone.setText(String.valueOf(selectedEdiGuest.getPhone()));
      }
    });

    //Adds a listener to changes in the searchField, and defines a predicate for the filteredList.
    staffSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredStaff.setPredicate(user -> {
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
    sortedStaff.comparatorProperty().bind(staffView.comparatorProperty());
    staffView.setItems(sortedStaff);

    

    //Adds a listener to changes in the searchField, and defines a predicate for the filteredList.
    guestSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredGuests.setPredicate(guest -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String searchValue = newValue.toLowerCase();

        if (String.valueOf(guest.getId()).toLowerCase().contains(searchValue)) {
          return true;
        } else if (guest.getFirstname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (guest.getLastname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (guest.getEmail().toLowerCase().contains(searchValue)) {
          return true;
        }
        return false;
      });
    });

    sortedGuests.comparatorProperty().bind(guestsView.comparatorProperty());
    guestsView.setItems(sortedGuests);

    //Adds a listener to changes in the searchField, and defines a predicate for the filteredList.
    reservationSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredReservations.setPredicate(reservation -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String searchValue = newValue.toLowerCase();

        if (String.valueOf(reservation.getRoom()).toLowerCase().contains(searchValue)) {
          return true;
        } else if (reservation.getFirstname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (reservation.getLastname().toLowerCase().contains(searchValue)) {
          return true;
        } else if (reservation.getDateEnd().toLowerCase().contains(searchValue)) {
          return true;
        } else if (reservation.getDateEnd().toLowerCase().contains(searchValue)) {
          return true;
        }
        return false;
      });
    });

    sortedReservations.comparatorProperty().bind(reservationView.comparatorProperty());
    reservationView.setItems(sortedReservations);
  }
  
  /**
   * Called upon when user presses check in on arrivals.
   *
   * @param event Buttonpress "check in".
   */
  @FXML
  private void arrivalCheckin(ActionEvent event) {
    BooleanErrors booleanError = new BooleanErrors();
    Button yes = booleanError.start(new Text("Are you sure?"));
    yes.setOnAction(f -> {
      Arrival arrival = arrivalsView.getSelectionModel().getSelectedItem();
      if (dbHandler.insert("reservation", "checked_in", "1", String.valueOf(arrival.getReservationId()))) {
        arrival.setCheckin("Yes");
        arrivalTable.remove(arrival);
        arrivalsView.getItems().remove(arrival);
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
        confirmation.run("Successfully checked in.");
      } else {
        errorDialog.start(new Text("Checking in the guest has failed!"));
      }
      arrivalsView.refresh(); // refreshes table.
    });
  }

  /**
   * For departure checkoutbutton.
   *
   * @param event when checkoutbutton is clicked.
   */
  @FXML
  private void departureCheckout(ActionEvent event) {
    BooleanErrors booleanError = new BooleanErrors();
    Button yes = booleanError.start(new Text("Are you sure?"));
    yes.setOnAction(f -> {
      Arrival departure = departureView.getSelectionModel().getSelectedItem();
      if (dbHandler.insert("reservation", "checked_in", "0", String.valueOf(departure.getReservationId()))) {
        departure.setCheckin("No");
        departuresTable.remove(departure);
        departureView.getItems().remove(departure);
        dbHandler.remove("reservation", departure.getReservationId());
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
        confirmation.run("Successfully checked out.");
      } else {
        errorDialog.start(new Text("Checking in the guest has failed!"));
      }
      arrivalsView.refresh(); // refreshes table.
    });
  }

  @FXML
  private void showArrivals(MouseEvent event) {
    receptionistPostIt.setVisible(true);
    vboxPressed(arrivalsButtonBox, arrivaldeparturesVbox);
    arrivalsView.getItems().clear();
    departureView.getItems().clear();
    if (dbHandler.getArrivals(arrivalTable)) {
      arrivalsView.setItems(arrivalTable);
    } else {
      // TODO: "error here"
    }

    if (dbHandler.getDepartures(departuresTable)) {
      departureView.setItems(departuresTable);
    } else {
      // TODO: "error here"
    }

  }

  @FXML
  private void showReservations(MouseEvent event) {
    receptionistPostIt.setVisible(true);
    reservationTable.clear();
    vboxPressed(reservationButtonBox, reservationVbox);
    dbHandler.getReservations(reservationTable);
  }

  @FXML
  private void deleteReservation(ActionEvent event) {

    Reservation reservation = reservationView.getSelectionModel().getSelectedItem();
    if (reservation != null) {
      BooleanErrors booleanError = new BooleanErrors();
      Button yes = booleanError.start(new Text("Are you sure?"));
      yes.setOnAction(f -> {
        clicked(true, reservation);
        Stage thisStage = (Stage) yes.getScene().getWindow();
        thisStage.close();
        confirmation.run("Successfully deleted a reservation.");
      });
    } else {
      errorDialog.start(new Text("Please select a guest!"));
      return;
    }
  }

  void updateAnno() {
    ResultSet updatedAnno = dbHandler.select("postit", "*");
    try {
      while (updatedAnno.next()) {
        Postit postit = new Postit(updatedAnno.getString(2), updatedAnno.getString(3),
            updatedAnno.getString(4), updatedAnno.getString(5));
        recepAnnoText.setText(postit.getAnnouncement());
        recepDayText.setText(postit.getDaily());
        recepToDoText.setText(postit.getRecepTodo());
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

  public void saveAnno() {
    String theText = recepToDoText.getText();
    dbHandler.insert("postit", "receptodo", theText, String.valueOf(1));
  }

  @FXML
  private void triggerSaveAnno(ActionEvent event) {
    saveAnno();
  }

  @FXML
  private void editReservation(ActionEvent event) {
    // TODO: "Fill up"
  }

  /**
   * Method that checks if button is clicked.
   */
  public void clicked(boolean answer, Object object) {
    if (answer) {
      if (object instanceof Guest) {
        Guest temp = (Guest) object;
        int tableIndex = guestsView.getSelectionModel().getSelectedIndex();
        int sourceIndex = sortedGuests.getSourceIndexFor(guestTable, tableIndex);
        guestTable.remove(sourceIndex);
        dbHandler.remove("guest", temp.getId());
      } else if (object instanceof Reservation) {
        Reservation temp = (Reservation) object;
        int tableIndex = reservationView.getSelectionModel().getSelectedIndex();
        int sourceIndex = sortedReservations.getSourceIndexFor(reservationTable, tableIndex);
        reservationTable.remove(sourceIndex);
        dbHandler.remove("reservation", temp.getReservationId());
      }
    } else {
      return;
    }
  }

  /**
   * Called when user presses any button on the two slides.
   *
   * @param pressed       The pressed 'button'.
   * @param selectedPanel the panel to show up.
   * @param stage
   * 
   */
  void vboxPressed(VBox pressed, Parent selectedPanel) {
    VBox[] vboxList = {newBookingButtonBox, reservationButtonBox,
        arrivalsButtonBox, calendarButtonBox, guestsButtonBox, staffButtonBox, housekeepingButtonBox};
    Parent[] panelsList = {reservationVbox, arrivaldeparturesVbox, guestsVbox, staffVbox};
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
    if (maximizeButton != null) {
      anchorPane.getChildren().remove(maximizeButton);
    }
    // If we coming from a stage
    try {
      stage.widthProperty().removeListener(listener);
    } catch (Exception e) {
      // throws null pointer if stage has no listener.
    }
    if (secondaryStage != null) {
      secondaryStage.close();
    }
  }

  @FXML
  private void showCalendar(MouseEvent event) {
    receptionistPostIt.setVisible(false);
    if (!stage.isMaximized()) { 
      stage.setMaximized(true);
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    vboxPressed(calendarButtonBox, null);
    secondaryStage = new Stage();
    secondaryStage.setTitle("Calendar");
    secondaryStage.initOwner(stage);
    secondaryStage.setScene(new Scene(root));
    secondaryStage.initStyle(StageStyle.UNDECORATED);
    secondaryStage.setResizable(false);
    CalendarfxController controller = loader.getController();
    controller.setSize(stage.getWidth() - 334, stage.getHeight() - 276);
    secondaryStage.setY(stage.getY() + 250);
    secondaryStage.setX(stage.getX() + 250);
    stage.widthProperty().addListener(listener);
    secondaryStage.show();
  }

  @FXML
  private void showHousekeeping(MouseEvent event) {
    receptionistPostIt.setVisible(true);
    if (!stage.isMaximized()) { 
      stage.setMaximized(true);
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Housekeeping.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    vboxPressed(housekeepingButtonBox, null);
    secondaryStage = new Stage();
    secondaryStage.setTitle("Housekeeping");
    secondaryStage.initOwner(stage);
    secondaryStage.setScene(new Scene(root));
    secondaryStage.initStyle(StageStyle.UNDECORATED);
    secondaryStage.setResizable(false);
    HousekeepingfxController controller = loader.getController();
    controller.setSize(stage.getWidth() - 334, stage.getHeight() - 276);
    secondaryStage.setY(stage.getY() + 155);
    secondaryStage.setX(stage.getX() + 220);
    stage.widthProperty().addListener(listener); 
    secondaryStage.show(); 
  }

  /* Used to close all other windows when clicking on one of the tools.
  *
  */
  void closeWindow() {

  }

  /**
   * Show the booking window when user presses "bookings".
   *
   * @param event Buttonpress "bookings"
   */
  @FXML
  private void showBookingwindow(MouseEvent event) { // menu book a room
    receptionistPostIt.setVisible(false);
    if (!stage.isMaximized()) { 
      stage.setMaximized(true);
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Booking_room.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    vboxPressed(newBookingButtonBox, null);
    secondaryStage = new Stage();
    secondaryStage.setTitle("Booking");
    secondaryStage.initOwner(stage);
    secondaryStage.setScene(new Scene(root));
    secondaryStage.initStyle(StageStyle.UNDECORATED);
    secondaryStage.setX(stage.getWidth() / 3.6);
    secondaryStage.setY(stage.getHeight() / 3);
    stage.widthProperty().addListener(listener);
    
    secondaryStage.show();
  }

  @FXML
  private void showGuests(MouseEvent event) {
    receptionistPostIt.setVisible(true);
    ResultSet guests = dbHandler.select("guest", "*");
    guestTable.clear();
    try {
      while (guests.next()) {
        Guest guest = new Guest(guests.getInt(1), guests.getString(2),
            guests.getString(3), guests.getString(4),
            guests.getInt(5), guests.getInt(6));
        guestTable.add(guest);
      }
      guests.close();
      dbHandler.close();
    } catch (SQLException e) {
      System.out.println("SQL Error while iterating through guests. Line 305-313");
      e.printStackTrace();
    }

    vboxPressed(guestsButtonBox, guestsVbox);
  }

  /**
   * When the attached button is pressed removes the chosen guest entry.
   *
   * @param event Mousebutton click.
   */
  @FXML
  void deleteGuest(ActionEvent event) {
    Guest guest = guestsView.getSelectionModel().getSelectedItem();
    if (guest != null) {
      BooleanErrors booleanError = new BooleanErrors();
      Button yes = booleanError.start(new Text("Are you sure?"));
      yes.setOnAction(f -> {
        clicked(true, guest);
        Stage thisStage = (Stage) yes.getScene().getWindow();
        confirmation.run("Successfully deleted a guest.");
        thisStage.close();
      });
    } else {
      errorDialog.start(new Text("Please select a guest!"));
      return;
    }

  }

  /**
   * When pressed the button will edit an existing guest entry from the tableview.
   *
   * @param event Mousebutton click
   */
  @FXML
  void editGuest(ActionEvent event) {
    BooleanErrors booleanError = new BooleanErrors();
    Button yes = booleanError.start(new Text("Are you sure?"));

    yes.setOnAction(f -> {
      try {
        if (!guestFirstname.getText().trim().isEmpty() && !guestLastname.getText().trim().isEmpty()
            && !guestEmail.getText().trim().isEmpty() && !guestPhone.getText().trim().isEmpty()) {
          dbHandler.editGuest(guestFirstname.getText(), guestLastname.getText(), guestEmail.getText(),
              guestPhone.getText(),
              selectedEdiGuest.getId(), selectedEdiGuest);
          confirmation.run("Successfully edited a guest entry.");
        } else {
          if (guestFirstname.getText().trim().isEmpty() || guestLastname.getText().trim().isEmpty()
              || guestEmail.getText().trim().isEmpty() || guestPhone.getText().trim().isEmpty()) {
            guestFirstname.clear();
            guestLastname.clear();
            guestEmail.clear();
            guestPhone.clear();
            errorDialog.start(new Text("Fields cannot be empty!"));
          }
        }
  
        try {
          ResultSet guests = dbHandler.select("guest", "*");
          guestTable.clear();
          while (guests.next()) {
            Guest guest = new Guest(guests.getInt(1), guests.getString(2),
                guests.getString(3), guests.getString(4),
                guests.getInt(5), guests.getInt(6));
            guestTable.add(guest);
          }
          guests.close();
          dbHandler.close();
          dbHandler.resetAutoincrement("guest");
        } catch (SQLException e) {
          e.printStackTrace();
        }
        guestsView.setItems(guestTable);
  
        // clearing the textfields in the guest tableview
        guestFirstname.clear();
        guestLastname.clear();
        guestEmail.clear();
        guestPhone.clear();
      } catch (Exception e) {
        e.printStackTrace();
        errorDialog.start(new Text("Please select a guest!"));
      }
      Stage thisStage = (Stage) yes.getScene().getWindow();
      thisStage.close();
    });
  }

  @FXML
  private void showStaff(MouseEvent event) {
    receptionistPostIt.setVisible(true);
    staffTable.clear();
    vboxPressed(staffButtonBox, reservationVbox);
    ResultSet staff = dbHandler.select("user", "*");
    try {
      while (staff.next()) {
        User user = new User(staff.getInt(1), staff.getInt(2) == 0 ? "Receptionist" : "Administrator",
            staff.getString(4), staff.getString(5),
            staff.getString(3), staff.getString(6),
            staff.getInt(7), staff.getString(8));
        staffTable.add(user);    
      }
      staff.close();
      dbHandler.close();
    } catch (SQLException e) {
      System.out.println("SQL Error while iterating through guests. Line 305-313");
      e.printStackTrace();
    }

    vboxPressed(staffButtonBox, staffVbox);
    //Binds the tableview column sorting to the sortedList.
  }

  /**
   * When the correct button is pressed this method will be called to add a guest
   * entry in to the database
   * and the tableview.
   *
   * @param event MouseButton click on addGuest.
   */
  @FXML
  void addGuest(ActionEvent event) {
    try {
      if (!guestFirstname.getText().trim().isEmpty() && !guestLastname.getText().trim().isEmpty()
          && !guestEmail.getText().trim().isEmpty() && !guestPhone.getText().trim().isEmpty()) {
        dbHandler.createGuest(guestFirstname.getText(), guestLastname.getText(), guestEmail.getText(),
            guestPhone.getText());
        confirmation.run("Successfully added a guest.");
      } else {
        if (guestFirstname.getText().trim().isEmpty() || guestLastname.getText().trim().isEmpty()
            || guestEmail.getText().trim().isEmpty() || guestPhone.getText().trim().isEmpty()) {
          guestFirstname.clear();
          guestLastname.clear();
          guestEmail.clear();
          guestPhone.clear();
          errorDialog.start(new Text("Fields cannot be empty!"));
        }
      }

      guestTable.clear();
      try {
        ResultSet guests = dbHandler.select("guest", "*");
        while (guests.next()) {
          Guest guest = new Guest(guests.getInt(1), guests.getString(2),
              guests.getString(3), guests.getString(4),
              guests.getInt(5), guests.getInt(6));
          guestTable.add(guest);
        }
        guests.close();
        dbHandler.close();
        dbHandler.resetAutoincrement("guest");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      guestsView.setItems(guestTable);

      guestFirstname.clear();
      guestLastname.clear();
      guestEmail.clear();
      guestPhone.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Logsout the user and returns to login state.
   *
   * @param event Buttonpress "logout".
   */
  @FXML
  private void logout(ActionEvent event) {
    Stage stage = (Stage) arrivalButton.getScene().getWindow();
    stage.close();
    loginStage.show();
  }

  /**
   * Sets current and last stage.
   *
   */
  public void set_stages(Stage loginStage, Stage stage) {
    this.stage = stage;
    this.loginStage = loginStage;
  }

  /**
   * Sets welcome info and displays a welcome.
   *
   */
  public void set_info(DbManager dbManager) {
    this.dbManager = dbManager;
    usernameLabel.setText(dbManager.get_username());
  }

  private void maximize(ActionEvent event) {
    stage.setMaximized(true);
    anchorPane.getChildren().remove(event.getSource());
  }
}