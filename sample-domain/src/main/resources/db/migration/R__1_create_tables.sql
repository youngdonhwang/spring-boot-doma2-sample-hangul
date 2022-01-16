CREATE TABLE IF NOT EXISTS persistent_logins(
  username VARCHAR(64) NOT NULL COMMENT '로그인ID'
  , ip_address VARCHAR(64) NOT NULL COMMENT 'IP주소'
  , user_agent VARCHAR(200) NOT NULL COMMENT 'UserAgent'
  , series VARCHAR(64) COMMENT '직렬토큰'
  , token VARCHAR(64) NOT NULL COMMENT '토큰'
  , last_used DATETIME NOT NULL COMMENT '최종사용일'
  , PRIMARY KEY (series)
  , KEY idx_persistent_logins(username, ip_address, user_agent)
  , KEY idx_persistent_logins_01(last_used)
) COMMENT='로그인기록';

CREATE TABLE IF NOT EXISTS code_category(
  code_category_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '코드분류ID'
  , category_key VARCHAR(50) NOT NULL COMMENT '코드분류키'
  , category_name VARCHAR(50) NOT NULL COMMENT '코드분류명'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (code_category_id)
  , KEY idx_code_category (category_key, deleted_at)
) COMMENT='코드분류';

CREATE TABLE IF NOT EXISTS code(
  code_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '코드ID'
  , code_category_id INT(11) unsigned NOT NULL COMMENT '코드분류ID'
  , code_key VARCHAR(50) NOT NULL COMMENT '코드키'
  , code_value VARCHAR(100) NOT NULL COMMENT '코드값'
  , code_alias VARCHAR(100) DEFAULT NULL COMMENT '코드별칭'
  , attribute1 VARCHAR(2) DEFAULT NULL COMMENT '속성1'
  , attribute2 VARCHAR(2) DEFAULT NULL COMMENT '속성2'
  , attribute3 VARCHAR(2) DEFAULT NULL COMMENT '속성3'
  , attribute4 VARCHAR(2) DEFAULT NULL COMMENT '속성4'
  , attribute5 VARCHAR(2) DEFAULT NULL COMMENT '속성5'
  , attribute6 VARCHAR(2) DEFAULT NULL COMMENT '속성6'
  , display_order INT(11) DEFAULT 0 COMMENT '표시순'
  , is_invalid TINYINT(1) NOT NULL DEFAULT 0 COMMENT '무효플래그'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제지'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (code_id)
  , KEY idx_code (code_key, deleted_at)
) COMMENT='코드';

CREATE TABLE IF NOT EXISTS permissions(
  permission_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '권한ID'
  , category_key VARCHAR(50) NOT NULL COMMENT '권한카테고리키'
  , permission_key VARCHAR(100) NOT NULL COMMENT '권한키'
  , permission_name VARCHAR(50) NOT NULL COMMENT '권한명'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (permission_id)
  , KEY idx_permissions (permission_key, deleted_at)
) COMMENT='권한';

CREATE TABLE IF NOT EXISTS roles(
  role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '역할ID'
  , role_key VARCHAR(100) NOT NULL COMMENT '역할키'
  , role_name VARCHAR(100) NOT NULL COMMENT '역할명'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (role_id)
  , KEY idx_roles (role_key, deleted_at)
) COMMENT='역할';

CREATE TABLE IF NOT EXISTS staff_roles(
  staff_role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '담당자역할ID'
  , staff_id INT(11) unsigned NOT NULL COMMENT '담당자ID'
  , role_key VARCHAR(100) NOT NULL COMMENT '역할키'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (staff_role_id)
  , KEY idx_staff_roles (staff_id, role_key, deleted_at)
) COMMENT='담당자역할';

CREATE TABLE IF NOT EXISTS user_roles(
  user_role_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '사용자역할ID'
  , user_id INT(11) unsigned NOT NULL COMMENT '사용자ID'
  , role_key VARCHAR(100) NOT NULL COMMENT '역할키'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (user_role_id)
  , KEY idx_user_roles (user_id, role_key, deleted_at)
) COMMENT='사용자역할';

CREATE TABLE IF NOT EXISTS staffs(
  staff_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '담당자ID'
  , first_name VARCHAR(40) NOT NULL COMMENT '이름'
  , last_name VARCHAR(40) NOT NULL COMMENT '성'
  , email VARCHAR(100) DEFAULT NULL COMMENT '메일주소'
  , password VARCHAR(100) DEFAULT NULL COMMENT '패스워드'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '전화번호'
  , password_reset_token VARCHAR(50) DEFAULT NULL COMMENT '패스워드리셋토큰'
  , token_expires_at DATETIME DEFAULT NULL COMMENT '토근실효일'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (staff_id)
  , KEY idx_staffs (email, deleted_at)
) COMMENT='담당자';

