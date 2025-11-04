-- Enable pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ===========================
-- Table: patients
-- ===========================
CREATE TABLE patients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    identifier VARCHAR(50) UNIQUE NOT NULL,
    given_name VARCHAR(100) NOT NULL,
    family_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    valid BOOLEAN DEFAULT FALSE
);

-- Indexes for patients
CREATE INDEX idx_patient_identifier ON patients(identifier);
CREATE INDEX idx_patient_family_name ON patients(family_name);
CREATE INDEX idx_patient_given_name ON patients(given_name);
CREATE INDEX idx_patient_birth_date ON patients(birth_date);

-- ===========================
-- Table: encounters
-- ===========================
CREATE TABLE encounters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    start TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    encounter_class VARCHAR(50) NOT NULL
);

-- Indexes for encounters
CREATE INDEX idx_encounter_patient_id ON encounters(patient_id);
CREATE INDEX idx_encounter_start ON encounters(start);
CREATE INDEX idx_encounter_class ON encounters(encounter_class);

-- ===========================
-- Table: observations
-- ===========================
CREATE TABLE observations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    encounter_id UUID REFERENCES encounters(id) ON DELETE SET NULL,
    code VARCHAR(50) NOT NULL,
    observation_value VARCHAR(255) NOT NULL,
    effective_date_time TIMESTAMP NOT NULL
);

-- Indexes for observations
CREATE INDEX idx_obs_patient_id ON observations(patient_id);
CREATE INDEX idx_obs_encounter_id ON observations(encounter_id);
CREATE INDEX idx_obs_code ON observations(code);
CREATE INDEX idx_obs_effective_date ON observations(effective_date_time);
