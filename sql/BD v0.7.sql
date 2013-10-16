use intemed;

CREATE TABLE IF NOT EXISTS frasesnegativas(
id int not null auto_increment,
frase varchar(200),

primary key(id));

insert into frasesnegativas(frase) values('sem sinal');
insert into frasesnegativas(frase) values('sem sinais');
insert into frasesnegativas(frase) values('dentro da normalidade');
insert into frasesnegativas(frase) values('sem alterações');
insert into frasesnegativas(frase) values('não apresenta');
insert into frasesnegativas(frase) values('sem dilatação');

CREATE TABLE IF NOT EXISTS regrasexecucao(
idExecucao int,
idRegra int,

primary key(idExecucao, idRegra),
FOREIGN KEY(idExecucao) references execucoes(id),
FOREIGN KEY(idRegra) references regras(idRegra));
