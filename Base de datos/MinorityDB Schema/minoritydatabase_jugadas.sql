USE minoritydatabase;
CREATE TABLE `jugadas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Sala` int(11) NOT NULL,
  `Pregunta` int(11) NOT NULL,
  `Activo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id_UNIQUE` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
