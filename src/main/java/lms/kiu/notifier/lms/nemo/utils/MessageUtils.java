package lms.kiu.notifier.lms.nemo.utils;

import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageUtils {
  @NotNull
  public static Predicate<Update> hasMessageWith(String msg) {
    return upd -> upd.getMessage().getText().equalsIgnoreCase(msg);
  }
}
