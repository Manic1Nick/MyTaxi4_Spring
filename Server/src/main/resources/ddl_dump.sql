CREATE DATABASE taxi_app;

use taxi_app;

CREATE TABLE cities (
  id int AUTO_INCREMENT PRIMARY KEY,
  city_name VARCHAR (50)
);

CREATE TABLE addresses (
  id INT AUTO_INCREMENT,
  city_id INT NOT NULL,
  street VARCHAR(50),
  num VARCHAR (5),
  PRIMARY KEY (id),
  FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE clients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_name VARCHAR (30),
  phone VARCHAR (12),
  address_id INT,
  FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE cars (
  id INT AUTO_INCREMENT PRIMARY KEY,
  car_number VARCHAR (20) NOT NULL UNIQUE,
  car_model VARCHAR (20),
  car_color VARCHAR (10)
);

CREATE TABLE drivers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_name VARCHAR (30),
  phone VARCHAR (12),
  car_id INT,
  address_id INT,
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE statuses(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR (10)
);

CREATE TABLE orders(
  id INT AUTO_INCREMENT PRIMARY KEY,
  crational_data TIMESTAMP NOT NULL,
  status_id INT NOT NULL,
  client_id INT NOT NULL,
  driver_id INT,
  from_id int NOT NULL,
  to_id INT,
  distance DOUBLE(5,2),
  price INT,
  FOREIGN KEY (client_id) REFERENCES clients(id),
  FOREIGN KEY (driver_id) REFERENCES drivers(id),
  FOREIGN KEY (from_id) REFERENCES addresses(id),
  FOREIGN KEY (to_id) REFERENCES addresses(id),
  FOREIGN KEY (status_id) REFERENCES statuses(id)
);

INSERT INTO statuses (name) VALUES ('NEW');
INSERT INTO statuses (name) VALUES ('IN_PROGRESS');
INSERT INTO statuses (name) VALUES ('CANCELED');
INSERT INTO statuses (name) VALUES ('DONE');
