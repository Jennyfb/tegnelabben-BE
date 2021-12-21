package com.example.tegnelabben;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.tegnelabben.model.Admin;
import com.example.tegnelabben.model.AuthenticationRequest;
import com.example.tegnelabben.model.Theme;
import com.example.tegnelabben.repo.AdminRepo;
import com.example.tegnelabben.repo.ThemeRepo;
import com.example.tegnelabben.security.Sha256PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

//TODO: legg til autorisasjon for create, update og delete
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-dev.properties")
class ThemeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ThemeRepo themeRepo;

  @Autowired
  private AdminRepo adminRepo;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${security.salt}")
  String salt;

  private Theme theme1;
  private Theme theme2;
  private Theme theme3;

  private AuthenticationRequest authenticationRequest;
  private Admin admin1;

  private static boolean jwtIsLoaded = false;
  private static String jwt;

  @BeforeEach
  public void SetUp(){
    admin1 = new Admin("Jenny", "jenny@email.no", hash("password123"));
    authenticationRequest = new AuthenticationRequest(admin1.getEmail(), "password123");
    adminRepo.save(admin1);

    if(!jwtIsLoaded) {
      jwt = getHeader();
      jwtIsLoaded = true;
    }

    theme1 = new Theme("Suksesjon", "Dette handler om livsløpet til naturen", 7, "Link til bilde", "Link til video");
    theme2 = new Theme("Dyrelivet", "Hierarkiet i dyrelivet", 5, "Link til bilde", "Link til video");
    theme3 = new Theme("Hageland", "Hvordan livsløpet er i en hage", 6, "Link til bilde", "Link til video");
    themeRepo.save(theme1);
    themeRepo.save(theme2);
  }

  @AfterEach
  public void tearDown(){
    themeRepo.deleteAll();
    adminRepo.deleteAll();
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
        .header("Authorization", jwt)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title", is("Livet i havet")));

    //Negativ test -  updating title
    theme1.setTitle("Hoy hvor det går");
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().isForbidden());

    //Tests updating description
    theme1.setDescription("Test description");
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", jwt)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.description", is("Test description")));

    //Negative test - updating description
    theme1.setDescription("Test description");
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().isForbidden());


    //Tests updating grade
    theme1.setGrade(4);
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", jwt)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.grade", is(4)));

    //Negative test - updating grade
    theme1.setGrade(4);
    mockMvc.perform(put("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme1)))
        .andExpect(status().isForbidden());

    //todo: kan legge til for thumbnail og video, men ikke prekert atm
  }

  @Test
  void testCreateTheme() throws Exception {
    //test if it is able to create theme
    mockMvc.perform(post("/tegnelabben").contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", jwt)
        .content(objectMapper.writeValueAsString(theme3)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.title", is(theme3.getTitle())));

    //Negative test - creating theme
    mockMvc.perform(post("/tegnelabben").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(theme3)))
        .andExpect(status().isForbidden());
  }

  @Test
  void testDeleteTheme() throws Exception {
    //Test if able to delete theme
    mockMvc.perform(delete("/tegnelabben/{id}", theme1.getId())
        .header("Authorization", jwt)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    //Negative test - delete theme
    mockMvc.perform(delete("/tegnelabben/{id}", theme1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  String getHeader() {
    try {
      MvcResult mvcResult = mockMvc.perform(post("/tegnelabben/admin/authenticate").contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(authenticationRequest)))
          .andReturn();
      String res = mvcResult.getResponse().getContentAsString();
      return "Bearer " + JsonPath.parse(res).read("$.jwt");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "";
    }
  }

  String hash(String toHash) {
    PasswordEncoder passwordEncoder = new Sha256PasswordEncoder(salt);
    return passwordEncoder.encode(toHash);
  }
}
