package com.example.tegnelabben.repo;

import com.example.tegnelabben.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for Admin
 * @version 1.0
 */
@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
  Admin findById(long id);
  Admin findUserByEmail(String email);
}
