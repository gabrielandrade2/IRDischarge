package br.gpri.controle;

public class Principal{
	
	public static void main(String[] args) {
		ControleLogin JanelaLogin = new ControleLogin();
		JanelaLogin.abreJanela();
	}
}

/*Rodar no MYSQL

create table arquivos(
idUsuario int,
idArquivo int,
absolutePath varchar(1000),
nomeArquivo varchar(100),

PRIMARY KEY (idUsuario,idArquivo));

Drop table if exists usuarios;

create table usuarios(
idUsuario int AUTO_INCREMENT,
Usuario varchar(20) UNIQUE,
Senha varchar(20),
Nome varchar(100),
Email varchar(100),

Primary Key (idUsuario));

*/