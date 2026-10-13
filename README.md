# 🏥 MediQueue — Smart Hospital Queue and Appointment Management System

> A Java-based hospital queue and appointment management system implemented using three Object-Oriented Design Patterns.

**Course:** ISWE404L — Design Pattern  
**Institution:** Vellore Institute of Technology (VIT)  
**Faculty:** Senthil Kumar M

## 👥 Team Members

| Name | Registration Number | Responsibility |
|---|---|---|
| Thulasiraman A T | 23MIS0164 | Abstract Factory and Observer Patterns |
| Suresh I | 23MIS0435 | Facade Pattern and UI Layer |

---

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Problem Statement](#-problem-statement)
- [Key Features](#-key-features)
- [Design Patterns Used](#-design-patterns-used)
- [System Architecture](#-system-architecture)
- [Project Structure](#-project-structure)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Installation and Setup](#-installation-and-setup)
- [How to Run](#-how-to-run)
- [How the System Works](#-how-the-system-works)
- [User Guide](#-user-guide)
- [Design Pattern Integration](#-design-pattern-integration)
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

The project demonstrates the practical application of three software design patterns: **Abstract Factory, Facade, and Observer**.

## ❗ Problem Statement

Traditional hospital queue management systems may encounter several challenges:

- Unorganized patient queues without a clear priority system.
- Limited visibility into queue positions and estimated waiting times.
- Complicated appointment booking, cancellation, and rescheduling.
- Delayed communication when emergency patients arrive.
- Difficulty monitoring multiple departments from a single interface.

### 💡 Proposed Solution

MediQueue addresses these challenges through a modular, object-oriented architecture.

| Problem | Design Pattern | Solution |
|---|---|---|
| Different departments require different queue and scheduling implementations. | Abstract Factory | Creates compatible families of department-specific objects. |
| The user interface should not directly manage multiple subsystems. | Facade | Provides a single entry point to system operations. |
| Patients, doctors, and administrators need queue updates. | Observer | Notifies registered observers when relevant queue events occur. |

---

## ✨ Key Features

- **Patient Registration:** Register patients with personal and contact details.
- **Digital Token Generation:** Generate unique tokens for patients joining queues.
- **Priority-Based Queues:** Support emergency, VIP, and general patient handling.
- **Queue Monitoring:** View queue positions and estimated waiting times.
- **Appointment Management:** Book, cancel, and reschedule appointments.
- **Automatic Notifications:** Notify registered patients, doctors, and administrators about relevant events.
- **Emergency Handling:** Admit emergency patients and broadcast alerts across departments.
- **Doctor Dashboard:** View appointments, monitor queues, and serve patients.
- **Administrator Panel:** Monitor patients, doctors, queues, tokens, and system statistics.
- **Modular Architecture:** Separate business logic, models, utilities, design patterns, and user interfaces.

---

## 🎨 Design Patterns Used

### 1. Abstract Factory Pattern — Creational

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

### 2. Facade Pattern — Structural

**Purpose:** Provide a simplified interface to a complex subsystem.

MediQueue contains multiple services responsible for registration, token generation, appointments, emergency handling, and wait-time calculations.

`MediQueueFacade` provides a single interface through which the user interface accesses these services.

**Subsystems:**

- `PatientRegistrationService`
- `TokenGeneratorService`
- `AppointmentService`
- `EmergencyHandlerService`
- `WaitTimeCalculatorService`

**How it works:**

1. The user performs an action through the patient portal, doctor dashboard, or admin panel.
2. The UI calls the appropriate method on `MediQueueFacade`.
3. The facade coordinates the required internal services.
4. The result is returned to the UI.

**Benefit:** Reduces coupling between the UI and the underlying services, making the application easier to maintain and extend.

### 3. Observer Pattern — Behavioral

**Purpose:** Notify multiple registered objects automatically when an event occurs.

MediQueue uses `LiveQueueSubject` to publish relevant queue updates to registered observers.

**Observer implementations:**

- `PatientObserver` — Receives relevant patient queue updates.
- `DoctorObserver` — Receives relevant queue and patient-serving updates.
- `AdminObserver` — Receives administrative updates and queue alerts.

**How it works:**

1. An event occurs, such as a patient joining or leaving a queue.
2. `LiveQueueSubject` updates the registered observers.
3. Each observer processes the event and produces the appropriate notification.
4. When an observer is removed, it no longer receives notifications from that subject.

**Example notifications:**

- Patient: "Your position has changed to #2."
- Doctor: "A new patient has joined your queue."
- Administrator: "The General Department queue has reached the alert threshold."

**Benefit:** Separates event generation from notification handling and makes it easier to add new observer types.

---

## 🏗️ System Architecture

MediQueue follows a layered architecture in which the UI communicates through the facade, department-specific objects are created through factories, and queue events are handled through the Observer pattern.

```text
+-----------------------------------------------------------+
|                    LAYER 4: USER INTERFACE                |
|                                                           |
|   PatientPortal   |   DoctorDashboard   |   AdminPanel    |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                    LAYER 3: FACADE                        |
|                                                           |
|                     MediQueueFacade                       |
|                                                           |
|  Simplifies access to registration, appointments, tokens, |
|  emergency handling, and wait-time calculations           |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                 LAYER 2: BUSINESS LOGIC                   |
|                                                           |
|  ABSTRACT FACTORY                  OBSERVER                |
|  +----------------------+          +-------------------+  |
|  | GeneralHospitalFactory|         | LiveQueueSubject  |  |
|  | EmergencyHospitalFactory        | PatientObserver   |  |
|  | VIPHospitalFactory    |         | DoctorObserver    |  |
|  +----------------------+          | AdminObserver     |  |
|                                    +-------------------+  |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                   LAYER 1: MODELS & UTILS                  |
|                                                           |
| Patient | Doctor | Token | Appointment | QueueEntry       |
| Department | InputValidator | DateTimeUtils               |
+-----------------------------------------------------------+
```

**Architecture highlights:**

- The UI uses the facade instead of directly coordinating all subsystems.
- The Abstract Factory pattern creates department-specific components.
- The Observer pattern distributes relevant queue-event notifications.
- Models represent the main entities managed by the application.

---

## 📁 Project Structure

The following directory tree represents the intended project organization.

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
    │   ├── creational/
    │   │   └── abstractfactory/
    │   │       ├── interfaces/
    │   │       │   ├── QueueManager.java
    │   │       │   ├── AppointmentScheduler.java
    │   │       │   ├── NotificationService.java
    │   │       │   └── HospitalFactory.java
    │   │       │
    │   │       ├── products/
    │   │       │   ├── general/
    │   │       │   ├── emergency/
    │   │       │   └── vip/
    │   │       │
    │   │       ├── factories/
    │   │       │   ├── GeneralHospitalFactory.java
    │   │       │   ├── EmergencyHospitalFactory.java
    │   │       │   └── VIPHospitalFactory.java
    │   │       │
    │   │       └── client/
    │   │           └── HospitalDepartment.java
    │   │
    │   ├── structural/
    │   │   └── facade/
    │   │       ├── MediQueueFacade.java
    │   │       └── subsystems/
    │   │
    │   └── behavioral/
    │       └── observer/
    │           ├── interfaces/
    │           │   ├── QueueObserver.java
    │           │   └── QueueSubject.java
    │           │
    │           ├── observers/
    │           │   ├── PatientObserver.java
    │           │   ├── DoctorObserver.java
    │           │   └── AdminObserver.java
    │           │
    │           └── subject/
    │               └── LiveQueueSubject.java
    │
    └── ui/
        ├── MainMenu.java
        ├── PatientPortal.java
        ├── DoctorDashboard.java
        └── AdminPanel.java
```

**Note:** The directory tree is formatted for readability. The actual folders and Java filenames in your repository should match your implementation.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Object-Oriented Programming | Encapsulation, abstraction, inheritance, and polymorphism where applicable |
| Abstract Factory | Department-specific object creation |
| Facade | Simplified access to application services |
| Observer | Event-driven queue notifications |
| Java Collections | Managing application data in memory, where implemented |
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

The application initializes the department factories, required services, observers, and default data as configured in the implementation.

The main menu provides access to three interfaces:

```text
========================================
             MEDIQUEUE SYSTEM
========================================

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
7. Receive relevant queue notifications when registered with the Observer system.

### 3. Doctor Workflow

1. Open the Doctor Dashboard.
2. Select the relevant department.
3. View the day's appointments.
4. Inspect the current patient queue.
5. Serve the next eligible patient.
6. Allow the queue subject to notify registered observers about the change.
7. View registered doctors and their availability.

### 4. Administrator Workflow

1. Open the Admin Panel.
2. View registered patients and doctors.
3. Monitor all department queues and appointment schedules.
4. Handle emergency cases.
5. Broadcast administrative alerts.
6. Serve patients from a selected department.
7. Review emergency records, issued tokens, registered observers, and system statistics.

### 5. Emergency Workflow

1. The administrator initiates emergency handling.
2. The system registers the emergency case.
3. The patient is routed to the emergency department's queue according to the implemented priority logic.
4. The Observer system broadcasts the relevant emergency event.
5. Registered observers receive notifications according to their responsibilities.

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
| 3. Serve Next Patient | Serve the next eligible patient and trigger queue updates. |
| 4. View All Doctors | View registered doctors and their availability. |

### Admin Panel — 11 Options

| Option | Function |
|---|---|
| 1. All Patients | View registered patients. |
| 2. All Doctors | View registered doctors. |
| 3. All Queues | View queues across the three departments. |
| 4. All Schedules | View department appointment schedules. |
| 5. Handle Emergency | Admit and handle an emergency case. |
| 6. Broadcast Alert | Send an alert to relevant observers or departments. |
| 7. Serve Patient | Serve the next eligible patient from a selected department. |
| 8. Emergency Log | View recorded emergency cases. |
| 9. All Tokens | View issued digital tokens. |
| 10. View Observers | View registered observers. |
| 11. System Summary | View system statistics and queue information. |

---

## 🔗 Design Pattern Integration

The three patterns work together during patient registration and queue admission.

```text
STEP 1: PatientPortal
        |
        | Calls the facade
        v
STEP 2: MediQueueFacade
        |
        | Coordinates internal services
        v
STEP 3: PatientRegistrationService
        |
        | Patient registration completed
        v
STEP 4: TokenGeneratorService
        |
        | Generates a digital token
        v
STEP 5: HospitalDepartment
        |
        | Uses department-specific objects
        | created through HospitalFactory
        v
STEP 6: Department Queue
        |
        | Patient admitted to the queue
        v
STEP 7: LiveQueueSubject
        |
        | Notifies registered observers
        v
STEP 8: Observer Notifications
        |
        +--> PatientObserver
        +--> DoctorObserver
        +--> AdminObserver
```

### How the Patterns Collaborate

| Pattern | Role in the Workflow |
|---|---|
| Facade | Provides a simplified entry point for the patient registration and token-generation workflow. |
| Abstract Factory | Supplies the appropriate queue and related department components. |
| Observer | Notifies registered stakeholders about relevant queue events. |

**Result:** The UI, service layer, department implementations, and notification mechanisms have distinct responsibilities, improving modularity and maintainability.

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

These estimates are illustrative values from the project description. Actual waiting times depend on the calculation logic implemented in `WaitTimeCalculatorService`.

**Note:** Emergency priority, VIP priority, and FIFO ordering must be enforced by the actual queue implementation. A priority label alone does not guarantee that patients are served in that order.

---

## 🔔 Observer Notification Events

The Observer pattern supports event-based notifications for relevant stakeholders.

| Event | Intended Recipients |
|---|---|
| Patient Added | Relevant patient, department doctor, and administrator |
| Patient Served | Relevant patient, department doctor, and administrator |
| Queue Updated | Observers registered for the relevant queue |
| Emergency Added | Relevant observers across departments, as configured |
| Queue Threshold Reached | Administrator |
| Observer Removed | The removed observer stops receiving updates from that subject |

For emergency broadcasts across all departments, the implementation must explicitly connect the emergency event to the relevant subjects or observers.

---

## 📊 System Summary

| Component | Count / Details |
|---|---|
| Design Patterns | 3 |
| Departments | 3 — General, Emergency, VIP |
| Java Source Files | 42, as reported in the project description |
| Model Classes | 6 |
| Abstract Factory Interfaces | 4 |
| Observer Interfaces | 2 |
| Facade Subsystems | 5 |
| Observer Implementations | 3 |
| Main UI Components | 4, including `MainMenu` |

**Note:** Counts reflect the supplied project description and should be verified against the final repository before submission.

---

## 🔮 Future Enhancements

The following improvements could extend MediQueue's capabilities:

- **Pediatric Department:** Introduce a `PediatricHospitalFactory` and its associated implementations.
- **Mobile Push Notifications:** Add push-notification support through a new notification service.
- **AI-Based Wait-Time Prediction:** Replace basic estimates with a machine-learning-based prediction model.
- **Database Integration:** Replace in-memory data structures with persistent storage using JPA and Hibernate.
- **Web Interface:** Introduce a Spring Boot REST API and a browser-based user interface.
- **Additional Observers:** Add a `NurseObserver` or other stakeholder-specific observers.
- **Improved Queue Scheduling:** Implement configurable priority policies and more accurate waiting-time calculations.
- **Authentication and Authorization:** Introduce role-based access control for patients, doctors, and administrators.

---

## 📚 References

1. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley Professional.

2. Refactoring Guru. [Abstract Factory Pattern](https://refactoring.guru/design-patterns/abstract-factory).

3. Refactoring Guru. [Facade Pattern](https://refactoring.guru/design-patterns/facade).

4. Refactoring Guru. [Observer Pattern](https://refactoring.guru/design-patterns/observer).

5. Oracle. [Java Documentation](https://docs.oracle.com/en/java/).

---

## 👨‍💻 Team

| Name | Registration Number | Contribution |
|---|---|---|
| Thulasiraman A T | 23MIS0164 | Abstract Factory and Observer Patterns |
| Suresh I | 23MIS0435 | Facade Pattern and UI Layer |

**Course:** ISWE404L — Design Pattern  
**Institution:** Vellore Institute of Technology (VIT)  
**Academic Period:** 2026–2027

---

*Built as an academic project to demonstrate the practical application of Object-Oriented Design Patterns in a hospital queue and appointment management system.*
