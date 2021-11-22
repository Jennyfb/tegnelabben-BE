package com.example.tegnelabben.service;

import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.repo.ThemeRepo;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

  @Autowired
  ThemeRepo themeRepo;


  Logger logger = LoggerFactory.getLogger(ThemeService.class);
  /**
   * todo: Method for returning all themes sorted by grade?
   * @return list of all themes sorted by grade or throw exception if there are no themes
   */
  public List<Theme> findAllThemes() {
    List<Theme> themes = themeRepo.findAll();
    //todo: burde sortere de etter grade/klasse
    if(themes.size() == 0) {
      throw new NoSuchElementException("There are no themes created yet");
    }

    return themes;
  }

  /**
   * Method for finding theme by theme id
   * @param id of the them to be found
   * @return them or throw exception if theme does not exist
   */
  public Theme findThemeById(long id) {
    if(themeRepo.findById(id) == null) {
      throw new NoSuchElementException("This theme does not exist");
    }
    return themeRepo.findById(id);
  }

  /**
   * Method for creating a theme
   * @param title of theme
   * @param description to theme
   * @param grade the theme belongs to
   * @param thumbnail link to thumbnail picture
   * @param videoLink link to video of theme
   * @return the theme or throw exception if any input is wrong
   */
  public Theme createTheme(String title, String description, int grade, String thumbnail, String videoLink) {

    if(title.equals("")){
      throw new IllegalArgumentException("Theme must have title.");
    } else if(description.equals("")){
      throw new IllegalArgumentException("Theme must have a description.");
    } else if(grade<0 || grade>20){
      throw new IllegalArgumentException("Grade must be between 1-19.");
    } else if(thumbnail.equals("")){
      throw new IllegalArgumentException("Theme must have thumbnail.");
    } else if(videoLink.equals("")){
      throw new IllegalArgumentException("Theme must have link to video.");
    }

    if(checkIfGradeHasTheme(title, grade)){
      throw new IllegalArgumentException("Title is not unique for grade.");
    }

    //todo: legg til autentisering av admin

    Theme theme = new Theme(title, description,grade,thumbnail,videoLink);
    return themeRepo.save(theme);
  }


  /**
   * Method for checking if the spesific grade already have a theme with this specific title
   * @param title to be checked
   * @param grade to be checked
   * @return true if this title already exist in this grade, false if this grade does not already have this title
   */
  public boolean checkIfGradeHasTheme(String title, int grade){
    List<Theme> allThemes = themeRepo.findAll();
    for(Theme theme: allThemes){
      if(theme.getTitle().equals(title) && theme.getGrade()==grade){
        logger.info("Title not unique");
        return true;
      }
    }
    return false;
  }

  /**
   * Method for updating exsisting theme
   * @param updatedTheme with updated info
   * @param id of theme to be updated
   * @return The updated theme or null if not valid
   */
  //todo: use try catch instead? throw illegal argument like in create?
  public Theme updateTheme(Theme updatedTheme, long id){
    Theme current = themeRepo.findById(id);

    //todo: sikkerhetsjekker
    if(current == null) {
      throw new NoSuchElementException("There are no themes with this id");
    }
    //todo: hvis det ikke er et tema?

    if(!updatedTheme.getTitle().equals("") && updatedTheme.getTitle()!=null){
      current.setTitle(updatedTheme.getTitle());
    }

    if(!updatedTheme.getDescription().equals("") && updatedTheme.getDescription()!=null){
      current.setDescription(updatedTheme.getDescription());
    }

    if(updatedTheme.getGrade()!=0){
      if(updatedTheme.getGrade()<0 || updatedTheme.getGrade()>20){
        //todo: ser ikke ut som denne blir kastet? 
        throw new IllegalArgumentException("Grade must be between 1-19.");
      } else {
        current.setGrade(updatedTheme.getGrade());
      }
    }

    if(!updatedTheme.getThumbnail().equals("") && updatedTheme.getThumbnail()!=null){
      current.setThumbnail(updatedTheme.getThumbnail());
    }

    if(!updatedTheme.getVideolink().equals("") && updatedTheme.getVideolink()!=null){
      current.setVideolink(updatedTheme.getVideolink());
    }

    return themeRepo.save(current);
  }


  /**
   * Method for deleting theme by id
   * @param id of theme to be deleted
   * throw exception if id does not exist
   */
  public void deleteTheme(long id) {
    Theme theme = themeRepo.findById(id);
    if(theme == null) {
      throw new NoSuchElementException("No such theme exists");
    }
    themeRepo.deleteById(id);
  }
}