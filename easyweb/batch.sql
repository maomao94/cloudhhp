DROP TABLE IF EXISTS `BATCH_TASK_INF`;
CREATE TABLE `BATCH_TASK_INF`
(
    `ID`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `TASK_NAME`    varchar(255) NOT NULL COMMENT '任务名',
    `TASK_EXPRESS` varchar(255) NOT NULL COMMENT '定时任务表达式',
    `ALIAS_NAME`   varchar(255)      DEFAULT NULL COMMENT '任务别名',
    `OPR_ID`       varchar(255) NOT NULL COMMENT '操作员id',
    `TASK_STAT`    char(2)      NOT NULL COMMENT '状态 00停止  01启动',
    `MACH_ID`      varchar(255)      DEFAULT NULL COMMENT '任务机器号',
    `CLUSTER_NAME` varchar(255)      DEFAULT NULL COMMENT '所属集群',
    `TASK_PARA`    varchar(2000)     DEFAULT NULL COMMENT '启动任务参数',
    `UPDATE_TIME`  timestamp    NULL DEFAULT NULL,
    `CREATE_TIME`  timestamp    NULL DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 146
  DEFAULT CHARSET = utf8 COMMENT ='定时任务控制表';

DROP TABLE IF EXISTS `ALIPAY_TRAN_TODAY`;
CREATE TABLE `ALIPAY_TRAN_TODAY`
(
    `TRAN_ID`         varchar(40) DEFAULT NULL,
    `CHANNEL`         varchar(20) DEFAULT NULL,
    `TRAN_TYPE`       varchar(10) DEFAULT NULL,
    `COUNTER_PARTY`   varchar(20) DEFAULT NULL,
    `GOODS`           varchar(40) DEFAULT NULL,
    `AMOUNT`          varchar(20) DEFAULT NULL,
    `IS_DEBIT_CREDIT` varchar(10) DEFAULT NULL,
    `STATE`           varchar(10) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;