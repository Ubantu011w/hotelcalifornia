package javaproject;

/**
 * Manages database.
 *
 */
public class DbManager {
  String username;
  int id;

  /**
   * Constructor for the db management.

   * @param username Specifies user.
   * @param id users id.
   */
  public DbManager(String username, int id) {
    this.username = username;
    this.id = id;
  }

  /**
   * Returns username.
   *
   */
  public String get_username() {
    return username;
  } 

  /**
   * Returns id.
   *
   */
  public int get_id() {
    return id;
  }
}