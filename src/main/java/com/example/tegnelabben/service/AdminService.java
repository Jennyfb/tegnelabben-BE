package com.example.tegnelabben.service;

import com.example.tegnelabben.model.Admin;
import com.example.tegnelabben.repo.AdminRepo;
import com.example.tegnelabben.security.BasicAdminDetails;
import com.example.tegnelabben.security.Sha256PasswordEncoder;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for admin service
 */
@Service
public class AdminService {
  @Autowired
  private AdminRepo adminRepo;
  @Value("${security.salt}")
  String salt;

  //todo: remove this so one can not just create a new admin.
  /**
   * Method for creating admin
   * @param admin data received from client to create user
   * @return user created or throw exception if input is wrong
   */
  public Admin createAdmin(Admin admin) {
    admin.setPassword(hashPassword(admin.getPassword()));

    if (!admin.getEmail().contains("@") || !admin.getEmail().contains(".")) {
      throw new IllegalArgumentException("Email must contain @ and .");
    }else if(admin.getUsername() == null) {
      setUsernameByEmail(admin);
    }

    return adminRepo.save(admin);
  }

  /* Should not be able to find all admins
  /**
   * @return list of all themes sorted by grade or throw exception if there are no themes
   *
  public List<Admin> findAllAdmins() {
    List<Admin> admins = adminRepo.findAll();
    if(admins.size() == 0) {
      throw new NoSuchElementException("There are no admins created yet");
    }

    return admins;
  }
   */

  /**
   * Method for updating admin.
   * @param newAdmin with info to be updated
   * @param id of admin to be updatet
   * @return The admin with updated information
   * @throws AccessException if it it the wrong admin id trying to update another
   */
  public Admin updateAdmin(Admin newAdmin, long id) throws AccessException {
    Admin current = adminRepo.findById(id);

    //admin only able to change their own info
    BasicAdminDetails authenticatedUser = (BasicAdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(authenticatedUser.getId() != id)
      throw new AccessException("Cannot update admin different than the authenticated admin");

    //will just have update username for now
    if ((newAdmin.getUsername() != null) && (!(newAdmin.getUsername().equals(current.getUsername())))) {
        current.setUsername(newAdmin.getUsername());
    }
    //could have update email.
    return adminRepo.save(current);
  }

  /**
   * Method for checking if password is equal to password in database
   * @param id id of admin that sends in password
   * @param password password sent in
   * @return boolean if password is correct or not
   */
  public boolean confirmPassword(long id, String password) {
    Admin admin = adminRepo.findById(id);
    if(admin == null) return false;

    return hashPassword(password).equals(admin.getPassword());
  }

  /**
   * todo: hva gjør vi om det bare er en admin igjen? skal man kunne slette den siste admin brukeren?
   * Method for deleting a admin
   * @param id, of the user that is to be deleted
   */
  public void deleteAdmin(long id) throws AccessException {
    //admin should only be able to delete their own user
    BasicAdminDetails authenticatedAdmin = (BasicAdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(authenticatedAdmin.getId() != id)
      throw new AccessException("Cannot delete admin user different than the authenticated admin user");

    if(adminRepo.findById(id) == null) {
      throw new NoSuchElementException("No such admin user with " + id + " exist");
    } else {
      adminRepo.deleteById(id);
    }
  }

  /**
   * A method to hash strings.
   * @param toHash the string that should be hashed
   * @return the hashed string
   */
  private String hashPassword(String toHash) {
    PasswordEncoder passwordEncoder = new Sha256PasswordEncoder(salt);
    return passwordEncoder.encode(toHash);
  }


  /**
   * Method used for setting username by email if username not given or wrong format
   * @param admin to get new username
   */
  private void setUsernameByEmail(Admin admin){
    String[] arrOfStr = admin.getEmail().split("@", 5);
    admin.setUsername(arrOfStr[0]);
  }
}
