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

  Theme findById(long id);
}


