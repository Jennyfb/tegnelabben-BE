package com.example.tegnelabben.security;

import com.example.tegnelabben.model.Admin;
import com.example.tegnelabben.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading userDetails
 */
@Service
public class AdminDetailsService implements UserDetailsService {
  @Autowired
  private AdminRepo adminRepo;

  /**
   * Method for getting userDetails by username
   * @param s username
   * @return userDetail
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    Admin admin = adminRepo.findUserByEmail(s);
    if(admin == null)
      throw new UsernameNotFoundException("No user with username found");
    return new BasicAdminDetails(admin.getPassword(), admin.getEmail(), admin.getId());
  }
}
