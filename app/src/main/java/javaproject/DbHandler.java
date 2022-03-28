package javaproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.collections.ObservableList;

/**
 * Class that handles all the querys to database.
 */
public class DbHandler {
  private String dbUser;
  private String dbPassword;
  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet result;

  /**
   * Constructs db handler.
   *
   * @param dbUser     Username to the database.
   * @param dbPassword Password to the database.
   */
  public DbHandler(String dbUser, String dbPassword) {
    this.dbUser = dbUser;
    this.dbPassword = dbPassword;
  }

  private void connect() {
    try {
      this.connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hotel?user=" + dbUser
          + "&password=" + dbPassword);
    } catch (SQLException e) {
      System.out.println("Error when establishing a connection");
      e.printStackTrace();
    }
  }

  /**
   * Closes all streams for the database.
   */
  public void close() {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    try {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    try {
      if (result != null) {
        result.close();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Login stage. Called upon whenever user logins to the application.
   *
   * @param username Staffs username.
   * @param password Staffs password.
   * @return usertype; 0: receptionist, 1: adminstrator;
   */
  public int login(String username, String password) {
    try {
      connect();
      String query = "SELECT usertype from user where username=? AND password=?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      result = preparedStatement.executeQuery();
      if (result.next()) {
        return result.getInt(1);
      } else {
        return -1;
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return -1;
  }

  /**
   * Basic select query to the database.
   *
   * @param table  Specifies the table in database.
   * @param column Specifies the column in database.
   * @return Returns a list of columns
   */
  public ResultSet select(String table, String column) {
    Statement stmt;
    try {
      connect();
      String query = "SELECT " + column + " FROM " + table + "";
      stmt = connection.createStatement();
      // preparedStatement.setString(1, column);
      // preparedStatement.setString(2, table);
      result = stmt.executeQuery(query);
      return result;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    }
    return null;
  }

  /**
   * Function that returns a string based on input table and column value.
   *
   * @param selected Selected action
   * @param table    Table that is to be queryd
   * @param column   Specifices column
   * @param value    Specifices which row
   * @return String
   */
  public String get(String selected, String table, String column, String value) {
    try {
      connect();
      String query = "SELECT " + selected + " from " + table + " where " + column + "=?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value);
      result = preparedStatement.executeQuery();
      while (result.next()) {
        String i = result.getString(1);
        return i;
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return null;
  }

  /**
   * Query that creates a room with following parameters.
   *
   * @param capacity How many people that can sleep there.
   * @param type     Type of the room.
   * @param location location of the room.
   * @return Returns true if succesfully created. False if not.
   */
  public int createRoom(String id, int capacity, int type, int price, String location) {
    try {
      connect();
      String query = "INSERT into room values(?,?,?,?,?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, id);
      preparedStatement.setInt(2, capacity);
      preparedStatement.setInt(3, type);
      preparedStatement.setInt(4, price);
      preparedStatement.setString(5, location);
      preparedStatement.executeUpdate();
      return Integer.parseInt(id);
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
      if (error.getErrorCode() == 1062) {
        return -2;
      }
    } finally {
      close();
    }
    return -1;
  }

  /**
   * Inserts the relationship between a room and a detail in the database.
   *
   * @param roomId   Unique id of the room.
   * @param detailId Unique id of the detail.
   * @return Boolean value true/false.
   */
  public boolean insertDetailRelation(int roomId, int detailId) {
    try {
      connect();
      String query = "INSERT into room_details (id, id_detail) values(?,?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, roomId);
      preparedStatement.setInt(2, detailId);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException error) {
      System.out.println("Error while inserting detail relation: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Query that creates a guest with following parameters.
   *
   * @param firstname Guests firstname.
   * @param lastname  Guests lastname.
   * @param email     Guests email.
   * @param phone     Guests phonenr.
   * @return Returns true if succesfully created. False if not.
   */
  public boolean createGuest(String firstname, String lastname, String email, String phone) {
    try {
      connect();
      String query = "INSERT into guest(firstname, lastname, email, phone) values(?,?,?,?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, firstname);
      preparedStatement.setString(2, lastname);
      preparedStatement.setString(3, email);
      preparedStatement.setString(4, phone);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Get specific guest.
   */
  public String[] getGuest(String guestEmail) {
    try {
      connect();
      String query = "SELECT * from guest where email =?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, guestEmail);
      result = preparedStatement.executeQuery();
      if (result.next()) {
        String[] guestinfo = { result.getString(1), result.getString(2),
            result.getString(3), result.getString(4),
            result.getString(5), result.getString(6) };
        return guestinfo;
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return null;
  }

  /**
   * Query that creates a receptionist with following parameters.
   *
   * @param firstname Receptionists firstname.
   * @param lastname  Receptionists lastname.
   * @param username  Receptionists username.
   * @param password  Receptionists password.
   * @return Returns the id of the receptionist.
   */
  public int createReceptionist(int usertype, String firstname, String lastname, String username,
      String password, int phone, String email) {
    try {
      connect();
      String query = "INSERT into user(usertype, username, firstname, lastname, password, phone, email)"
          + "values(?,?,?,?,?,?,?)";
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, usertype);
      preparedStatement.setString(2, username);
      preparedStatement.setString(3, firstname);
      preparedStatement.setString(4, lastname);
      preparedStatement.setString(5, password);
      preparedStatement.setInt(6, phone);
      preparedStatement.setString(7, email);
      preparedStatement.executeUpdate();
      result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
        return result.getInt(1);
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return -1;
  }

  /**
   * Query that creates a reservation with following parameters.
   *
   * @param guestName      Name of the guest.
   * @param roomId         Which room.
   * @param receptionistId Which receptionist made the booking.
   * @param dateFrom       Start date of the stay.
   * @param dateEnd        End date of the stay.
   * @return Returns true if succesfully created. False if not.
   */
  public int createReservation(String guestName, int roomId,
      int receptionistId, String dateFrom, String dateEnd, int totalCost) {
    String guestId = get("id", "guest", "firstname", guestName);
    try {
      connect();
      String query = "INSERT into reservation(id_guest, id_room,"
          + " id_receptionist, date_start, date_end, total_cost) values(?,?,?,?,?,?)";
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, guestId);
      preparedStatement.setInt(2, roomId);
      preparedStatement.setInt(3, receptionistId);
      preparedStatement.setString(4, dateFrom);
      preparedStatement.setString(5, dateEnd);
      preparedStatement.setInt(6, totalCost);
      preparedStatement.executeUpdate();
      result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
        return result.getInt(1);
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return -1;
  }

  /**
   * Gets the reservations based on room id.
   *
   * @return Resultset of reservations made for that room.
   */
  public boolean getReservations(ObservableList<Reservation> reservationList) {
    try {
      connect();
      String query = "SELECT guest.id, guest.firstname, guest.lastname, reservation."
          + "date_start, reservation.date_end, reservation.id_room, reservation.total_cost,"
          + "reservation.id FROM guest JOIN reservation ON guest.id=reservation.id_guest";
      preparedStatement = connection.prepareStatement(query);
      result = preparedStatement.executeQuery();
      while (result.next()) {
        reservationList.add(new Reservation(result.getInt(1), result.getString(2),
            result.getString(3), result.getDate(4).toString(),
            result.getDate(5).toString(), result.getInt(6), result.getInt(7), result.getInt(8)));
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Gets settings from database.
   *
   * @param id id.
   * @return returns arraylist of strings.
   */
  public ArrayList<String> getSettings(int id) {
    try {
      connect();
      String query = "SELECT data FROM settings where id = ?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      result = preparedStatement.executeQuery();
      while (result.next()) {
        ArrayList<String> settings = new ArrayList<String>(Arrays.asList(result.getString(1).split("/")));
        while (settings.size() < 5) { // TODO: check me out.
          settings.add("Deleted");
        } 
        return settings;
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return null;
  }


  /**
   * Gets all the arrivals for the current day.
   *
   * @return Returns a list of arrivals lists.
   */
  public boolean getArrivals(ObservableList<Arrival> arrivalsList) {
    try {
      connect();
      String query = "SELECT reservation.id_room, firstname, lastname, email,"
          + " phone, reservation.id, reservation.checked_in, reservation.total_cost FROM guest JOIN "
          + "reservation ON guest.id = reservation.id_guest WHERE reservation.date_start =? AND "
          + "reservation.checked_in != '1'";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, LocalDate.now().toString());
      result = preparedStatement.executeQuery();
      while (result.next()) {
        arrivalsList.add(new Arrival(result.getInt(1), result.getString(2),
            result.getString(3), result.getString(4),
            result.getInt(5), result.getInt(6), (result.getInt(7) == 1 ? "Yes" : "No"), result.getInt(8)));
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Gets all the departures for the current day.
   */
  public boolean getDepartures(ObservableList<Arrival> departureList) {
    try {
      connect();
      String query = "SELECT reservation.id_room, firstname, lastname, email,"
          + " phone, reservation.id, reservation.checked_in, reservation.total_cost FROM guest JOIN "
          + " reservation ON guest.id = reservation.id_guest WHERE reservation.date_end =? AND "
          + "reservation.checked_in = '1'";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, LocalDate.now().toString());
      result = preparedStatement.executeQuery();
      while (result.next()) {
        departureList.add(new Arrival(result.getInt(1), result.getString(2),
            result.getString(3), result.getString(4),
            result.getInt(5), result.getInt(6), (result.getInt(7) == 1 ? "Yes" : "No"), result.getInt(8)));
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;

  }

  /**
   * Insert query based on following parameters.
   *
   * @param table    Which table.
   * @param column   Which column.
   * @param value    Specifies which column value.
   * @param uniqueId Which unique id the value should be asigned to.
   * @return Return true if succes. Otherwise false.
   */
  public boolean insert(String table, String column, String value, String uniqueId) {
    try {
      connect();
      String query = "UPDATE " + table + " SET " + column + "=? WHERE id=?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value);
      preparedStatement.setInt(2, Integer.parseInt(uniqueId));
      preparedStatement.executeUpdate();
      if (column.equals("checked_in") && value.equals("1")) {
        int roomId = Integer.parseInt(get("id_room", "reservation", "id", uniqueId));
        String date = get("date_end", "reservation", "id", uniqueId);
        createCleaningtask(roomId, Integer.parseInt(uniqueId), 0, 1, date, 0);
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Returns rooms that are not booked within the dates.
   *
   * @param dateStart starting date
   * @param dateEnd   ending date
   * @return returns result
   */
  public boolean notBookedRooms(LocalDate dateStart, LocalDate dateEnd, ObservableList<Room> roomTable) {
    ArrayList<String> roomTypes = getSettings(1);
    ArrayList<String> roomCapacity = getSettings(2);
    try {
      connect();
      String query = "SELECT room.id, room.capacity, room.type, room.price, room.location"
          + " FROM room LEFT JOIN reservation ON reservation.id_room = room.id"
          + " AND NOT ( '" + dateStart + "' >= reservation.date_end OR '" + dateEnd + "' <= reservation.date_start )"
          + " WHERE reservation.id IS NULL";
      preparedStatement = connection.prepareStatement(query);
      result = preparedStatement.executeQuery(query);
      while (result.next()) {
        roomTable.add(new Room(result.getInt(1), roomCapacity.get(result.getInt(2) - 1),
            roomTypes.get(result.getInt(3) - 1), result.getInt(4), result.getString(5)));
      }
      return true;
    } catch (SQLException e) {
      System.out.println("SQL Error while iterating through rooms. Line 411-430" + e.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Querys the database to find the details of a room, based on its id.
   *
   * @param id Unique id of the room.
   * @return A string containing comma separated details.
   */
  public String getRoomDetails(int id) {
    String details = "";

    try {
      connect();
      String query = "select details.detail from room "
          + "inner join room_details on room.id = room_details.id "
          + "inner join details on room_details.id_detail = details.id where room.id=" + id;
      preparedStatement = connection.prepareStatement(query);
      // preparedStatement.setInt(1, id);
      result = preparedStatement.executeQuery(query);
      while (result.next()) {
        details += result.getString(1) + ", ";
      }
      if (details.length() > 0) {
        details = details.substring(0, details.length() - 2);
        return details;
      }
      return details;
    } catch (SQLException e) {
      System.out.println("SQL Error while retrieving room details " + e.getMessage());
    } finally {
      close();
    }
    return "-1";
  }

  /**
   * Removes a objects from database.
   *
   * @param table Specifies which table to remove
   * @param id    Specifies the column to remove
   * @return result
   */
  public boolean remove(String table, int id) {
    try {
      connect();
      String query = "DELETE FROM " + table + " WHERE id = ?;";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
      if (resetAutoincrement(table)) {
        return true;
      }
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Used to update room ids after deletion.
   *
   * @param table Specifies the table to update.
   * @return Returns boolean true/false.
   */
  public boolean resetAutoincrement(String table) {
    try {
      connect();
      String query = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Method that allows one to edit a guest entry in the database.
   *
   * @param firstname of the guest
   * @param lastname  of the guest
   * @param email     of the guest
   * @param phone     of the guest
   */
  public boolean editGuest(String firstname, String lastname, String email, String phone, int id, Guest guest) {
    try {
      connect();
      String query = "UPDATE guest SET firstname = ?, lastname = ?, email = ?, phone = ? WHERE id = ?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, firstname);
      preparedStatement.setString(2, lastname);
      preparedStatement.setString(3, email);
      preparedStatement.setString(4, phone);
      preparedStatement.setInt(5, id);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("An error has occurred: " + e.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Method that edits a room entry in the database.
   *
   * @param id Room id
   * @param capacity Room Capacity
   * @param type Room type
   * @param price Room price
   * @param location Room location
   * @return Boolean value
   */
  public boolean editRoom(int id, int capacity, int type, int price, String location) {
    try {
      connect();
      String query = "UPDATE room SET capacity = ?, type = ?, price = ?, location = ? WHERE id = ?";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, capacity);
      preparedStatement.setInt(2, type);
      preparedStatement.setInt(3, price);
      preparedStatement.setString(4, location);
      preparedStatement.setInt(5, id);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("An error has occurred: " + e.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Used to get the related reservations of a given month.
   *
   * @param reservationList the list of reservations to fill.
   * @param date            the given date.
   * @return Returns boolean true/false.
   */
  public boolean getReservationSpecific(ObservableList<Reservation> reservationList, LocalDate date) {
    try {
      connect();
      String dateFrom = date.toString();
      String query = "SELECT guest.id, guest.firstname, guest.lastname, "
          + "reservation.date_start, reservation.date_end, reservation.id_room, "
          + "reservation.id, reservation.total_cost FROM guest "
          + "JOIN reservation ON guest.id=reservation.id_guest "
          + "WHERE MONTH(?) = MONTH(reservation.date_start) "
          + "AND YEAR(?) = YEAR(reservation.date_start) "
          + "OR MONTH(?) = MONTH(reservation.date_end) "
          + "AND YEAR(?) = YEAR(reservation.date_end) "
          + "ORDER BY reservation.date_start";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, dateFrom);
      preparedStatement.setString(2, dateFrom);
      preparedStatement.setString(3, dateFrom);
      preparedStatement.setString(4, dateFrom);
      result = preparedStatement.executeQuery();
      while (result.next()) {
        reservationList.add(new Reservation(result.getInt(1), result.getString(2),
            result.getString(3), result.getDate(4).toString(),
            result.getDate(5).toString(), result.getInt(6), result.getInt(7), result.getInt(8)));
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }

  /**
   * Used to create a cleaning task.
   *
   * @param roomId       room id.
   * @param reservatonId the reservation id if the task is created on check in,
   *                     else = null.
   * @param status       either 1 = clean, 0 = dirty.
   * @param availability either 'due out' = 1 or occupied = 0.
   * @param date         task date.
   * @param priority     either 1 = high or 0 = normal.
   * @return Returns integer of the generated id of the task or -1 if failed.
   */
  public int createCleaningtask(int roomId, int reservatonId, int status, int availability, String date, int priority) {
    try {
      connect();
      String query = "INSERT into housekeeping(id_room, id_reservation, status, availability,"
          + "task_date, priority) values(?,?,?,?,?,?)";
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, roomId);
      Object object = reservatonId == -1 ? null : reservatonId;
      preparedStatement.setObject(2, object);
      preparedStatement.setInt(3, status);
      preparedStatement.setInt(4, availability);
      preparedStatement.setString(5, date);
      preparedStatement.setInt(6, priority);
      preparedStatement.executeUpdate();
      result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
        return result.getInt(1);
      }
    } catch (SQLException error) {
      System.out.println("Cleaning task already created.");
    } finally {
      close();
    }
    return -1;
  }

  /**
   * Used to get cleanings tasks after a specific date.
   *
   * @param cleaningList observableList of the return cleaning tasks as linked
   *                     Lists.
   * @param date         the given date.
   * @return Returns Boolean true if success, false if failed.
   */
  public boolean getCleaningtasks(ObservableList<LinkedList<Integer>> cleaningList, LocalDate date) {
    try {
      connect();
      String dateFrom = date.toString();
      String query = "SELECT id_room, status, availability,"
          + "task_date, priority, id from housekeeping WHERE task_date >= ? "
          + "Order By task_date;";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, dateFrom);
      result = preparedStatement.executeQuery();
      while (result.next()) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(result.getInt(1));
        list.add(result.getInt(2));
        list.add(result.getInt(3));
        String temp = result.getString(4);
        list.add(Integer.parseInt(temp.replace("-", "")));
        list.add(result.getInt(5));
        list.add(result.getInt(6));
        cleaningList.add(list);
      }
      return true;
    } catch (SQLException error) {
      System.out.println("An error has occurred: " + error.getMessage());
    } finally {
      close();
    }
    return false;
  }
}