package javaproject;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the controller class for the booking interface.
 * It handles all the user interactions.
 */

public class BookingfxController implements Initializable {
  DbHandler dbHandler = new DbHandler("root", "root");
  ErrorHandler errorDialog = new ErrorHandler();
  Confirmation confirmation = new Confirmation();

  @FXML
  private ComboBox<String> rooms;
  @FXML
  private TextField firstname;
  @FXML
  private TextField lastname;
  @FXML
  private TextField email;
  @FXML
  private TextField phone;
  @FXML
  private DatePicker dateFrom;
  @FXML
  private DatePicker dateEnd;
  @FXML
  public VBox guestinput;
  @FXML
  public VBox bookingcalendar;
  @FXML
  private TableView<Room> roomView;
  @FXML
  private TableColumn<Room, String> roomColumn;
  @FXML
  private TableColumn<Room, String> capacityRoomColumn;
  @FXML
  private TableColumn<Room, String> typeRoomColumn;
  @FXML
  private TableColumn<Room, String> priceColumn;
  @FXML
  private TableColumn<Room, String> locationRoomColumn;
  @FXML
  private TableColumn<Room, String> detailRoomColumn;
  @FXML
  private Button dateSubmitButton;
  @FXML
  private Button bookingButton;
  @FXML
  private Button nextButton;
  @FXML
  private Label bookingTotalPrice;
  @FXML
  private Label totalPriceInBooking;
  @FXML
  private Label roomNoInBooking;
  @FXML
  private Label dateFromInBooking;
  @FXML
  private Label confirmLabel;
  @FXML
  private VBox parent;
  @FXML
  private Label dateToInBooking;
  @FXML
  private Button bookingCancelButton;

  private ObservableList<Room> roomTable = FXCollections.observableArrayList();


  LocalDate from;
  LocalDate to; 
  Room room;
  Reservation reservationCalendar;

  /**
   * Loading some values on initializing.
   * 
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    roomView.setPlaceholder(new Label(""));
    parent.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    dateFrom.setValue(LocalDate.now());
    dateEnd.setValue(dateFrom.getValue().plusDays(1));

    roomColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    capacityRoomColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    typeRoomColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    locationRoomColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    detailRoomColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

    email.textProperty().addListener((observable, oldValue, newValue) -> {
      if (email.getText().contains("@")) {
        checkIfGuestExists(null);
      }
    });

    phone.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != "") {
        try {
          Integer.parseInt(newValue);
        } catch (Exception e) {
          phone.setText(oldValue);
        }
      }
    });

    roomView.setOnMouseClicked((MouseEvent event) -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        if (!roomView.getSelectionModel().isEmpty()) {
          bookingTotalPrice();
        }
      }
    });
  }
  
  @FXML
  private void bookingTotalPrice() {
    Room room = roomView.getSelectionModel().getSelectedItem();
    int price = room.getPrice();
    int days = dateEnd.getValue().compareTo(dateFrom.getValue());
    bookingTotalPrice.setText(Integer.toString(price * days));
  }
  /**
   * Button to create a reservation in the end of booking.
   *
   * @param event mouseclick event
   */

  @FXML
  private void bookingButton(ActionEvent event) {
    int nights = (int) ChronoUnit.DAYS.between(dateFrom.getValue(), dateEnd.getValue());
    if (from != null) { // if it was called from calendar
      nights = (int) ChronoUnit.DAYS.between(from, to);
      dateFrom.setValue(from);
      dateEnd.setValue(to);
      
    } else {
      room = roomView.getSelectionModel().getSelectedItem();
    }
    if (checkGuestFields()) {
      int resId = dbHandler.createReservation(firstname.getText(), room.getId(), 1,
          dateFrom.getValue().toString(), dateEnd.getValue().toString(), room.getPrice() * nights);
      if (resId != -1) {
        String[] guest = dbHandler.getGuest(email.getText());
        int points = (Integer.parseInt(guest[5])) + 1;
        dbHandler.insert("guest", "points", String.valueOf(points), guest[0]);

        firstname.clear();
        lastname.clear();
        email.clear();
        phone.clear();

        BookingData bd = new BookingData(resId, guest[1], guest[2], Integer.parseInt(guest[4]),  
            guest[3], dateFrom.getValue().toString(), dateEnd.getValue().toString(),
            room.getId(), capacityIndex(room.getCapacity()), room.getLocation(), 
            dbHandler.getRoomDetails(room.getId()), room.getPrice(), room.getPrice() * nights, nights); 
        BookingConfirmation bc = new BookingConfirmation(bd);
        Thread pdfThread = new Thread(bc);
        pdfThread.start();
        Stage thisStage = (Stage) firstname.getScene().getWindow();
        if (from != null) {
          reservationCalendar = new Reservation(0, guest[1], guest[2], from.toString(),
            to.toString(), room.getId(), 0, resId);
        }
        confirmation.run("Booking was successful.");
        thisStage.close();

      } else {
        errorDialog.start(new Text("Booking has failed!"));
      }
    }
  }

