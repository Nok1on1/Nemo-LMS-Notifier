package lms.kiu.notifier.lms.nemo.playwright.util;

public class MessageUtils {

  public static String cropMessages(String message, int charLimit) {
    if (message == null || message.length() <= charLimit) {
      return message;
    }

    String oddlyCut = message.substring(0, charLimit);

    int lastSpace = oddlyCut.lastIndexOf(' ');

    if (lastSpace == -1) {
      return oddlyCut.trim();
    }

    return oddlyCut.substring(0, lastSpace).trim();
  }

}
