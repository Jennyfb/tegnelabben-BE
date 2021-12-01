package com.example.tegnelabben;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.repo.ThemeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

//TODO: legg til autorisasjon for create, update og delete
//@Profile("dev")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-dev.properties")
public class ThemeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ThemeRepo themeRepo;

  @Autowired
  ObjectMapper objectMapper;

  private Theme theme1;
  private Theme theme2;
  private Theme theme3;

  @BeforeEach
  public void SetUp(){
    theme1 = new Theme("Suksesjon", "Dette handler om livsløpet til naturen", 7, "Link til bilde", "Link til video");
    theme2 = new Theme("Dyrelivet", "Hierarkiet i dyrelivet", 5, "Link til bilde", "Link til video");
    theme3 = new Theme("Hageland", "Hvordan livsløpet er i en hage", 6, "Link til bilde", "Link til video");
    themeRepo.save(theme1);
    themeRepo.save(theme2);

  }

  @AfterEach
  public void tearDown(){
    themeRepo.deleteAll();
  }

  @Test
  void testFindAllThemes () throws Exception {
    mockMvc.perform(get("/tegnelabben").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title", is("Suksesjon")))
        .andExpect(jsonPath("$[1].title", is("Dyrelivet")));
  }

  @Test
  void testUpdateTheme() throws Exception {
    //Tests updating title
    theme1.setTitle("Livet i havet");
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title", is("Livet i havet")));

    //Tests updating description
    theme1.setDescription("Test description");
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.description", is("Test description")));

    //Tests updating grade
    theme1.setGrade(4);
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.grade", is(4)));

    //todo: kan legge til for thumbnail og video, men ikke prekert atm
  }

  @Test
  void testCreateTheme() throws Exception {
    mockMvc.perform(post("/tegnelabben").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme3)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title", is(theme3.getTitle())));
  }

  @Test
  void testDeleteTheme() throws Exception {
    mockMvc.perform(delete("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
