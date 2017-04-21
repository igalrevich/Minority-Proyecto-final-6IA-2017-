CREATE TABLE IF NOT EXISTS `MinorityDataBase`.`Respuestas` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `Pregunta` INT NOT NULL,
  `Usuario` INT NOT NULL,
  `RespuestaFinal` INT NULL,
  `RespuestaTemporal` INT NULL,
  `Sala` INT NOT NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `Sala`
    FOREIGN KEY (`Sala`)
    REFERENCES `MinorityDataBase`.`SalasDeJuegos` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Pregunta`
    FOREIGN KEY (`Pregunta`)
    REFERENCES `MinorityDataBase`.`Preguntas` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Usuario`
    FOREIGN KEY (`Usuario`)
    REFERENCES `MinorityDataBase`.`Usuarios` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
