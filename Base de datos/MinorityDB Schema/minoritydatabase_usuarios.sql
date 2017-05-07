USE minoritydatabase;
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

