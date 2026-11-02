-- ==============================================================================
-- RUANGTUNGGU DATABASE SCHEMA & SEED DATA (SUPABASE POSTGRESQL)
-- UAS Pemrograman Mobile - Sistem Antrean Rumah Sakit & Reservasi Dokter
-- ==============================================================================

-- 1. Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Drop existing tables if re-running
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS queues CASCADE;
DROP TABLE IF EXISTS registrations CASCADE;
DROP TABLE IF EXISTS schedules CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS polyclinics CASCADE;
DROP TABLE IF EXISTS hospitals CASCADE;
DROP TABLE IF EXISTS profiles CASCADE;

-- ==============================================================================
-- TABLES
-- ==============================================================================

-- Profiles / Pasien
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nik VARCHAR(16) UNIQUE NOT NULL,
    nama VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    no_hp VARCHAR(20) NOT NULL,
    tanggal_lahir DATE DEFAULT '1995-01-01',
    jenis_kelamin VARCHAR(20) DEFAULT 'Laki-laki',
    is_verified BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Hospitals / Rumah Sakit
CREATE TABLE hospitals (
    id VARCHAR(50) PRIMARY KEY,
    nama VARCHAR(150) NOT NULL,
    alamat TEXT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    rating NUMERIC(2, 1) DEFAULT 4.8,
    open_hours VARCHAR(50) DEFAULT 'Buka 24 jam',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Polyclinics / Poli
CREATE TABLE polyclinics (
    id VARCHAR(50) PRIMARY KEY,
    hospital_id VARCHAR(50) REFERENCES hospitals(id) ON DELETE CASCADE,
    nama VARCHAR(100) NOT NULL,
    doctor_info VARCHAR(100) DEFAULT '5 dokter · mulai 08.00',
    icon VARCHAR(50) DEFAULT 'local_hospital',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Doctors / Dokter
CREATE TABLE doctors (
    id VARCHAR(50) PRIMARY KEY,
    polyclinic_id VARCHAR(50) REFERENCES polyclinics(id) ON DELETE CASCADE,
    nama VARCHAR(150) NOT NULL,
    spesialis VARCHAR(100) NOT NULL,
    rating NUMERIC(2, 1) DEFAULT 4.9,
    pengalaman VARCHAR(50) DEFAULT '6 tahun pengalaman',
    initials VARCHAR(4) DEFAULT 'DR',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Schedules / Jadwal Praktik
CREATE TABLE schedules (
    id VARCHAR(50) PRIMARY KEY,
    doctor_id VARCHAR(50) REFERENCES doctors(id) ON DELETE CASCADE,
    hari VARCHAR(20) NOT NULL,
    tanggal DATE NOT NULL,
    jam_slot VARCHAR(20) NOT NULL,
    kuota_total INT DEFAULT 20,
    kuota_terisi INT DEFAULT 8,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Registrations / Pendaftaran
CREATE TABLE registrations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES profiles(id) ON DELETE SET NULL,
    schedule_id VARCHAR(50) REFERENCES schedules(id) ON DELETE SET NULL,
    queue_number VARCHAR(10) NOT NULL,
    status VARCHAR(20) DEFAULT 'aktif', -- 'aktif', 'selesai', 'dijeda', 'batal'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Queues / Status Antrean Real-time
CREATE TABLE queues (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    registration_id UUID REFERENCES registrations(id) ON DELETE CASCADE,
    queue_number VARCHAR(10) NOT NULL,
    current_serving VARCHAR(10) NOT NULL,
    remaining_count INT DEFAULT 5,
    estimated_minutes INT DEFAULT 25,
    status VARCHAR(20) DEFAULT 'aktif',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Notifications / Notifikasi Pasien
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES profiles(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    time_ago VARCHAR(50) DEFAULT 'Baru saja',
    is_important BOOLEAN DEFAULT FALSE,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ==============================================================================
-- INITIAL SEED DATA (MATCHING EXACT FIGMA ARTBOARDS)
-- ==============================================================================

-- 1. Insert Profile Demo
INSERT INTO profiles (id, nik, nama, email, no_hp, tanggal_lahir, jenis_kelamin, is_verified)
VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    '3515020101950001',
    'Nama Pasien',
    'pasien@ruangtunggu.com',
    '081234567890',
    '1995-01-01',
    'Laki-laki',
    TRUE
);

-- 2. Insert Hospitals
INSERT INTO hospitals (id, nama, alamat, latitude, longitude, rating, open_hours) VALUES
('rs_01', 'RS Sehat Sidoarjo', 'Jl. Raya Candi No. 45, Sidoarjo', -7.4721, 112.7153, 4.8, 'Buka 24 jam'),
('rs_02', 'RS Harapan Bunda', 'Jl. Pahlawan No. 12, Sidoarjo', -7.4560, 112.7180, 4.8, 'Buka 24 jam'),
('rs_03', 'RS Medika', 'Jl. Mojopahit No. 667, Sidoarjo', -7.4612, 112.7121, 4.8, 'Buka 24 jam');

-- 3. Insert Polyclinics
INSERT INTO polyclinics (id, hospital_id, nama, doctor_info, icon) VALUES
('poli_umum', 'rs_01', 'Poli Umum', '8 dokter · mulai 08.00', 'medical_services'),
('poli_anak', 'rs_01', 'Poli Anak', '5 dokter · mulai 08.00', 'child_care'),
('poli_gigi', 'rs_01', 'Poli Gigi', '3 dokter · mulai 09.00', 'healing'),
('poli_kandungan', 'rs_01', 'Poli Kandungan', '4 dokter · mulai 09.00', 'pregnant_woman'),
('poli_mata', 'rs_01', 'Poli Mata', '2 dokter · mulai 10.00', 'visibility'),
('poli_penyakit_dalam', 'rs_01', 'Poli Penyakit Dalam', '6 dokter · mulai 08.00', 'local_hospital');

-- 4. Insert Doctors
INSERT INTO doctors (id, polyclinic_id, nama, spesialis, rating, pengalaman, initials) VALUES
('doc_01', 'poli_anak', 'dr. Abdul, Sp.A', 'Spesialis Anak', 4.9, '6 tahun pengalaman', 'AL'),
('doc_02', 'poli_anak', 'dr. Budi Santoso, Sp.A', 'Spesialis Anak', 4.8, '10 tahun pengalaman', 'BS'),
('doc_03', 'poli_anak', 'dr. Clara Wijaya, Sp.A', 'Spesialis Anak', 4.9, '8 tahun pengalaman', 'CW');

-- 5. Insert Schedules
INSERT INTO schedules (id, doctor_id, hari, tanggal, jam_slot, kuota_total, kuota_terisi) VALUES
('sch_01', 'doc_01', 'Kamis', '2026-10-22', '08:00 WIB', 20, 8),
('sch_02', 'doc_01', 'Kamis', '2026-10-22', '09:30 WIB', 20, 15),
('sch_03', 'doc_01', 'Kamis', '2026-10-22', '13:00 WIB', 20, 5);

-- 6. Insert Active Registration & Queue
INSERT INTO registrations (id, user_id, schedule_id, queue_number, status)
VALUES (
    'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380b22',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'sch_01',
    'A-023',
    'aktif'
);

INSERT INTO queues (registration_id, queue_number, current_serving, remaining_count, estimated_minutes, status)
VALUES (
    'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380b22',
    'A-023',
    'A-018',
    5,
    25,
    'aktif'
);

-- 7. Insert Notifications
INSERT INTO notifications (user_id, title, message, time_ago, is_important, is_read) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Giliran Anda sudah dekat!', 'Sisa 2 antrean. Segera menuju Poli Anak RS Sehat Sidoarjo.', 'Baru saja', TRUE, FALSE),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Pendaftaran Berhasil', 'Pendaftaran antrean A-023 di RS Sehat Sidoarjo berhasil dikonfirmasi.', '1 jam lalu', FALSE, TRUE),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Pemeriksaan Selesai', 'Kunjungan Anda pada tanggal 12 Okt telah selesai. Berikan ulasan dokter.', '2 hari lalu', FALSE, TRUE);

