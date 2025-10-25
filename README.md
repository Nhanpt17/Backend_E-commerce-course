
# 🎓 Graduate Project – Setup & Run Guide

## ⚡ Quick Start (TL;DR)

1. **Clone** this repository
2. **Open** it in **IntelliJ IDEA**
3. Edit your local config in

   ```
   src/main/resources/application.properties
   ```
4. Make sure MySQL is running
5. Press **Run ▶️** — done ✅

---

## 🧩 Introduction

This is a **Spring Boot** project for a graduate management system.
It uses **MySQL**, **Spring Security OAuth2 (Google, Facebook)**, and **Spring Mail**.

---

## ⚙️ 1. Requirements

You only need:

* **IntelliJ IDEA** (Community or Ultimate)
* **MySQL 8+**

> 💡 IntelliJ automatically manages JDK and Maven for you.
> Just make sure to use **Java 17+** when prompted.

---

## 🗂️ 2. Configure Database and Local Settings

Open this file:

```
src/main/resources/application.properties
```

Edit it according to your local setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<your database>
spring.datasource.username=<your username>
spring.datasource.password=<your password>

# Example:
# spring.datasource.url=jdbc:mysql://localhost:3306/graduate_schema
# spring.datasource.username=root
# spring.datasource.password=mypassword
```

---

## 🔐 3. OAuth2 Configuration (Google / Facebook)

If you have OAuth apps created, replace these values:

```properties
spring.security.oauth2.client.registration.google.client-id=<your client id>
spring.security.oauth2.client.registration.google.client-secret=<your client secret>

spring.security.oauth2.client.registration.facebook.client-id=<your client id>
spring.security.oauth2.client.registration.facebook.client-secret=<your client secret>
```

If not, leave them blank — the backend will still work locally.

---

## 📧 4. Email Configuration (SMTP)

If the project uses email features, set your credentials:

```properties
spring.mail.username=<your email>
spring.mail.password=<your password>
```

> ⚠️ **Note:**
> For Gmail, use an **App Password** instead of your real password:
> [https://myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)

---

## 💻 5. Frontend Configuration

If your frontend runs locally (e.g., Angular), update:

```properties
app.client.url=http://localhost:4200
frontend.redirect-url=http://localhost:4200/customer/handle-payment-momo
```

---

## ▶️ 6. Run the Project in IntelliJ IDEA

1. Open **IntelliJ IDEA**
2. Go to **File → Open...** → select the project folder
3. Wait for IntelliJ to load dependencies
4. Open:

   ```
   src/main/java/com/yourpackage/GraduateProjectApplication.java
   ```
5. Click **Run ▶️** (or press `Shift + F10`) to start the server.

---

## 🌐 7. Check the Application

Once started, open:

```
http://localhost:8080
```

Frontend (if available):

```
http://localhost:4200
```

---



