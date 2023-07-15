CREATE TABLE IF NOT EXISTS tags
(
    id
    BIGINT
    PRIMARY
    KEY
    AUTO_INCREMENT,
    tag_id
    VARCHAR
(
    255
) NOT NULL,
    type VARCHAR
(
    255
) NOT NULL,
    content VARCHAR
(
    255
) NOT NULL,
    CONSTRAINT tag_content_unique UNIQUE
(
    content
)
    );


INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (1,'국내','e4302d24-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (2,'중국','e43030eb-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (3,'일본','e4303240-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (4,'대만','e43032aa-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (5,'홍콩','e43032fb-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (6,'태국','e430334a-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (7,'베트남','e4303394-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (8,'서유럽','e43033e6-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (9,'남유럽','e4303432-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (10,'동유럽','e4303475-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (11,'북유럽','e43034b6-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (12,'동남아','e43034f7-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (13,'서남아','e430353a-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (14,'중동','e430357a-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (15,'미국','e43035bd-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (16,'캐나다','e43035fe-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (17,'중남미','e4303642-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (18,'오세아니아','e4303682-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (19,'남태평양','e43036c8-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (20,'아프리카','e430370c-2199-11ee-9ef2-0a0027000003','region');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (21,'자연','e430375c-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (22,'도시','e430379f-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (23,'역사','e43037e0-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (24,'문화','e430381f-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (25,'예술','e4303860-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (26,'식도락','e430389f-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (27,'바다','e43038e0-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (28,'산','e430391e-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (29,'숲','e4303964-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (30,'모험','e43039a5-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (31,'액티비티','e43039e5-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (32,'테마파크','e4303a28-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (33,'레저','e4303a6c-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (34,'골프','e4303aac-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (35,'건축','e4303ae9-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (36,'음악','e4303b27-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (37,'미술','e4303b66-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (38,'전시','e4303ba3-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (39,'축제','e4303be6-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (40,'공연','e4303c25-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (41,'휴양','e4303c68-2199-11ee-9ef2-0a0027000003','theme');
INSERT INTO tags (`id`,`content`,`tag_id`,`type`) VALUES (42,'로맨틱','e4303ca5-2199-11ee-9ef2-0a0027000003','theme');