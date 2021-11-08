package com.example.tegnelabben.controller;


import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.service.ThemeService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class for Theme
 */
@RestController
@RequestMapping("/tegnelabben")
@CrossOrigin
public class ThemeController {

  @Autowired
  ThemeService themeService;


  /**
   * GetMapping for finding all themes sorted by grade?
   * todo: how do we structure this best? all themes sorted by grade?
   * @return grades and themes. HttpStatus.OK or HttpStatus.BAD_REQUEST with error message
   */
  @GetMapping
  public ResponseEntity<?> findAllGradesWithThemes() {
    try {
      List<Theme> themes = themeService.findAllGradesWithThemes();
      return new ResponseEntity<>(themes, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }


  /**
   * Getmapping for finding theme by id
   * todo: skal vi finne theme basert på noe annet enn id?
   * @param id PathVariable
   * @return theme and HttpStatus.OK or HttpStatus.BAD_REQUEST and error message
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> findThemeById(@PathVariable long id) {
    try {
      Theme theme = themeService.findThemeById(id);
      return new ResponseEntity<>(theme, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
