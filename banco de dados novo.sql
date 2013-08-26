DROP DATABASE IF EXISTS `intemed`;
CREATE DATABASE IF NOT EXISTS `intemed` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `intemed`;

DROP TABLE IF EXISTS usuarios;
CREATE TABLE IF NOT EXISTS usuarios(
	idUsuario int(10)  NOT NULL AUTO_INCREMENT,
	usuario varchar(20) UNIQUE NOT NULL,
	senha varchar(10),
	nome varchar(50),
	email varchar(50) UNIQUE,
	PRIMARY KEY(idUsuario));

DROP TABLE IF EXISTS arquivos;
CREATE TABLE IF NOT EXISTS arquivos(
idUsuario int NOT NULL,
idArquivo int NOT NULL AUTO_INCREMENT,
ordem int,
absolutePath varchar(1000),
nomeArquivo varchar(100),
PRIMARY KEY (idUsuario,idArquivo));

DROP TABLE IF EXISTS conjuntos;
CREATE TABLE IF NOT EXISTS conjuntos(
idConjunto int NOT NULL AUTO_INCREMENT,
nomeConjunto varchar(100) UNIQUE,
PRIMARY KEY (idConjunto));

DROP TABLE IF EXISTS elementos;
CREATE TABLE IF NOT EXISTS elementos(
idElemento int NOT NULL AUTO_INCREMENT,
nomeElemento varchar(100) UNIQUE,
descricaoElemento varchar(500),
PRIMARY KEY (idElemento));

DROP TABLE IF EXISTS regras;
CREATE TABLE IF NOT EXISTS regras(
idUsuario int,
idRegra int,
idConjunto int,
idElemento int, 
dataRegra timestamp,
previa varchar(100),
texto varchar(100),
idTexto int,
idArquivo int,
PRIMARY KEY (idRegra),
FOREIGN KEY (idUsuario) references usuarios(idUsuario),
FOREIGN KEY (idConjunto) references conjuntos(idConjunto),
FOREIGN KEY (idElemento) references elementos(idElemento),
FOREIGN KEY (idArquivo) references arquivos(idArquivo));

DROP TABLE IF EXISTS subregras;
CREATE TABLE IF NOT EXISTS subregras(
idRegra int,
idSubregra int,
dataRegra timestamp,
previa varchar(100),
texto varchar(100),
PRIMARY KEY (idRegra, idSubregra),
FOREIGN KEY (idRegra) references regras(idRegra));

DROP TABLE IF EXISTS termosregras;
CREATE TABLE IF NOT EXISTS termosregras(
idRegra int,
idTermo int,
ordem int,
termo varchar(60),
PRIMARY KEY (idRegra, idTermo),
FOREIGN KEY (idRegra) references regras(idRegra));

DROP TABLE IF EXISTS termossubregras;
CREATE TABLE IF NOT EXISTS termossubregras(
idRegra int,
idSubRegra int,
idTermo int,
ordem int,
termo varchar(60),
PRIMARY KEY (idRegra, idSubRegra, idTermo),
FOREIGN KEY (idRegra,idSubRegra) references subregras(idRegra,idSubRegra));