CREATE SCHEMA `minoritydatabase` ;
USE minoritydatabase;
CREATE TABLE `categorias` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` mediumtext NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `jugadas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Sala` int(11) NOT NULL,
  `Pregunta` int(11) NOT NULL,
  `Activo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `preguntas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Opcion A` mediumtext NOT NULL,
  `Opcion B` mediumtext NOT NULL,
  `Categoria` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`),
  KEY `Categoria` (`Categoria`),
  CONSTRAINT `Categoria` FOREIGN KEY (`Categoria`) REFERENCES `categorias` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `respuestas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Pregunta` int(11) NOT NULL,
  `Usuario` int(11) NOT NULL,
  `RespuestaFinal` mediumtext,
  `RespuestaTemporal` mediumtext,
  `Sala` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Sala_idx` (`Sala`),
  KEY `Pregunta_idx` (`Pregunta`),
  KEY `Usuario_idx` (`Usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `salasdejuegos` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` mediumtext NOT NULL,
  `Cant Jugadores` int(11) NOT NULL,
  `Monto a ganar` int(11) NOT NULL,
  `Disponible` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `usuarios` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` mediumtext NOT NULL,
  `Mail` mediumtext NOT NULL,
  `Password` mediumtext NOT NULL,
  `Monedas` int(11) NOT NULL,
  `Salas de juego` mediumtext,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


