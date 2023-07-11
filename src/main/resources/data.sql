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
    20
) NOT NULL,
    content VARCHAR
(
    20
) NOT NULL,
    CONSTRAINT tag_content_unique UNIQUE
(
    content
)
    );

INSERT INTO tags (tag_id, type, content)

VALUES (UUID(), 'region', '국내'),
       (UUID(), 'region', '중국'),
       (UUID(), 'region', '일본'),
       (UUID(), 'region', '대만'),
       (UUID(), 'region', '홍콩'),
       (UUID(), 'region', '태국'),
       (UUID(), 'region', '베트남'),

       (UUID(), 'region', '서유럽'),
--        (UUID(), 'region', '영국'),
--        (UUID(), 'region', '프랑스'),
--        (UUID(), 'region', '이탈리아'),
--        (UUID(), 'region', '스위스'),
--        (UUID(), 'region', '독일'),

       (UUID(), 'region', '남유럽'),
--        (UUID(), 'region', '그리스'),
--        (UUID(), 'region', '스페인'),
--        (UUID(), 'region', '포르투갈'),
--        (UUID(), 'region', '크로아티아'),

       (UUID(), 'region', '동유럽'),
--        (UUID(), 'region', '체코'),
--        (UUID(), 'region', '헝가리'),
--        (UUID(), 'region', '오스트리아'),
--        (UUID(), 'region', '폴란드'),


       (UUID(), 'region', '북유럽'),

       (UUID(), 'region', '동남아'),
--        (UUID(), 'region', '태국'),
--        (UUID(), 'region', '베트남'),
--        (UUID(), 'region', '라오스'),
--        (UUID(), 'region', '말레이시아'),
--        (UUID(), 'region', '싱가포르'),

       (UUID(), 'region', '서남아'),
--        (UUID(), 'region', '인도'),

       (UUID(), 'region', '중동'),
--        (UUID(), 'region', '두바이'),

       (UUID(), 'region', '미국'),
       (UUID(), 'region', '캐나다'),
       (UUID(), 'region', '중남미'),

       (UUID(), 'region', '오세아니아'),
       (UUID(), 'region', '남태평양'),
--        (UUID(), 'region', '호주'),
--        (UUID(), 'region', '뉴질랜드'),

       (UUID(), 'region', '아프리카'),

       (UUID(), 'theme', '자연'),
       (UUID(), 'theme', '도시'),
       (UUID(), 'theme', '역사'),
       (UUID(), 'theme', '문화'),
       (UUID(), 'theme', '예술'),
       (UUID(), 'theme', '식도락'),

       (UUID(), 'theme', '바다'),
       (UUID(), 'theme', '산'),
       (UUID(), 'theme', '숲'),

       (UUID(), 'theme', '모험'),
       (UUID(), 'theme', '액티비티'),
       (UUID(), 'theme', '테마파크'),
       (UUID(), 'theme', '레저'),
       (UUID(), 'theme', '골프'),

       (UUID(), 'theme', '건축'),
       (UUID(), 'theme', '음악'),
       (UUID(), 'theme', '미술'),
       (UUID(), 'theme', '전시'),
       (UUID(), 'theme', '축제'),
       (UUID(), 'theme', '공연'),

       (UUID(), 'theme', '휴양'),
       (UUID(), 'theme', '로맨틱');
