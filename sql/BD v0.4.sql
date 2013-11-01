delimiter $$

CREATE TABLE `textorapidminer` (
  `datahora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `texto` text,
  `idTexto` int(11) DEFAULT NULL,
  PRIMARY KEY (`datahora`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

