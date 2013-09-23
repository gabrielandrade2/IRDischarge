CREATE TABLE `textorapidminer` (
  `datahora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `texto` text,
  PRIMARY KEY (`datahora`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$;

alter table intemed.textoRapidMiner add idTexto int;