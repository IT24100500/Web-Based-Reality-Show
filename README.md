# 🎤 Web-Based Reality Show Voting System 🏆

A **Spring Boot + Thymeleaf** web application designed for managing **reality show competitions**.
It allows **admins** to create and manage episodes, contestants, and voting sessions, while **users** can participate by voting, viewing results, and submitting feedback.

---

## ✨ Features

### 🎬 Episode Management - 

* Add, edit, delete, and view episodes.
* Episodes include **title, description, type, date, time, and status** (`Upcoming`, `Ongoing`, `Completed`).
* Automatically generated episode IDs (`EPxxxx`).
* Admins can see all **voting sessions** linked to episodes.

### 👤 Contestant Management - 

* Add contestants with **name, bio, image, and status**.
* Assign contestants to episodes.
* Edit/update contestant profiles with photo upload.
* Users can view contestant profiles and vote for **active contestants only**.

### 🗳 Voting Session Management - 

* Admins create sessions linked to episodes.
* Sessions define **start & end times**, and whether they are **active**.
* Multiple sessions per episode supported.
* Users see only **ongoing sessions**.

### 🔑 Admin & Role Management - 

* Provides secure role-based access control.
* Supports two roles: Admin and User.

  Users can:

* Register and log in securely
* View available episodes and contestants
* Participate in voting sessions
* Submit feedback after voting

  Admins can:

* Log in to a dedicated admin dashboard
* Manage episodes, contestants, and voting sessions
* Oversee results and rankings
* Monitor and moderate user feedback
* Control contestant status (e.g., Active, Eliminated)

### 🏆 Results & Rankings

* Votes are tracked in `Result` entities.
* Contestant rankings automatically calculated.
* Admin can view votes, placements, winners, and runner-ups.
* Users can view winners and rankings after sessions close.

### 💬 Feedback System

* After voting, users are redirected to **feedback page**.
* Users can:

  * Submit **feedback messages**
  * Give ratings (⭐ 1–5 stars)
  * See their **past feedback history**
  * View their **average rating**
* Admins can:

  * View all feedback with ratings
  * See **system-wide average rating**
  * Delete inappropriate entries

---

## 🛠 Tech Stack

* **Backend**: Spring Boot, Spring MVC, JDBC Template
* **Frontend**: Thymeleaf, Bootstrap 5
* **Database**: MySQL (with JPA/Hibernate annotations in entities)
* **Authentication**: Session-based (Admin & User separation)
* **Build Tool**: Maven

---

## 📂 Project Structure

```
src/main/java/com/example/demo
│── Controller   # Web controllers (Admin, User, Vote, Contestant, Feedback, Result)
│── DAO          # Data Access Layer (JdbcTemplate)
│── Entity       # JPA entities (Episode, Vote, Contestant, Result, Feedback, User, Admin)
│── Service      # Business logic and validation
│── templates    # Thymeleaf HTML templates
│── static       # CSS, JS, images
```

---

## ⚙️ Setup & Installation

1. **Clone the repo**

   ```bash
   git clone https://github.com/your-username/reality-show-voting-system.git
   cd reality-show-voting-system
   ```

2. **Configure Database**

   * Create a MySQL database (e.g., `reality_show_db`).
   * Update `application.properties`:

     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/reality_show_db
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build & Run**

   ```bash
   mvn spring-boot:run
   ```

4. **Access App**

   * Admin Login → `http://localhost:8080/loginA`
   * User Login → `http://localhost:8080/loginU`

---

## 🔒 Roles

* **Admin**

  * Manage Episodes, Contestants, Voting Sessions, Results, and Feedback.

* **User**

  * View Episodes, Vote for Contestants, Submit Feedback, View Results.

---

## 📸 Screenshots (Optional)

* Episode Management Page
* Contestant Profiles
* Voting Session UI
* Results with Winner/Runner-up
* Feedback with Ratings

---

## 🐛 Known Issues / Limitations

* Votes currently tracked per session but not tied to individual users in DB (session-level restriction).
* Feedback ratings are optional; no text analysis yet.
* No export feature (planned for next release).

---

## 🔮 Roadmap

* 📊 **Admin Dashboard** with voting statistics and feedback insights.
* ✍️ **Inline editing** for results and feedback (modal-based).
* 📱 Enhanced **mobile UI/UX**.
* 📤 **Export results & feedback** to CSV/Excel.
* 🔒 Stronger **vote restriction per user/device**.

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m "Added new feature"`)
4. Push branch (`git push origin feature/new-feature`)
5. Open a Pull Request 🎉

---

## 📜 License

This project is licensed under the **MIT License** – feel free to use, modify, and share.
