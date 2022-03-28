package javaproject;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This is the controller class for the Calendar interface.
 * It handles all futures of the Calendar.
 */
public class CalendarfxController implements Initializable {

  /** 
  * This listener is activated when selectingLabel change prefWidth.
  * It checks if there is any 'touching' labels to avoid overlapping.
  */
  ChangeListener<? super Number> listener = new ChangeListener<>() {
    @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
          if (positionsList.get((int) (selectingLabel.getLayoutY() / labelHeight)).size() == 0 || locked) {
            return;
          }
          for (Integer pos : positionsList.get((int) (selectingLabel.getLayoutY() / labelHeight))) {
            int selectingLabelXstart = (int) selectingLabel.getLayoutX() / labelWidth;
            int selectingLabelXend = (int) (selectingLabel.getLayoutX() + (double) newValue) / labelWidth;
            if (selectingLabelXstart <= pos && selectingLabelXend > pos) {
              selectingLabel.setMaxWidth((double) oldValue);
              locked = true;
              break;
            }
          }
        }
  };

  DbHandler dbHandler = new DbHandler("root", "root");
  ErrorHandler errorHandler = new ErrorHandler();

  @FXML
  private ScrollPane parent;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private VBox roomsVbox;
  @FXML
  private HBox monthHboxParent;
  @FXML
  private HBox monthHbox;
  @FXML
  private HBox calendarHbox;
  @FXML
  private HBox calendarDaysHbox;
  @FXML
  private HBox weeksHbox;
  @FXML
  private Pane calendarBody;
  @FXML
  private Pane background;
  @FXML
  private Pane backgroundNumbers;
  @FXML
  private Pane calendarDaysParent;
  @FXML
  private Pane calendarPane;
  @FXML
  private Text monthText;
  @FXML
  private Text yearText;
  @FXML
  private Label floorNumber;
  @FXML
  private ImageView normalMap;
  @FXML
  private Slider slider;
  @FXML
  private Pane placeHolder; 

  private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();
  LinkedList<LinkedList<Room>> roomsList = new LinkedList<>();

  /* used to avoid new booking from overlapping other reservations. */
  LinkedList<LinkedList<Integer>> positionsList = new LinkedList<>();

  LocalDate localDate;
  int floor = 1;
  boolean selectingMode = false;
  Label selectingLabel;

  int labelWidth = 44;
  int labelHeight = 54;
  String[] colorTags = {"#003cffbb", "#ffe600bb", "#7a0303bb"};
  boolean locked;

  ArrayList<String> roomTypes = dbHandler.getSettings(1);
  ArrayList<String> roomCapacity = dbHandler.getSettings(2);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    LinkedList<Room> newList = new LinkedList<>();
    int initFloor = 1;

    // rooms to list of lists.
    try {
      ResultSet rooms = dbHandler.select("room", "*");
      while (rooms.next()) {
        Room room = new Room(rooms.getInt(1), roomCapacity.get(rooms.getInt(2) - 1),
            roomTypes.get(rooms.getInt(3) - 1), rooms.getInt(4), rooms.getString(5));
        int nowFloor = room.getId() / 100;
        if (nowFloor == initFloor) { // if we reach next floor
          newList.add(room);
        } else {
          initFloor++;
          roomsList.add(newList);
          newList = new LinkedList<>();
          newList.add(room);
        }
      }
      if (!newList.isEmpty()) {
        roomsList.add(newList);
      }
      rooms.close();
      dbHandler.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    parent.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    localDate = LocalDate.now();
    loadingRooms();

    Image image = new Image(getClass().getResource("normalMap.png").toExternalForm());
    normalMap.setImage(image);

    Circle circle = new Circle(40, 40, 40);
    GaussianBlur blur = new GaussianBlur();
    blur.setRadius(15);
    circle.setEffect(blur);
    circle.setVisible(false);
    normalMap.setClip(circle);
    normalMap.setOpacity(1);
    
    placeHolder.setOnMouseMoved(e -> {
      if (selectingMode) {
        int x = (int) (e.getX() / labelWidth);
        int size = labelWidth * (x - (int) (circle.getCenterX() / labelWidth));
        if (size > selectingLabel.getPrefWidth() || size < selectingLabel.getPrefWidth()) {
          if (!locked || size <= selectingLabel.getMaxWidth()) {
            selectingLabel.setPrefWidth(size);
          }
        }
        circle.setVisible(false);
        return;
      } else {
        circle.setVisible(true);
        circle.setCenterX(e.getX());
        circle.setCenterY(e.getY());
      }
    });

    placeHolder.setOnMouseExited(e -> {
      circle.setVisible(false);
    });

    placeHolder.setOnMouseClicked(e -> { 
      locked = false;
      if (e.getButton() == MouseButton.PRIMARY && e.getY() / labelHeight <= roomsList.get(floor - 1).size()) {
        if (selectingMode) {
          selectingMode = false;
          if (((int) e.getY() / labelHeight) + slider.getValue() != (int) selectingLabel.getLayoutY() / labelHeight
              || (int) (e.getX() / labelWidth) <= (int) (circle.getCenterX() / labelWidth)) {
            calendarBody.getChildren().remove(selectingLabel);
            selectingLabel = null;
            circle.setVisible(true);
            circle.setCenterX(e.getX());
            circle.setCenterY(e.getY());
          } else {
            int index = (int) (e.getY() / labelHeight + slider.getValue());
            Room room = roomsList.get(floor - 1).get(index);

            LocalDate ldf = LocalDate.of(Integer.parseInt(yearText.getText()),
                Month.valueOf(monthText.getText()), (int) selectingLabel.getLayoutX() / labelWidth + 1);
            LocalDate ldt = ldf.plusDays((int) (selectingLabel.getPrefWidth() / labelWidth));
            bookRoom(ldf, ldt, room);
          }
        } else {
          circle.setVisible(false);
          int x = (int) e.getX() / labelWidth;
          int y = (int) e.getY() / labelHeight;
          createBookingLabel(x, y);
        }
      } else if (e.getButton() == MouseButton.SECONDARY && selectingMode) {
        calendarBody.getChildren().remove(selectingLabel);
        circle.setVisible(true);
        circle.setCenterX(e.getX());
        circle.setCenterY(e.getY());
        selectingLabel = null;
        selectingMode = false;
      }
    });

    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      scrollPane.setVvalue((Double) newValue / slider.getMax());
      placeHolder.setLayoutY((Double) newValue * labelHeight);
    });

    // Stops mouse wheel from working when on top of scrollPane
    scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> event.consume());
  }

  /**
   * Loads the calendar.
   *
   */
  public void loadingCalendar(LocalDate localDate) {
    for (int i = 0; i < positionsList.size(); i++) {
      positionsList.get(i).clear();
    }
    weeksHbox.getChildren().clear();
    reservationsList.clear();
    calendarDaysHbox.getChildren().clear();
    // removing all children except the first three: 'background', 'numbers', 'placeHolder'
    calendarBody.getChildren().remove(3, calendarBody.getChildren().size());
    monthHboxParent.getChildren().remove(1, monthHboxParent.getChildren().size());
    int[] colorDiverser = new int[roomsList.get(floor - 1).size()];
    dbHandler.getReservationSpecific(reservationsList, localDate);
    YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    int daysinMonth = yearMonth.lengthOfMonth();
    if (daysinMonth != 31) { // We create new pane that starts from the next month
      backgroundNumbers.setPrefWidth(1360 - (31 - daysinMonth) * labelWidth);
      Pane restofNumbers = new Pane();
      restofNumbers.getStyleClass().add("backgroundNumbers");
      restofNumbers.setLayoutX(backgroundNumbers.getPrefWidth() + 4);
      restofNumbers.setPrefSize((31 - daysinMonth) * labelWidth, backgroundNumbers.getPrefHeight());
      restofNumbers.setMouseTransparent(false); // false to avoid bug of booking the same room (showing next month)
      calendarBody.getChildren().add(restofNumbers);
    } else {
      backgroundNumbers.setPrefWidth(1360);
    }
    for (Reservation reservation : reservationsList) {
      String str = Integer.toString(reservation.getRoom()); // get a single digit of the room number
      int roomFirstnum = Integer.parseInt(String.valueOf(str.charAt(0)));
      if (roomFirstnum == floor) {
        Room tempRoom = getRoom(reservation.getRoom());
        int location = roomsList.get(floor - 1).indexOf(tempRoom);
        if (location == -1) { // If room is in reservations but not in rooms.
          continue;
        }
        Label newlabel = new Label(reservation.getFirstname() + " " + reservation.getLastname());
        
        newlabel.setTextFill(Color.WHITE);
        newlabel.setFont(Font.font(16));
        newlabel.getStyleClass().add("booking");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateStart = LocalDate.parse(reservation.getDateStart(), formatter);
        LocalDate dateEnd = LocalDate.parse(reservation.getDateEnd(), formatter);

        // 4 Cases:
        // 1: booking starts same month ends same month
        if (dateStart.getMonthValue() == dateEnd.getMonthValue()) { 
          int period = dateEnd.getDayOfMonth() - dateStart.getDayOfMonth();
          newlabel.setPrefSize(period * labelWidth, 40);
          newlabel.setLayoutY(5 + location * labelHeight);
          newlabel.setLayoutX((dateStart.getDayOfMonth() * labelWidth) - 24);
          // 2: starts prev ends same
        } else if (dateStart.getMonthValue() < localDate.getMonthValue()
            && dateEnd.getMonthValue() == localDate.getMonthValue()) {
          int period = dateEnd.getDayOfMonth();
          newlabel.setPrefSize((period * labelWidth + 20) - labelWidth, 40);
          newlabel.setLayoutY(5 + location * labelHeight);
          newlabel.setLayoutX(0);
          // 3: starts same ends next
        } else if (dateStart.getMonthValue() == localDate.getMonthValue()
            && dateEnd.getMonthValue() > localDate.getMonthValue()) {
          int period = yearMonth.lengthOfMonth() - dateStart.getDayOfMonth();
          // If it is mbene bnfs l5ra
          if (dateEnd.getDayOfMonth() <= 31 - daysinMonth && dateEnd.getMonth() == localDate.plusMonths(1).getMonth()) {
            period += dateEnd.getDayOfMonth();
            newlabel.setPrefSize(period * labelWidth, 40);
          } else {
            newlabel.setPrefSize((31 - dateStart.getDayOfMonth()) * labelWidth + 24, 40);
          }
          newlabel.setLayoutY(5 + location * labelHeight);
          newlabel.setLayoutX((dateStart.getDayOfMonth() * labelWidth) - 24);
          // 4: starts prev ends next
        } else if (dateStart.getMonthValue() < localDate.getMonthValue()
            && dateEnd.getMonthValue() > localDate.getMonthValue()) {
          int period = 31;
          newlabel.setPrefSize(period * labelWidth + 24, 40);
          newlabel.setLayoutY(5 + location * labelHeight);
          newlabel.setLayoutX(0);
        }
        newlabel.setStyle("-fx-background-color: " + colorTags[colorDiverser[location]]);
        if (colorDiverser[location] == 1) { // if background is yellow
          newlabel.setTextFill(Color.BLACK);
        }
        colorDiverser[location]++;
        if (colorDiverser[location] == 3) { // if we reach the end of the array of tags
          colorDiverser[location] = 0;
        }  
        calendarBody.getChildren().add(newlabel);
        positionsList.get(location).add((int) newlabel.getLayoutX() / labelWidth);
      } else {
        ;
      }
    }
    monthText.setText(localDate.getMonth().name());
    yearText.setText(Integer.toString(localDate.getYear()));

    monthHbox.setPrefWidth(1360);
    if (daysinMonth < 31) {
      int difference = 31 - daysinMonth;
      Label nextMonth = new Label();
      nextMonth.setPrefHeight(30);
      nextMonth.setPrefWidth(difference * labelWidth - 4);
      monthHbox.setPrefWidth(1360 - difference * labelWidth);
      nextMonth.setStyle("-fx-background-color:  #e2e7eb;");
      monthHboxParent.getChildren().add(nextMonth);
    }
    int backgroundPosition = 0;
    int firstWeeksize = 304; // only last and first weeks needs to be adjusted
    firstWeeksize -= (yearMonth.atDay(1).getDayOfWeek().getValue() - 1)  * labelWidth;
    if (yearMonth.atDay(1).getDayOfWeek().getValue() < 5) {
      backgroundPosition -= (yearMonth.atDay(1).getDayOfWeek().getValue() - 1) * labelWidth;
    } else {
      backgroundPosition += (8 - yearMonth.atDay(1).getDayOfWeek().getValue()) * labelWidth;
    }

    background.setStyle("-fx-background-position:" + backgroundPosition + "px 0px");
    Label[] arrayofElements = new Label[31];
    String[] daysinWeek = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
    // Just for the first week of the year.
    int weekNumber = yearMonth.atDay(1).getDayOfYear() / 7 != 0 ? (yearMonth.atDay(1).getDayOfYear() / 7) + 1 : 1;
    Label[] weeksArray = new Label[5]; // Every month has (5) weeks
    int whereweAt = 0; // used to hold day number of the last full week in a spec month 
    int dayValue = yearMonth.atDay(1).getDayOfWeek().getValue() - 1;

    // Loading first row.
    for (int i = 0, j = 0; i < 31; i++) {
      if (dayValue == 7) { // New week
        if (j == 0) {
          LocalDate temp = localDate.minusMonths(1);
          YearMonth kza = YearMonth.of(temp.getYear(), temp.getMonthValue());
          int firstWeek = (kza.atDay(kza.lengthOfMonth()).getDayOfYear() / 7) + 1;
          weeksArray[j] = new Label("Week " + Integer.toString(firstWeek));
          if (firstWeeksize < 150) {
            weeksArray[j].setText("");
          }
          weeksArray[j].setPrefWidth(firstWeeksize);
        }
        if (j != 0) {
          weeksArray[j] = new Label("Week " + Integer.toString(weekNumber));
          weeksArray[j].setPrefWidth(304);
        } else if (j == 4) {
          weeksArray[j] = new Label("Week " + Integer.toString(weekNumber));
          if (i != daysinMonth) {
            weeksArray[j].setPrefWidth((daysinMonth - i) * labelWidth - 4);
          } else { // last is one day
            weeksArray[j].setPrefWidth(40);
          }
          
        }
        weekNumber = (yearMonth.atDay(i).getDayOfYear() / 7) + 1;
        weeksArray[j].setPrefHeight(40);
        weeksArray[j].getStyleClass().add("week");
        weeksArray[j].setAlignment(Pos.CENTER);
        weeksHbox.getChildren().add(weeksArray[j]);
        dayValue = 0;
        j++;
        whereweAt = i;
      }
      if (i + 1 > daysinMonth) {
        arrayofElements[i] = new Label(daysinWeek[dayValue] + "\n" + (i + 1  - daysinMonth));
        arrayofElements[i].getStyleClass().add("calendarElementNext");
      } else {
        arrayofElements[i] = new Label(daysinWeek[dayValue] + "\n" + (i + 1));
        arrayofElements[i].getStyleClass().add("calendarElement");
        arrayofElements[i].setOnMouseEntered((MouseEvent event) -> {});
        arrayofElements[i].setId(Integer.toString(i)); // Used to identify every node.
      }
      arrayofElements[i].wrapTextProperty().setValue(true);
      arrayofElements[i].textAlignmentProperty().set(TextAlignment.CENTER);
      arrayofElements[i].setAlignment(Pos.CENTER);
      arrayofElements[i].setPrefWidth(40);
      arrayofElements[i].setPrefHeight(labelWidth);
      calendarDaysHbox.getChildren().add(arrayofElements[i]);
      if (dayValue == 5 || dayValue == 6) { // Weekends has another css style class
        arrayofElements[i].getStyleClass().add("calendarElementWeekend");
        arrayofElements[i].wrapTextProperty().setValue(true);
      }
      dayValue++;
    }
    weeksArray[4] = new Label("Week " + Integer.toString(weekNumber));
    weekNumber = (yearMonth.atDay(daysinMonth).getDayOfYear() / 7) + 1;
    int lastWeeksize = 31 - whereweAt;
    weeksArray[4].setPrefWidth((lastWeeksize) * labelWidth - 4);
    if (lastWeeksize < 4) {
      weeksArray[4].setText("");
    }
    weeksArray[4].setPrefHeight(40);
    weeksArray[4].getStyleClass().add("week");
    weeksArray[4].setAlignment(Pos.CENTER);
    weeksHbox.getChildren().add(weeksArray[4]);
  }

  /**
   * Loads the rooms.
   *
   */
  public void loadingRooms() {
    placeHolder.setLayoutY(0); // because it moves on slider
    roomsVbox.getChildren().clear();
    positionsList = new LinkedList<>();
    VBox[] listofVboxes = new VBox[roomsList.get(floor - 1).size()];
    int z = 0; 
    for (Room room : roomsList.get(floor - 1)) {
      int roomId = room.getId();
      char str = Integer.toString(roomId).charAt(0);
      if (str == floorNumber.getText().charAt(0)) {
        Text upperText = new Text("Room " + room.getId());
        upperText.setFont(Font.font("System", FontWeight.BOLD, 25));
        upperText.setFill(Color.WHITE);
        Text lowerText = new Text(room.getType() + ", " + room.getCapacity());
        lowerText.setFont(Font.font("System", FontWeight.NORMAL, 15));
        lowerText.setFill(Color.WHITE);
        listofVboxes[z] = new VBox(upperText, lowerText);
        listofVboxes[z].setAlignment(Pos.TOP_RIGHT);
        listofVboxes[z].setPrefHeight(40);
        listofVboxes[z].setPrefWidth(200);
        positionsList.add(new LinkedList<>());
        z++;
      }
    }
    if (listofVboxes[0] != null) { // if not empty room vbox
      roomsVbox.getChildren().addAll(listofVboxes);
    }
    if (roomsVbox.getChildren().size() > 10) {
      calendarPane.setPrefHeight(roomsVbox.getChildren().size() * labelHeight);
      slider.setVisible(true);
      slider.setMax(roomsVbox.getChildren().size() - 10);
    } else {
      slider.setVisible(false);
      slider.setValue(0);
      calendarPane.setPrefHeight(labelHeight * 10);
    }
    background.setPrefHeight(calendarPane.getPrefHeight());
    backgroundNumbers.setPrefHeight(calendarPane.getPrefHeight());

    loadingCalendar(localDate);
  }
  
  @FXML
  private void monthSwap(ActionEvent event) {
    if (((Button) event.getSource()).getRotate() == 0.0) { // to the right
      localDate =  localDate.plusMonths(1);
      loadingCalendar(localDate);
    } else { // to the left
      localDate = localDate.minusMonths(1);
      loadingCalendar(localDate);
    }
  }

  @FXML
  private void floorSwap(ActionEvent event) {
    floor = Integer.parseInt(floorNumber.getText());
    if (((Button) event.getSource()).getRotate() == 270.0) { // upwards
      floor++;
      if (roomsList.size() < floor) {
        errorHandler.start(new Text("No rooms found in floor " + floor));
        floor--;
        return;
      }
      floorNumber.setText(Integer.toString(floor));
      loadingRooms();
    } else { // downwards
      floor--;
      if (floor <= 0) {
        errorHandler.start(new Text("No rooms found in floor " + floor));
        floor++;
        return;
      }
      floorNumber.setText(Integer.toString(floor));
      loadingRooms();
    }
  }

  /**
   * Sets the parent size on initializing.
   *
   */
  public void setSize(Double width, Double height) {
    parent.setMaxSize(width, height);
  }

  /**
   * Make a booking label in given coordinates.
   *
   * @param x coordinate x
   * @param y coordinate y
   */
  public void createBookingLabel(int x, int y) {
    if (x + 1 == localDate.lengthOfMonth()) {
      return;
    }
    // Check if there is any other reservation on the same day
    for (Integer pos : positionsList.get(y)) {
      if (x == pos) {
        return;
      }
    }
    selectingMode = true;
    selectingLabel = new Label("New Booking");
    selectingLabel.setMinSize(labelWidth, 40);
    selectingLabel.setPrefSize(labelWidth, 40);
    selectingLabel.setLayoutX((x  * labelWidth) + 20);
    selectingLabel.setLayoutY((slider.getValue() + y) * labelHeight + 5);
    selectingLabel.getStyleClass().add("calendarSelecting");
    selectingLabel.setMouseTransparent(true);
    selectingLabel.prefWidthProperty().addListener(listener);
    calendarBody.getChildren().add(selectingLabel);
  }

  /**
   * Return a room from roomslist.
   *
   * @param roomId given id
   * @return Room.
   */
  private Room getRoom(int roomId) {
    for (Room room : roomsList.get(floor - 1)) {
      if (room.getId() == roomId) {
        return room;
      }
    }
    return null;
  }

  /**
   * Make a booking.
   *
   * @param from localdate.
   * @param to localdate.
   * @param room room.  
   */
  private void bookRoom(LocalDate from, LocalDate to, Room room) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Booking_room.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.initStyle(StageStyle.UNDECORATED);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner((Stage) Stage.getWindows().get(0));
    stage.centerOnScreen();
    BookingfxController controller = loader.getController();
    controller.calendarBookingSetup(from, to, room);
    stage.show();
    stage.setOnHiding(e -> {
      if (controller.reservationCalendar != null) {
        selectingLabel.getStyleClass().clear();
        selectingLabel.getStyleClass().add("booking");
        selectingLabel.setText(controller.reservationCalendar.getFirstname() 
            + " " + controller.reservationCalendar.getLastname());
        int y = (int) selectingLabel.getLayoutY() / labelHeight;
        int reservationSizeInRoom = positionsList.get(y).size();
        String hexColor = (reservationSizeInRoom + 1) % 3 == 0 ? colorTags[2] : colorTags[reservationSizeInRoom % 2];
        selectingLabel.setTextFill(Color.WHITE);
        if (reservationSizeInRoom == 0) {
          hexColor = colorTags[0];
        } else if (hexColor.equals(colorTags[1])) { // if yellow
          selectingLabel.setTextFill(Color.BLACK);
        }
        selectingLabel.setStyle("-fx-background-color: " + hexColor);
        selectingLabel.setMouseTransparent(false);
        reservationsList.add(controller.reservationCalendar);
        positionsList.get(y).add((int) selectingLabel.getLayoutX() / labelWidth);
      } else {
        stage.close();
        calendarBody.getChildren().remove(selectingLabel);
      }
      selectingLabel = null;
    });
  }
}