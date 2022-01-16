DELETE FROM staffs WHERE email = 'test@sample.com';
INSERT INTO staffs(first_name, last_name, email, password, tel, created_by, created_at) VALUES
('john', 'doe', 'test@sample.com', '$2a$06$hY5MzfruCds1t5uFLzrlBuw3HcrEGeysr9xJE4Cml5xEOVf425pmK', '09011112222', 'none', NOW());

DELETE FROM users WHERE email = 'test@sample.com';
INSERT INTO users(first_name, last_name, email, password, tel, address, created_by, created_at) VALUES
('john', 'doe', 'test@sample.com', '$2a$06$hY5MzfruCds1t5uFLzrlBuw3HcrEGeysr9xJE4Cml5xEOVf425pmK', '09011112222', 'tokyo, chuo-ku 1-2-3', 'none', NOW());

DELETE FROM mail_templates WHERE created_by = 'none';
INSERT INTO mail_templates (category_key, template_key, subject, template_body, created_by, created_at) VALUES
(NULL, 'passwordReset', '패스워드 리셋 완료에 대한 부탁', CONCAT('[[$', '{staff.firstName}]]님\r\n\r\n아래의 링크에 접속해서 패스워드를 리셋해주세요.\r\n[[$', '{url}]]'), 'none', NOW());
