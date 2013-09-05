-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               5.5.8 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2012-11-19 16:50:06
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for irdischarge
DROP DATABASE IF EXISTS `irdischarge`;
CREATE DATABASE IF NOT EXISTS `irdischarge` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `irdischarge`;


-- Dumping structure for table irdischarge.conjuntos
DROP TABLE IF EXISTS `conjuntos`;
CREATE TABLE IF NOT EXISTS `conjuntos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Conjunto de regras';

-- Dumping data for table irdischarge.conjuntos: ~2 rows (approximately)
/*!40000 ALTER TABLE `conjuntos` DISABLE KEYS */;
INSERT INTO `conjuntos` (`id`, `nome`) VALUES
	(1, 'Regras para execução de TESTE usando Stemming como regra-base'),
	(2, 'Regras para execução de TESTE usando Stemming como sub-regra');
/*!40000 ALTER TABLE `conjuntos` ENABLE KEYS */;


-- Dumping structure for table irdischarge.elementos
DROP TABLE IF EXISTS `elementos`;
CREATE TABLE IF NOT EXISTS `elementos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `abreviacao` varchar(20) NOT NULL,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.elementos: ~9 rows (approximately)
/*!40000 ALTER TABLE `elementos` DISABLE KEYS */;
INSERT INTO `elementos` (`id`, `abreviacao`, `nome`) VALUES
	(1, 'PACIENTE', 'Identificação do paciente e caracterização da internação'),
	(2, 'MOTIVO', 'Motivo da admissão, diagnósticos e comorbidades'),
	(3, 'ACHADOS', 'Achados clínicos relevantes e outros achados'),
	(4, 'PROCEDIMENTOS', 'Procedimentos diagnósticos e terapêuticos realizados'),
	(5, 'MEDICAMENTOS', 'Medicações importantes, inclusive as de alta (medicamentos a serem tomados em casa)'),
	(6, 'EXAMES', 'Exames pendentes'),
	(7, 'EVOLUÇÃO', 'Evolução e Condições do estado do paciente na alta'),
	(8, 'CONTINUIDADE', 'Instruções relativas ao acompanhamento (educação ao paciente e familiar)'),
	(9, 'ANEXOS', 'Anexos (exames, receitas, encaminhamentos)');
/*!40000 ALTER TABLE `elementos` ENABLE KEYS */;


-- Dumping structure for table irdischarge.processamentos
DROP TABLE IF EXISTS `processamentos`;
CREATE TABLE IF NOT EXISTS `processamentos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dataprocessamento` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `observacoes` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.processamentos: ~0 rows (approximately)
/*!40000 ALTER TABLE `processamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `processamentos` ENABLE KEYS */;


-- Dumping structure for table irdischarge.regras
DROP TABLE IF EXISTS `regras`;
CREATE TABLE IF NOT EXISTS `regras` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `regrapai_id` int(10) unsigned DEFAULT NULL,
  `conjunto_id` int(10) unsigned NOT NULL,
  `elemento_id` int(10) unsigned NOT NULL,
  `dataregra` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ordem` tinyint(4) NOT NULL,
  `previa` varchar(100) DEFAULT NULL,
  `observacao` varchar(100) DEFAULT NULL COMMENT 'Para quais casos a regra foi criada',
  PRIMARY KEY (`id`),
  KEY `FK_regras_elemento` (`elemento_id`),
  KEY `FK_regras_regrapai` (`regrapai_id`),
  KEY `FK_regras_conjunto` (`conjunto_id`),
  CONSTRAINT `FK_regras_conjunto` FOREIGN KEY (`conjunto_id`) REFERENCES `conjuntos` (`id`),
  CONSTRAINT `FK_regras_elemento` FOREIGN KEY (`elemento_id`) REFERENCES `elementos` (`id`),
  CONSTRAINT `FK_regras_regrapai` FOREIGN KEY (`regrapai_id`) REFERENCES `regras` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.regras: ~10 rows (approximately)
/*!40000 ALTER TABLE `regras` DISABLE KEYS */;
INSERT INTO `regras` (`id`, `regrapai_id`, `conjunto_id`, `elemento_id`, `dataregra`, `ordem`, `previa`, `observacao`) VALUES
	(1, NULL, 1, 8, '2012-10-16 19:27:47', 1, '[V_INF][N_M_S][ADJ_M_S]', 'manter acompanhamento ambulatorial'),
	(2, NULL, 1, 8, '2012-10-16 19:28:09', 2, '[V_INF][N_M_S][PRP]', 'manter uso de'),
	(3, NULL, 1, 8, '2012-10-16 19:28:15', 4, '[V_INF][N_M_S]', 'manter acompanhamento'),
	(4, NULL, 1, 8, '2012-10-16 19:28:23', 3, '[V_INF][PRP][N_M_S]', 'persistir em acompanhamento'),
	(5, NULL, 1, 8, '2012-10-18 18:25:40', 5, '[encaminh]', NULL),
	(6, NULL, 2, 8, '2012-11-03 16:05:15', 1, '[V_INF][N_M_S][ADJ_M_S]', 'manter acompanhamento ambulatorial'),
	(7, NULL, 2, 8, '2012-11-03 16:05:41', 2, '[V_INF][N_M_S][PRP]', 'manter uso de'),
	(8, NULL, 2, 8, '2012-11-03 16:06:19', 3, '[V_INF][PRP][N_M_S]', 'persistir em acompanhamento'),
	(9, NULL, 2, 8, '2012-11-03 16:07:17', 4, '[V_INF][N_M_S]', NULL),
	(10, 9, 2, 8, '2012-11-03 16:08:22', 1, '[V_INF][acompanh]', 'manter acompanhamento');
/*!40000 ALTER TABLE `regras` ENABLE KEYS */;


-- Dumping structure for table irdischarge.termosregras
DROP TABLE IF EXISTS `termosregras`;
CREATE TABLE IF NOT EXISTS `termosregras` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `regra_id` int(10) unsigned NOT NULL,
  `tipotermo_id` int(10) unsigned NOT NULL,
  `ordem` tinyint(3) unsigned NOT NULL,
  `termo` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_termosregras_regra` (`regra_id`),
  KEY `FK_termosregras_tipotermo` (`tipotermo_id`),
  CONSTRAINT `FK_termosregras_regra` FOREIGN KEY (`regra_id`) REFERENCES `regras` (`id`),
  CONSTRAINT `FK_termosregras_tipotermo` FOREIGN KEY (`tipotermo_id`) REFERENCES `tipotermosregras` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.termosregras: ~25 rows (approximately)
