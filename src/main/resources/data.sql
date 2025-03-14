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

INSERT INTO Notice(notice_id, user_id, title, content, write_dtm)
VALUES(1,1, '안녕','하세요', now());
INSERT INTO Notice(notice_id, user_id, title, content, write_dtm)
VALUES(2,1, '나는','1번이다.', now());
INSERT INTO Notice(notice_id, user_id, title, content, write_dtm)
VALUES(3,1, '테스트','테스트테스트테스트', now());
