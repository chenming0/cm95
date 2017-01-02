create table tag(
	tid int primary key auto_increment,
	tname varchar(100) not null,
	tcount int default 1
);

create table favorite(
	fid int primary key auto_increment,
	flabel varchar(100) not null,
	furl varchar(100) not null,
	ftags varchar(100),
	fdesc varchar(100)
);


insert into tag(tname,tcount) values('未定义',0);











