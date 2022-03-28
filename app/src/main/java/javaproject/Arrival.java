package javaproject;

/**
 * Based on the Oracle documentation on the topic 
 * of tables and table views in javafx
 * This class handles receptionist objects, it is used
 * in the administrators table view of receptionists.
 */
public class Arrival {

  private int room;
  private String firstname;
  private String lastname;
  private String email;
  private int phone;
  private int reservationId;
  private String checkin;
  private int cost;

  /**
   * Constructs a receptionist object.

   * @param firstname The username of the receptionist, used for login.
   * @param lastname The first name of the receptionist.
   */
  public Arrival(int room, String firstname, String lastname, String email, 
      int phone, int reservationId, String checkin, int cost) {
    this.room = room;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
    this.reservationId = reservationId;
    this.checkin = checkin;
    this.cost = cost;
  }

  /**
   * Constructs a receptionist object.

   * @param firstname The username of the receptionist, used for login.
   * @param lastname The first name of the receptionist.
   */
  
  public Arrival(int room, String firstname, String lastname, String email, int phone) {
    this.room = room;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
  }

  // It is necessary to implement get-methods for every variable in the class that
  // will be displayed on a table, otherwise it will raise an error
  // (referring to the oracle documentation on javafx and tables), and error handling
  public int getRoom() {
    return room;
  }

  public String getFirstname() {
    return firstname;
  }
  
  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public int getPhone() {
    return phone;
  }

  public int getReservationId() {
    return reservationId;
  }

  public String getCheckin() {
    return checkin;
  }

  public int getCost() {
    return cost;
  }

  public void setRoom(int room) {
    this.room = room;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(int phone) {
    this.phone = phone;
  }

  public void setCheckin(String checkin) {
    this.checkin = checkin;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }
}