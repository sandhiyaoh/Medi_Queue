# 🏥 MediQueue — Smart Hospital Queue and Appointment Management System

> A Java-based hospital queue and appointment management system implemented using the Abstract Factory Object-Oriented Design Pattern.

**Course:** ISWE404L — Design Pattern  
**Institution:** Vellore Institute of Technology (VIT)  
**Faculty:** Senthil Kumar M

## 👥 Team Members

| Name | Registration Number | Responsibility |
|---|---|---|
| Sandhiya O H | 23MIS0114 | Abstract Factory Pattern and UI Layer |
| Deepak Kumar S | 23MIS0191 | Abstract Factory Pattern and Core Models |

---

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Problem Statement](#-problem-statement)
- [Key Features](#-key-features)
- [Design Pattern Used](#-design-pattern-used)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Installation and Setup](#-installation-and-setup)
- [How to Run](#-how-to-run)
- [How the System Works](#-how-the-system-works)
- [User Guide](#-user-guide)
- [Queue Priority System](#-queue-priority-system)
- [System Summary](#-system-summary)
- [Future Enhancements](#-future-enhancements)
- [References](#-references)

---

## 🎯 About the Project

**MediQueue** is a Smart Hospital Queue and Appointment Management System developed entirely in Java. It aims to simplify patient registration, digital token generation, queue management, appointment scheduling, and communication between patients, doctors, and hospital administrators.

The system supports three hospital departments:

- **General Department** — Handles regular patient queues.
- **Emergency Department** — Prioritizes emergency cases.
- **VIP Department** — Supports premium queue handling and priority scheduling.

The project demonstrates the practical application of the **Abstract Factory** software design pattern.

## ❗ Problem Statement

Traditional hospital queue management systems may encounter several challenges:

- Unorganized patient queues without a clear priority system.
- Limited visibility into queue positions and estimated waiting times.
- Complicated appointment booking, cancellation, and rescheduling.
- Delayed communication when emergency patients arrive.
- Difficulty managing multiple departments with different queue and scheduling requirements.

### 💡 Proposed Solution

MediQueue addresses these challenges through a modular, object-oriented architecture using the **Abstract Factory** design pattern.

| Problem | Design Pattern | Solution |
|---|---|---|
| Different departments require different queue and scheduling implementations. | Abstract Factory | Creates compatible families of department-specific objects. |

---

## ✨ Key Features

- **Patient Registration:** Register patients with personal and contact details.
- **Digital Token Generation:** Generate unique tokens for patients joining queues.
- **Priority-Based Queues:** Support emergency, VIP, and general patient handling.
- **Queue Monitoring:** View queue positions and estimated waiting times.
- **Appointment Management:** Book, cancel, and reschedule appointments.
- **Department Notifications:** Notify patients and doctors via department-specific channels (SMS, Emergency Alert, Premium).
- **Emergency Handling:** Admit emergency patients and broadcast alerts across departments.
- **Doctor Dashboard:** View appointments, monitor queues, and serve patients.
- **Administrator Panel:** Monitor patients, doctors, queues, and system statistics.
- **Modular Architecture:** Separate business logic, models, utilities, design pattern, and user interfaces.

---

## 🎨 Design Pattern Used

### Abstract Factory Pattern — Creational

**Purpose:** Create families of related objects without requiring the client to know their concrete implementation classes.

In MediQueue, each department has its own queue management, appointment scheduling, and notification implementations.

**Factory implementations:**

| Concrete Factory | Queue Manager | Appointment Scheduler | Notification Service |
|---|---|---|---|
| `GeneralHospitalFactory` | `GeneralQueue` | `GeneralScheduler` | `SMSNotification` |
| `EmergencyHospitalFactory` | `EmergencyQueue` | `EmergencyScheduler` | `EmergencyAlertNotification` |
| `VIPHospitalFactory` | `PremiumQueue` | `PriorityScheduler` | `PremiumNotification` |

**How it works:**

1. The system selects a department.
2. The corresponding hospital factory creates the appropriate objects.
3. `HospitalDepartment` coordinates the department-specific components.
4. The application uses common interfaces instead of depending directly on concrete implementations.

**Benefit:** New departments can be introduced by adding appropriate factories and implementations, reducing changes to existing client code.

---

## 🏗️ System Architecture

MediQueue follows a layered architecture in which the UI communicates directly with department objects created through the Abstract Factory.

```text
+-----------------------------------------------------------+
|                    LAYER 3: USER INTERFACE                |
|                                                           |
|   PatientPortal   |   DoctorDashboard   |   AdminPanel    |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|              LAYER 2: ABSTRACT FACTORY                    |
|                                                           |
|   HospitalDepartment (Client)                             |
|   +---------------------+                                |
|   | GeneralHospitalFactory                               |
|   | EmergencyHospitalFactory                             |
|   | VIPHospitalFactory  |                                |
|   +---------------------+                                |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                   LAYER 1: MODELS & UTILS                 |
|                                                           |
| Patient | Doctor | Token | Appointment | QueueEntry       |
| Department | InputValidator | DateTimeUtils               |
+-----------------------------------------------------------+
```

**Architecture highlights:**

- The UI uses `HospitalDepartment` directly — no additional intermediate layers.
- The Abstract Factory pattern creates department-specific components.
- Models represent the main entities managed by the application.

---

## 📁 Project Structure

```text
MediQueue/
└── src/
    ├── Main.java
    │
    ├── models/
    │   ├── Department.java
    │   ├── Patient.java
    │   ├── Doctor.java
    │   ├── Token.java
    │   ├── Appointment.java
    │   └── QueueEntry.java
    │
    ├── utils/
    │   ├── ConsoleColors.java
    │   ├── DateTimeUtils.java
    │   ├── InputValidator.java
    │   └── ConsolePrinter.java
    │
    ├── patterns/
    │   └── creational/
    │       └── abstractfactory/
    │           ├── Phase1Runner.java
    │           ├── interfaces/
    │           │   ├── QueueManager.java
    │           │   ├── AppointmentScheduler.java
    │           │   ├── NotificationService.java
    │           │   └── HospitalFactory.java
    │           │
    │           ├── products/
    │           │   ├── general/
    │           │   │   ├── GeneralQueue.java
    │           │   │   ├── GeneralScheduler.java
    │           │   │   └── SMSNotification.java
    │           │   ├── emergency/
    │           │   │   ├── EmergencyQueue.java
    │           │   │   ├── EmergencyScheduler.java
    │           │   │   └── EmergencyAlertNotification.java
    │           │   └── vip/
    │           │       ├── PremiumQueue.java
    │           │       ├── PriorityScheduler.java
    │           │       └── PremiumNotification.java
    │           │
    │           ├── factories/
    │           │   ├── GeneralHospitalFactory.java
    │           │   ├── EmergencyHospitalFactory.java
    │           │   └── VIPHospitalFactory.java
    │           │
    │           └── client/
    │               └── HospitalDepartment.java
    │
    └── ui/
        ├── MainMenu.java
        ├── PatientPortal.java
        ├── DoctorDashboard.java
        └── AdminPanel.java
```

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Object-Oriented Programming | Encapsulation, abstraction, inheritance, and polymorphism where applicable |
| Abstract Factory | Department-specific object creation |
| Java Collections | Managing application data in memory |
| Git and GitHub | Version control and project hosting |
| Console UI | Patient, doctor, and administrator interaction |

---

## ⚙️ Prerequisites

Before running MediQueue, ensure you have:

- **Java Development Kit (JDK):** Version 8 or above, subject to compatibility with your source code.
- **Git:** To clone the repository.
- **Java IDE (optional):** IntelliJ IDEA, Eclipse, or Visual Studio Code with the Java extensions.

### Verify Java Installation

Open a terminal or command prompt and run:

```bash
java -version
javac -version
git --version
```

If Java is not installed, download a suitable JDK from the [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/) page.

---

## 🚀 Installation and Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/Thulasiraman0/mediQueue_DesignPattern.git
```

### Step 2: Navigate to the Project

```bash
cd mediQueue_DesignPattern
```

### Step 3: Open the Project

Choose one of the following options:

**IntelliJ IDEA**
1. Open IntelliJ IDEA.
2. Select **File → Open**.
3. Choose the cloned project folder.
4. Configure the JDK if prompted.
5. Locate `Main.java` under `src`.

**Visual Studio Code**
1. Open VS Code.
2. Install the **Extension Pack for Java**.
3. Select **File → Open Folder**.
4. Choose the cloned project folder.
5. Open `Main.java` under `src`.

---

## ▶️ How to Run

### Option 1: Run Using an IDE

This is the simplest option for a project with multiple Java source files.

1. Open the project in your preferred Java IDE.
2. Ensure `src` is configured as the source directory.
3. Open `Main.java`.
4. Run the `main()` method.

### Option 2: Run Using Linux, macOS, or Git Bash

From the project root, execute:

```bash
cd src

mkdir -p out

find . -name "*.java" > sources.txt

javac -d out @sources.txt

java -cp out Main
```

### Option 3: Run Using Windows PowerShell

From the project root, execute:

```powershell
cd src

New-Item -ItemType Directory -Force -Path out | Out-Null

Get-ChildItem -Recurse -Filter *.java |
    ForEach-Object { $_.FullName } |
    Set-Content sources.txt

javac -d out @sources.txt

java -cp out Main
```

**Important:** These terminal commands assume that `Main` is in the default Java package. If your project declares a package for `Main.java`, use its fully qualified class name when running the application.

---

## 🖥️ How the System Works

### 1. Application Startup

The application initializes the three department factories and their associated objects. Each department is created using its corresponding `HospitalFactory`:

- `GeneralHospitalFactory` → `GeneralQueue` + `GeneralScheduler` + `SMSNotification`
- `EmergencyHospitalFactory` → `EmergencyQueue` + `EmergencyScheduler` + `EmergencyAlertNotification`
- `VIPHospitalFactory` → `PremiumQueue` + `PriorityScheduler` + `PremiumNotification`

The main menu provides access to three interfaces:

```text
========================================
             MEDIQUEUE SYSTEM
========================================
         Design Pattern : Abstract Factory

[1] Patient Portal
[2] Doctor Dashboard
[3] Admin Panel
[4] Exit

========================================
```

### 2. Patient Workflow

1. Open the Patient Portal.
2. Register with the required details, including name, age, contact information, email, and department.
3. Receive a unique patient ID.
4. Generate a digital token and join the selected department's queue.
5. View the queue position and estimated waiting time.
6. Book, cancel, or reschedule appointments.

### 3. Doctor Workflow

1. Open the Doctor Dashboard.
2. Select the relevant department.
3. View the day's appointments.
4. Inspect the current patient queue.
5. Serve the next eligible patient.

### 4. Administrator Workflow

1. Open the Admin Panel.
2. View registered patients and doctors.
3. Monitor all department queues and appointment schedules.
4. Handle emergency cases.
5. Broadcast administrative alerts to all departments.
6. Serve patients from a selected department.
7. View system statistics.

### 5. Emergency Workflow

1. The administrator initiates emergency handling.
2. The system registers the emergency patient.
3. The patient is routed to the Emergency department's queue.
4. The `EmergencyAlertNotification` service broadcasts the alert.
5. All departments receive the emergency notification.

---

## 👤 User Guide

### Patient Portal — 8 Options

| Option | Function |
|---|---|
| 1. Register | Create a patient profile. |
| 2. Book Appointment | Select a doctor and preferred time slot. |
| 3. Generate Token | Generate a queue token and join a department queue. |
| 4. View Queue | View queue positions and waiting-time estimates. |
| 5. Check Wait Time | Check the estimated waiting time. |
| 6. Cancel Appointment | Cancel an appointment using its ID. |
| 7. Reschedule | Change an appointment's scheduled time. |
| 8. My Details | View personal information and appointment details. |

### Doctor Dashboard — 4 Options

| Option | Function |
|---|---|
| 1. Today's Appointments | View appointments scheduled for the day. |
| 2. Current Queue | View the current department queue. |
| 3. Serve Next Patient | Serve the next eligible patient. |
| 4. View All Doctors | View registered doctors and their availability. |

### Admin Panel — 9 Options

| Option | Function |
|---|---|
| 1. All Patients | View registered patients per department. |
| 2. All Doctors | View registered doctors per department. |
| 3. All Queues | View queues across the three departments. |
| 4. All Schedules | View department appointment schedules. |
| 5. Handle Emergency | Admit and handle an emergency case. |
| 6. Broadcast Alert | Send an alert to all departments. |
| 7. Serve Patient | Serve the next eligible patient from a selected department. |
| 8. Emergency Patients | View emergency patients. |
| 9. System Summary | View system statistics. |

---

## ⏱️ Queue Priority System

The system is designed to support different levels of queue priority.

| Priority | Department | Intended Handling |
|---|---|---|
| 1 | Emergency | Highest priority for emergency cases |
| 2 | VIP | Priority handling for VIP patients |
| 3 | General | Regular queue handling, typically FIFO |

### Illustrative Waiting-Time Estimates

| Department | Example Estimate |
|---|---:|
| Emergency | 5 minutes |
| VIP | 7 minutes |
| General | 10 minutes |

These estimates are illustrative values. Actual waiting times depend on the calculation logic implemented in each department's `QueueManager`.

---

## 📊 System Summary

| Component | Count / Details |
|---|---|
| Design Patterns | 1 — Abstract Factory |
| Departments | 3 — General, Emergency, VIP |
| Java Source Files | ~30 |
| Model Classes | 6 |
| Abstract Factory Interfaces | 4 |
| Concrete Factories | 3 |
| Product Classes | 9 (3 per department) |
| Main UI Components | 4, including `MainMenu` |

---

## 🔮 Future Enhancements

The following improvements could extend MediQueue's capabilities:

- **Pediatric Department:** Introduce a `PediatricHospitalFactory` and its associated implementations.
- **Mobile Push Notifications:** Add push-notification support through a new notification service.
- **AI-Based Wait-Time Prediction:** Replace basic estimates with a machine-learning-based prediction model.
- **Database Integration:** Replace in-memory data structures with persistent storage using JPA and Hibernate.
- **Web Interface:** Introduce a Spring Boot REST API and a browser-based user interface.
- **Improved Queue Scheduling:** Implement configurable priority policies and more accurate waiting-time calculations.
- **Authentication and Authorization:** Introduce role-based access control for patients, doctors, and administrators.

---

## 📚 References

1. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley Professional.

2. Refactoring Guru. [Abstract Factory Pattern](https://refactoring.guru/design-patterns/abstract-factory).

3. Oracle. [Java Documentation](https://docs.oracle.com/en/java/).

---

## 👨‍💻 Team

| Name | Registration Number | Contribution |
|---|---|---|
| Sandhiya O H | 23MIS0114 | Abstract Factory Pattern and UI Layer |
| Deepak Kumar S | 23MIS0191 | Abstract Factory Pattern and Core Models |

**Course:** ISWE404L — Design Pattern  
**Institution:** Vellore Institute of Technology (VIT)  
**Academic Period:** 2026–2027

---

*Built as an academic project to demonstrate the practical application of the Abstract Factory Object-Oriented Design Pattern in a hospital queue and appointment management system.*
