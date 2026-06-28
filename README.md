# 🏥 Hospital Appointment Management System

A full-stack **Hospital Appointment Management System** built with **Java 21 + Spring Boot** (backend) and **HTML/CSS/Bootstrap/JavaScript** (frontend). Designed as a production-style resume project with JWT authentication, role-based access control, and a clean responsive UI.

---

## 📋 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Setup & Installation](#setup--installation)
- [Step-by-Step Implementation Plan](#step-by-step-implementation-plan)
- [Default Credentials](#default-credentials)
- [Screenshots](#screenshots)

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based stateless authentication
- BCrypt password encryption
- Role-based access control (ADMIN / DOCTOR / PATIENT)
- Secure REST API with Spring Security

### 👤 Patient Module
- Register & login as patient
- Complete health profile (blood group, allergies, medical history)
- Browse & search doctors by name/specialization
- Book appointments with date/time slot selection
- Cancel pending appointments
- View full appointment history with status tracking

### 🩺 Doctor Module
- Complete professional profile (specialization, availability, fees)
- View all assigned appointments
- Approve or reject appointments with reason
- Add prescriptions and mark appointments as completed
- Dashboard with appointment statistics

### 🛡️ Admin Module
- Dashboard with real-time statistics
- Add, activate/deactivate doctors
- View all registered patients
- Monitor all appointments across the system

---

## 🛠 Tech Stack

| Layer       | Technology                              |
|-------------|----------------------------------------|
| Language    | Java 21                                |
| Framework   | Spring Boot 3.2.x                      |
| Security    | Spring Security + JWT (jjwt 0.12.5)   |
| Persistence | Spring Data JPA + Hibernate            |
| Database    | MySQL 8.x                              |
| Build Tool  | Maven                                  |
| Lombok      | Boilerplate reduction                  |
| Frontend    | HTML5, CSS3, Bootstrap 5.3, JavaScript |
| Icons       | Bootstrap Icons 1.11                   |

---

## 📁 Project Structure

```
hospital-appointment-management-system/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/hospital/management/
│       │   ├── HospitalManagementApplication.java
│       │   ├── config/
│       │   │   ├── DataInitializer.java       ← Seeds roles + admin
│       │   │   └── WebSecurityConfig.java     ← JWT + CORS + routes
│       │   ├── controller/
│       │   │   ├── AuthController.java
│       │   │   ├── PatientController.java
│       │   │   ├── DoctorController.java
│       │   │   ├── AdminController.java
│       │   │   └── PublicDoctorController.java
│       │   ├── dto/
│       │   │   ├── request/
│       │   │   │   ├── LoginRequest.java
│       │   │   │   ├── RegisterRequest.java
│       │   │   │   ├── AppointmentRequest.java
│       │   │   │   ├── PrescriptionRequest.java
│       │   │   │   ├── DoctorProfileRequest.java
│       │   │   │   └── PatientProfileRequest.java
│       │   │   └── response/
│       │   │       ├── JwtResponse.java
│       │   │       ├── MessageResponse.java
│       │   │       ├── DoctorResponse.java
│       │   │       ├── AppointmentResponse.java
│       │   │       └── DashboardStatsResponse.java
│       │   ├── entity/
│       │   │   ├── User.java
│       │   │   ├── Role.java
│       │   │   ├── Doctor.java
│       │   │   ├── Patient.java
│       │   │   ├── Appointment.java
│       │   │   ├── Prescription.java
│       │   │   └── enums/
│       │   │       ├── ERole.java
│       │   │       └── AppointmentStatus.java
│       │   ├── exception/
│       │   │   ├── ResourceNotFoundException.java
│       │   │   └── GlobalExceptionHandler.java
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── RoleRepository.java
│       │   │   ├── DoctorRepository.java
│       │   │   ├── PatientRepository.java
│       │   │   ├── AppointmentRepository.java
│       │   │   └── PrescriptionRepository.java
│       │   ├── security/
│       │   │   ├── jwt/
│       │   │   │   ├── JwtUtils.java
│       │   │   │   ├── AuthTokenFilter.java
│       │   │   │   └── AuthEntryPointJwt.java
│       │   │   └── services/
│       │   │       ├── UserDetailsImpl.java
│       │   │       └── UserDetailsServiceImpl.java
│       │   └── service/
│       │       ├── AuthService.java
│       │       ├── DoctorService.java
│       │       ├── PatientService.java
│       │       ├── AppointmentService.java
│       │       └── AdminService.java
│       └── resources/
│           ├── application.properties
│           └── static/                        ← Frontend served here
│               ├── index.html
│               ├── login.html
│               ├── register.html
│               ├── admin-dashboard.html
│               ├── doctor-dashboard.html
│               ├── patient-dashboard.html
│               ├── css/style.css
│               └── js/app.js
├── frontend/                                  ← Source frontend files
│   ├── css/style.css
│   ├── js/app.js
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── admin-dashboard.html
│   ├── doctor-dashboard.html
│   └── patient-dashboard.html
└── database/
    └── schema.sql
```

---

## 🗄️ Database Schema

```
users ──────────────────────────────────────────
  id, username, email, password, first_name,
  last_name, phone, enabled, created_at

roles ──────────────────────────────────────────
  id, name  (ROLE_ADMIN | ROLE_DOCTOR | ROLE_PATIENT)

user_roles (junction) ──────────────────────────
  user_id → users.id
  role_id → roles.id

doctors ────────────────────────────────────────
  id, user_id → users.id,
  specialization, qualification, license_number,
  experience_years, department, bio,
  consultation_fee, available_days,
  available_time_start, available_time_end, active

patients ───────────────────────────────────────
  id, user_id → users.id,
  date_of_birth, gender, blood_group,
  address, city, state, pincode,
  emergency_contact_name, emergency_contact_phone,
  allergies, medical_history

appointments ───────────────────────────────────
  id, patient_id → patients.id,
  doctor_id → doctors.id,
  appointment_date, appointment_time,
  status (PENDING|APPROVED|REJECTED|CANCELLED|COMPLETED),
  reason_for_visit, symptoms, doctor_notes, rejection_reason

prescriptions ──────────────────────────────────
  id, appointment_id → appointments.id,
  medications, dosage_instructions,
  additional_notes, follow_up_date
```

---

## 🌐 API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login, returns JWT |
| POST | `/api/auth/register` | Register new user |

### Patient (ROLE_PATIENT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/patient/profile` | Get patient profile |
| PUT | `/api/patient/profile` | Update profile |
| POST | `/api/patient/appointments/book` | Book appointment |
| GET | `/api/patient/appointments` | View my appointments |
| PUT | `/api/patient/appointments/{id}/cancel` | Cancel appointment |

### Doctor (ROLE_DOCTOR)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/doctor/profile` | Get doctor profile |
| PUT | `/api/doctor/profile` | Update profile |
| GET | `/api/doctor/appointments` | View appointments |
| PUT | `/api/doctor/appointments/{id}/approve` | Approve appointment |
| PUT | `/api/doctor/appointments/{id}/reject` | Reject with reason |
| POST | `/api/doctor/prescriptions` | Add prescription |

### Admin (ROLE_ADMIN)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/dashboard` | Dashboard stats |
| GET | `/api/admin/doctors` | All doctors |
| POST | `/api/admin/doctors` | Add doctor |
| PUT | `/api/admin/doctors/{id}/deactivate` | Deactivate doctor |
| PUT | `/api/admin/doctors/{id}/activate` | Activate doctor |
| GET | `/api/admin/patients` | All patients |
| GET | `/api/admin/appointments` | All appointments |

### Shared (Authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/doctors/list?search=` | List/search active doctors |
| GET | `/api/doctors/{id}` | Doctor details |

---

## 🚀 Setup & Installation

### Prerequisites
- Java 21+
- Maven 3.6+
- MySQL 8.x
- Git

### Step 1 – Clone the Repository
```bash
git clone https://github.com/yourusername/hospital-appointment-management-system.git
cd hospital-appointment-management-system
```

### Step 2 – Configure MySQL
```sql
-- Option A: Let Spring Boot auto-create (createDatabaseIfNotExist=true in properties)
-- Option B: Run the schema manually
mysql -u root -p < database/schema.sql
```

### Step 3 – Update application.properties
```properties
# backend/src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4 – Build & Run
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

### Step 5 – Access the Application
```
http://localhost:8080
```

The `DataInitializer` automatically seeds:
- 3 roles (ROLE_ADMIN, ROLE_DOCTOR, ROLE_PATIENT)
- Default admin user (admin / Admin@123)

---

## 📋 Step-by-Step Implementation Plan

### Phase 1 – Project Setup (Day 1)
1. Create Spring Boot project via [start.spring.io](https://start.spring.io) with Web, JPA, Security, Validation, MySQL, Lombok, DevTools
2. Configure `application.properties` with DB connection and JWT secret
3. Create MySQL database `hospital_db`

### Phase 2 – Entity Layer (Day 1–2)
4. Create `ERole` and `AppointmentStatus` enums
5. Create `Role`, `User`, `Doctor`, `Patient`, `Appointment`, `Prescription` entities
6. Configure JPA relationships (`@OneToOne`, `@ManyToMany`, `@OneToMany`)
7. Run app and verify Hibernate auto-creates tables

### Phase 3 – Repository Layer (Day 2)
8. Create all 6 repositories extending `JpaRepository`
9. Add custom JPQL queries (search, count by status, etc.)

### Phase 4 – Security Layer (Day 2–3)
10. Implement `UserDetailsImpl` and `UserDetailsServiceImpl`
11. Create `JwtUtils` (generate, validate, extract username)
12. Create `AuthTokenFilter` (OncePerRequestFilter)
13. Create `AuthEntryPointJwt` (401 handler)
14. Configure `WebSecurityConfig` (CORS, CSRF, route security, filter chain)

### Phase 5 – DTOs (Day 3)
15. Create Request DTOs (Login, Register, Appointment, Prescription, Profile)
16. Create Response DTOs (JWT, Message, Doctor, Appointment, DashboardStats)

### Phase 6 – Service Layer (Day 3–4)
17. `AuthService` – register user, authenticate, assign roles
18. `DoctorService` – CRUD profile, search, activate/deactivate
19. `PatientService` – CRUD profile
20. `AppointmentService` – book, cancel, approve, reject, prescribe
21. `AdminService` – dashboard statistics

### Phase 7 – Controller Layer (Day 4)
22. `AuthController` – login & register endpoints
23. `PatientController` – all patient operations
24. `DoctorController` – all doctor operations
25. `AdminController` – admin management
26. `PublicDoctorController` – shared doctor list/search
27. `GlobalExceptionHandler` – consistent error responses

### Phase 8 – Data Seeding (Day 4)
28. `DataInitializer` CommandLineRunner to seed roles and default admin

### Phase 9 – Frontend (Day 5–6)
29. Create `style.css` with sidebar layout, cards, auth pages
30. Create `app.js` with Auth helper, HTTP wrapper, Toast notifications
31. Build `login.html` and `register.html` with form validation
32. Build `admin-dashboard.html` with stats, doctor/patient/appointment management
33. Build `doctor-dashboard.html` with appointment approval/rejection/prescription
34. Build `patient-dashboard.html` with doctor search, booking, history, profile
35. Copy all frontend files to `src/main/resources/static/`

### Phase 10 – Testing & Polish (Day 7)
36. Test all API endpoints via Postman or browser
37. Verify JWT flow: register → login → protected routes
38. Test role isolation (patients can't access /api/admin/**, etc.)
39. Test appointment lifecycle: book → approve → prescribe → complete
40. Fix edge cases, improve error messages

---

## 🔑 Default Credentials

| Role    | Username | Password   |
|---------|----------|------------|
| Admin   | admin    | Admin@123  |
| Doctor  | Register via Admin panel or /register |
| Patient | Register via /register |

---

## 🔒 Security Notes
- Change `app.jwt.secret` in production to a strong random key (256-bit minimum)
- Change default admin password immediately after first login
- Set `spring.jpa.hibernate.ddl-auto=validate` in production
- Enable HTTPS (SSL/TLS) in production deployment

---

## 📌 Possible Enhancements
- Email notifications for appointment status changes
- PDF prescription download
- Payment integration
- Doctor availability calendar view
- Patient appointment reminder (cron job)
- Swagger/OpenAPI documentation

---

## 👨‍💻 Author
Built as a full-stack portfolio project demonstrating:
- Spring Boot REST API design
- Spring Security with JWT
- JPA entity relationships
- Role-based access control
- Responsive Bootstrap frontend

---

## 📄 License
MIT License – free to use for learning and portfolio purposes.
