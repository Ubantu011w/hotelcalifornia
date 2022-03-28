package javaproject;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * This is the controller class for the Housekeeping tab.
 * It handles all futures of the housekeeping.
 */
public class HousekeepingfxController implements Initializable {
  DbHandler dbHandler = new DbHandler("root", "root");
  ErrorHandler errorHandler = new ErrorHandler();
  Confirmation confirmation = new Confirmation();

  private ObservableList<Room> roomsList = FXCollections.observableArrayList();
  private HBox newRow;
  private LinkedList<Integer> newList;
  LinkedList<CheckBox> listOfcheckboxes = new LinkedList<>();
  double numberofTasks = 0;
  double finishedTasks = 0;

  @FXML
  VBox parent;
  @FXML
  Label count;
  @FXML
  Label date;
  @FXML
  HBox firstRow;
  @FXML
  CheckBox checkboxParent;
  @FXML
  VBox scrollPanecontent;
  @FXML
  ProgressBar progressBar;

  ArrayList<String> roomTypes = dbHandler.getSettings(1);
  ArrayList<String> roomCapacity = dbHandler.getSettings(2);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    parent.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    date.setText(LocalDate.now().toString());
    try {
      ResultSet rooms = dbHandler.select("room", "*");
      while (rooms.next()) {
        Room room = new Room(rooms.getInt(1), roomCapacity.get(rooms.getInt(2) - 1),
            roomTypes.get(rooms.getInt(3) - 1), rooms.getInt(4), rooms.getString(5));
        roomsList.add(room);    
      }
      rooms.close();
      dbHandler.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ObservableList<LinkedList<Integer>> cleaningList = FXCollections.observableArrayList();
    LocalDate date = LocalDate.now();
    if (dbHandler.getCleaningtasks(cleaningList, date)) {
      for (LinkedList<Integer> list : cleaningList) {
        scrollPanecontent.getChildren().add(createNewRow(list));
      }
    }

    checkboxParent.setOnAction(e -> {
      if (checkboxParent.isSelected()) {
        for (CheckBox checkBox : listOfcheckboxes) {
          checkBox.setSelected(true);
        }
      } else {
        for (CheckBox checkBox : listOfcheckboxes) {
          checkBox.setSelected(false);
        }
      }
    });
    count.textProperty().addListener((observable, oldValue, newValue) -> {
      progressBar.setProgress((Double.parseDouble(count.getText().split(" of ")[0])) 
          / (Double.parseDouble(count.getText().split(" of ")[1])));
    });
    progressBar.setProgress(finishedTasks / numberofTasks);
  }

