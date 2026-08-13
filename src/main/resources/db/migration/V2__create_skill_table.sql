INSERT INTO skills (name) VALUES
('Java'),
('Spring'),
('Kotlin'),
('SQL'),
('Docker'),
('Git'),
('AWS'),
('React'),
('TypeScript')
ON CONFLICT (name) DO NOTHING;