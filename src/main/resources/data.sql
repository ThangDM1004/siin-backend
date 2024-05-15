insert into accessory(name,image,price,quantity)
values ('Stone',null,100000,100),
       ('Metal',null,50000,100),
       ('Fabric',null,60000,100);

insert into product_material(color_name,size,image,price,quantity)
values ('Red','S',null,20000,100),
       ('Red','M',null,22000,100),
       ('Red','L',null,24000,100),
       ('Pink','S',null,20000,100),
       ('Pink','M',null,22000,100),
       ('Pink','L',null,24000,100),
       ('Black','S',null,20000,100),
       ('Black','M',null,22000,100),
       ('Black','L',null,24000,100),
       ('Orange','S',null,20000,100),
       ('Orange','M',null,22000,100),
       ('Orange','L',null,24000,100),
       ('Purple','S',null,20000,100),
       ('Purple','M',null,22000,100),
       ('Purple','L',null,24000,100);

insert into product_category(name)
values ('Gift'),
       ('Customize'),
       ('Normal'),
       ('DIY');

insert into product(name,cover_image,price,quantity,status,category_id)
values ('Gold Infinity',null,110000,50,true,3),
       ('Remar Love',null,80000,50,true,3),
       ('TOUS Mana',null,100000,50,true,3),
       ('Chỉ đỏ may mắn 50cm',null,10000,50,true,4),
       ('Charm bạc may mắn',null,50000,50,true,4),
       ('Hạt chuỗi xanh lục',null,221000,50,true,4),
       ('Hạt chuỗi tím',null,211000,50,true,4),
       ('Hạt chuỗi nâu',null,300000,50,true,4),
       ('Chỉ xanh 1m',null,20000,50,true,4),
       ('Chỉ hồng 1m',null,20000,50,true,4),
       ('Charm II',null,99000,50,true,4);

insert into product_sub_image(url,product_id)
values ('picture_1.png',1),
       ('picture_2.png',1),
       ('picture_3.png',1),
       ('picture_4.png',1),
       ('picture_1.png',2),
       ('picture_2.png',2),
       ('picture_3.png',2),
       ('picture_4.png',2),
       ('picture_1.png',3),
       ('picture_2.png',3),
       ('picture_3.png',3),
       ('picture_4.png',3),
       ('picture_1.png',4),
       ('picture_1.png',5),
       ('picture_1.png',6),
       ('picture_1.png',7),
       ('picture_1.png',8),
       ('picture_1.png',9),
       ('picture_1.png',10),
       ('picture_1.png',11);

insert into user_model(full_name,email,password,phone,avatar,dob,status)
values ('Admin','admin@gmail.com','admin@123','0822818111','admin.png','10/04/2002',true),
       ('Customer 1','customer_1@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 2','customer_2@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 3','customer_3@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 4','customer_4@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 5','customer_5@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 6','customer_6@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 7','customer_7@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 8','customer_8@gmail.com','123','0822818111','customer.png','10/04/2002',true),
       ('Customer 9','customer_9@gmail.com','123','0822818111','customer.png','10/04/2002',true);

insert into user_address(address,district,ward,province,user_id)
values ('123 Nguyễn Văn Cừ','Quận 5','Phường 10','TP.HCM',2),
       ('45 Lê Duẩn','Quận 1','Phường Bến Nghé','TP.HCM',3),
       ('78 Trần Quốc Toản','Quận 3','Phường 4','TP.HCM',4),
       ('201 Nguyễn Thị Minh Khai','Quận 1','Phường Đa Kao','TP.HCM',5),
       ('16 Trương Định','Quận Bình Thạnh','Phường 24','TP.HCM',6),
       ('32 Võ Thị Sáu','Quận 3','Phường 6','TP.HCM',7),
       ('68 Ngô Quyền','Quận 5','Phường 6','TP.HCM',8),
       ('114 Lê Lợi','Quận Gò Vấp','Phường 9','TP.HCM',9),
       ('27 Pasteur','Quận 1','Phường Bến Nghé','TP.HCM',1),
       ('92 Phan Đăng Lưu','Quận Bình Thạnh','Phường 3','TP.HCM',3),
       ('55 Trần Hưng Đạo','Quận 5','Phường 7','TP.HCM',4);