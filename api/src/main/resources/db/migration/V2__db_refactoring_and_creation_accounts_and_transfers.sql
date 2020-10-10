drop table TRANSACTION;

create table ACCOUNT (
    ID numeric(20) NOT NULL,
    CODE varchar(100) NOT NULL
);

alter table account add constraint pk_account primary key(ID);

create table TRANSACTION (
    ID numeric(20) NOT NULL,
    OPERATION numeric(1) NOT NULL,
    VALUE_TRANSACTION numeric(21,2) NOT NULL,
    DATA_TRANSACTION TIMESTAMP not null,
    FK_ACCOUNT_ID numeric (20) NOT NULL
);

alter table "transaction" add constraint pk_transaction primary key(ID);

alter table "transaction" add constraint fk_transaction_account foreign key (FK_ACCOUNT_ID)
references account (ID) on delete no action on update no action;