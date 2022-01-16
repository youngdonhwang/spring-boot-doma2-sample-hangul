DELETE FROM roles WHERE created_by = 'none';
INSERT INTO roles (role_key, role_name, created_by, created_at, version) VALUES
('system_admin', '시스템관리자', 'none', NOW(), 1),
('admin', '관리자', 'none', NOW(), 1),
('operator', '오퍼레이터', 'none', NOW(), 1),
('user', '사용자', 'none', NOW(), 1);

DELETE FROM permissions WHERE created_by = 'none';
INSERT INTO permissions (category_key, permission_key, permission_name, created_by, created_at, version) VALUES
('*', '.*', '모든조작', 'none', NOW(), 1),
('code', '^Code\\.(find|show|download)Code$', '코드검색', 'none', NOW(), 1),
('code', '^Code\\.(new|edit)Code$', '사용자등록・편집', 'none', NOW(), 1),
('home', '^Home\\.index$', '홈색인', 'none', NOW(), 1),
('role', '^Role\\.(find|show|download)Role$', '역할검색', 'none', NOW(), 1),
('role', '^Role\\.(new|edit)Role$', '역할등록・편집', 'none', NOW(), 1),
('upload', '^UploadFiles\\..*', '파일업로드', 'none', NOW(), 1),
('user', '^User\\.(find|show|downloadCsv|downloadExcel|downloadPdf)User$', '사용자검색', 'none', NOW(), 1),
('user', '^User\\.(new|edit)User$', '사용자등록・편집', 'none', NOW(), 1),
('staff', '^Staff\\.(find|show|download)Staff$', '담당자검색', 'none', NOW(), 1),
('staff', '^Staff\\.(new|edit)Staff$', '담당자등록・편집', 'none', NOW(), 1);

DELETE FROM role_permissions WHERE created_by = 'none';
INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES
('system_admin', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', NOW(), 1);
INSERT INTO role_permissions (role_key, permission_id, created_by, created_at, version) VALUES
('user', (SELECT permission_id FROM permissions WHERE permission_key = '.*'), 'none', NOW(), 1);

DELETE FROM staff_roles WHERE created_by = 'none';
INSERT INTO staff_roles (staff_id, role_key, created_by, created_at, version) VALUES
((SELECT staff_id FROM staffs WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'system_admin', 'none', NOW(), 1);

DELETE FROM user_roles WHERE created_by = 'none';
INSERT INTO user_roles (user_id, role_key, created_by, created_at, version) VALUES
((SELECT user_id FROM users WHERE email = 'test@sample.com' AND deleted_at IS NULL), 'user', 'none', NOW(), 1);
