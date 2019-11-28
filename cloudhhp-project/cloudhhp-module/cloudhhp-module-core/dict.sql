-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `DICT`;
CREATE TABLE `DICT`
(
    `DICT_CODE`        varchar(100) NOT NULL DEFAULT '' COMMENT '字典分类代码',
    `DICT_NAME`        varchar(100) NOT NULL DEFAULT '' COMMENT '字典分类名称',
    `DICT_REMARK`      varchar(100) NOT NULL DEFAULT '' COMMENT '字典分类描述',
    `CREATE_TIME`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `LAST_UPDATE_TIME` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`DICT_CODE`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='数据字典';

-- ----------------------------
-- Table structure for dict_code
-- ----------------------------
DROP TABLE IF EXISTS `DICT_CODE`;
CREATE TABLE `DICT_CODE`
(
    `DICT_CODE`        varchar(100) NOT NULL DEFAULT '' COMMENT '字典名称',
    `CODE`             varchar(100) NOT NULL DEFAULT '' COMMENT '字典值',
    `NAME`             varchar(100) NOT NULL DEFAULT '' COMMENT '字典描述',
    `REMARK`           varchar(100) NOT NULL DEFAULT '' COMMENT '字典分类描述',
    `CREATE_TIME`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `LAST_UPDATE_TIME` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`DICT_CODE`, `CODE`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='数据字典子节点';