  /**
   * Created a new HBox.
   *
   * @param list of task details that returns from the database.
   * @return HBox.
   */
  private HBox createNewRow(LinkedList<Integer> list) {
    CheckBox checkbox = new CheckBox();
    listOfcheckboxes.add(checkbox);
    
    String str = list.get(0).toString();
    Room room = roomsList.get(Integer.parseInt(str.substring(2)) == 0 ? 9 : Integer.parseInt(str.substring(2)) - 1);
    Label roomNo = new Label("" + list.get(0));
    roomNo.setAlignment(Pos.CENTER);
    roomNo.setFont(Font.font(15));
    roomNo.setTextFill(Color.WHITE);
    roomNo.setPrefSize(90, 21);
    
    Label roomType = new Label(room.getType());
    roomType.setAlignment(Pos.CENTER);
    roomType.setFont(Font.font(15));
    roomType.setTextFill(Color.WHITE);
    roomType.setPrefSize(90, 21);

    Label roomSize = new Label(room.getCapacity());
    roomSize.setPrefSize(90, 21);
    roomSize.setAlignment(Pos.CENTER);
    roomSize.setFont(Font.font(15));
    roomSize.setTextFill(Color.WHITE);

    Label status = new Label(list.get(1).equals(1) ? "Clean" : "Dirty");
    status.setPrefSize(50, 20);
    status.setAlignment(Pos.CENTER);
    status.getStyleClass().add(list.get(1).equals(1) ? "statusClean" : "statusDirty");
    HBox statusHbox = new HBox(status);
    statusHbox.setPrefSize(90, 70);
    statusHbox.setAlignment(Pos.CENTER);

    Label availability = new Label(list.get(2).equals(1) ? "Due Out" : "Occupied");
    availability.setPrefSize(90, 70);
    availability.setAlignment(Pos.CENTER);
    availability.setFont(Font.font(15));
    availability.setTextFill(Color.WHITE);

    StringBuilder stbuilder = new StringBuilder();
    String year = list.get(3).toString().substring(0, 4);
    String month = list.get(3).toString().substring(4, 6);
    String day = list.get(3).toString().substring(6, 8);
    stbuilder.append(year + "-" + month + "-" + day);
    Label date = new Label(stbuilder.toString());
    date.setPrefSize(90, 70);
    date.setAlignment(Pos.CENTER);
    date.setFont(Font.font(15));
    date.setTextFill(Color.WHITE);
    if (date.getText().equals(LocalDate.now().toString())) {
      numberofTasks++;
    } 

    Label priority = new Label(list.get(4).equals(1) ? "High" : "Normal");
    priority.setPrefSize(90, 70);
    priority.getStyleClass().add(priority.getText().equals("High") ? "high" : "normal");
    priority.setAlignment(Pos.CENTER);

    Button action = new Button();
    action.setPrefSize(43, 40);
    action.getStyleClass().add(list.get(1).equals(1) ? "uncheck" : "check");
    action.setId(list.get(1) + "/" + date.getText());
    if (list.get(1) == 1) {
      finishedTasks++;
    }
    HBox actionHbox = new HBox(action);
    actionHbox.setPrefSize(90, 70);
    actionHbox.setAlignment(Pos.CENTER);

    HBox row = new HBox(checkbox, roomNo, roomType, roomSize, statusHbox, availability, date, priority, actionHbox);
    row.setSpacing(30);
    row.setMinWidth(1000);
    row.setMinHeight(70);
    row.setAlignment(Pos.CENTER);
    row.getStyleClass().add("row");
    row.setId("" + list.get(5)); // housekeeping ID

    action.setOnAction(e -> action(e, row, status));

    // Housekeeping ID + "/" + Task date + "/" + Status
    checkbox.setId(list.get(5) + "/" + date.getText() + "/" + list.get(1));
    count.setText((int) finishedTasks + " of " +  (int) numberofTasks);
    return row;
  }
  
  /**
   * Creates a new HBox.
   *
   * @param e ActionEvent that is called when clicked on button action.
   * @param row the Hbox that has the task details.
   * @param status is the status label that we change to Clean if task is done. 
   * @return HBox.
   */
  private void action(ActionEvent e, HBox row, Label status) {
    Button action = (Button) e.getSource();
    String actionId = action.getId().split("/")[0];
    String taskDate = action.getId().split("/")[1];
    CheckBox checkbox = (CheckBox) row.getChildren().get(0);
    if (actionId.equals("1")) { // Make it dirty
      dbHandler.insert("housekeeping", "status", "0", row.getId());
      action.setId("0" + "/" + taskDate);
      action.getStyleClass().clear();
      status.getStyleClass().clear();
      action.getStyleClass().add("check");
      status.getStyleClass().add("statusDirty");
      status.setText("Dirty");
      if (taskDate.equals(LocalDate.now().toString())) {
        finishedTasks--;
      }
    } else { // Make it clean
      dbHandler.insert("housekeeping", "status", "1", row.getId());
      action.setId("1" + "/" + taskDate);
      action.getStyleClass().clear();
      status.getStyleClass().clear();
      action.getStyleClass().add("uncheck");
      status.getStyleClass().add("statusClean");
      status.setText("Clean");
      if (taskDate.equals(LocalDate.now().toString())) {
        finishedTasks++;
      }
    }
    count.setText((int) finishedTasks + " of " +  (int) numberofTasks);
    checkbox.setId(row.getId() + "/" + taskDate + "/" 
        + action.getId().split("/")[0]);
  }

