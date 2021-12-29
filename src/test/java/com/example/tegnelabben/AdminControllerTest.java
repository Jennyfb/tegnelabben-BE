package com.example.tegnelabben;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tegnelabben.model.Admin;
import com.example.tegnelabben.model.AuthenticationRequest;
import com.example.tegnelabben.repo.AdminRepo;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AdminRepo adminRepo;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${security.salt}")
  String salt;

  private Admin admin1;
  private Admin admin2;

  private AuthenticationRequest authenticationRequest;

  private static boolean jwtIsLoaded = false;
  private static String jwt;

  @BeforeEach
  public void setUp() {
    admin1 = new Admin("Jenny", "jenny@email.no", hash("password123"));
    admin2 = new Admin("Jenney", "hey@email.no", "Password123!");
    authenticationRequest = new AuthenticationRequest(admin1.getEmail(), "password123");

    adminRepo.save(admin1);

    if(!jwtIsLoaded) {
      jwt = getHeader();
      jwtIsLoaded = true;
    }
  }

  @AfterEach
  public void tearDown() {
    adminRepo.deleteAll();
  }


  @Test
  void testCreateAdmin() throws Exception {
    mockMvc.perform(post("/admin").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(admin2.getPrivateAdmin()))
        .header("Authorization", jwt))
        .andExpect(status().is(201));
  }


  @Test
  void testUpdateAdmin() throws Exception {
    //Tests updating username
    admin1.setUsername("Janne");
    mockMvc.perform(put("/admin/{id}", admin1.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(admin1))
        .header("Authorization", jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username", is("Janne")));

    //Negative test - updating username
    admin1.setUsername("Henny");
    mockMvc.perform(put("/admin/{id}", admin1.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(admin1)))
        .andExpect(status().isForbidden());
  }


  @Test
  void testConfirmPassword() throws Exception {
    Admin admin3 = new Admin("Henny", "henny@gmail.no", "password123");

    mockMvc.perform(post("/admin/{id}/confirm-password", admin1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(admin3.getPrivateAdmin()))
        .header("Authorization", jwt))
        .andExpect(status().isOk());
  }

  @Test
  void testDeleteAdmin() throws Exception {
    mockMvc.perform(delete("/admin/{id}", admin1.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", jwt))
        .andExpect(status().isOk());
    //Negative test - delete user
    mockMvc.perform(delete("/admin/{id}", admin1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  String getHeader() {
    try {
      MvcResult mvcResult = mockMvc.perform(post("/admin/authenticate").contentType(MediaType.APPLICATION_JSON)
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
