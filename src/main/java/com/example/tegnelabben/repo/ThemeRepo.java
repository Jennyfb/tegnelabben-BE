package com.example.tegnelabben.repo;

import com.example.tegnelabben.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for Theme
 * @version 1.0
 */
@Repository
public interface ThemeRepo extends JpaRepository<Theme, Long> {

  //todo: This is how I have done it earlier. Make an assessment if it should be different.
  Theme findById(long id);
}