  /**
   * Creates a new cleaning task to fill with details and we send it then to the meth above.
   */
  private void createNewcleaningTask() {
    newList = new LinkedList<>();
    CheckBox checkbox = new CheckBox();
    checkbox.setVisible(false);

    ComboBox<Room> roomCombobox = new ComboBox<>();
    roomCombobox.setPromptText("Room No.");
    roomCombobox.setItems(roomsList);
    roomCombobox.setMaxSize(100, 25);
    roomCombobox.setConverter(new StringConverter<Room>() {
      @Override
      public String toString(Room room) {
        if (room == null) {
          return null;
        } else {
          return "" + room.getId();
        }
      }

      @Override
      public Room fromString(String id) {
          return null;
      }
    });

    Label roomType = new Label("N/A");
    roomType.setAlignment(Pos.CENTER);
    roomType.setFont(Font.font(15));
    roomType.setTextFill(Color.WHITE);
    roomType.setPrefSize(85, 21);

    Label roomSize = new Label("N/A");
    roomSize.setAlignment(Pos.CENTER);
    roomSize.setFont(Font.font(15));
    roomSize.setTextFill(Color.WHITE);
    roomSize.setPrefSize(95, 21);
    
    Label status = new Label("N/A");
    status.setAlignment(Pos.CENTER);
    status.setFont(Font.font(15));
    status.setTextFill(Color.WHITE);
    status.setPrefSize(75, 21);

    ComboBox<String> availability = new ComboBox<>();
    availability.setPrefSize(100, 25);
    availability.getItems().addAll("Occupied", "Due Out");
    availability.getSelectionModel().select(0);

    DatePicker taskDate = new DatePicker();
    taskDate.setValue(LocalDate.now());
    taskDate.setPrefWidth(90);

    ComboBox<String> priority = new ComboBox<>();
    priority.setPrefSize(90, 25);
    priority.getItems().addAll("Normal", "High");
    priority.getSelectionModel().select(0);

    Button submit = new Button("Submit");
    Button cancel = new Button("Cancel");
    submit.getStyleClass().add("errorButton");
    cancel.getStyleClass().add("errorButton");
    submit.setPrefSize(90, 32);
    cancel.setPrefSize(90, 32);

    roomCombobox.setOnAction(e -> {
      Room room = roomCombobox.getSelectionModel().getSelectedItem();
      roomType.setText(room.getType());
      roomSize.setText(room.getCapacity());
    });
    
    submit.setOnAction(e -> {
      if (!roomCombobox.getSelectionModel().isEmpty()) {
        submit();
      
      } else {
        errorHandler.start(new Text("Please select a room!"));
      }
    });
    cancel.setOnAction(e -> {
      scrollPanecontent.getChildren().remove(newRow);
      newRow = null;
    });
    VBox vbox = new VBox(submit, cancel);
    vbox.setSpacing(5);
    
    newRow = new HBox(checkbox, roomCombobox, roomType, roomSize, status, availability, taskDate, priority, vbox);
    newRow.setSpacing(30);
    newRow.setMinWidth(1000);
    newRow.setMinHeight(70);
    newRow.setAlignment(Pos.CENTER);
    newRow.getStyleClass().add("row");
    scrollPanecontent.getChildren().add(newRow);
  }

  /**
   * Sends the new task details to the database.
   */
  private void submit() {
    ObservableList<Node> list = newRow.getChildren();
    LocalDate tempDate = LocalDate.now();
    for (Node node : list) {
      if (node instanceof ComboBox) {
        try {
          @SuppressWarnings("unchecked")
          ComboBox<Room> temp = (ComboBox<Room>) node;
          newList.add(temp.getSelectionModel().getSelectedItem().getId());
          newList.add(0); // Status = 0 = dirty;
        } catch (Exception e) {
          @SuppressWarnings("unchecked")
          ComboBox<String> temp = (ComboBox<String>) node;
          newList.add(temp.getSelectionModel().getSelectedIndex());
        }

      } else if (node instanceof DatePicker) {
        DatePicker temp = (DatePicker) node;
        tempDate = temp.getValue();
        newList.add(Integer.parseInt(tempDate.toString().replace("-", "")));
      }
    }
    int generatedTaskId = dbHandler.createCleaningtask(newList.get(0), -1, newList.get(1), newList.get(2),
          newList.get(3).toString(), newList.get(4));

    if (generatedTaskId != -1) {
      newList.add(generatedTaskId);
      scrollPanecontent.getChildren().remove(newRow);
      newRow = null;
      scrollPanecontent.getChildren().add(createNewRow(newList));
      confirmation.run("Successfully created a cleaning task.");
    }
  }
  
  /**
   * Called when clicked on 'Create New Task' button.
   */
  @FXML
  private void createNewTask(ActionEvent event) {
    if (newRow == null) {
      createNewcleaningTask();
    }
  }

