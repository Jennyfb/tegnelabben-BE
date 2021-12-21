package com.example.tegnelabben.controller;


import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.service.ThemeService;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class for Theme
 */
//todo: endre til /tegnelabben/tema evt bare /tema
@RestController
@RequestMapping("/tegnelabben")
@CrossOrigin
public class ThemeController {

  @Autowired
  ThemeService themeService;

  Logger logger = LoggerFactory.getLogger(ThemeController.class);

  /**
   * GetMapping for finding all themes
   * @return List with all themes. HttpStatus.OK or HttpStatus.BAD_REQUEST with error message
   */
  @GetMapping
  public ResponseEntity<?> findAllThemes() {
    try {
      List<Theme> themes = themeService.findAllThemes();
      logger.info("Found all themes");
      return new ResponseEntity<>(themes, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Get mapping for finding theme by id
   * @param id PathVariable, id of theme
   * @return theme and HttpStatus.OK or HttpStatus.BAD_REQUEST and error message
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> findThemeById(@PathVariable long id) {
    try {
      Theme theme = themeService.findThemeById(id);
      logger.info("Found theme with id " + id);
      return new ResponseEntity<>(theme, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Get mapping for finding all themes in specific grade
   * @param grade PathVariable
   * @return all themes in one grade and HttpStatus.OK or HttpStatus.BAD_REQUEST and error message
   */
  @GetMapping("/klasse/{grade}")
  public ResponseEntity<?> findThemesByGrade(@PathVariable int grade) {
    try {
      List<Theme> themes = themeService.findThemesByGrade(grade);
      logger.info("Found all themes in grade " + grade);
      return new ResponseEntity<>(themes, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * todo: should we do the upload of videos through here?
   * Post mapping for creating a theme
   * @param theme Requesting body, theme to be created
   * @return theme and HttpStatus.CREATED or HttpStatus.BAD_REQUEST and error message
   */
  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> createTheme(@RequestBody Theme theme) {
    try {
      Theme theme1 = themeService.createTheme(theme.getTitle(), theme.getDescription(), theme.getGrade(), theme.getThumbnail(), theme.getVideolink());
      logger.info("Theme was created");
      return new ResponseEntity<>(theme1, HttpStatus.CREATED);
    } catch (IllegalArgumentException | NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Put mapping for updating theme info
   * @param theme Requesting Body, new theme info
   * @param id PathVariable, theme id
   * @return Updated theme and HttpStatus.OK or BAD_REQUEST
   */
  @PutMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateTheme(@RequestBody Theme theme, @PathVariable long id) {
    Theme updatedTheme = null;
    try{
      updatedTheme = themeService.updateTheme(theme, id);
      logger.info("Updating theme with id: " + id);
      return new ResponseEntity<>(updatedTheme, HttpStatus.CREATED);
    } catch(IllegalArgumentException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * DeleteMapping for deleting a theme by id
   * @param id PathVariable
   * @return HttpStatus.OK or HttpStatus.BAD_REQUEST and error message
   */
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTheme(@PathVariable long id) {
    try {
      themeService.deleteTheme(id);
      logger.info("Deleted theme with id: " + id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
