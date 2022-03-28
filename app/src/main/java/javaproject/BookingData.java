package javaproject;

/**
 * Class for different booking data.
 */
public class BookingData {
  int resId;
  String firstname;
  String lastname;
  int phone;
  String email;
  String start;
  String end;
  int roomId;
  int capacity;
  String roomLocation;
  String attributes;
  int price;
  int totPrice;
  int nights;

  /**
   * Constructor.
   */
  public BookingData(int resId, String firstname, String lastname, int phone, String email, String start, String end,
      int roomId, int capacity, String roomLocation, String attributes, int price, int totPrice, int nights) {
    this.resId = resId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.phone = phone;
    this.email = email;
    this.start = start;
    this.end = end;
    this.roomId = roomId;
    this.capacity = capacity;
    this.roomLocation = roomLocation;
    this.attributes = attributes;
    this.price = price;
    this.totPrice = totPrice;
    this.nights = nights;
  }

  public int getNights() {
    return nights;
  }

  public void setNights(int nights) {
    this.nights = nights;
  }
  
  public int getResId() {
    return resId;
  }

  public void setResId(int resId) {
    this.resId = resId;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firtname) {
    this.firstname = firtname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public int getPhone() {
    return phone;
  }

  public void setPhone(int phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getRoomLocation() {
    return roomLocation;
  }

  public void setRoomLocation(String roomLocation) {
    this.roomLocation = roomLocation;
  }

  public String getAttributes() {
    return attributes;
  }

  public void setAttributes(String attributes) {
    this.attributes = attributes;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getTotPrice() {
    return totPrice;
  }

  public void setTotPrice(int totPrice) {
    this.totPrice = totPrice;
  }
}
