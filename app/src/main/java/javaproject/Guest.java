package javaproject;

/**
 * Based on the Oracle documentation on the topic 
 * of tables and table views in javafx
 * This class handles receptionist objects, it is used
 * in the administrators table view of receptionists.
 */
public class Guest {

  private int id;
  private String firstname;
  private String lastname;
  private String email;
  private int phone;
  private int points;

  /**
   * Constructs a receptionist object.

   * @param firstname The username of the receptionist, used for login.
   * @param lastname The first name of the receptionist.
   */
  public Guest(int id, String firstname, String lastname, String email,
        int phone, int points) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
    this.points = points;
  }

  // It is necessary to implement get-methods for every variable in the class that
  // will be displayed on a table, otherwise it will raise an error
  // (referring to the oracle documentation on javafx and tables), and error handling
  public int getId() {
    return id;
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

  public int getPoints() {
    return points;
  }

  public void setId(int id) {
    this.id = id;
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

  public void setPoints(int points) {
    this.points = points;
  }
}