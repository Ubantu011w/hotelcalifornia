package javaproject;

/**
 * Based on the Oracle documentation on the topic 
 * of tables and table views in javafx
 * This class handles receptionist objects, it is used
 * in the administrators table view of receptionists.
 */
public class Reservation {

  private int guestId;
  private String firstname;
  private String lastname;
  private String dateStart;
  private String dateEnd;
  private int room;
  private int cost;
  private int reservationId;

  /**
   * Constructs a receptionist object.

   * @param guestId The unique Id of the guest.
   * @param firstname The first name of the receptionist.
   * @param lastname The last name of the receptionist.
   * @param dateStart The start of the stay.
   * @param dateEnd The end of the stay.
   * @param room Which room the booking contains.
   */
  public Reservation(int guestId, String firstname, String lastname, String dateStart,
        String dateEnd, int room, int cost, int reservationId) {
    this.guestId = guestId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
    this.room = room;
    this.cost = cost;
    this.reservationId = reservationId;
  }

  // It is necessary to implement get-methods for every variable in the class that
  // will be displayed on a table, otherwise it will raise an error
  // (referring to the oracle documentation on javafx and tables), and error handling

  public int getCost() {
    return cost;
  }

  public int getGuestId() {
    return guestId;
  }
  
  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getDateStart() {
    return dateStart;
  }

  public String getDateEnd() {
    return dateEnd;
  }

  public int getRoom() {
    return room;
  }

  public int getReservationId() {
    return reservationId;
  }

  public void setGuestid(int guestId) {
    this.guestId = guestId;
  }
  
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setDateStart(String dateStart) {
    this.dateStart = dateStart;
  }

  public void setDateEnd(String dateEnd) {
    this.dateEnd = dateEnd;
  }

  public void setRoom(int room) {
    this.room = room;
  }
  
  public void setCost(int cost) {
    this.cost = cost;
  }
}