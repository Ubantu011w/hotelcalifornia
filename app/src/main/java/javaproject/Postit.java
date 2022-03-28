package javaproject;

/**
 * This class handles the "post-it notes" in the program.
 */
public class Postit {

  private String announcement; 
  private String daily;
  private String admintodo;
  private String receptodo;

  /**
   * Constructor.
   *
   * @param announcement First box.
   * @param daily second box.
   * @param admintodo third box.
   * @param receptodo fourth box.
   */
  public Postit(String announcement, String daily, String admintodo, String receptodo) {
    this.announcement = announcement;
    this.daily = daily;
    this.admintodo = admintodo;
    this.receptodo = receptodo;
  }

  public String getAnnouncement() {
    return announcement;
  }

  public String getDaily() {
    return daily;
  }

  public String getAdminTodo() {
    return admintodo;
  }

  public String getRecepTodo() {
    return receptodo;
  }

  public void setAnnouncement(String newAnnouncement) {
    this.announcement = newAnnouncement;
  }

  public void setDaily(String newdaily) {
    this.daily = newdaily;
  }

  public void setAdminTodo(String newTodo) {
    this.admintodo = newTodo;
  }

  public void setRecepTodo(String newTodo) {
    this.receptodo = newTodo;
  }
}
