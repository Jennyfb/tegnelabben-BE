package com.example.tegnelabben.controller;

import com.example.tegnelabben.model.Admin;
import com.example.tegnelabben.model.AuthenticationRequest;
import com.example.tegnelabben.model.AuthenticationResponse;
import com.example.tegnelabben.security.AdminDetailsService;
import com.example.tegnelabben.security.BasicAdminDetails;
import com.example.tegnelabben.security.JwtUtil;
import com.example.tegnelabben.service.AdminService;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tegnelabben/admin")
@CrossOrigin
public class AdminController {
  @Autowired
  private AuthenticationManager authenticationmanager;
  @Autowired
  private AdminDetailsService adminDetailsService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  AdminService adminService;

  private Logger logger = LoggerFactory.getLogger(AdminController.class);

  /**
   * PostMapping for creating admin user
   * @param admin RequestBody from client
   * @return admin user and HttpStatus.CREATED or HttpStatus.BAD_REQUEST
   */
  @PostMapping
  public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
    try {
      Admin admin1 = adminService.createAdmin(admin);
      logger.info("Admin user was created");
      return new ResponseEntity<>(admin1, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      logger.info(e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * GetMapping for finding all themes sorted by grade?
   * @return grades and themes. HttpStatus.OK or HttpStatus.BAD_REQUEST with error message
   */
  @GetMapping
  public ResponseEntity<?> findAllAdmins() {
    try {
      List<Admin> admins = adminService.findAllAdmins();
      return new ResponseEntity<>(admins, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

    /**
     * PutMapping for updating admin info
     * @param admin, Requesting Body with new admin info
     * @param id, admins id, PathVariable from client
     * @return admin and HttpStatus.OK if all ok or BAD_REQUEST if wrong input from admin
     */
  @PutMapping("/{id}")
  //@PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> updateAdmin(@RequestBody Admin admin, @PathVariable long id) {
    try {
      Admin updatedAdmin = adminService.updateAdmin(admin, id);
      logger.info("Updated admin with id: " + id);
      return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      logger.info(e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (AccessException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }


  /**
   * DeleteMapping for deleting a admin
   * @param id, PathVariable which is the admins id
   * @return ok og bad request
   */
  @DeleteMapping("/{id}")
  //@PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> deleteAdmin(@PathVariable long id) {
    try {
      adminService.deleteAdmin(id);
      logger.info("Deleted admin user with id: " + id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NoSuchElementException e) {
      logger.info(e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (AccessException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }

  //todo: trenger vi muligheten til å oppdatere passord, dropper det, is a lot

  /**
   * Post mapping to authorise a admin and get an authorisation-token in return
   * @param authenticationRequest request body containing email and password
   * @return the token used to authorise access in later requests
   * @throws Exception
   */
  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      authenticationmanager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong username or password");
    }

    BasicAdminDetails adminDetails = (BasicAdminDetails) adminDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String jwt = jwtUtil.generateToken(adminDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt, adminDetails.getId()));
  }

  /**
   * Method for confirming if password sent by admin is equal to password in database
   * @param id id of admin sending password
   * @param admin The admin sending the password
   * @return 200 if ok, 400 if not
   */
  @PostMapping("/{id}/confirm-password")
  public ResponseEntity<?> confirmPassword(@PathVariable long id, @RequestBody Admin admin) {
    if(adminService.confirmPassword(id, admin.getPassword()))
      return new ResponseEntity<>(HttpStatus.OK);
    else
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

}
