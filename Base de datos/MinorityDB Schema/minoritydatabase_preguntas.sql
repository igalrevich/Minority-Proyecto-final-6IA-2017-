USE minoritydatabase;
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

