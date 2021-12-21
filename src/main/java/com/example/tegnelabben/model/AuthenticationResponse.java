package com.example.tegnelabben.model;

/**
 * Class for authenticating responses
 */
public class AuthenticationResponse {
  private final String jwt;
  private long id;

  /**
   * Constructor
   * @param jwt Session key
   */
  public AuthenticationResponse(String jwt) {
    this.jwt = jwt;
  }

  /**
   * Constructor
   * @param jwt Session key
   * @param id Id of user
   */
  public AuthenticationResponse(String jwt, long id) {
    this.jwt = jwt;
    this.id = id;
  }

  /**
   *getters
   */
  public String getJwt() {
    return jwt;
  }
  public long getId() {
    return id;
  }
}
