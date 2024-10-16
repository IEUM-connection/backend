
CREATE TABLE IF NOT EXISTS GUARDIAN (
    GUARDIAN_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    EMAIL VARCHAR(100) UNIQUE NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    BIRTH_DATE DATE,
    ADDRESS VARCHAR(255),
    DETAILED_ADDRESS VARCHAR(255),
    POSTAL_CODE VARCHAR(10),
    TEL VARCHAR(20),
    PHONE VARCHAR(20),
    ROLE VARCHAR(50) NOT NULL,

    GUARDIAN_STATUS VARCHAR(50) NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- GUARDIAN 데이터 삽입
INSERT INTO GUARDIAN ( EMAIL, PASSWORD, NAME, BIRTH_DATE, ADDRESS, DETAILED_ADDRESS, POSTAL_CODE, TEL, PHONE, ROLE, GUARDIAN_STATUS)
VALUES ('guardian1@example.com', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', '김보호자', '1969-05-10', '서울시 강남구', 'A동 101호', '06234', '02-1234-5678', '010-1111-2222', 'GUARDIAN', 'GUARDIAN_ACTIVE');

INSERT INTO GUARDIAN ( EMAIL, PASSWORD, NAME, BIRTH_DATE, ADDRESS, DETAILED_ADDRESS, POSTAL_CODE, TEL, PHONE, ROLE, GUARDIAN_STATUS)
VALUES ('guardian2@example.com', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', '박보호자',  '1970-05-10', '서울시 서초구', 'B동 202호', '06543', '02-2345-6789', '010-2222-3333', 'GUARDIAN', 'GUARDIAN_ACTIVE');

INSERT INTO GUARDIAN (EMAIL, PASSWORD, NAME, BIRTH_DATE, ADDRESS, DETAILED_ADDRESS, POSTAL_CODE, TEL, PHONE, ROLE, GUARDIAN_STATUS)
VALUES ( 'guardian3@example.com', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', '이보호자', '1978-05-10', '서울시 송파구', 'C동 303호', '13894', '02-3456-7890', '010-3333-4444', 'GUARDIAN', 'GUARDIAN_ACTIVE');

INSERT INTO GUARDIAN ( EMAIL, PASSWORD, NAME, BIRTH_DATE, ADDRESS, DETAILED_ADDRESS, POSTAL_CODE, TEL, PHONE, ROLE, GUARDIAN_STATUS)
VALUES ( 'guardian4@example.com', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', '최보호자','1975-05-10', '서울시 광진구', 'D동 404호', '04994', '02-4567-8901', '010-4444-5555', 'GUARDIAN', 'GUARDIAN_ACTIVE');

INSERT INTO GUARDIAN (EMAIL, PASSWORD, NAME, BIRTH_DATE, ADDRESS, DETAILED_ADDRESS, POSTAL_CODE, TEL, PHONE, ROLE, GUARDIAN_STATUS)
VALUES ('guardian5@example.com', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', '정보호자', '1976-05-10', '서울시 마포구', 'E동 505호', '04084', '02-5678-9012', '010-5555-6666', 'GUARDIAN', 'GUARDIAN_ACTIVE');

-- ADMIN 테이블 생성
CREATE TABLE IF NOT EXISTS ADMIN (
    ADMIN_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL,
    ADMIN_CODE VARCHAR(50) NOT NULL UNIQUE,
    ADMIN_STATUS VARCHAR(50) NOT NULL,
    LOCATION VARCHAR(100),
    PASSWORD VARCHAR(255) NOT NULL,
    ROLE VARCHAR(50),
    PHONE VARCHAR(20)
);

-- ADMIN 데이터 추가
INSERT INTO ADMIN (ADMIN_CODE, NAME, ADMIN_STATUS, LOCATION, PASSWORD, ROLE, PHONE)
VALUES
('ADMIN001', '박이연','ADMIN_ACTIVE', '서울시 강남구', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i',  'ADMIN', '010-8259-1984');
INSERT INTO ADMIN ( ADMIN_CODE, NAME, ADMIN_STATUS, LOCATION, PASSWORD, ROLE, PHONE)
VALUES
( 'ADMIN002', '옥성민','ADMIN_ACTIVE', '서울시 서초구', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i',  'ADMIN', '010-6778-9176');
INSERT INTO ADMIN (ADMIN_CODE, NAME, ADMIN_STATUS, LOCATION, PASSWORD,  ROLE, PHONE)
VALUES
( 'ADMIN003','황해진', 'ADMIN_ACTIVE', '서울시 송파구', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i',  'ADMIN', '010-7459-3085');
INSERT INTO ADMIN (ADMIN_CODE, NAME, ADMIN_STATUS, LOCATION, PASSWORD,  ROLE, PHONE)
VALUES
('ADMIN004', '김찬준','ADMIN_ACTIVE', '서울시 광진구', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i', 'ADMIN', '010-3745-6954');
INSERT INTO ADMIN (ADMIN_CODE, NAME, ADMIN_STATUS, LOCATION, PASSWORD,  ROLE, PHONE)
VALUES
( 'ADMIN005','윤영아', 'ADMIN_ACTIVE', '서울시 마포구', '$2a$10$n7p4K3MiMB9Jh5zRaRkdtOdkl5NNCs8ZDT6gP6KIANtYX/yj.8a3i',  'ADMIN', '010-7456-3954');


-- MEMBER 테이블 데이터 (사용자 5명)
CREATE TABLE IF NOT EXISTS MEMBER (
    MEMBER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    ADDRESS VARCHAR(255),
    ADMIN_NOTE TEXT,
    AGE INT,
    BIRTH_DATE DATE,
    DETAILED_ADDRESS VARCHAR(255),
    DOCUMENT_ATTACHMENT VARCHAR(255),
    EMERGENCY_CONTACT VARCHAR(255),
    MEDICAL_HISTORY VARCHAR(255),
    MEMBER_CODE VARCHAR(50) UNIQUE,
    MEMBER_STATUS VARCHAR(50),
    MILK_DELIVERY_REQUEST VARCHAR(20),
    NAME VARCHAR(100),
    NOTES TEXT,
    PHONE VARCHAR(20),
    PHONE_INACTIVE_DURATION INT,
    POSTAL_CODE VARCHAR(10),
    POWER_USAGE INT,
    RELATIONSHIP VARCHAR(50),
    ROLE VARCHAR(50),
    TEL VARCHAR(20),
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "X" VARCHAR(255),
    "Y" VARCHAR(255),
    GUARDIAN_ID BIGINT,
    ADMIN_NAME VARCHAR(50)
);

-- MEMBER 데이터 삽입
INSERT INTO MEMBER (ADDRESS, ADMIN_NOTE, AGE, BIRTH_DATE, DETAILED_ADDRESS, DOCUMENT_ATTACHMENT, EMERGENCY_CONTACT, MEDICAL_HISTORY, MEMBER_CODE, MEMBER_STATUS, MILK_DELIVERY_REQUEST, NAME, NOTES, PHONE, PHONE_INACTIVE_DURATION, POSTAL_CODE, POWER_USAGE, RELATIONSHIP,  ROLE, TEL, X, Y, GUARDIAN_ID,ADMIN_NAME)
VALUES ( '서울시 강남구', '고혈압 치료 중', 75, '1949-05-10', 'A동 101호', 'document1.pdf', '010-9876-5432', '고혈압', 'MEM001', 'ACTIVE', 'YES', '김철수', '매주 목요일 약 복용 확인', '010-1234-5678', 20, '06234', 180, '자녀', 'MEMBER', '02-1234-5678', '20.44654', '20.4564',  1, '박이연');

INSERT INTO MEMBER ( ADDRESS, ADMIN_NOTE, AGE, BIRTH_DATE, DETAILED_ADDRESS, DOCUMENT_ATTACHMENT, EMERGENCY_CONTACT, MEDICAL_HISTORY, MEMBER_CODE, MEMBER_STATUS, MILK_DELIVERY_REQUEST, NAME, NOTES, PHONE, PHONE_INACTIVE_DURATION, POSTAL_CODE, POWER_USAGE, RELATIONSHIP,  ROLE, TEL,  X, Y, GUARDIAN_ID, ADMIN_NAME)
VALUES ( '서울시 서초구', '당뇨 관리 중', 68, '1956-03-22', 'B동 202호', 'document2.pdf', '010-8765-4321', '당뇨', 'MEM002', 'ACTIVE', 'NO', '박영희', '간식 제한 필요', '010-2345-6789', 15, '06543', 150, '자녀',  'MEMBER', '02-2345-6789', '20.446546', '20.45646',2 ,  '황해진');

INSERT INTO MEMBER ( ADDRESS, ADMIN_NOTE, AGE, BIRTH_DATE, DETAILED_ADDRESS, DOCUMENT_ATTACHMENT, EMERGENCY_CONTACT, MEDICAL_HISTORY, MEMBER_CODE, MEMBER_STATUS, MILK_DELIVERY_REQUEST, NAME, NOTES, PHONE, PHONE_INACTIVE_DURATION, POSTAL_CODE, POWER_USAGE, RELATIONSHIP,  ROLE, TEL,  X, Y, GUARDIAN_ID, ADMIN_NAME)
VALUES ( '서울시 송파구', '심장 약 복용 중', 72, '1952-08-11', 'C동 303호', 'document3.pdf', '010-7654-3210', '심장 질환', 'MEM003', 'ACTIVE', 'YES', '이민수', '주말에만 운동 가능', '010-3456-7890', 25, '13894', 170, '친척',  'MEMBER', '02-3456-7890', '20.44654', '20.45646',3,  '옥성민');

INSERT INTO MEMBER ( ADDRESS, ADMIN_NOTE, AGE, BIRTH_DATE, DETAILED_ADDRESS, DOCUMENT_ATTACHMENT, EMERGENCY_CONTACT, MEDICAL_HISTORY, MEMBER_CODE, MEMBER_STATUS, MILK_DELIVERY_REQUEST, NAME, NOTES, PHONE, PHONE_INACTIVE_DURATION, POSTAL_CODE, POWER_USAGE, RELATIONSHIP,  ROLE, TEL,  X, Y, GUARDIAN_ID, ADMIN_NAME)
VALUES ( '서울시 광진구', '허리디스크 치료 중', 70, '1954-12-25', 'D동 404호', 'document4.pdf', '010-6543-2109', '허리디스크', 'MEM004', 'ACTIVE', 'NO', '최명숙', '조심스러운 움직임 필요', '010-4567-8901', 10, '04994', 190, '자녀',  'MEMBER', '02-4567-8901', '20.446546', '20.4564',4,  '김찬준');

INSERT INTO MEMBER ( ADDRESS, ADMIN_NOTE, AGE, BIRTH_DATE, DETAILED_ADDRESS, DOCUMENT_ATTACHMENT, EMERGENCY_CONTACT, MEDICAL_HISTORY, MEMBER_CODE, MEMBER_STATUS, MILK_DELIVERY_REQUEST, NAME, NOTES, PHONE, PHONE_INACTIVE_DURATION, POSTAL_CODE, POWER_USAGE, RELATIONSHIP,  ROLE, TEL,  X, Y, GUARDIAN_ID, ADMIN_NAME)
VALUES ( '서울시 마포구', '치매 초기 증상', 66, '1958-11-15', 'E동 505호', 'document5.pdf', '010-5432-1098', '치매', 'MEM005', 'ACTIVE', 'YES', '정우성', '자주 잊어버림, 확인 필요', '010-5678-9012', 30, '04084', 210, '자녀',  'MEMBER', '02-5678-9012','20.446546', '20.45646', 5,  '윤영아');


