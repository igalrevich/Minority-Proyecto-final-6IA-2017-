USE minoritydatabase;
CREATE TABLE `salasdejuegos` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` mediumtext NOT NULL,
  `Cant Jugadores` int(11) NOT NULL,
  `Monto a ganar` int(11) NOT NULL,
  `Disponible` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

