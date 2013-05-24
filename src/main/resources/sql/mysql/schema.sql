drop table if exists admin_task;
drop table if exists admin_user;
drop table if exists admin_user_role;
drop table if exists admin_role;

create table admin_task (
	id bigint auto_increment,
  title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table admin_user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	email varchar(128),
	status varchar(32),
	register_date timestamp not null default 0,
	last_login_date timestamp,
	last_login_ip varchar(64),
	primary key (id)
) engine=InnoDB;

create table admin_role (
	  id bigint auto_increment,
  	name varchar(255) not null unique,
  	permissions varchar(255),
    primary key (id)
) engine=InnoDB;

create table admin_user_role (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
) engine=InnoDB;