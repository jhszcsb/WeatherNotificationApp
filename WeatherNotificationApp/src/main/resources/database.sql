DROP TABLE IF EXISTS `SUBSCRIPTION`;

CREATE TABLE `SUBSCRIPTION` (
	`EMAIL` VARCHAR(40) NULL DEFAULT NULL,
	`CITY` VARCHAR(40) NULL DEFAULT NULL,
	`ID` INT(10) NOT NULL AUTO_INCREMENT,
	`TEMPERATURE` DOUBLE(10, 0) NULL DEFAULT NULL,
	`NOTIFICATION` INT(1) NULL DEFAULT NULL,
	PRIMARY KEY (`ID`)
)
DEFAULT CHARACTER SET latin2 COLLATE latin2_hungarian_ci
ENGINE=InnoDB
AUTO_INCREMENT=6;