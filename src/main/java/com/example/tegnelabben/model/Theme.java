package com.example.tegnelabben.model;

//Importerte artifak:  spring-boot-starter-data-jpa
import javax.persistence.*;

/**
 * Class for Theme
 * @version 1.0
 */
@Entity
@Table(name = "Theme")
public class Theme {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "grade")
  private int grade;

  @Column(name = "thumbnail")
  private String thumbnail;

  @Column(name = "video")
  private String videolink;

  /**
   * Empty constructor
   */
  public Theme() {
  }

  /**
   * Constructor for theme
   * @param title of theme
   * @param description of theme
   * @param grade theme belongs to
   * @param thumbnail Picture to used before video starts
   * @param videolink that links to where the video is stored
   */
  public Theme(String title, String description, int grade, String thumbnail, String videolink) {
    this.title = title;
    this.description = description;
    this.grade = grade;
    this.thumbnail = thumbnail;
    this.videolink = videolink;
  }

  /**
   * Getters and setters for all attributes for now.
   */
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getVideolink() {
    return videolink;
  }

  public void setVideolink(String videolink) {
    this.videolink = videolink;
  }
}
