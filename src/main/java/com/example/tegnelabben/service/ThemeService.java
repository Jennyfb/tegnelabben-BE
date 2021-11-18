package com.example.tegnelabben.service;

import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.repo.ThemeRepo;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

  @Autowired
  ThemeRepo themeRepo;


  /**
   * todo: Method for returning all themes sorted by grade?
   * @return list of all themes sorted by grade or throw exception if there are no themes
   */
  public List<Theme> findAllGradesWithThemes() {
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
   * Method for creating a reservation
   * @param theme to be created
   * @return a reservation or throw exception if any input is wrong
   */
  public Theme createTheme(Theme theme) {
    //todo: burde ha sjekker for å sikre at objektet er bra
    return themeRepo.save(theme);
  }
}
