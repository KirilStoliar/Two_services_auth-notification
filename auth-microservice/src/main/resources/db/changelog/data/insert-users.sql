INSERT INTO users (email, password, first_name, last_name, role)
VALUES ('admin1@example.com', '$2a$10$7uE91RSdKhmcixCLf6xYgenfF/ckHHikL40.52EkXNy0PQ2iYSJ/u', 'admin1', 'Admin', 'ADMIN'), -- password for all admins: admin123
       ('admin2@example.com', '$2a$10$7uE91RSdKhmcixCLf6xYgenfF/ckHHikL40.52EkXNy0PQ2iYSJ/u', 'admin2', 'Admin', 'ADMIN');

-- И 10 пользователей
INSERT INTO users (email, password, first_name, last_name, role)
VALUES
    ('user1@example.com', '$2a$10$SEXjpcmyDDCSM2rwQf9L.e.b8pcNSCgT6RMAovKR902j/IROoDzPu', 'user1', 'User', 'USER'), -- password for all users: user123
    ('user2@example.com', '$2a$10$SEXjpcmyDDCSM2rwQf9L.e.b8pcNSCgT6RMAovKR902j/IROoDzPu', 'user2', 'User', 'USER'),
    ('user3@example.com', '$2a$10$SEXjpcmyDDCSM2rwQf9L.e.b8pcNSCgT6RMAovKR902j/IROoDzPu', 'user3', 'User', 'USER'),
    ('user4@example.com', '$2a$10$SEXjpcmyDDCSM2rwQf9L.e.b8pcNSCgT6RMAovKR902j/IROoDzPu', 'user4', 'User', 'USER');
