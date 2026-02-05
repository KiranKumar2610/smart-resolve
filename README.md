Smart Resolve – Complaint Management System

--> Smart Resolve is a full-stack complaint management system built with Spring Boot, JWT Security, MySQL, and Vanilla JavaScript.
It supports role-based access (USER / ADMIN), real-time complaint tracking, admin analytics, and email notifications.

--> This project demonstrates backend architecture, security best practices, transactional data handling, and frontend integration.

✨ FEATURES

👤 USER FEATURES

Register & login with JWT authentication

->Create complaints

->View personal complaints

->Edit complaints until resolved

->Filter complaints by:

   ->ID

   ->Title
   
   ->Status (OPEN / IN_PROGRESS / RESOLVED / REJECTED)

   

🛠 ADMIN FEATURES

->Secure admin dashboard

->View all complaints with pagination

->Change complaint status:

OPEN → IN_PROGRESS → RESOLVED / REJECTED

->Email notification sent to user on status update

->Real-time analytics:

   ->Total complaints
   
   ->Open complaints
   
   ->In-progress complaints
   
   ->Resolved complaints

   

🔐 SECURITY


->JWT-based stateless authentication


->Role-based authorization

->Protected API routes

->Secure password hashing (BCrypt)



🧱TECH STACK

Backend

->Java 17

->Spring Boot 3

->Spring Security (JWT)

->Spring Data JPA (Hibernate)

->MySQL

->Java Mail Sender (Email notifications)

Frontend

->HTML5

->CSS3 (Custom dashboard UI + dark mode)

->Vanilla JavaScript (Fetch API)


Tools

->Maven

->Git & GitHub

->IntelliJ IDEA




🏗 ARCHITECHTURE OVERVIEW

       Controller Layer
              ↓
       Service Layer (Business Logic + Transactions)
              ↓
       Repository Layer (JPA / Hibernate)
              ↓
       MySQL Database



->DTO pattern used for request/response isolation

->Transactional boundaries to prevent lazy loading & null persistence issues

->JWT filter for stateless authentication

->Global exception handling




🔑 ROLES AND ACESS CONTROL


Role	            Permissions

USER	   ---->   Create, view, edit own complaints

ADMIN	   ---->   View all complaints, update status, analytics




⚙️ SETUP INSTRUCTION


1️⃣ Clone Repository

git clone https://github.com/KiranKumar2610/smart-resolve.git

cd smart-resolve


2️⃣ Configure Database


Create MySQL database:

CREATE DATABASE smart_resolve;


update application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/smart_resolve
    spring.datasource.username=YOUR_USERNAME
    spring.datasource.password=YOUR_PASSWORD


3️⃣ Email Configuration (Gmail)

    spring.datasource.url=jdbc:mysql://localhost:3306/smart_resolve
    spring.datasource.username=YOUR_USERNAME
    spring.datasource.password=YOUR_PASSWORD

3️⃣ Email Configuration (Gmail)

    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@gmail.com
    spring.mail.password=app_password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true

⚠️ Use Gmail App Password, not your real password.

4️⃣ Run Application
  mvn spring-boot:run

Open:
   http://localhost:8080


🧪ADMIN ACCOUNT SETUP

Admins are created by updating role in database:

UPDATE users SET role='ADMIN' WHERE email='admin@test.com';


🚀 FUTURE IMPROVEMENTS

REST API documentation (Swagger / OpenAPI)

->Docker support

->Unit & integration tests

->Role-based UI rendering

->Audit logs for admin actions

👨‍💻 AUTHOR

Kiran Kumar
📧 Email: kirankumar.7b@gmail.com

🔗 GitHub: https://github.com/KiranKumar2610

⭐ If you like this project

Give it a ⭐ on GitHub — it really helps!









