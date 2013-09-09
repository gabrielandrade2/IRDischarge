use intemed;


/* CUIDADO APAGA TODOS OS RESULTADOS*/


drop table execucoes;
create table execucoes(
	id int not null auto_increment,
	idUsuario int,
	idArquivo int,
	dataExecucao timestamp,

	primary key (id),
	FOREIGN KEY (idUsuario) references textos(idUsuario),
	FOREIGN KEY (idArquivo) references textos(idArquivo));

drop table resultados;
CREATE TABLE IF NOT EXISTS resultados(
idTexto int,
idExecucao int,
id int not null auto_increment,
trechoEncontrado text,
idRegra int,
idSubregra int,
isSubregra boolean,
PRIMARY KEY (id),

FOREIGN KEY (idTexto) references textos(idTexto),
FOREIGN KEY (idExecucao) references execucoes(id),
FOREIGN KEY (idRegra) references regras(idRegra),
FOREIGN KEY (idSubregra) references subregras(idSubregra)
);

Create index idArquivo on arquivos(idArquivo);
Create index idRegra on regras(idRegra);
Create index idSubregra on subregras(idSubregra);
