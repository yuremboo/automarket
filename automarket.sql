-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Мар 17 2014 г., 17:27
-- Версия сервера: 5.6.14
-- Версия PHP: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `automarket`
--

-- --------------------------------------------------------

--
-- Структура таблицы `COMMODITY_CIRCULATION`
--

CREATE TABLE IF NOT EXISTS `COMMODITY_CIRCULATION` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goodsCount` int(11) DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `isSale` tinyint(1) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_en3pdw09fww4kjofbjp8sj5nn` (`goods_id`),
  KEY `FK_fo4t29otdp0j4xkndq9k7cdea` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `COUNTER`
--

CREATE TABLE IF NOT EXISTS `COUNTER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goodsCount` int(11) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_a1lodps5nlemckoyf2jp6pben` (`goods_id`),
  KEY `FK_dnan63eqkddd0i7p3x33tvvbj` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `GOODS`
--

CREATE TABLE IF NOT EXISTS `GOODS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k4sdul8ftetr7wjx02xe4ifck` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `STORE`
--

CREATE TABLE IF NOT EXISTS `STORE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isDefault` tinyint(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `COMMODITY_CIRCULATION`
--
ALTER TABLE `COMMODITY_CIRCULATION`
  ADD CONSTRAINT `FK_fo4t29otdp0j4xkndq9k7cdea` FOREIGN KEY (`store_id`) REFERENCES `STORE` (`id`),
  ADD CONSTRAINT `FK_en3pdw09fww4kjofbjp8sj5nn` FOREIGN KEY (`goods_id`) REFERENCES `GOODS` (`id`);

--
-- Ограничения внешнего ключа таблицы `COUNTER`
--
ALTER TABLE `COUNTER`
  ADD CONSTRAINT `FK_dnan63eqkddd0i7p3x33tvvbj` FOREIGN KEY (`store_id`) REFERENCES `STORE` (`id`),
  ADD CONSTRAINT `FK_a1lodps5nlemckoyf2jp6pben` FOREIGN KEY (`goods_id`) REFERENCES `GOODS` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
