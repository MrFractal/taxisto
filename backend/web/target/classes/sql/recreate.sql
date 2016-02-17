DROP DATABASE taxisto;
CREATE DATABASE IF NOT EXISTS taxisto
  CHARACTER SET utf8
  COLLATE utf8_unicode_ci;
GRANT ALL PRIVILEGES ON taxisto.* TO 'sa'@'localhost'
IDENTIFIED BY 'zx9504';
CREATE USER 'sa'@'localhost'
  IDENTIFIED BY 'zx9504';