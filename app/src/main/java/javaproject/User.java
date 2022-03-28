package javaproject;

/**
 * Based on the Oracle documentation on the topic 
 * of tables and table views in javafx
 * This class handles receptionist objects, it is used
 * in the administrators table view of receptionists.
 */
public class User {

  private int id;
  private String usertype;
  private String firstname;
  private String lastname;
  private String username;
  private String password;
  private int phone;
  private String email;

  /**
   * Constructs a receptionist object.

   * @param firstname The username of the receptionist, used for login.
   * @param lastname The first name of the receptionist.
   * @param username The last name of the receptionist.
   * @param password The password the receptionist, used for login.
   * 
   */

  public User(int id, String usertype, String firstname, String lastname, String username,
      String password, int phone, String email) {
    this.id = id;
    this.usertype = usertype;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
    this.phone = phone;
    this.email = email;
  }

  // It is necessary to implement get-methods for every variable in the class that
  // will be displayed on a table, otherwise it will raise an error
  // (referring to the oracle documentation on javafx and tables), and error handling
  
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getUsertype() {
    return usertype;
  }
  
  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getPhone() {
    return phone;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUsertype(String usertype) {
    this.usertype = usertype;
  }
  
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPhone(int phone) {
    this.phone = phone;
  }
}