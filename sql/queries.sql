drop table product_spec;
drop table product;

delete from product_spec;

select * from product where ID='AXT-A20045M';

select * from product where big_pic is null;
select * from category where id=4;

delete from product;

update product set base_price=0,price_5=0,price_6=0,price_7=0,price_8=0,promotion=0,is_package=false;

select id from category where legacy_name='SOLDADORAS INVERSORES' and line=(select id from line where legacy_name='AX TECH');

select * from agent where id=2;

select * from client where id=33333;

select * from category where line=1;

delete from category where name not in ('all', 'paqs_promos');

select * from product where id='CIC-H7200RKITNO5';
select * from product where id like 'MAS%';
select * from product where id='AXT-MS1201';

delete from PRODUCT_SPEC;
delete from product;



select * from product_spec where product='ASP-PD3000';

insert into product_spec (product,spec,value) values ('ASP-PD3000','Generador 2,500Wtts, OHV 4 tiempos','6.5 HP');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Potencia Maxima','3000 Watts');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Potencia continua','2500 Watts');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Motor 4 tiempos (gasolina)',' ');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Voltaje','120/240 Volts');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Enchufe tipo Americano',' ');
insert into product_spec (product,spec,value) values ('ASP-PD3000','Seguro de bajo nivel de aceite',' ');

select count(*) from product;
