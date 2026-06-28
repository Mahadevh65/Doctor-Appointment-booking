-- ============================================================
--  Hospital Appointment Management System – Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS hospital_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hospital_db;

-- ─── Roles ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS roles (
    id   INT          AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20)  NOT NULL UNIQUE
);

-- ─── Users ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(120) NOT NULL,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    phone       VARCHAR(15),
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME,
    updated_at  DATETIME
);

-- ─── User–Role junction ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id INT    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id)  ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id)  ON DELETE CASCADE
);

-- ─── Doctors ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS doctors (
    id                   BIGINT        AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT        NOT NULL UNIQUE,
    specialization       VARCHAR(100)  NOT NULL,
    qualification        VARCHAR(100),
    license_number       VARCHAR(50),
    experience_years     INT,
    department           VARCHAR(100),
    bio                  TEXT,
    consultation_fee     DOUBLE        NOT NULL DEFAULT 0,
    available_days       VARCHAR(100),
    available_time_start VARCHAR(10),
    available_time_end   VARCHAR(10),
    active               BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at           DATETIME,
    updated_at           DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── Patients ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS patients (
    id                      BIGINT      AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT      NOT NULL UNIQUE,
    date_of_birth           DATE,
    gender                  VARCHAR(20),
    blood_group             VARCHAR(10),
    address                 VARCHAR(255),
    city                    VARCHAR(50),
    state                   VARCHAR(50),
    pincode                 VARCHAR(10),
    emergency_contact_name  VARCHAR(100),
    emergency_contact_phone VARCHAR(15),
    allergies               TEXT,
    medical_history         TEXT,
    created_at              DATETIME,
    updated_at              DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── Appointments ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS appointments (
    id               BIGINT      AUTO_INCREMENT PRIMARY KEY,
    patient_id       BIGINT      NOT NULL,
    doctor_id        BIGINT      NOT NULL,
    appointment_date DATE        NOT NULL,
    appointment_time TIME        NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    reason_for_visit TEXT,
    symptoms         TEXT,
    doctor_notes     TEXT,
    rejection_reason TEXT,
    created_at       DATETIME,
    updated_at       DATETIME,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES doctors(id)  ON DELETE CASCADE
);

-- ─── Prescriptions ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS prescriptions (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id       BIGINT NOT NULL UNIQUE,
    medications          TEXT,
    dosage_instructions  TEXT,
    additional_notes     TEXT,
    follow_up_date       VARCHAR(20),
    created_at           DATETIME,
    updated_at           DATETIME,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

-- ─── Seed Roles ─────────────────────────────────────────────
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_DOCTOR'), ('ROLE_PATIENT');
