package com.example.tegnelabben.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model class for Admin
 * @version 1.0
 */
@Entity
@Table(name = "admin")
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name="username")
  private String username;
  @Column(name="email", unique=true)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String email;
  @Column(name="password")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  /**
   * Empty class constructor
   */
  public Admin() {
  }

  /**
   * Class constructor for admin
   * @param username Name to be show on website
   * @param email to use for login
   * @param password to use for login
   */
  public Admin(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  /**
   * Getters and Setters
   */
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * To string method
   * @return String with admin info
   */
  @Override
  public String toString() {
    return "Admin{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

  /**
   * Generated equals method. This checks if an object o is equal to current admin.
   * @param o to be compared to
   * @return boolean if equal or not.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Admin admin = (Admin) o;
    return Objects.equals(username, admin.username) &&
        email.equals(admin.email);
  }

  /**
   * @return int generated based on username?
   */
  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  /**
   * Method for retrieving private admin
   * @return private admin object
   */
  @JsonIgnore
  public Object getPrivateAdmin(){
    return new PrivateAdmin(id, username, email, password);
  }

}

/**
 * Class for Private Admin
 */
class PrivateAdmin{
  private long id;
  private String username;
  private String email;
  private String password;

  /**
   * Getters
   */
  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Constructor
   * @param id of admin
   * @param username Name to be show on website
   * @param email to use for login
   * @param password to use for login
   */
  public PrivateAdmin(long id, String username, String email, String password) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
