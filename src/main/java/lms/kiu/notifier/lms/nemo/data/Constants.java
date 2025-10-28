package lms.kiu.notifier.lms.nemo.data;

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
  public static final long PLAYWRIGHT_THREAD_SLEEP_TIME = 800L;
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
      
      3️⃣ Check for updates:
         • Use /check_news anytime
         • Get notifications about new posts and homework
      
      💡 Tip: Use /commands to see all available commands
      
      Let's begin! Type "register token" to start registration.
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
      
      1️⃣ Open browser DevTools (F12)
      2️⃣ Go to Application/Storage tab
      3️⃣ Find LocalStorage → lms.kiu.edu.ge
      4️⃣ Copy the "Student-Token" value
      5️⃣ Save it as a .txt file
      6️⃣ Send the file here
      
      ⚠️ Keep your token private - don't share it with anyone!
      """;
}
