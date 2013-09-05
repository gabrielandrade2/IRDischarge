use intemed;

create table execucoes(
	id int not null auto_increment,
	dataExecucao timestamp,

	primary key (id));

/* CUIDADO APAGA TODOS OS RESULTADOS*/
drop table resultados;
CREATE TABLE IF NOT EXISTS resultados(
idUsuario int,
idArquivo int,
idTexto int,
idExecucao int,
id int not null auto_increment,
trechoEncontrado text,
idRegra int,
idSubregra int,
isSubregra boolean,
PRIMARY KEY (id),
FOREIGN KEY (idUsuario) references textos(idUsuario),
FOREIGN KEY (idArquivo) references textos(idArquivo),
FOREIGN KEY (idTexto) references textos(idTexto),
FOREIGN KEY (idExecucao) references execucoes(id));


