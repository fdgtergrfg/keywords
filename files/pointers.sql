CREATE TABLE `pointers` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `SourceTableName` varchar(255) DEFAULT NULL,
  `TargetTableName` varchar(255) DEFAULT NULL,
  `Pointer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;