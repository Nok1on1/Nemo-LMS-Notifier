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
- 🕷️ Web scraping with Playwright (the fancy kind of stalking)
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
- 🎭 **Playwright** - Browser automation, Held by single Thread... (literally)
- 🍃 **MongoDB** - NoSQL database
- 📱 **Telegram Bot API** - Your notification delivery system
- 🔐 **Spring Security Crypto** - Keeping tokens encrypted
- 🎯 **Lombok** - Less boilerplate, more productivity

## 📋 Prerequisites

Before you dive in, make sure you have:

- ☕ Java 21 or higher
- 🍃 MongoDB instance (local or cloud)
- 🤖 Telegram Bot Token (from [@BotFather](https://t.me/botfather))
- 🎓 KIU LMS Student Token (from your browser)
- 🧠 A moderate understanding of what you're doing

## ⚙️ Setup

### 1. Clone this beauty

```bash
git clone https://github.com/yourusername/Nemo-LMS-Notifier.git
cd Nemo-LMS-Notifier
```

```


### 2. Configure application properties

Create `src/main/resources/application.properties`:

```properties
# Telegram Bot Configuration
telegram.bot.token=YOUR_TELEGRAM_BOT_TOKEN
telegram.bot.username=YOUR_BOT_USERNAME

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/nemo-lms
# or for MongoDB Atlas:
# spring.data.mongodb.uri=mongodb+srv://user:pass@cluster.mongodb.net/nemo-lms

# Encryption (generate your own!)
app.encryption.password=YOUR-SUPER-SECRET-ENCRYPTION-KEY
app.encryption.salt=YOUR-SALT-VALUE

# Async Thread Pool
spring.task.execution.pool.core-size=2
spring.task.execution.pool.max-size=5
spring.task.execution.pool.queue-capacity=100
```

### 3. Build the project

```shell script
./gradlew build
```

### 4. Run it!

```shell script
./gradlew bootRun
```

## 🎮 How to Use

### Step 1: Get Your Student Token

1. Open your browser and go to [lms.kiu.edu.ge](https://lms.kiu.edu.ge)
2. Press `F12` to open DevTools
3. Navigate to **Application** → **Local Storage** → `lms.kiu.edu.ge`
4. Find the `Student-Token` key and copy its value
5. Save it to a `.txt` file

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

| Command          | Description                   |
|------------------|-------------------------------|
| `/start`         | Welcome message & setup guide |
| `/commands`      | List all available commands   |
| `register token` | Begin registration process    |
| `/init_student`  | Initialize enrolled courses   |
| `/check_news`    | Check for new updates         |
| `/help`          | Get help message              |

## 🏗️ Project Structure

p.s might not be accurate
```text
src/main/java/lms/kiu/notifier/lms/nemo/
├── data/ # Constants and models
├── lms/ # LMS automation logic
│ ├── model/ # Request/response models
│ └── service/ # LMS API services
├── mongo/ # Database layer
│ ├── model/ # MongoDB entities
│ ├── repository/ # Spring Data repositories
│ └── service/ # Database services
├── playwright/ # Browser automation
│ ├── page/ # Page objects
│ ├── operation/ # Browser operations
│ └── util/ # Helper utilities
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

### Playwright Magic 🎭

Automated browser navigation to scrape course data. It's like having a robot assistant who never
sleeps (or complains).

### MongoDB Storage 💾

Reactive MongoDB with TTL indexes ensures data doesn't stick around forever (7-day expiry on student
records).

### Secure by Design 🔐

Student tokens are encrypted using Spring Security's `TextEncryptor`. No plain-text secrets here!

### Automatic Scheduler 🕒

- The bot automatically checks your LMS 3 times a day — at 11:00, 16:00, and 20:00 (Tbilisi time)
- You’ll receive updates even if you don’t run /check_news yourself
- Sit back and let the bot keep you in the loop 📬

## 🐛 Known Issues (Features?)

- Playwright can be moody sometimes (hence the `PLAYWRIGHT_THREAD_SLEEP_TIME = 800L`)
- Blocking calls in reactive chains (we're working on it... eventually)
- The bot might send you homework notifications at 2 AM (don't shoot the messenger)

## 🤔 FAQ

**Q: Why "Nemo"?**  
A: Because finding course updates is like finding Nemo. Get it? 🐠 Also Tevzadze...

**Q: Is my token safe?**  
A: Yes! Tokens are encrypted before storage. But still, don't share your token file to other people.

**Q: Can I use this for other LMS systems?**  
A: Not without significant modifications. This is specifically built for KIU's LMS.

**Q: Why does initialization take so long?**  
A: Playwright needs to navigate through each course and subsection. Blame the LMS, not the bot.

**Q: Will this get me in trouble?**  
A: It's just automating what you'd do manually. But use responsibly! 🙏

**Q: Will this get you in trouble?**  
A: yea 😊 maybe 😒 I don't know 🙏

## 🚀 Future Improvements (Maybe)

- [ ] Add scheduled news checks
- [ ] Docker support
- [ ] Better error messages
- [ ] Unit tests (who needs those)
- [ ] Remove all blocking calls in reactive chains

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
- KIU for having an LMS that's scrapable
- Telegram for their amazing Bot API
- Playwright team for making web automation less painful
- The person reading this for considering using this bot

## 📞 Support

If something breaks:

1. Check the logs
2. Google the error
3. Cry a little
4. Open an issue on GitHub

---

Made with 💻, Xachapuri, and questionable life choices.

