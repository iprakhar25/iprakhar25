-- Portfolio Database Schema
-- This script creates tables and inserts sample data

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Roles Table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role)
);

-- Visitors Table (for analytics)
CREATE TABLE IF NOT EXISTS visitors (
    id BIGSERIAL PRIMARY KEY,
    ip_address VARCHAR(45) UNIQUE NOT NULL,
    user_agent TEXT,
    visited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Projects Table
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    image_url TEXT,
    project_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Project Tech Stack Table
CREATE TABLE IF NOT EXISTS project_tech_stack (
    project_id BIGINT NOT NULL,
    tech VARCHAR(100) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, tech)
);

-- Skills Table
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    proficiency INT CHECK (proficiency >= 1 AND proficiency <= 5),
    skill_order INT DEFAULT 0
);

-- Contact Messages Table
CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert Sample Skills (Requested via UI redesign)
INSERT INTO skills (name, category, proficiency, skill_order) VALUES
-- Row 1: Languages
('Java', 'Backend', 5, 1),
('Python', 'Backend', 5, 2),
('C#', 'Backend', 4, 3),
('JavaScript', 'Frontend', 5, 4),

-- Row 2: Platforms/Frameworks
('Spring Boot', 'Backend', 5, 5),
('.NET', 'Backend', 4, 6),
('React', 'Frontend', 5, 7),
('Azure', 'Infra', 4, 8);

-- Insert Sample Projects
INSERT INTO projects (title, short_description, description, github_url, live_url, project_order) VALUES
(
    'Nexus',
    'Multi-client chat application built with Java',
    'A simple and efficient multi-client chat application supporting broadcasting messages and private messaging between clients via a central server. Built with core Java networking concepts.',
    'https://github.com/iprakhar25/Nexus',
    'https://github.com/iprakhar25/Nexus',
    1
),
(
    'CURLS',
    'Real-time collaborative code editor for multiple users',
    'A real-time code editor designed for collaborative coding sessions. Multiple users can edit code simultaneously with live updates. Built with fundamental web technologies for seamless collaboration.',
    'https://github.com/iprakhar25/CURLS',
    'https://github.com/iprakhar25/CURLS',
    2
),
(
    'To-Do Application',
    'Lightweight task tracking app with clean interface',
    'A responsive To-Do application to track daily tasks efficiently. Features include adding, editing, and tracking tasks with a clean and intuitive user interface.',
    'https://github.com/iprakhar25/to-do',
    'https://github.com/iprakhar25/to-do',
    3
),
(
    'DSA Problem Solver',
    'Data structures and algorithms problem solutions',
    'Collection of Data Structures and Algorithms problems solved with optimized solutions. Includes implementations of various DSA concepts and algorithms for interview preparation.',
    'https://github.com/iprakhar25/DSA-problems',
    'https://github.com/iprakhar25/DSA-problems',
    4
);

-- Insert tech stacks for projects
INSERT INTO project_tech_stack (project_id, tech) VALUES
(1, 'Java'), (1, 'Networking'), (1, 'Socket Programming'),
(2, 'Web Technologies'), (2, 'Real-time'), (2, 'Collaboration'),
(3, 'JavaScript'), (3, 'Responsive Design'), (3, 'DOM Manipulation'),
(4, 'Data Structures'), (4, 'Algorithms'), (4, 'Problem Solving');
