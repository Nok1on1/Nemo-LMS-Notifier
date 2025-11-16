package lms.kiu.notifier.data;

public class Constants {

  public static final String MAIN_URL = "https://lms.kiu.edu.ge";

  public static final String COMMANDS =
      """
      💬 Commands:
            /start — Welcome & setup
            register token — Upload your LMS token
            /init_student — Initialize your courses
            /check_news — Manually check updates
            /check_news_from <positiveInteger> <hour(s)|day(s)|week(s)|month(s)> — Check past updates (e.g., /check_news_from 2 days)
            /report_bug <Message> - Report bug to the admin
            /commands — See all commands
            /about — You’re here already 🎉
      """;

  public static final String WELCOME_MESSAGE =
      """
      👋 Welcome to KIU LMS Notifier Bot!

      📋 Here's how to get started:

      1️⃣ Register your LMS token:
      • Type "register token"
      • Follow the instructions to upload your token file

      2️⃣ Initialize your courses:
      • Use /init_student
      • This will fetch all your enrolled courses (takes a few seconds)

      3️⃣ Check for updates manually:
      • Get notifications about new posts and homework
      • Use /check_news anytime (first check will fetch all of the news from past 3 months)
      • Use /check_news_from <positiveNumber> <timeUnit> to check past updates \n (e.g., /check_news_from 2 days)

      🕒 Automatic Scheduler:
      • The bot automatically checks your LMS 3 times a day — at 11:00, 16:00, and 20:00 (Tbilisi time)
      • You'll receive updates even if you don't run /check_news yourself

      💡 Tip: Use /commands to see all available commands.

      🚀 Type "register token" to start registration.
      """;

  public static final String HELP_MESSAGE =
      """
      ❓ I didn't understand that command.

      Here's what you can do:

      🔹 /start - View full setup guide
      🔹 /commands - See all available commands
      🔹 Type "register token" - Begin registration

      Need help? Use /start to see step-by-step instructions!
      """;

  public static final String REGISTRATION_HELPER_MESSAGE =
      """
      📋 How to get your Student Token:

      1️⃣ Go to https://lms.kiu.edu.ge and make sure you're logged in.
      2️⃣ Open your browser’s Developer Tools:

         • Chrome / Edge / Brave: Press F12 or Ctrl+Shift+I
         • Firefox: Press F12 or Ctrl+Shift+I
         • Safari: First enable Developer menu
           (Safari → Settings → Advanced → "Show Develop menu"),
           then press Option+Command+I

      3️⃣ Open the Application (Chrome/Edge/Brave), Storage (Firefox),
         or Storage (Safari → Develop Tools).

      4️⃣ Find Local Storage → select lms.kiu.edu.ge.

      5️⃣ Look for the key "Student-Token" and copy its value.

      6️⃣ Paste the token into a .txt file.

      7️⃣ Send that .txt file here.

      ⚠️ Keep your token private — don’t share it with anyone!
      """;

  public static final String ABOUT_MESSAGE =
      """
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
      Java 21 ☕ + Spring Boot 🍃 + WebFlux 🌊 + MongoDB 💾 + Telegram Bot API 📱
      → Basically, Reactive chaos wrapped in a friendly bot.

      """
          + COMMANDS
          + """

      👀 Fun fact:
      Finding LMS updates is like finding Nemo. 🐠
      """;

  public static final String STUDENT_INITIALIZATION_SUCCESS =
      """
      Student courses initialized successfully!
      next step: try /check_news_from <positiveInteger> <timeUnit>
      (e.g., /check_news_from 2 days)
      or /check_news for all of the news from past 3 months
      """;

  public static final String INVALID_TIME_PERIOD_ERROR =
      "⚠️ Invalid number format. Please enter a valid number for the time period.";
  public static final String INVALID_TIME_UNIT_ERROR =
      "❌ Invalid time unit. Please use one of the following:\n"
          + "• hours"
          + "• days\n"
          + "• weeks\n"
          + "• months\n";
  public static final String FILE_NOT_TXT_ERROR = "File is not .txt extension. Start Again!";
  public static final String NEGATIVE_TIME_PERIOD_ERROR =
      "❌ The time period must be greater than 0.";
  public static final String FAILED_CHECKING_NEWS_ERROR =
      "Failed to check news. Please try again later.";

  public static final String ASSIGNMENT_LIST_URL_PATH =
      "student/lms/learningCourses/group/getAssignmentList";
  public static final String ANNOUNCEMENT_LIST_URL_PATH =
      "student/lms/learningCourses/group/announcementList";
  public static final String COURSES_INFO_URL_PATH = "/student/lms/learningCourses/list";
  public static final String REGISTRATION_GROUP_ID_URL_PATH =
      "/student/registration/group/getGroupForRegistration";
  public static final String STUDENT_DASHBOARD_URL_PATH = "/student/dashboardInfo/";
}
