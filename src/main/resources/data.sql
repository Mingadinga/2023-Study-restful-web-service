-- // 초기 데이터 작업 수행

insert into user values(10001, sysdate(), 'User1', 'test1111', '701010-1111111');
insert into user values(10002, sysdate(), 'User2', 'test2222', '701010-2222222');
insert into user values(10003, sysdate(), 'User3', 'test3333', '701010-3333333');

insert into post values(10001, 'My first post', 10001);
insert into post values(10002, 'My second post', 10001);