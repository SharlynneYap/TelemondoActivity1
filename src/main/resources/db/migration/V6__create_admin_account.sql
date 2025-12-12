INSERT INTO users (id, username, email, password)
VALUES (
           UUID(),
           'admin',
           'admin@gmail.com',
           '$2a$10$7QJGPnUBt8zTUtGiWNq6H.McxV2Qf2t0cS7KaGpAQt0D4X.KbQS4W' -- 123456
       );

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin'
  AND r.name = 'ROLE_ADMIN';