/*!40000 ALTER TABLE `termosregras` DISABLE KEYS */;
INSERT INTO `termosregras` (`id`, `regra_id`, `tipotermo_id`, `ordem`, `termo`) VALUES
	(1, 1, 1, 1, 'V_INF'),
	(2, 1, 1, 2, 'N_M_S'),
	(3, 1, 1, 3, 'ADJ_M_S'),
	(4, 2, 1, 1, 'V_INF'),
	(5, 2, 1, 2, 'N_M_S'),
	(6, 2, 1, 3, 'PRP'),
	(7, 3, 1, 1, 'V_INF'),
	(8, 3, 1, 2, 'N_M_S'),
	(9, 4, 1, 1, 'V_INF'),
	(10, 4, 1, 2, 'PRP'),
	(11, 4, 1, 3, 'N_M_S'),
	(12, 5, 2, 1, 'encaminh'),
	(13, 6, 1, 1, 'V_INF'),
	(14, 6, 1, 2, 'N_M_S'),
	(15, 6, 1, 3, 'ADJ_M_S'),
	(16, 7, 1, 1, 'V_INF'),
	(17, 7, 1, 2, 'N_M_S'),
	(18, 7, 1, 3, 'PRP'),
	(19, 8, 1, 1, 'V_INF'),
	(20, 8, 1, 2, 'PRP'),
	(21, 8, 1, 3, 'N_M_S'),
	(22, 9, 1, 1, 'V_INF'),
	(23, 9, 1, 2, 'N_M_S'),
	(24, 10, 1, 1, 'V_INF'),
	(25, 10, 2, 2, 'acompanh');
/*!40000 ALTER TABLE `termosregras` ENABLE KEYS */;


-- Dumping structure for table irdischarge.tipoprocessamentos
DROP TABLE IF EXISTS `tipoprocessamentos`;
CREATE TABLE IF NOT EXISTS `tipoprocessamentos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.tipoprocessamentos: ~3 rows (approximately)
/*!40000 ALTER TABLE `tipoprocessamentos` DISABLE KEYS */;
INSERT INTO `tipoprocessamentos` (`id`, `nome`) VALUES
	(1, 'POS-Tagging'),
	(2, 'Stemming'),
	(3, 'Data Identification');
/*!40000 ALTER TABLE `tipoprocessamentos` ENABLE KEYS */;


-- Dumping structure for table irdischarge.tipotermosregras
DROP TABLE IF EXISTS `tipotermosregras`;
CREATE TABLE IF NOT EXISTS `tipotermosregras` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table irdischarge.tipotermosregras: ~3 rows (approximately)
/*!40000 ALTER TABLE `tipotermosregras` DISABLE KEYS */;
INSERT INTO `tipotermosregras` (`id`, `nome`) VALUES
	(1, 'POS-tagging'),
	(2, 'Stemming'),
	(3, 'RegEx');
/*!40000 ALTER TABLE `tipotermosregras` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
