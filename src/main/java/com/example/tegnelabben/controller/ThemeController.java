package com.example.tegnelabben.controller;


import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.service.ThemeService;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
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

  Logger logger = LoggerFactory.getLogger(ThemeController.class);

  /**
   * GetMapping for finding all themes sorted by grade?
   * todo: how do we structure this best? all themes sorted by grade?
   * @return grades and themes. HttpStatus.OK or HttpStatus.BAD_REQUEST with error message
   */
  @GetMapping
  public ResponseEntity<?> findAllThemes() {
    try {
      List<Theme> themes = themeService.findAllThemes();
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

  /**
   * PostMapping for creating a theme
   * @param theme RequestBody
   * @return reservation and HttpStatus.CREATED or HttpStatus.BAD_REQUEST and error message
   */
  //todo: skal man ta imot id? skal ikke denne genereres automatisk?
  @PostMapping
  ResponseEntity<?> createTheme(@RequestBody Theme theme) {
    try {
      Theme theme1 = themeService.createTheme(theme.getTitle(), theme.getDescription(), theme.getGrade(), theme.getThumbnail(), theme.getVideolink());
      return new ResponseEntity<>(theme1, HttpStatus.CREATED);
    } catch (IllegalArgumentException | NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }


  /**
   * PutMapping for updating theme info
   * @param theme Requesting Body with new theme info
   * @param id of theme, PathVariable from client
   * @return Theme and HttpStatus.OK if all ok or BAD_REQUEST
   */
  @PutMapping("/{id}")
  //@PreAuthorize("isAuthenticated()")
  //todo: add authentication
  public ResponseEntity<?> updateActivity(@RequestBody Theme theme, @PathVariable long id) {
    Theme updatedTheme = null;
    try{
      updatedTheme = themeService.updateTheme(theme, id);
      logger.info("Updating theme with id:" + id);
      return new ResponseEntity<>(updatedTheme, HttpStatus.CREATED);
    } catch(IllegalArgumentException e){
      logger.info("Wrong format was given when updating theme");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //todo: catch accesException
  /*
  } catch (AccessException e) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
  }
   */

  }


  /**
   * DeleteMapping for deleting a theme by id
   * @param id PathVariable
   * @return HttpStatus.OK or HttpStatus.BAD_REQUEST and error message
   */
  //@PreAuthorize("hasRole('ROLE_ADMIN')")
  //todo: add authoriassjon
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRoom (@PathVariable long id) {
    try {
      themeService.deleteTheme(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
