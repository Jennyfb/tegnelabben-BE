package com.example.tegnelabben.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * class to hash passwords with sha256
 */
public class Sha256PasswordEncoder implements PasswordEncoder  {
  private final String salt;

  public Sha256PasswordEncoder(String salt) {
    this.salt = salt;
  }

  /**
   * Method to hash password with sha256
   * @param password password to be hashed
   * @return hashed password
   */
  public String sha256(String password) {
    MessageDigest digest = null;
    try {
      digest = MessageDigest.getInstance("SHA-256");
      String toHash = password + salt;
      byte[] hash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(hash);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * Converts bytes to string of hex
   * @param hash hash as bytes
   * @return string in hex format
   */
  private String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  /**
   * Method to hash password
   * @param charSequence to be hashed
   * @return hashed password
   */
  @Override
  public String encode(CharSequence charSequence) {
    return sha256(charSequence.toString());
  }

  /**
   * Method to see if new password matches already hashed password
   * @param rawPassword
   * @param encodedPassword
   * @return if matches
   */
  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (rawPassword == null)
      throw new IllegalArgumentException("rawPassword cannot be null");
    return encodedPassword.equals(sha256(rawPassword.toString()));
  }
}