  /**
   * Deletes tasks that has their checkbox checked.
   */
  @FXML
  private void deleteTask(MouseEvent event) {
    Boolean selected = false;
    for (CheckBox checkbox : listOfcheckboxes) {
      if (checkbox.isSelected()) {
        selected = true;
      } 
    }
    if (!selected) {
      errorHandler.start(new Text("Please select a task!"));
      return;
    }
    BooleanErrors booleanError = new BooleanErrors();
    Button yes = booleanError.start(new Text("Are you sure?"));
    yes.setOnAction(f -> {
      LinkedList<CheckBox> deletedCheckboxes = new LinkedList<>();
      for (CheckBox checkBox : listOfcheckboxes) {
        String taskId = checkBox.getId().split("/")[0];
        String taskDate = checkBox.getId().split("/")[1];
        String taskStatus = checkBox.getId().split("/")[2];
        if (checkBox.isSelected()) {
          if (dbHandler.remove("housekeeping", Integer.parseInt(taskId))) {
            scrollPanecontent.getChildren().remove(checkBox.getParent());
            deletedCheckboxes.add(checkBox);
            if (taskDate.equals(LocalDate.now().toString())) {
              numberofTasks--;
              if (taskStatus.equals("1")) {
                finishedTasks--;
              }
            }
          } else {
            errorHandler.start(new Text("Failed to load tasks!"));
          }
        }
      }
      for (CheckBox index : deletedCheckboxes) {
        listOfcheckboxes.remove(index);
      }
      checkboxParent.setSelected(false);
      count.setText((int) finishedTasks + " of " +  (int) numberofTasks);
      Stage errorStage = (Stage) yes.getScene().getWindow();
      errorStage.close();
      confirmation.run("Successfully deleted a cleaning task.");
    });
  }

  /**
   * Creates an VBox with selected tasks details and sends it to the primary printer.
   */
  @FXML
  private void printTask(MouseEvent event) {
    Boolean selected = false;
    for (CheckBox checkbox : listOfcheckboxes) {
      if (checkbox.isSelected()) {
        selected = true;
      } 
    }
    if (!selected) {
      errorHandler.start(new Text("Please select a task!"));
      return;
    }
    HBox titlesRow = new HBox(2);
    titlesRow.setAlignment(Pos.CENTER);
    for (Node node : firstRow.getChildren()) {
      if (node instanceof Label) {
        String text = ((Label) node).getText();
        if (text.equals("Action")) {
          break;
        }
        Label label = new Label(((Label) node).getText());
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(60, 15);
        label.setFont(Font.font(null, FontWeight.BOLD, 10));
        titlesRow.getChildren().add(label);
      }
    }
    VBox printOut = new VBox(titlesRow);
    printOut.setSpacing(10);
    printOut.setAlignment(Pos.CENTER);
    for (CheckBox checkBox : listOfcheckboxes) {
      if (checkBox.isSelected()) {
        String status = checkBox.getId().split("/")[0];
        HBox row = new HBox(2);
        row.setAlignment(Pos.CENTER);
        Boolean gotHim = false;
        for (Node node : checkBox.getParent().getChildrenUnmodifiable()) {
          if (node instanceof Label) {
            Label label = new Label(((Label) node).getText());
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(60, 15);
            label.setFont(Font.font(10));
            row.getChildren().add(label);
          } else if (node instanceof HBox && !gotHim) {
            Label label = new Label(status.equals("1") ? "Clean" : "Dirty");
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(60, 15);
            label.setFont(Font.font(10));
            row.getChildren().add(label);
            gotHim = true;
          }
        }
        printOut.getChildren().add(row);
      }
    }
    PrinterJob job = PrinterJob.createPrinterJob();
    boolean proceed = job.showPrintDialog((Stage) firstRow.getScene().getWindow());
    if (job != null) {
      if (proceed) {
        boolean printed = job.printPage(printOut);
        if (printed) {
          confirmation.run("Added to printer successfully.");
          job.endJob();
        } else {
          errorHandler.start(new Text("Failed to print!"));
        }
      } 
    }
  }
  
  /**
   * Resize the stage on showing.
   *
   * @param width coordinate.
   * @param height coordinate.
   */
  public void setSize(double width, double height) {
    parent.setMaxSize(width, height);
  }
}