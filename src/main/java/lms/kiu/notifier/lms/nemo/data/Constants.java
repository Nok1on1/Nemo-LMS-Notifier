package lms.kiu.notifier.lms.nemo.data;

import java.net.URI;
import java.util.function.Function;
import org.springframework.web.util.UriBuilder;

public class Constants {

  // Playwright was acting tuff so I made it Alpha Sub ٩◔̯◔۶
  //                       ∧＿∧
  //          　          (｡･ω･｡)つ━☆・*。
  //                    ⊂/   /　     ・゜
  //                   　しーＪ　　　   °。+ * 。　
  //    　　　　　                           .・゜
  //    　　　　　                           ゜｡ﾟﾟ･｡･ﾟﾟ。
  //    　　　　                            　ﾟ。　 　｡ﾟ
  //                                         　ﾟ･｡･ﾟ
  public static final long PLAYWRIGHT_THREAD_SLEEP_TIME = 1000L;
  public static final String MAIN_URL = "https://lms.kiu.edu.ge";


  public static final String WELCOME_MESSAGE = """
      👋 Welcome to KIU LMS Notifier Bot!
      
      📋 Here's how to get started:
      
      1️⃣ Register your LMS token:
      • Type "register token"
      • Follow the instructions to upload your token file
      
      2️⃣ Initialize your courses:
      • Use /init_student
      • This will fetch all your enrolled courses (takes ~1 minute)
      
      3️⃣ Check for updates manually:
      • Use /check_news anytime
      • Get notifications about new posts and homework
      
      🕒 Automatic Scheduler:
      • The bot automatically checks your LMS 3 times a day — at 11:00, 16:00, and 20:00 (Tbilisi time)
      • You’ll receive updates even if you don’t run /check_news yourself
      • Sit back and let the bot keep you in the loop 📬
      
      💡 Tip: Use /commands to see all available commands.
      
      🚀 Let’s begin! Type "register token" to start registration.
      """;
  public static final String HELP_MESSAGE = """
      ❓ I didn't understand that command.
      
      Here's what you can do:
      
      🔹 /start - View full setup guide
      🔹 /commands - See all available commands
      🔹 Type "register token" - Begin registration
      
      Need help? Use /start to see step-by-step instructions!
      """;
  public static final String REGISTRATION_HELPER_MESSAGE = """
      📋 How to get your Student Token:
      
      1️⃣ Go to https://lms.kiu.edu.ge and make sure you're logged in
      2️⃣ Open browser DevTools (F12) or right click with mouse and choose "Inspect"
      3️⃣ Go to Application/Storage tab
      4️⃣ Find LocalStorage → lms.kiu.edu.ge
      5️⃣ Copy the "Student-Token" value
      6️⃣ Save it as a .txt file
      7️⃣ Send the file here
      
      ⚠️ Keep your token private — don't share it with anyone!
      """;

  public static final String ABOUT_MESSAGE = """
      🤖 Nemo LMS Notifier Bot
      
      "Because when you think you can replace Teams with a broken LMS, this is what you get..."
      
      This bot automatically monitors your KIU LMS and notifies you about new posts, homework, and course updates — so you don’t have to refresh the page every 5 minutes.
      
      🧠 What it does:
      • Tracks all your enrolled courses
      • Detects new homework, announcements & files
      • Sends you clean Telegram messages with course names, deadlines & links
      • Automatically checks your LMS 3 times a day — 11:00, 16:00, and 20:00 (Tbilisi time)
      • Stores your data securely (tokens are encrypted 🔒)
      
      ⚙️ Tech magic behind it:
      Java 21 ☕ + Spring Boot 🍃 + WebFlux 🌊 + Playwright 🎭 + MongoDB 💾 + Telegram Bot API 📱
      → Basically, async chaos wrapped in a friendly bot.
      
      🐛 Known features (not bugs):
      • Playwright sometimes needs a nap
      • Initialization takes time — blame the LMS
      
      💬 Commands:
      /start — Welcome & setup
      register token — Upload your LMS token
      /init_student — Initialize your courses
      /check_news — Manually check updates
      /commands — See all commands
      /about — You’re here already 🎉
      
      👀 Fun fact:
      Finding LMS updates is like finding Nemo. 🐠
      """;
  public static final String INVALID_TIME_PERIOD = "⚠️ Invalid number format. Please enter a valid number for the time period.";
  public static final String INVALID_TIME_UNIT =
      "❌ Invalid time unit. Please use one of the following:\n" +
          "• hours" +
          "• days\n" +
          "• weeks\n" +
          "• months\n";
  public static final String NEGATIVE_TIME_PERIOD_ERROR = "❌ The time period must be greater than 0.";
  public static final String FAILED_CHECKING_NEWS = "Failed to check news. Please try again later.";
  public static final String ASSIGNMENT_LIST_URL_PATH = "student/lms/learningCourses/group/getAssignmentList";
  public static final String ANNOUNCEMENT_LIST_URL_PATH = "student/lms/learningCourses/group/announcementList";
}