  /**
   * Check if guest exist in database by checking the entered e-mail and autofill
   * the fields.
   *
   * @param event mouseclick event
   */
  @FXML
  private void checkIfGuestExists(ActionEvent event) {
    String[] guestInfo = dbHandler.getGuest(email.getText());
    if (guestInfo != null) {
      firstname.setText(guestInfo[1]);
      lastname.setText(guestInfo[2]);
      phone.setText(guestInfo[4]);
    } else {
      // uncheck icon label here instead
      /* errorDialog.start(new Text("Guest does not exist.")); */
    }
  }

  @FXML
  private void nextButton(ActionEvent event) {
    Room room = roomView.getSelectionModel().getSelectedItem();
    if (room != null) {
      guestinput.setVisible(true);
      bookingcalendar.setVisible(false);
      int days = dateEnd.getValue().compareTo(dateFrom.getValue());
      totalPriceInBooking.setText(Integer.toString(room.getPrice() * days) + "kr");
      roomNoInBooking.setText(Integer.toString(room.getId()));
      dateFromInBooking.setText(dateFrom.getValue().toString());
      dateToInBooking.setText(dateEnd.getValue().toString());
    } else {
      errorDialog.start(new Text("Must choose a room!"));
    }
  }
  
  @FXML
  private void bookingCancelButton(ActionEvent event) {
    Stage thisStage = (Stage) firstname.getScene().getWindow();
    guestinput.setVisible(false);
    bookingcalendar.setVisible(false);
    thisStage.close();
  }

  /**
   * Booking window, enter guest name and press check, if guest does not exist,
   * create guest.
   *
   * @param event mouseclick event
   */
  private Boolean checkGuestFields() {
    if (checkGuest()) {
      guestinput.setVisible(false);
      bookingcalendar.setVisible(false);
      return true;
    }
    if ((!firstname.getText().trim().isEmpty() && !lastname.getText().trim().isEmpty()
        && !email.getText().trim().isEmpty() && !phone.getText().trim().isEmpty()) && !checkGuest()) {
      dbHandler.createGuest(firstname.getText(), lastname.getText(), email.getText(), phone.getText());
      // confirmation.confirms("Information is valid, proceed to the booking date", confirmBox, confirmLabel);
      // guestinput.setVisible(false);
      // bookingcalendar.setVisible(false);
      return true;
    } else {
      if (firstname.getText().trim().isEmpty() || lastname.getText().trim().isEmpty()
          || email.getText().trim().isEmpty() || phone.getText().trim().isEmpty()) {
        // bookingcalendar.setVisible(false);
        // guestinput.setVisible(false);
        errorDialog.start(new Text("Fields cannot be empty!"));
        return false;
      }
    }
    return null;
  }

  /**
   * get's all rooms that are not booked during period "dateFrom" to "dateEnd"
   * and,
   * shows them in bookingView.
   *
   * @param event mouseclick event
   */
  @FXML
  private void dateSubmitButton(ActionEvent event) {
    roomView.getItems().clear(); // This clears the view everytime submit is pushed, so it doesn't add up rooms.
    if (dateFrom.getValue().isBefore(dateEnd.getValue())) {
      if (dbHandler.notBookedRooms(dateFrom.getValue(), dateEnd.getValue(), roomTable)) {
        for (Room room : roomTable) {
          room.setDetails(dbHandler.getRoomDetails(room.getId()));
        }
        roomView.setItems(roomTable);

      } else {
        errorDialog.start(new Text("Database error, check connection!"));
      }
    } else {
      errorDialog.start(new Text("Invalid date set!"));
    }
  }

  /**
   * Check if guest exist in database by checking the entered e-mail.
   *
   * @return return
   */
  private Boolean checkGuest() {
    String guestId = dbHandler.get("id", "guest", "email", email.getText());
    if (guestId != null) {
      return true;
    }
    return false;
  }

  /**
   * This method is used because we changed 'capacity, type' constructors in Room.java to strings.
   *
   * @param capacity given capacity.
   * @return index of capacity.
   */
  private int capacityIndex(String capacity) {
    
    ArrayList<String> roomcapacity = dbHandler.getSettings(2);
    int i = 0;
    for (String temp : roomcapacity) {
      i++;
      if (capacity.equals(temp)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * This method is used because we changed 'capacity, type' constructors in Room.java to strings.
   *
   * @param from localdate.
   * @param to localdate.
   * @param room room.
   */
  public void calendarBookingSetup(LocalDate from, LocalDate to, Room room) {
    bookingcalendar.setVisible(false);
    guestinput.setVisible(true);
    this.from = from;
    this.to = to;
    this.room = room;
    int days = to.compareTo(from);
    totalPriceInBooking.setText(Integer.toString(room.getPrice() * days) + "kr");
    roomNoInBooking.setText(Integer.toString(room.getId()));
    dateFromInBooking.setText(from.toString());
    dateToInBooking.setText(to.toString());
  }
}