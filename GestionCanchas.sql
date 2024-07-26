create database GestionDeCanchas;
use GestionDeCanchas;

create table Administradores(
Id int auto_increment primary key,
nombre varchar(25),
password varchar(25),
email varchar(25),
cedula double
);

create table Jugador(
nombreCompleto varchar(45),
email varchar(25),
telefono double,
direccion varchar(30),
fechaNacimiento varchar(25),
cedula double primary key
);