CREATE TABLE IF NOT EXISTS users(
  user_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '사용자ID'
  , first_name VARCHAR(40) NOT NULL COMMENT '이름'
  , last_name VARCHAR(40) NOT NULL COMMENT '성'
  , email VARCHAR(100) DEFAULT NULL COMMENT '메일주소'
  , password VARCHAR(100) DEFAULT NULL COMMENT '패스워드'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '전화번호'
  , zip VARCHAR(20) DEFAULT NULL COMMENT '우편번호'
  , address VARCHAR(100) DEFAULT NULL COMMENT '주소'
  , upload_file_id INT(11) unsigned DEFAULT NULL COMMENT '첨부파일'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (user_id)
  , KEY idx_users (email, deleted_at)
) COMMENT='사용자';

CREATE TABLE IF NOT EXISTS upload_files(
  upload_file_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '파일ID'
  , file_name VARCHAR(100) NOT NULL COMMENT '파일명'
  , original_file_name VARCHAR(200) NOT NULL COMMENT '원래파일명'
  , content_type VARCHAR(50) NOT NULL COMMENT '컨텐츠타입'
  , content LONGBLOB NOT NULL COMMENT '컨텐츠'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (upload_file_id)
  , KEY idx_upload_files (file_name, deleted_at)
) COMMENT='업로드파일';

CREATE TABLE IF NOT EXISTS mail_templates(
  mail_template_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '메일템플릿ID'
  , category_key VARCHAR(50) DEFAULT NULL COMMENT '템플릿분류키'
  , template_key VARCHAR(100) NOT NULL COMMENT '템플릿키'
  , subject VARCHAR(100) NOT NULL COMMENT '메일타이틀'
  , template_body TEXT NOT NULL COMMENT '메일본문'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (mail_template_id)
  , KEY idx_mail_templates (template_key, deleted_at)
) COMMENT='메일템플릿';

CREATE TABLE IF NOT EXISTS send_mail_queue(
  send_mail_queue_id BIGINT(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '메일송신큐ID'
  , from_address varchar(255) NOT NULL COMMENT 'from주소'
  , to_address varchar(255) DEFAULT NULL COMMENT 'to주소'
  , cc_address varchar(255) DEFAULT NULL COMMENT 'cc주소'
  , bcc_address varchar(255) DEFAULT NULL COMMENT 'bcc주소'
  , subject varchar(255) DEFAULT NULL COMMENT '건명'
  , body TEXT
  , sent_at DATETIME DEFAULT NULL COMMENT '메일송신일시'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (send_mail_queue_id, created_at)
  , KEY idx_send_mail_queue (sent_at, deleted_at)
) COMMENT='메일송신큐' PARTITION BY RANGE (YEAR(created_at))(
  PARTITION p2017 VALUES LESS THAN (2017),
  PARTITION p2018 VALUES LESS THAN (2018),
  PARTITION p2019 VALUES LESS THAN (2019),
  PARTITION p2020 VALUES LESS THAN (2020),
  PARTITION p2021 VALUES LESS THAN (2021),
  PARTITION p2022 VALUES LESS THAN (2022),
  PARTITION p2023 VALUES LESS THAN (2023),
  PARTITION p2024 VALUES LESS THAN (2024),
  PARTITION p2025 VALUES LESS THAN (2025),
  PARTITION p2026 VALUES LESS THAN (2026),
  PARTITION p2027 VALUES LESS THAN (2027),
  PARTITION p2028 VALUES LESS THAN (2028),
  PARTITION p2029 VALUES LESS THAN (2029),
  PARTITION p2030 VALUES LESS THAN (2030)
);

CREATE TABLE IF NOT EXISTS role_permissions(
  role_permission_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '역할권한관계ID'
  , role_key VARCHAR(100) NOT NULL COMMENT '역할키'
  , permission_id INT(11) NOT NULL COMMENT '권한ID'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (role_permission_id)
  , KEY idx_role_permissions (role_key, deleted_at)
) COMMENT='역할권한관계';

CREATE TABLE IF NOT EXISTS holidays(
  holiday_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '공휴일ID'
  , holiday_name VARCHAR(100) NOT NULL COMMENT '공휴일명'
  , holiday_date DATE NOT NULL COMMENT '날짜'
  , created_by VARCHAR(50) NOT NULL COMMENT '등록자'
  , created_at DATETIME NOT NULL COMMENT '등록일시'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '갱신자'
  , updated_at DATETIME DEFAULT NULL COMMENT '갱신일시'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '삭제자'
  , deleted_at DATETIME DEFAULT NULL COMMENT '삭제일시'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '개정번호'
  , PRIMARY KEY (holiday_id)
  , KEY idx_holidays (holiday_name, deleted_at)
) COMMENT='공휴일';

