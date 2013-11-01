use intemed;

alter table resultados add column isEncontrado boolean;
update resultados set isEncontrado = true;