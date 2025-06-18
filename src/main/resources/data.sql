INSERT INTO USERS(user_id, email, nickname, gender, main_animal, social_type, role)
VALUES (1, 'test@example.com', '테스트유저', '여', 1, 'Kakao','USER');

INSERT INTO CHARACTERS(character_id, character) VALUES(1, '주말마다 집에 가요');
INSERT INTO CHARACTERS(character_id, character) VALUES(2, '밖에 잘 안 나가요');
INSERT INTO CHARACTERS(character_id, character) VALUES(3, '잠귀가 어두워요');
INSERT INTO CHARACTERS(character_id, character) VALUES(4, '잠귀가 밝아요');
INSERT INTO CHARACTERS(character_id, character) VALUES(5, '조용한게 좋아요');
INSERT INTO CHARACTERS(character_id, character) VALUES(6, '대화하고 싶어요');
INSERT INTO CHARACTERS(character_id, character) VALUES(7, '깨끗한게 좋아요');
INSERT INTO CHARACTERS(character_id, character) VALUES(8, '깨끗하지 않아도 상관 없어요');
INSERT INTO CHARACTERS(character_id, character) VALUES(9, '알람이 필수에요');
INSERT INTO CHARACTERS(character_id, character) VALUES(10, '방 안에서 먹어요');

INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(1,4,1, '안녕','하세요', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(2,345,1, '나는','1번이다.', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(3,23456,1, '테스트','테스트테스트테스트', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(4,53,1, '세상에서','id = 4', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(5, 999,1, '제일 가는','id = 5', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(6, 2, 1, '포테이토','id = 6', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(7, 568, 1, '칩','id = 7', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(8,1,1, '야호','아 귀찮아', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(9, 134, 1, '야나두','으아아악', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(10,9, 1, '안들려','이게 뭐지', now());
INSERT INTO Post(post_id, post_check_id, user_id, title, content, write_dtm)
VALUES(11, 10, 1, '다음','페이지여야 함', now());

