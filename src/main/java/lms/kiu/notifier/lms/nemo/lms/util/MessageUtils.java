package lms.kiu.notifier.lms.nemo.lms.util;

public class MessageUtils {

  public static String cropMessage(String message, int charLimit) {
    if (message == null || message.length() <= charLimit) {
      return "";
    }

    String oddlyCut = message.substring(0, charLimit);

    int lastSpace = oddlyCut.lastIndexOf(' ');

    if (lastSpace == -1) {
      return oddlyCut.trim();
    }

    return oddlyCut.substring(0, lastSpace).trim();
  }
}
