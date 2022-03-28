package javaproject;

import javafx.application.Application;

/**
 * Main class that runs the application.
 */
public class App {
  /**
   * Get the nice greeting.
   *
   * @return the greeting string.
   */
  public String getGreeting() {
    return "Hello World!";
  }

  public static void main(String[] args) {
    Application.launch(Start.class, args);
  }
}