-- Portfolio Database Schema
-- This script creates tables and inserts sample data

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ip_address VARCHAR(45) UNIQUE NOT NULL,
    user_agent TEXT,
    visited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Projects Table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    image_url TEXT,
    `order` INT DEFAULT 0,
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
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    proficiency INT CHECK (proficiency >= 1 AND proficiency <= 5),
    `order` INT DEFAULT 0
);

-- Contact Messages Table
CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert Sample Skills
INSERT INTO skills (name, category, proficiency, `order`) VALUES
-- Backend
('Java', 'Backend', 5, 1),
('Spring Boot', 'Backend', 5, 2),
('Python', 'Backend', 4, 3),
('Node.js', 'Backend', 4, 4),
('PostgreSQL', 'Backend', 5, 5),
('MongoDB', 'Backend', 4, 6),

-- Frontend
('React', 'Frontend', 5, 1),
('TypeScript', 'Frontend', 5, 2),
('Tailwind CSS', 'Frontend', 5, 3),
('Next.js', 'Frontend', 4, 4),
('Vue.js', 'Frontend', 3, 5),
('JavaScript', 'Frontend', 5, 6),

-- Infrastructure
('Docker', 'Infra', 5, 1),
('Kubernetes', 'Infra', 4, 2),
('AWS', 'Infra', 4, 3),
('CI/CD', 'Infra', 4, 4),
('Linux', 'Infra', 5, 5),
('Git', 'Infra', 5, 6),

-- AI & Data
('Machine Learning', 'AI', 4, 1),
('TensorFlow', 'AI', 3, 2),
('Data Analysis', 'AI', 4, 3),
('SQL', 'AI', 5, 4);

-- Insert Sample Projects
INSERT INTO projects (title, short_description, description, github_url, live_url, `order`) VALUES
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
