# 🤖 Nemo LMS Notifier Bot

> *"Because when you think you can replace Teams with a broken LMS, this is what you get..."*

A Telegram bot that automatically monitors your KIU Learning Management System (LMS) and notifies
you about new posts, homework, and course updates. Built with the power of Playwright automation,
Spring WebFlux magic, and a sprinkle of chaos.

## 🎭 What Does This Thing Do?

Ever wished someone would just tell you when your professor uploads homework at 11:59 PM? Well, this
bot is that someone.

**Features:**

- 🔐 Secure token-based authentication
- 📚 Automatically fetches all your enrolled courses
- 🔔 Checks for new posts and homework assignments
- 💾 MongoDB storage (because data should persist somewhere)
- 🔄 Reactive programming with Spring WebFlux (async all the things!)
- 🔒 Encrypted token storage (your secrets are safe... mostly)

## 🛠️ Tech Stack

This project is powered by:

- ☕ **Java 21** - The latest and greatest
- 🍃 **Spring Boot** - Because uhh... beans.
- 🌊 **Spring WebFlux** - Reactive programming
- 🍃 **MongoDB** - NoSQL database
- 📱 **Telegram Bot API** - Your notification delivery system
- 🔐 **Spring Security Crypto** - Keeping tokens encrypted

## 🎮 How to Use

### Step 1: Get Your Student Token

1. Go to https://lms.kiu.edu.ge and make sure you're logged in.
2. Open your browser’s Developer Tools:

    - **Chrome / Edge / Brave**: Press F12 or Ctrl+Shift+I
    - **Firefox**: Press F12 or Ctrl+Shift+I
    - **Safari (macOS)**: First enable Developer menu (Safari → Settings → Advanced → "Show Develop
      menu"), then press Option+Command+I

3. Open the **Application** (Chrome/Edge/Brave), **Storage** (Firefox),
   or **Storage** (Safari → Develop Tools).

4. Find **Local Storage** → select **lms.kiu.edu.ge**.

5. Look for the key **"Student-Token"** and copy its value.

6. Paste the token into a .txt file.

7. Send that .txt file here.

⚠️ Keep your token private — don’t share it with anyone!

### Step 2: Register with the Bot

1. Start a chat with your bot on Telegram
2. Send `/start` to see the welcome message
3. Type `register token`
4. Follow instructions and upload your token file

### Step 3: Initialize Your Courses

```
/init_student
```

This launches Playwright automation to:

- Navigate through your LMS
- Extract all enrolled courses
- Store them in the database
- Takes about 1 minute ⏱️

### Step 4: Check for Updates

```
/check_news
```

The bot will:

- Log into your LMS
- Check all your courses
- Report new posts and homework
- Keep you in the loop 📢

## 🤝 Available Commands

| Command                                                                | Description                                        |
|------------------------------------------------------------------------|----------------------------------------------------|
| `/start`                                                               | Welcome message & setup guide                      |
| `/commands`                                                            | List all available commands                        |
| `register token`                                                       | Begin registration process                         |
| `/init_student`                                                        | Initialize enrolled courses                        |
| `/check_news`                                                          | Manually check updates                             |
| `/check_news_from <PositiveInteger> <hour(s)/day(s)/week(s)/month(s)>` | Check past updates (e.g., /check_news_from 2 days) |
| `/help`                                                                | Get help message                                   |
| `/report_bug <Message>`                                                | Report bug to the admin                            |
| `/about`                                                               | About the bot                                      |

# ⚙️ Setup

## 📋 Prerequisites

Before you dive in, make sure you have:

- ☕ Java 21 or higher
- 🍃 MongoDB instance (local or cloud)
- 🤖 Telegram Bot Token (from [@BotFather](https://t.me/botfather))
- 🎓 KIU LMS Student Token (from your browser)
- 🧠 A moderate understanding of what you're doing

### 1. Clone this beauty

```bash
git clone https://github.com/yourusername/Nemo-LMS-Notifier.git
cd Nemo-LMS-Notifier
```

```


### 2. Configure application properties

Create `src/main/resources/application.yaml`:

```properties
spring:
  data:
    mongodb:
      uri: <uri>
      database: <databaseName>

telegram:
  bot:
    token: <botToken>
    username: <botUsername>
  admin:
    id: <adminId>

encryptor:
  password: <hexEncodedPassword>
  salt: <hexEncodedSalt>
  
```

### 3. Build the project

```shell script
./gradlew build
```

### 4. Run it!

```shell script
./gradlew bootRun
```

## 🏗️ Project Structure

p.s might not be accurate

```text
src/main/java/lms/kiu/notifier/
├── data/ # Constants and models
├── lms/ # LMS automation logic
│ ├── model/ # Request/response models
│ └── service/ # LMS API services
├── mongo/ # Database layer
│ ├── model/ # MongoDB entities
│ ├── repository/ # Spring Data repositories
│ └── service/ # Database services
├── scheduler/ # Scheduled tasks
├── security/ # Encryption, tokens
├── telegram/ # Telegram bot
│ ├── command/ # Bot commands
│ ├── config/ # Bot config
│ └── service/ # Bot services
└── NemoLmsNotifierApplication.java

```

## 🎨 Architecture Highlights

### Reactive All The Way 🌊

Uses Spring WebFlux and Project Reactor for non-blocking, async operations. Because blocking is for
traffic, not code.

### MongoDB Storage 💾

Reactive MongoDB with TTL indexes ensures data doesn't stick around forever (7-day expiry on student
records).

### Secure by Design 🔐

Student tokens are encrypted using Spring Security's `TextEncryptor`. No plain-text secrets here!

### Automatic Scheduler 🕒

- The bot automatically checks your LMS 3 times a day — at 11:00, 16:00, and 20:00 (Tbilisi time)
- You’ll receive updates even if you don’t run /check_news yourself
- Sit back and let the bot keep you in the loop 📬

## 🤔 FAQ

**Q: Why "Nemo"?**  
A: Because finding course updates is like finding Nemo. Get it? 🐠 Also Tevzadze...

**Q: Is my token safe?**  
A: Yes! Tokens are encrypted before storage. But still, don't share your token file to other people.

**Q: Can I use this for other LMS systems?**  
A: Not without significant modifications. This is specifically built for KIU's LMS.

**Q: Will this get me in trouble?**  
A: It's just automating what you'd do manually. But use responsibly! 🙏

**Q: Will this get you in trouble?**  
A: yea 😊 maybe 😒 I don't know 🙏

## 🚀 Future Improvements (Maybe)

- [x] Add scheduled news checks
- [ ] Docker support
- [x] Better error messages
- [ ] Unit tests
- [ ] Performance tests
- [x] Remove all blocking calls in reactive chains

## 🤝 Contributing

Found a bug? Want to add a feature? PRs are welcome! Just:

1. Fork it
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## ⚖️ License

This project is licensed under the "Do Whatever You Want But Don't Blame Me" License.

Actually, probably MIT. Check the LICENSE file.

## 🙏 Acknowledgments

- KIU for dumping Teams for this abomination
- LMS team for making API easily accessible
- Telegram for their amazing Bot API
- The person reading this for considering using this bot

## 📞 Support

If something breaks:

1. report to me with /report_bug <Message>
2. Check the logs
3. Google the error
4. Cry a little
5. Open an issue on GitHub

---

Made with 💻, Xachapuri, and questionable life choices.

