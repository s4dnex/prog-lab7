package server.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class PasswordManager {
  private static final String pepper = "VerySecretPepper";
  private final MessageDigest digest;

  public PasswordManager() throws NoSuchAlgorithmException {
    this.digest = MessageDigest.getInstance("SHA-1");
  }

  public String hashPassword(String password, String salt) {
    byte[] hash = digest.digest((password + salt + pepper).getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(hash);
  }

  public String generateSalt() {
    return UUID.randomUUID().toString();
  }
}
