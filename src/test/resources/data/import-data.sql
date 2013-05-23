insert into admin_task (id, title, description, user_id) values(1, 'Study PlayFramework 2.0','http://www.playframework.org/', 2);
insert into admin_task (id, title, description, user_id) values(2, 'Study Grails 2.0','http://www.grails.org/', 2);
insert into admin_task (id, title, description, user_id) values(3, 'Try SpringFuse','http://www.springfuse.com/', 2);
insert into admin_task (id, title, description, user_id) values(4, 'Try Spring Roo','http://www.springsource.org/spring-roo', 2);
insert into admin_task (id, title, description, user_id) values(5, 'Release SpringSide 4.0','As soon as posibble.', 2);

insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(1,'admin','超级管理员','admin@lebooo.com','691b14d79bf0fa2215f155235df5e670b64394cc','7efbd59d9741d34f','enabled','2012-06-04 01:00:00');
insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(2,'user','Calvin','user@lebooo.com','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled','2012-06-04 01:00:00');
insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(3,'user2','Jack','jack@lebooo.com','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled','2012-06-04 01:00:00');
insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(4,'user3','Kate','kate@lebooo.com','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled','2012-06-04 01:00:00');
insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(5,'user4','Sawyer','sawyer@lebooo.com','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled','2012-06-04 01:00:00');
insert into admin_user (id, login_name, name, email, password, salt, status, register_date) values(6,'user5','Ben','ben@lebooo.com','2488aa0c31c624687bd9928e0a5d29e7d1ed520b','6d65d24122c30500','enabled','2012-06-04 01:00:00');

insert into admin_role (id, name, permissions) values(1,'admin','user:view,user:edit');
insert into admin_role (id, name, permissions) values(2,'user','user:view');

insert into admin_user_role (user_id, role_id) values(1,1);
insert into admin_user_role (user_id, role_id) values(1,2);
insert into admin_user_role (user_id, role_id) values(2,2);
insert into admin_user_role (user_id, role_id) values(3,2);
insert into admin_user_role (user_id, role_id) values(4,2);
insert into admin_user_role (user_id, role_id) values(5,2);
insert into admin_user_role (user_id, role_id) values(6,2);