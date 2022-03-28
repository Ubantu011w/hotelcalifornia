use hotel;

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `room_details`;
DROP TABLE IF EXISTS `details`;
DROP TABLE IF EXISTS `housekeeping`;
DROP TABLE IF EXISTS `postit`;
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `user` (
                  id int(64) NOT NULL AUTO_INCREMENT,
                  usertype int(1) NOT NULL,
                  username varchar(14) NOT NULL,
                  firstname varchar(14) NOT NULL,
                  lastname varchar(14) NOT NULL,
                  password varchar(36) NOT NULL,
                  phone int(12) NOT NULL,
                  email varchar(36) NOT NULL,
                  PRIMARY KEY (id)
                  );

                   
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
                  id int(64) NOT NULL UNIQUE,
                  capacity int(1) NOT NULL,
                  type int(1) NOT NULL,
                  price int(4) NOT NULL,
                  location varchar(36) NOT NULL,
                  PRIMARY KEY (id)
                  );
DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest` (
                  id int(64) NOT NULL AUTO_INCREMENT,
                  firstname varchar(36) NOT NULL,
                  lastname varchar(36) NOT NULL,
                  email varchar(36) NOT NULL UNIQUE,
                  phone int(12) NOT NULL,
                  points int(12) NOT NULL DEFAULT 0,
                  PRIMARY KEY (id)
                  );

CREATE TABLE `details` (
                      id int(64) NOT NULL AUTO_INCREMENT,
                      detail varchar(36) NOT NULL,
                      PRIMARY KEY (id)
                      );

CREATE TABLE `room_details` (
                          id int(64) REFERENCES room(id),
                          id_detail varchar(36) REFERENCES details(id),
                          PRIMARY KEY (id, id_detail)
                          );


DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
                  id int(64) NOT NULL AUTO_INCREMENT,
                  id_guest int(64) NOT NULL,
                  id_room int(64) NOT NULL,
				  id_receptionist int(64) NOT NULL,
                  date_start date NOT NULL,
                  date_end date NOT NULL,
                  checked_in boolean DEFAULT 0,
                  total_cost int(64) NOT NULL,
                  PRIMARY KEY (id)
                  );

CREATE TABLE `postit` (
                  id int(64) NOT NULL AUTO_INCREMENT,
                  announce varchar(300) NOT NULL,
                  daily varchar(300) NOT NULL,
                  admintodo varchar(300) NOT NULL,
                  receptodo varchar(300) NOT NULL,
                  PRIMARY KEY (id)
                  );

LOCK TABLES `postit` WRITE;
INSERT INTO `postit`(id,announce,daily,admintodo,receptodo) VALUES (1,'välkommen till jobbet', 'kolla bokningar', 'Lägg in ny användare', 'Ring kund');
UNLOCK TABLES;

CREATE TABLE `housekeeping` (
                  id int(64) NOT NULL AUTO_INCREMENT,
                  id_room int(64) NOT NULL,
                  id_reservation int(64) UNIQUE,
                  status boolean DEFAULT 0,
                  availability boolean DEFAULT 0,
                  task_date date NOT NULL,
                  priority int(1) NOT NULL,
                  PRIMARY KEY (id)
                  );

LOCK TABLES `details` WRITE;
INSERT INTO `details` (detail) VALUES ('WiFi'), ('TV'), ('Minibar'), ('Air Conditioner'), ('Balcony'), ('Allergy friendly'), ('Phone');
UNLOCK TABLES;

LOCK TABLES `room_details` WRITE;
INSERT INTO `room_details` (id, id_detail) VALUES 
        (101, 0),(101, 1),(101, 2),(101, 3),(101, 4),(101, 5),
        (102, 0),(102, 3),(102, 4),(102, 5),(102, 6),(102, 7),
        (103, 1),(103, 2),(103, 3),(103, 5),
        (104, 0),(104, 2),(104, 3),(104, 4),(104, 7),
        (105, 0),(105, 1),(105, 3),(105, 4),(105, 6),(105, 7),
        (106, 1),(106, 4),(106, 5),(106, 6),(106, 7),
        (107, 4),(107, 5),(107, 6),
        (108, 0),(108, 1),(108, 2),(108, 4),(108, 7),
        (109, 0),(109, 3),(109, 4),(109, 5),(109, 6),
        (110, 0),(110, 2),(110, 3),(110, 4),(110, 6),(110, 7),
        (201, 0),(201, 1),(201, 2),(201, 3),(201, 4),(201, 7),
        (202, 0),(202, 1),(202, 3),(202, 6),(202, 7),
        (203, 0),(203, 1),(203, 2),(203, 3),(203, 5),
        (204, 0),(204, 3),(204, 4),(204, 7),
        (205, 0),(205, 1),(205, 3),(205, 4),(205, 6),(205, 7),
        (206, 0),(206, 1),(206, 2),(206, 5),(206, 6),(206, 7),
        (207, 0),(207, 1),(207, 2),(207, 3),(207, 5),(207, 7),
        (208, 1),(208, 3),(208, 4),(208, 5),(208, 6),(208, 7),
        (209, 0),(209, 2),(209, 3),(209, 4),(209, 5),(209, 7),
        (210, 1),(210, 2),(210, 4),(210, 5),(210, 7),
        (211, 0),(211, 2),(211, 4),(211, 5),(211, 6),
        (212, 1),(212, 2),(212, 3),(212, 4),(212, 5),(212, 6),
        (213, 0),(213, 1),(213, 5),(213, 6),(213, 7),
        (214, 0),(214, 1),(214, 3),(214, 4),(214, 5),(214, 6),(214, 7),
        (215, 0),(215, 1),(215, 2),(215, 4),(215, 5),(215, 7),
        (301, 0),(301, 1),(301, 3),(301, 5),(301, 7),
        (302, 3),(302, 4),(302, 5),(302, 6),(302, 7),
        (303, 0),(303, 1),(303, 2),(303, 3),(303, 4),(303, 6),
        (304, 2),(304, 3),(304, 4),(304, 5),(304, 6),(304, 7),
        (305, 1),(305, 2),(305, 3),(305, 4),(305, 6),(305, 7),
        (306, 0),(306, 1),(306, 3),(306, 4),(306, 7),
        (307, 0),(307, 1),(307, 2),(307, 4),(307, 5),(307, 6),
        (308, 1),(308, 4),(308, 5),(308, 6),
        (309, 0),(309, 1),(309, 3),(309, 4),(309, 6),
        (310, 0),(310, 2),(310, 3),(310, 4),(310, 5),
        (311, 1),(311, 2),(311, 3),(311, 5),(311, 6),
        (312, 0),(312, 3),(312, 4),(312, 5),(312, 6),
        (313, 1),(313, 3),(313, 4),(313, 5),(313, 6),(313, 7),
        (314, 0),(314, 1),(314, 2),(314, 3),(314, 4),(314, 7),
        (315, 1),(315, 2),(315, 4),(315, 7);
UNLOCK TABLES;

CREATE TABLE `settings` (
/* id
1 Types
2 Capacity
3 Location 
*/
                      id int(1) NOT NULL AUTO_INCREMENT,
                      data TINYTEXT NOT NULL,
                      PRIMARY KEY (id)
                      );

LOCK TABLES `settings` WRITE;

INSERT INTO `settings`(data) VALUES ('Economy/Standard/VIP'), ('Single/Double/Triple'), ('South/West/East'); 

UNLOCK TABLES;

LOCK TABLES `user` WRITE;
INSERT INTO `user`(usertype,username,firstname,lastname,password,phone,email) VALUES (1,'Svea','Sven','Svensson','123','0722432562','svea.andersson@gmail.com'), (0,'Abbe','Albin','Kzason','123','0722432572','albin_sven@hotmail.com'), (1, 'ajdin', 'Ajdin', 'Majdanac', '123', '0724256972', 'am223yc@student.lnu.se'), (1, 'abdulla', 'Abdulla', 'Abu-Ainin', '123', '0735896432', 'aa225qw@student.lnu.se'), (0, 'isac', 'Isac', 'Holm', '123', '0722455852', 'ih222nz@student.lnu.se'), (0, 'joakim', 'Joakim', 'Nilsson', '123', '0722413362', 'jn223rk@student.lnu.se');
UNLOCK TABLES;

LOCK TABLES `room` WRITE;
INSERT INTO `room`
VALUES 
(101,1,2,500,'South'),
(102,1,1,300,'West'),
(103,1,2,500,'East'),
(104,3,1,300,'East'),
(105,3,1,300,'West'),
(106,1,3,700,'South'),
(107,3,2,500,'East'),
(108,3,1,300,'South'),
(109,1,1,300,'West'),
(110,1,2,500,'West'),
(201,1,2,500,'South'),
(202,1,2,500,'East'),
(203,2,1,300,'West'),
(204,3,3,700,'East'),
(205,3,3,700,'South'),
(206,2,1,300,'South'),
(207,2,2,500,'East'),
(208,1,2,500,'South'),
(209,2,3,700,'South'),
(210,1,1,300,'East'),
(211,3,1,300,'East'),
(212,3,2,500,'West'),
(213,2,3,700,'West'),
(214,1,2,500,'East'),
(215,2,3,700,'West'),
(301,3,3,700,'West'),
(302,3,3,700,'East'),
(303,3,2,500,'West'),
(304,2,1,300,'West'),
(305,2,1,300,'West'),
(306,3,1,300,'East'),
(307,1,2,500,'South'),
(308,2,1,300,'West'),
(309,3,2,500,'East'),
(310,1,2,500,'West'),
(311,3,1,300,'East'),
(312,1,2,500,'East'),
(313,1,1,300,'West'),
(314,2,2,500,'West'),
(315,3,3,700,'West');

UNLOCK TABLES;

LOCK TABLES `guest` WRITE;
INSERT INTO `guest` (firstname, lastname, email, phone) VALUES ('karl-olof', 'lindahl', 'kolle_cool@lnu.se', 0701234567), ('Knugen', 'bernadotte', 'kingen_i_bingen@yahoo.com', 0701234341), ('Sven', 'Gustafsson', 'sven.gust@yahoo.com', 0754546141), ('Sara', 'Lindorff', 'Sara.lind@hotmail.com', 0756165165), ('Cecilia', 'Fredriksson', 'Cecilia.Fred@gmail.com', 0723654894);
UNLOCK TABLES;

LOCK TABLES `reservation` WRITE;
INSERT INTO `reservation` (id_guest, id_room, id_receptionist, date_start, date_end, total_cost) VALUES (1, 101, 1, 20211201, 20211202, 500), (3, 102, 1, 20211201, 20211212, 5500), (2, 101, 1, 20220101, 20220112, 5500);
UNLOCK TABLES;

LOCK TABLES `housekeeping` WRITE;
INSERT INTO `housekeeping` (id_room, id_reservation, status, availability, task_date, priority) VALUES (102, NULL, 0, 0, 20220216, 1), (104, NULL, 0, 0, 20220217, 0), (110, NULL, 0, 0, 20220216, 1);
UNLOCK TABLES;

SET GLOBAL time_zone = '+1:00';
