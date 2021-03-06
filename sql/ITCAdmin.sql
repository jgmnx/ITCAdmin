drop table client;
drop table product_spec;
drop table product;
drop table category;
drop table line;
drop table agent;
drop table app_version;
drop table IMAGES_CATALOG;

create table agent (
    id          int not null generated by default as identity constraint agent_pk primary key,
    name        varchar(100),
    superuser   boolean,        -- true just for Carlos and Israel
    username    varchar(20),
    passwd      varchar(20),
    active      boolean);

create table client (
    id          int not null generated by default as identity constraint client_pk primary key,
    name        varchar(100),
    price_list  int,
    agent       int,
    username    varchar(20),
    passwd      varchar(20),
    active      boolean);

alter table client
    add constraint agent_id_fk
    foreign key (agent) references agent(id);

create table line (
    id          int not null generated by default as identity constraint line_pk primary key,
    name        varchar(20),
    text        varchar(50),
    legacy_name varchar(50),
    description varchar(100),
    image       varchar(50),        --(relative to /resources)
    norder      int);

create table category (
    id          int not null generated by default as identity (start with 1, increment by 1) constraint category_pk primary key,
    line        int,
    name        varchar(50),
    text        varchar(50),
    legacy_name varchar(50),
    description varchar(100),
    image       varchar(50),        --(relative to /resources)
    norder       int);

alter table category
    add constraint line_id_fk
    foreign key (line) references line(id);

create table product (
    id              varchar(30) primary key,
    description     varchar(200),
    base_price      float,
    price_2         float,
    price_3         float,
    price_4         float,
    price_5         float,
    price_6         float,
    price_7         float,
    price_8         float,
    promotion       float,
    is_package      boolean,
    category        int,
    small_pic       blob(1M),
    chksum_small    bigint,
    big_pic         blob(1M),
    chksum_big      bigint);

alter table product
    add constraint category_id_fk
    foreign key (category) references category(id);

create table product_spec (
    id          int not null generated by default as identity (start with 1, increment by 1) constraint product_spec_pk primary key,
    product     varchar(30),
    spec        varchar(200),
    value       varchar(50));

alter table product_spec
    add constraint product_fk
    foreign key (product) references product(id);

create table app_version (
    id          varchar(30) primary key,
    comment     varchar(200)
);


create table images_catalog (
    id          int not null generated by default as identity (start with 1, increment by 1) constraint images_catalog_pk primary key,
    type        varchar(5),     -- 'PROMO' or 'NEW'
    norder      int,
    image       blob(1M),
    chksum      bigint
);
