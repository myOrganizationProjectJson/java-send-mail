
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `batchemailuser`;
CREATE TABLE `batchemailuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `status` int(5) DEFAULT '1',
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8;
