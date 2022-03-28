package javaproject;

/**
 * Based on the Oracle documentation on the topic
 * of tables and table views in javafx
 * This class handles receptionist objects, it is used
 * in the administrators table view of receptionists.
 */
public class Room {

  private int id;
  private String capacity;
  private String type;
  private int price;
  private String details;
  private String location; 

  /**
   * Room constructor.
   *
   * @param id id
   * @param capacity capacity
   * @param type type
   * @param price price
   * @param location loc
   */
  public Room(int id, String capacity, String type, int price, String location) {
    this.id = id;
    this.capacity = capacity;
    this.type = type;
    this.price = price;
    this.location = location;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public int getId() {
    return id;
  }
  
  public String getCapacity() {
    return capacity;
  }

  public void setCapacity(String capacity) {
    this.capacity = capacity;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int newPrice) {
    price = newPrice;
  }
  
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}