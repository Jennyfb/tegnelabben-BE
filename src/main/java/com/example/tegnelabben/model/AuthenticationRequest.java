package com.example.tegnelabben.model;

/**
 * Class for authentication requests
 */
public class AuthenticationRequest {
  private String username;
  private String password;

  /**
   * Empty constructor
   */
  public AuthenticationRequest() {
  }

  /**
   * Constructor for authenticating user
   * @param username Username of user
   * @param password Password of user
   */
  public AuthenticationRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   *getters and setters
   */
  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
