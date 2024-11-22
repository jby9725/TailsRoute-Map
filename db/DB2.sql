## 병원 테이블
CREATE TABLE hospital(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '고유 병원 ID',
                         `name` TEXT NOT NULL COMMENT '병원 이름',
                         callNumber VARCHAR(20) DEFAULT NULL COMMENT '소재지전화번호',
                         jibunAddress TEXT COMMENT '병원의 지번 주소',
                         roadAddress TEXT COMMENT '병원의 도로명 주소',
                         latitude VARCHAR(20) DEFAULT NULL COMMENT '좌표정보(x)',
                         longitude VARCHAR(20) DEFAULT NULL COMMENT '좌표정보(y)',
                         businessStatus ENUM('영업', '폐업') DEFAULT '영업' COMMENT '영업 상태',
                         `type` ENUM('일반', '야간', '24시간') NOT NULL DEFAULT '일반' COMMENT '병원 타입'
);

USE `tails_route`;

SHOW TABLES;

SELECT * FROM hospital;

SELECT COUNT(*) FROM hospital;

## 0이 목표
SELECT COUNT(*) FROM hospital WHERE latitude IS NULL AND longitude IS NULL;

## 결과 없음이 목표
SELECT * FROM hospital WHERE latitude IS NULL AND longitude IS NULL;

### 비어있는 위경도값 채워넣기
UPDATE hospital SET latitude = 35.2013422688121, longitude = 126.898935594636
WHERE id = 7 AND `name` LIKE '이든%';

UPDATE hospital SET latitude = 37.2919346310962, longitude = 127.634312905728
WHERE id = 1313 AND `name` LIKE '강산%';

UPDATE hospital SET latitude = 37.30179405844902, longitude = 127.64579349938467
WHERE id = 1328 AND `name` LIKE '세종%';

UPDATE hospital SET latitude = 36.2815766242188, longitude = 126.912868565514
WHERE id = 2520 AND `name` LIKE '참좋%';

UPDATE hospital SET latitude = 36.4179580165704, longitude = 127.349397996496
WHERE id = 2578 AND `name` LIKE '국군%';

UPDATE hospital SET latitude = 37.3450683900994, longitude = 127.177514281345
WHERE id = 3604 AND `name` LIKE '분당%';

UPDATE hospital SET latitude = 37.2409304055933, longitude = 127.049357240986
WHERE id = 4023 AND `name` LIKE '채움%';

UPDATE hospital SET latitude = 37.3780939681787, longitude = 127.253638982493
WHERE id = 4444 AND `name` LIKE '광주%';

UPDATE hospital SET latitude = 37.3600253564851, longitude = 127.151818706834
WHERE id = 4450 AND `name` LIKE '애플%';

UPDATE hospital SET latitude = 37.3588496176654, longitude = 127.160146463286
WHERE id = 4550 AND `name` LIKE '참좋%';

SELECT * FROM hospital WHERE id IN (7, 1313, 1328, 2520, 2578, 3604, 4023, 4444, 4450, 4550);

USE `tails_route`;
SHOW TABLES;
SELECT * FROM hospital;
SELECT * FROM hospital WHERE place_id IS NULL;
SELECT COUNT(*) FROM hospital WHERE place_id IS NULL;
