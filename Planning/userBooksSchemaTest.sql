-- MySQL Script generated by MySQL Workbench
-- Mon Jan  3 16:57:39 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema userbookstest
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `userbookstest` ;

-- -----------------------------------------------------
-- Schema userbookstest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `userbookstest` DEFAULT CHARACTER SET utf8 ;
USE `userbookstest` ;

-- -----------------------------------------------------
-- Table `userbookstest`.`app_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userbookstest`.`app_role` ;

CREATE TABLE IF NOT EXISTS `userbookstest`.`app_role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `userbookstest`.`app_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userbookstest`.`app_user` ;

CREATE TABLE IF NOT EXISTS `userbookstest`.`app_user` (
  `app_user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(2048) NOT NULL,
  `disabled` TINYINT(1) NOT NULL default(0),
  `idRole` INT NOT NULL,
  PRIMARY KEY (`app_user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  INDEX `fk_app_user_app_role1_idx` (`idRole` ASC) VISIBLE,
  CONSTRAINT `fk_app_user_app_role1`
    FOREIGN KEY (`idRole`)
    REFERENCES `userbookstest`.`app_role` (`idRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `userbookstest`.`authors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userbookstest`.`authors` ;

CREATE TABLE IF NOT EXISTS `userbookstest`.`authors` (
  `idAuthor` INT NOT NULL AUTO_INCREMENT,
  `author_first_name` VARCHAR(45) NOT NULL,
  `author_last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idAuthor`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `userbookstest`.`books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userbookstest`.`books` ;

CREATE TABLE IF NOT EXISTS `userbookstest`.`books` (
  `idBooks` INT NOT NULL AUTO_INCREMENT,
  `approval_status` TINYINT(1) NOT NULL,
  `book_title` VARCHAR(45) NOT NULL,
  `genre` VARCHAR(45) NOT NULL,
  `publication_year` INT NULL,
  `idAuthor` INT NOT NULL,
  PRIMARY KEY (`idBooks`),
  INDEX `fk_books_authors1_idx` (`idAuthor` ASC) VISIBLE,
  CONSTRAINT `fk_books_authors1`
    FOREIGN KEY (`idAuthor`)
    REFERENCES `userbookstest`.`authors` (`idAuthor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `userbookstest`.`app_user_has_books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `userbookstest`.`app_user_has_books` ;

CREATE TABLE IF NOT EXISTS `userbookstest`.`app_user_has_books` (
  `app_user_id` INT NOT NULL,
  `idBooks` INT NOT NULL,
  `completion_status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`app_user_id`, `idBooks`),
  INDEX `fk_app_user_has_books_books1_idx` (`idBooks` ASC) VISIBLE,
  INDEX `fk_app_user_has_books_app_user1_idx` (`app_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_app_user_has_books_app_user1`
    FOREIGN KEY (`app_user_id`)
    REFERENCES `userbookstest`.`app_user` (`app_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_app_user_has_books_books1`
    FOREIGN KEY (`idBooks`)
    REFERENCES `userbookstest`.`books` (`idBooks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- 1. MySQL's default statement terminator is `;`.
-- Since we include `;` inside our procedure, we temporarily change
-- the statement terminator to `//`.
-- That way, the SQL inside is treated as text.
delimiter //
create procedure set_known_good_state()
begin

SET FOREIGN_KEY_CHECKS = 0;

    -- 2. Throws out all records without executing deletes.
    -- Resets the auto_increment value.
    truncate table books;
    
SET FOREIGN_KEY_CHECKS = 1;

    -- 3. Add test data.
    
    insert into authors
        (`author_first_name`, `author_last_name`)
    values
        ("Christopher", "Robin"),
        ("JK", "Rowling"),
        ("Henry", "Smith");
        
    insert into books
        (`approval_status`, `book_title`, `genre`, `publication_year`, `idAuthor`)
    values
        (true, "Winnie the Pooh", "Fiction", 1932, 1),
        (false, "Harry Potter: The First Book", "Fiction", 1996, 2),
        (true, "Fossils and more!", "Nonfiction", 2003, 3);
        
	
     
end // -- ensures that 
-- 4. Change the statement terminator back to the original.
delimiter ;

    insert into books
        (`approval_status`, `book_title`, `genre`, `publication_year`, `idAuthor`)
    values
        (true, "Winnie the Pooh", "Fiction", 1932, 1),
        (false, "Harry Potter: The First Book", "Fiction", 1996, 2),
        (true, "Fossils and more!", "Nonfiction", 2003, 3);
        
	insert into authors
        (`author_first_name`, `author_last_name`)
    values
        ( "Christopher", "Robin"),
        ("JK", "Rowling"),
        ("Henry", "Smith");
        
    insert into app_user
    (`username`, `password_hash`, `disabled`, `idRole`)
    values
    ("EdwardV", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", false, 1),
    ("AmyR", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", false, 2);
        
	insert into app_role
    (`role_name`)
    values
    ("USER"), -- id 1
    ("ADMIN"); -- id 2
    
        
     -- testing here
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
