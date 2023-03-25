use master
go

if (object_id('MSF_Forum')) is not null
	drop database MSF_Forum
go

create database MSF_Forum
go

use MSF_Forum

if (object_id('Post')) is not null
	drop table Post
go
if (object_id('Thread')) is not null
	drop table Thread
go

create table Thread(
	id int identity(1,1) primary key,
	title varchar(100),
	addDate datetime,
	visits int,
	publisher varchar(100)
);

create table Post(
	id int identity(1,1) primary key,
	threadId int not null,
	publisher varchar(150),
	addDate datetime,
	body char(1000)
);
go

alter table Post
add constraint fkPost foreign key (threadId) references Thread(id)
go

insert into Thread (title,addDate,visits,publisher)
values ('Alguém sabe como fazer uma tabela em HTML????',getdate(),0,'Jack');

insert into Thread (title,addDate,visits,publisher)
values ('Alguém me explica o que é o ModelBinding???',getdate(),0,'Rob');

insert into Post (threadId,body,addDate,publisher)
values (1,'Primeiro escolhes o número de linhas e depois o número de colunas! Espero ter ajudado!',getdate(),'Bill')

insert into Post (threadId,body,addDate,publisher)
values (1,'A parte mais importante é o CSS!! Nunca esquecer!',getdate(),'Steve')


insert into Post (threadId,body,addDate,publisher)
values (2,'Isso tem a haver com modelos!',getdate(),'Tom')

insert into Post (threadId,body,addDate,publisher)
values (2,'Sim sim! É quando fazes o bind a um modelo, não tem grande importância!',getdate(),'Bruce')
