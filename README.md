# 🚆 Train Reservation System (Python CLI Project)

A simple **command-line based Train Reservation System** built using Python.  
This project simulates real-world railway ticket booking by taking user input, allocating seats, and calculating fares dynamically.

---

## 📌 Project Overview

This application mimics a basic railway reservation workflow where users can:

- Enter passenger details
- Select journey preferences
- Receive a confirmed ticket with:
  - Coach number
  - Berth number
  - Fare details

The system uses **Object-Oriented Programming (OOP)** principles and Python's built-in modules to simulate booking logic.

---

## 🎯 Features

- 🎫 Ticket booking simulation via CLI
- 🧍 Passenger data input (name, age, gender, etc.)
- 🚉 Source & destination selection
- 🛏️ Random berth and coach allocation
- 💰 Fare calculation based on coach type:
  - AC
  - Sleeper
  - Unreserved
- 👋 Welcome and exit messages (static methods)

---

## 🧠 System Design

### 🔹 Core Class: `Train`

| Method | Description |
|------|------------|
| `__init__()` | Initializes passenger and journey details |
| `book()` | Handles booking workflow |
| `berth_and_fare()` | Assigns berth & calculates fare |
| `greet()` | Displays welcome message |
| `greet1()` | Displays farewell message |

---

## 🛠️ Tech Stack

- **Language:** Python 3.x
- **Concepts Used:**
  - Object-Oriented Programming (OOP)
  - Classes & Methods
  - Randomization
  - User Input Handling

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/AlekhyaGangopadhyay/Train_Reservation.git
cd Train_Reservation
```

### ▶️ Run the Program

```bash
python main.py
```
---

## 🧾 User Inputs Required

- Train Number  
- Passenger Name  
- Age  
- Gender (M/F)  
- Source Station  
- Destination Station  
- Coach Type (AC / SL / Unreserved)  

---

## 📤 Sample Output

```
Welcome to Indian Railways

Ticket booked for Rahul (25M)
Train No: 1101
From: Mumbai → Delhi
Coach: A3
Berth: 42
Fare: ₹1500

Have a safe journey!
```

---

## 📂 Project Structure

```
Train_Reservation/
│── main.py        # Main application logic
│── README.md      # Project documentation
```

---

## 🚀 Future Improvements

- Add database integration (MySQL / SQLite)  
- Implement real seat availability logic  
- Build a GUI (Tkinter / Web App)  
- Add ticket cancellation & history  
- Introduce user authentication system  
- API integration with real train data  

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository  
2. Create a new branch  
3. Make your changes  
4. Submit a pull request  

---

## 📜 License

This project is licensed under the MIT License.

---
