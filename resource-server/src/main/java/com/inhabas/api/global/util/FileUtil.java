package com.inhabas.api.global.util;

public class FileUtil {

  private static final String INVALID_CHARS_REGEX = "[\\\\/:*?\"<>|]";
  private static final String[] RESERVED_NAMES = {
    "con", "nul", "prn", "aux", "com1", "com2", "com3", "com4", "lpt1", "lpt2", "lpt3", "lpt4"
  };
  private static final String[] DANGEROUS_EXTENSIONS = {
    "exe", "bat", "cmd", "sh", "js", "jsp", "php", "asp", "html"
  };

  public static boolean isValidFileName(String fileName) {

    if (fileName == null || fileName.trim().isEmpty()) {
      return false;
    }

    // Check for invalid characters
    if (fileName.matches(INVALID_CHARS_REGEX)) {
      return false;
    }

    // Check for path traversal attacks
    if (fileName.contains("../") || fileName.contains("..\\")) {
      return false;
    }

    // Check for reserved names
    String fileOnlyName = fileName.toLowerCase().replaceAll("\\.[^.]+$", "");
    for (String reservedName : RESERVED_NAMES) {
      if (fileOnlyName.equals(reservedName)) {
        return false;
      }
    }

    // Check for dangerous extensions
    String extension = getExtension(fileName).toLowerCase();
    for (String dangerousExtension : DANGEROUS_EXTENSIONS) {
      if (extension.equals(dangerousExtension)) {
        return false;
      }
    }

    return true;
  }

  public static String getExtension(String fileName) {
    int lastIndex = fileName.lastIndexOf(".");
    if (lastIndex > 0) {
      return fileName.substring(lastIndex + 1);
    } else {
      return "";
    }
  }

  public static String getNameWithoutExtension(String fileName) {
    return fileName.substring(0, fileName.lastIndexOf("."));
  }
}
