package lms.kiu.notifier.lms.api.util;

public class MessageUtils {

  public static String cropMessage(String message, int charLimit) {
    if (message == null) {
      return "";
    }
    if (message.length() <= charLimit) {
      return message.trim();
    }

    String oddlyCut = message.substring(0, charLimit);

    int lastSpace = oddlyCut.lastIndexOf(' ');

    if (lastSpace == -1) {
      return oddlyCut.trim();
    }

    return oddlyCut.substring(0, lastSpace).trim();
  }
}
