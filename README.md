# 🔐 AXON [Cryptographic Portal]

AXON is a **secure desktop-based cryptographic communication system** built using Java that enables users to send and receive confidential messages with enhanced security features such as encryption keys, time-based deletion, and steganography.

---

## 📌 Problem Statement

In today’s digital communication, sensitive messages can be easily accessed, copied, or misused due to lack of proper security mechanisms. Most systems do not provide features like time-based deletion or controlled visibility of data. This creates a risk of information leakage and unauthorized access.

---

## 🎯 Objective

To develop a secure messaging system that allows users to send encrypted messages using a unique access key and a time limit. The system ensures messages are accessible only for a limited duration and are automatically deleted afterward. It also enhances security using anti-copy protection and supports steganography for hidden communication.

---

## 🚀 Features

* 🔑 **Encryption with Unique Key** – Each message is secured with a generated code
* ⏳ **Time-To-Live (TTL)** – Messages automatically expire after a set time
* 📋 **Copy to Clipboard** – Easily copy generated keys
* 🛡️ **Anti-Copy Protection** – Prevents unauthorized copying of sensitive data
* 🖼️ **Steganography Support** – Hide and extract messages inside images
* ⚠️ **Limited Access Attempts** – Prevent brute-force access
* 🔄 **Auto Message Deletion** – Data is wiped after usage or expiration

---

## 🛠️ Technologies U

* **Java** – Core programming language
* **Java Swing** – GUI development
* **MySQL** – Database management
* **JDBC (MySQL Connector)** – Database connectivity
* **Steganography Techniques** – Image-based hidden communication
* **OOP Concepts** – Structured and modular design

---

## 📂 Project Structure

```id="proj1"
src/com/project/
│
├── ui/         # User Interface (MainUI.java)
├── service/    # Business logic (Encryption, Stego, etc.)
├── model/      # Data models
├── database/   # Database connection and queries
```

---

## 🗄️ Database Setup

### Step 1: Create Database

```sql id="sql1"
CREATE DATABASE secret_message_db;
USE secret_message_db;
```

---

### Step 2: Create Table

```sql id="sql2"
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE,
    text TEXT,
    viewed BOOLEAN DEFAULT FALSE
);
```

---

## ⚙️ How to Run Locally (VS Code + Terminal)

### 1️⃣ Compile the Project

```bash id="run1"
javac -cp ".;lib/mysql-connector-j-9.7.0.jar" -d . src/com/project/model/*.java src/com/project/database/*.java src/com/project/service/*.java src/com/project/ui/*.java
```

---

### 2️⃣ Run the Application

```bash id="run2"
java -cp ".;lib/mysql-connector-j-9.7.0.jar" com.project.ui.MainUI
```

---

## 💡 How It Works

1. User enters a message and sets a timer
2. System generates a **secure key**
3. Message is stored in database
4. Receiver enters the key to access message
5. Message:


   * Expires after time (TTL)
   * Can be accessed limited times
   * Is protected from copying
6. Steganography Module:
   
   * User can hide secret messages inside an image
   * The system encodes the message into the image file
   * Receiver can upload the image to extract the hidden message securely

---

## 🖥️ User Interface Demonstration
* HOME SCREEN
<img width="1581" height="818" alt="image" src="https://github.com/user-attachments/assets/2f7a9a90-3e6f-4ff4-9858-aefe501e9678" />

*Encryption Panel 
<img width="1346" height="819" alt="image" src="https://github.com/user-attachments/assets/bed5631b-1579-4298-9d2d-618f46edae53" />

*Decryption Panel
<img width="1345" height="813" alt="image" src="https://github.com/user-attachments/assets/b1cdb730-2859-4802-b710-bf3e22d9d824" />

*Image Steganography 
<img width="1332" height="809" alt="image" src="https://github.com/user-attachments/assets/168b3668-adce-4e8b-8ac7-6bdf4f5ce26b" />


---

## 🔐 Security Features

* Clipboard restriction for sensitive data
* Focus-based content protection
* Limited access attempts
* Automatic data destruction
* Masked content display

---

## 🔮 Future Improvements

* Web version using Spring Boot
* AES encryption for stronger security
* User authentication system
* Cloud-based deployment
* Real-time secure messaging


---

## 📜 License

MIT License
