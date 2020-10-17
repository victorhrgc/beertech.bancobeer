delete from transaction where id > 1;
delete from account where id > 1;

create table users (
    ID numeric(20) NOT NULL,
    DOCUMENT_NUMBER varchar(20) NOT NULL,
    EMAIL varchar(200) NOT NULL,
    ROLE int NOT NULL
);

alter table users add constraint pk_user primary key(ID);


alter table account add column FK_USER_ID numeric(20) not null;

alter table account add constraint fk_account_user foreign key (FK_USER_ID)
references users (ID) on delete no action on update no action;