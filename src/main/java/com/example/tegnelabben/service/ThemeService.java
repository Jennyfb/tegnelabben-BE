package com.example.tegnelabben.service;

import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.repo.ThemeRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {
  @Autowired
  ThemeRepo themeRepo;

  /**
   * Method for finding all themes
   * @return List of all themes or throw exception if there are no themes.
   */
  public List<Theme> findAllThemes() {
    List<Theme> themes = themeRepo.findAll();
    //todo: trenger vi å returnere bad request? eller kan det bare returneres en tom liste?
    if(themes.size() == 0) {
      throw new NoSuchElementException("There are no themes created yet");
    }
    return themes;
  }

  /**
   * Method for finding all themes that belong to a specific grade
   * @param grade the specific grade
   * @return List with themes in grade
   */
  public List<Theme> findThemesByGrade(int grade){
    List<Theme> allThemes = themeRepo.findAll();
    List<Theme> foundThemes = new ArrayList<>();

    for (Theme theme : allThemes) {
      if (theme.getGrade() == grade) {
        foundThemes.add(theme);
      }
    }
    return foundThemes;
  }

  /**
   * Method for finding theme by theme id
   * @param id of the theme to be found
   * @return theme or throw exception if theme does not exist
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
    } else if(thumbnail.equals("")){ //todo: do upload to s3 bucket here?
      throw new IllegalArgumentException("Theme must have thumbnail.");
    } else if(videoLink.equals("")){
      throw new IllegalArgumentException("Theme must have link to video.");
    }

    if(checkIfGradeHasTheme(title, grade)){
      throw new IllegalArgumentException("Title is not unique for grade.");
    }

    Theme theme = new Theme(title, description,grade,thumbnail,videoLink);
    return themeRepo.save(theme);
  }


  /**
   * Method for updating existing theme
   * @param updatedTheme with updated info
   * @param id of theme to be updated
   * @return The updated theme or null if not valid
   */
  public Theme updateTheme(Theme updatedTheme, long id){
    Theme current = themeRepo.findById(id);

    //Check if theme has info
    if(current == null) {
      throw new NoSuchElementException("There are no themes with this id");
    }

    //Check if title has information
    if(!updatedTheme.getTitle().equals("") && updatedTheme.getTitle()!=null){
      current.setTitle(updatedTheme.getTitle());
    }

    //Check if desc has info
    if(!updatedTheme.getDescription().equals("") && updatedTheme.getDescription()!=null){
      current.setDescription(updatedTheme.getDescription());
    }

    //check if grade is correct format
    //todo: check if this works
    if(updatedTheme.getGrade()<1 || updatedTheme.getGrade()>20){
      throw new IllegalArgumentException("Grade must be between 1-19.");
    } else {
      current.setGrade(updatedTheme.getGrade());
    }

    //todo: here we could upload video and thumbnail to aws s3 buckets

    //checks thumbnail not null
    if(!updatedTheme.getThumbnail().equals("") && updatedTheme.getThumbnail()!=null){
      current.setThumbnail(updatedTheme.getThumbnail());
    }

    //checks videolink not null
    if(!updatedTheme.getVideolink().equals("") && updatedTheme.getVideolink()!=null){
      current.setVideolink(updatedTheme.getVideolink());
    }

    return themeRepo.save(current);
  }

  /**
   * Method for deleting theme by id
   * @param id of theme to be deleted
   * throws exception if id does not exist
   */
  public void deleteTheme(long id) {
    Theme theme = themeRepo.findById(id);
    if(theme == null) {
      throw new NoSuchElementException("No such theme exists");
    }
    themeRepo.deleteById(id);
  }

  /**
   * Method for checking if the specific grade already have a theme with this specific title
   * @param title to be checked
   * @param grade to be checked
   * @return True if this title already exist in this grade, False if this grade does not already have this title
   */
  private boolean checkIfGradeHasTheme(String title, int grade){
    List<Theme> allThemes = themeRepo.findAll();
    for(Theme theme: allThemes){
      if(theme.getTitle().equals(title) && theme.getGrade()==grade){
        return true;
      }
    }
    return false;
  }
}