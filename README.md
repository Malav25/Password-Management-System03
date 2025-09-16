# 🔐 Password Management System  

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java&logoColor=white)](https://www.oracle.com/java/)  
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql&logoColor=white)](https://www.mysql.com/)  
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen?logo=githubactions&logoColor=white)](https://github.com/)  
[![License](https://img.shields.io/badge/License-MIT-yellow?logo=open-source-initiative&logoColor=white)](LICENSE)  

This is a **command-line-based Password Management System** built in **Java** that helps users securely store, retrieve, and manage their passwords.  
The system includes features for **user authentication, password encryption, and a MySQL database connection** for persistent storage.  

---

## ✨ Features

- **Secure Authentication**  
  Users can register and log in with their credentials to access the system.

- **Password Management**
  - ➕ **Add Passwords**: Store new website credentials in the database.  
  - 📂 **Retrieve Passwords**: Access saved passwords for specific websites.  
  - ✏️ **Update Passwords**: Modify existing passwords for a website.  
  - 🗑️ **Delete Passwords**: Remove saved credentials from the database.  

- **Encryption**  
  Passwords are encrypted before being stored in the database using a custom character-to-symbol mapping method.

- **Password Strength Checker**  
  Validates new passwords to ensure they meet security requirements (length, uppercase, lowercase, digit presence).

- **OTP-based Password Reset**  
  Users can reset their email password through a simulated OTP verification process.

---

## 💻 Tech Stack

- **Languages**: Java  
- **Database**: MySQL  
- **Libraries**: `java.sql` for JDBC connectivity  

---

## 🚀 Getting Started

### ✅ Prerequisites

- **Java Development Kit (JDK)** installed  
- **MySQL** running on your system  
- **MySQL Connector/J** JDBC Driver  

### 🗄️ Database Setup

Run the following SQL commands:

```sql
CREATE DATABASE lju;

USE lju;

CREATE TABLE password (
    Sr_no INT AUTO_INCREMENT PRIMARY KEY,
    website_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
