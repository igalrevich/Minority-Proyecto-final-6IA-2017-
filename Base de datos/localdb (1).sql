-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:53808
-- Generation Time: Jun 11, 2017 at 12:15 PM
-- Server version: 5.7.9
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `localdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `categorias`
--

CREATE TABLE `categorias` (
  `Id` int(11) NOT NULL,
  `Nombre` mediumtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categorias`
--

INSERT INTO `categorias` (`Id`, `Nombre`) VALUES
(1, 'Deportes'),
(2, 'TV/Pelis'),
(3, 'Comida'),
(4, 'Otros');

-- --------------------------------------------------------

--
-- Table structure for table `jugadas`
--

CREATE TABLE `jugadas` (
  `Id` int(11) NOT NULL,
  `Sala` int(11) NOT NULL,
  `Pregunta` int(11) NOT NULL,
  `Activo` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `preguntas`
--

CREATE TABLE `preguntas` (
  `Id` int(11) NOT NULL,
  `OpcionA` mediumtext NOT NULL,
  `OpcionB` mediumtext NOT NULL,
  `Categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `preguntas`
--

INSERT INTO `preguntas` (`Id`, `OpcionA`, `OpcionB`, `Categoria`) VALUES
(2, 'Futbol', 'Basquet', 1),
(3, 'Messi', 'CR7', 1),
(4, 'Hockey', 'Voley', 1),
(5, 'Barcelona', 'Real Madrid', 1),
(6, 'Tenis', 'Badminton', 1),
(7, 'Atlanta', 'Chacarita', 1),
(8, 'How I Met Your Mother', 'Friends', 2),
(9, 'Accion', 'Comedia', 2),
(10, 'Star Wars', 'Star Trek', 2),
(11, 'Simpsons', 'Futurama', 2),
(12, 'Toy Story', 'Monsters Inc', 2),
(13, 'Series', 'Peliculas', 2),
(14, 'Pizza', 'Empanadas', 3),
(15, 'Hamburguesa', 'Pancho', 3),
(16, 'Helado', 'Mousse', 3),
(17, 'Sprite', '7UP', 3),
(18, 'Coca', 'Pepsi', 3),
(19, 'Mayonesa', 'Ketchup', 3),
(20, 'Frio', 'Calor', 4),
(21, 'Montaña', 'Playa', 4),
(22, 'Nike', 'Adidas', 4),
(23, 'Carpeta', 'Cuaderno', 4),
(24, 'Aeropostale', 'Hollister', 4),
(25, 'Ojotas', 'Crocs', 4);

-- --------------------------------------------------------

--
-- Table structure for table `respuestas`
--

CREATE TABLE `respuestas` (
  `Id` int(11) NOT NULL,
  `Pregunta` int(11) NOT NULL,
  `Usuario` int(11) NOT NULL,
  `RespuestaFinal` mediumtext,
  `RespuestaTemporal` mediumtext,
  `Sala` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `salasdejuegos`
--

CREATE TABLE `salasdejuegos` (
  `Id` int(11) NOT NULL,
  `Nombre` mediumtext NOT NULL,
  `CantJugadores` int(11) NOT NULL,
  `MontoAGanar` int(11) NOT NULL,
  `Disponible` tinyint(1) NOT NULL,
  `NRonda` int(11) DEFAULT NULL,
  `HoraComienzo` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salasdejuegos`
--

INSERT INTO `salasdejuegos` (`Id`, `Nombre`, `CantJugadores`, `MontoAGanar`, `Disponible`, `NRonda`, `HoraComienzo`) VALUES
(1, 'D', 50, 49, 0, 1, '00:00:00'),
(3, 'A', 30, 29, 0, 2, '00:00:00'),
(5, 'B', 0, 0, 0, 1, '00:00:00'),
(6, 'C', 0, 0, 0, 1, '00:00:00'),
(7, 'E', 0, 0, 0, 1, '00:00:00'),
(8, 'F', 0, 0, 1, 1, '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `Id` int(11) NOT NULL,
  `Nombre` varchar(50) NOT NULL,
  `Mail` mediumtext NOT NULL,
  `password` mediumtext NOT NULL,
  `Monedas` int(11) NOT NULL,
  `SalasDeJuego` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`Id`, `Nombre`, `Mail`, `password`, `Monedas`, `SalasDeJuego`) VALUES
(2, 'IgalRevich', 'igalrevich@hotmail.com', 'atlantacampeon', 49, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `jugadas`
--
ALTER TABLE `jugadas`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Id_UNIQUE` (`Id`),
  ADD KEY `Sala_idx` (`Sala`),
  ADD KEY `Pregunta_idx` (`Pregunta`);

--
-- Indexes for table `preguntas`
--
ALTER TABLE `preguntas`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Id_UNIQUE` (`Id`),
  ADD KEY `Categoria` (`Categoria`);

--
-- Indexes for table `respuestas`
--
ALTER TABLE `respuestas`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `Sala_idx` (`Sala`),
  ADD KEY `Pregunta_idx` (`Pregunta`);

--
-- Indexes for table `salasdejuegos`
--
ALTER TABLE `salasdejuegos`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Id_UNIQUE` (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categorias`
--
ALTER TABLE `categorias`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `jugadas`
--
ALTER TABLE `jugadas`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `preguntas`
--
ALTER TABLE `preguntas`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
--
-- AUTO_INCREMENT for table `respuestas`
--
ALTER TABLE `respuestas`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `salasdejuegos`
--
ALTER TABLE `salasdejuegos`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `jugadas`
--
ALTER TABLE `jugadas`
  ADD CONSTRAINT `Pregunta` FOREIGN KEY (`Pregunta`) REFERENCES `preguntas` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `Sala` FOREIGN KEY (`Sala`) REFERENCES `salasdejuegos` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `preguntas`
--
ALTER TABLE `preguntas`
  ADD CONSTRAINT `Categoria` FOREIGN KEY (`Categoria`) REFERENCES `categorias` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`azure`@`localhost` EVENT `ActualizarSalasDeJuego` ON SCHEDULE EVERY 30 SECOND STARTS '2017-06-11 15:50:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
	
		UPDATE salasdejuegos SET HoraComienzo=CURRENT_TIME WHERE Disponible=false;
        
        UPDATE salasdejuegos SET HoraComienzo=TIMESTAMPADD(MINUTE,4,CURRENT_TIME) WHERE  Disponible=true;
	    
    END$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
