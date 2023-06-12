create database setemp3;
use setemp3;
/*==============================================================*/
/* Table: t_user                                                                                                                                       */
/*==============================================================*/
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user
(
    user_name            VARCHAR(63) COMMENT '用户昵称',
    account              VARCHAR(63) COMMENT '用户账户名',
    user_password        VARCHAR(63) COMMENT '用户密码BC加密',
    gender               INT COMMENT '用户性别 0男 1女',
    birthday             DATE COMMENT '用户生日',
    mobile               VARCHAR(11),
    email                VARCHAR(63),
    image                VARCHAR(255),
    last_login_time      DATETIME COMMENT '上次登录时间',
    is_online            INT COMMENT '用户状态 1在线 0离线',
    add_time             DATETIME COMMENT '创建时间',
    update_time          DATETIME COMMENT '更新时间',
    del_flag             INT DEFAULT 0 COMMENT '逻辑删除标志',
    role_id              INT COMMENT '2普通用户  1高级用户 0管理员',
    PRIMARY KEY (account)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '用户表';

/*==============================================================*/
/* Table: t_customer                                                                                                                               */
/*==============================================================*/
DROP TABLE IF EXISTS t_customer;
CREATE TABLE t_customer
(
    id                     VARCHAR(50) COMMENT '客户id',
    customer_name          VARCHAR(63) COMMENT '客户名称', # 使用客户州地址和客户市地址联合生成
    birthday               DATETIME COMMENT '客户出生日期', # 下面的随机生成
    gender                 INT COMMENT '客户性别 0男1女',
    marriage               INT DEFAULT 0 COMMENT '客户婚姻情况 0不明 1未婚 2已婚',
    degree                 INT DEFAULT 0 COMMENT '客户学历 0不明 1小学及以下 2中学 3高中 4大学 5大学及以上 ',
    income_level           INT DEFAULT 0 COMMENT '收入水平 0不明 1低收入 2中收入 3高收入 ',
    customer_type          INT COMMENT '客户类型 0为零售客户 1是批发客户', # 根据用户的付款方式生成
    interest_id            Varchar(50) COMMENT '客户感兴趣的商品id', # 统计客户不同商品的购买次数
    add_time               DATETIME,
    update_time            DATETIME,
    del_flag               INT DEFAULT 0,
    PRIMARY KEY (id),
    INDEX customer_type_index(customer_type)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '客户表';

/*==============================================================*/
/* Table: t_customer_address                                           */
/*==============================================================*/

DROP TABLE IF EXISTS t_customer_address;
CREATE TABLE t_customer_address
(
    id                   VARCHAR(50) COMMENT '客户ID',
    province             INT COMMENT '省ID',
    city                 INT COMMENT '市ID',
    detail               VARCHAR(255) COMMENT '详细地址',
    add_time             DATETIME COMMENT '创建时间',
    update_time          DATETIME COMMENT '修改时间',
    del_flag             INT DEFAULT 0 COMMENT '逻辑删除标志',
    PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '客户地址表';



# 各省的ID约定为其人口的排名,如人口第一大省广东省的ID为1
# mtbatisplus是针对单主键的框架，若出现联合主键，启动类后直接报错
/*==============================================================*/
/* Table: t_region                                            */
/*==============================================================*/
DROP TABLE IF EXISTS t_region;
CREATE TABLE t_region
(
    id                   INT NOT NULL,
    layer_id             INT NOT NULL,
    parent_id            INT NOT NULL DEFAULT '0' COMMENT '行政区域父ID，例如区县的pid指向市，市的pid指向省，省的pid则是0',
    region_name          VARCHAR(63) NOT NULL COMMENT '行政区域名称' ,
    region_type          INT DEFAULT '0' COMMENT '行政区域类型，如如1则是省， 如果是2则是市，如果是3则是区县',
    PRIMARY KEY (id),
    INDEX layer_id_index(layer_id),
    INDEX parent_id_index(parent_id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '行政区域表';


# 还是可以分级用种类表
/*==============================================================*/
/* Table: t_category                                            */
/*==============================================================*/
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category
(
    id                   INT NOT NULL AUTO_INCREMENT COMMENT '类别id',
    category_name        VARCHAR(63) NOT NULL UNIQUE COMMENT '类别名称',
    parent_id            INT COMMENT '父类别id',
    sort_order           INT COMMENT '排序顺序', #暂时没用到
    is_parent            INT COMMENT '是否为父类别',
    add_time             DATETIME,
    update_time        DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '类别表';

# 可以乱点鸳鸯谱的品牌
/*==============================================================*/
/* Table: t_brand                                             */
/*==============================================================*/
DROP TABLE IF EXISTS t_brand;
CREATE TABLE t_brand
(
    id                   INT NOT NULL AUTO_INCREMENT COMMENT '品牌id',
    brand_name           VARCHAR(63) UNICODE COMMENT '品牌名称',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0 COMMENT '逻辑删除标志',
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '品牌表';

# 在这个表里添加规格值
/*==============================================================*/
/* Table: t_good                                                */
/*==============================================================*/
DROP TABLE IF EXISTS t_good;
CREATE TABLE t_good
(
    id                   VARCHAR(50) COMMENT '商品id', # 源数据集里商品有自己的主键
    category_id          INT COMMENT '类别id',
    brand_id             INT COMMENT '品牌id',
    good_name            VARCHAR(63) COMMENT '商品名称', # 源数据里没有，故采用名称的长度作为名称
    product_weight_g     DECIMAL(10,2) COMMENT '商品重量(g)',
    product_length_cm    DECIMAL(10,2) COMMENT '商品长度(cm)',
    product_height_cm    DECIMAL(10,2) COMMENT '商品高度(cm)',
    product_width_cm     DECIMAL(10,2) COMMENT '商品宽度(cm)',
    image                VARCHAR(255), # 这个源数据集里没有,但是确实有用,保留
    price                DECIMAL(10,2) COMMENT '商品当前价格',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '商品表';


/*==============================================================*/
/* Table: t_good_order 假定一个订单一种商品                       */
/*==============================================================*/
DROP TABLE IF EXISTS t_good_order;
CREATE TABLE t_good_order
(
    id                   VARCHAR(50) not null COMMENT '订单id',
    num                  INT NOT NULL COMMENT '数量',
    good_id              VARCHAR(50) not null COMMENT '商品id',
    add_time             DATETIME COMMENT '订单支付时间',
    price                DECIMAL(10,2) NOT NULL COMMENT '单价',
    total_price          DECIMAL(10,2) NOT NULL COMMENT '订单总价',
    customer_id          VARCHAR(50) not null COMMENT '客户id',
    is_comment           INT COMMENT '用户是否评论 0否 1是',
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id),
    INDEX good_id_index(good_id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '商品销售订单表';

/*==============================================================*/
/* Table: t_order_comment 假定一个用户对于一个订单仅能评价一次     */
/*==============================================================*/
DROP TABLE IF EXISTS t_order_comment;
CREATE TABLE t_order_comment
(
    id                   VARCHAR(50) COMMENT 'id',
    order_id             VARCHAR(50) not null COMMENT '订单id',
    customer_id          VARCHAR(50) not null COMMENT '客户id',
    star                 INT COMMENT '评价等级',
    detail               VARCHAR(255) COMMENT '评价详情',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '订单评价表';

/*==============================================================*/
/* Table: t_permission                                        */
/*==============================================================*/
DROP TABLE IF EXISTS t_permission;
CREATE TABLE t_permission
(
    id                   INT NOT NULL AUTO_INCREMENT COMMENT '权限id',
    permission_name      VARCHAR(63) COMMENT '权限名称',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '权限表';


/*==============================================================*/
/* Table: t_role                                              */
/*==============================================================*/
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role
(
    id                   INT NOT NULL COMMENT '角色id',
    role_name            VARCHAR(63) COMMENT ' 0管理员 1高级用户 2普通用户',
    detail               VARCHAR(255) COMMENT '角色描述',
    is_enabled           INT DEFAULT '1' COMMENT '是否启用',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '角色表';


/*==============================================================*/
/* Table: t_role_permission                                   */
/*==============================================================*/
DROP TABLE IF EXISTS t_role_permission;
CREATE TABLE t_role_permission
(
    id                   INT NOT NULL AUTO_INCREMENT,
    role_id              INT NOT NULL COMMENT '角色id',
    permission_id        INT NOT NULL COMMENT '权限id',
    add_time             DATETIME,
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '角色权限表';


/*==============================================================*/
/* Table: t_instock                                             */
/*==============================================================*/
DROP TABLE IF EXISTS t_instock;
CREATE TABLE t_instock
(
    id                   INT NOT NULL AUTO_INCREMENT COMMENT '入库id',
    good_id              VARCHAR(50) COMMENT '商品id',
    price                DECIMAL(10,2) COMMENT '入库单价',
    unit                 VARCHAR(10) DEFAULT '件' COMMENT '库存单位',
    num                  INT COMMENT '入库数量',
    total_price          DECIMAL(10,2) COMMENT '入库总金额',
    add_time             DATETIME COMMENT '入库日期',
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id),
    INDEX good_id_index(good_id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '入库表';

/*==============================================================*/
/* Table: t_stock                                                                                                                                     */
/*==============================================================*/
DROP TABLE IF EXISTS t_stock;
CREATE TABLE t_stock
(
    id                   INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    good_id              VARCHAR(50) COMMENT '商品id',
    num                  INT COMMENT '本日的数量（有正负）',
    stock_num            INT COMMENT '总的库存量（是加上该单据发生后的即时库存余额）',
    add_time             DATETIME COMMENT '添加日期',
    update_time          DATETIME,
    del_flag             INT DEFAULT 0,
    PRIMARY KEY (id),
    INDEX good_id_index(good_id)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '库存表';

/*==============================================================*/
/* Table: t_recommend*/
/*==============================================================*/
DROP TABLE IF EXISTS t_recommend;
create table t_recommend
(
    customer_id varchar(50) null comment '客户id',
    good_id     varchar(50) null comment '商品id',
    score       double      null comment '推荐指数'
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '基于购买的商品推荐表';


/*==============================================================*/
/* Table: t_recommend_planb*/
/*==============================================================*/
DROP TABLE IF EXISTS t_recommend_planb;
create table t_recommend_planb
(
    customer_id varchar(50) null comment '客户id',
    good_id     varchar(50) null comment '商品id',
    score       double      null comment '推荐指数'
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '基于购买的商品推荐表';


/*==============================================================*/
/* 触发器*/
/*==============================================================*/


/*如果有报错两个触发器分开执行一下*/

DROP TRIGGER IF EXISTS increaseStock_trigger;
DELIMITER //
CREATE TRIGGER increaseStock_trigger
AFTER INSERT ON t_instock
FOR EACH ROW BEGIN

DECLARE existflag INT DEFAULT 0;
DECLARE last_stock_num INT DEFAULT 0;

SELECT COUNT(*) INTO existflag FROM t_stock WHERE good_id=new.good_id AND DATE(add_time)=DATE(new.add_time) LIMIT 0,1;
# 选取最近的库存量
SELECT stock_num INTO last_stock_num FROM t_stock WHERE good_id=new.good_id and new.add_time>add_time
                                                  ORDER BY new.add_time-add_time ASC LIMIT 0, 1;

IF(existflag=1)
	THEN UPDATE t_stock SET num=num+new.num,add_time=new.add_time,stock_num=stock_num+new.num WHERE good_id=new.good_id
	AND DATE(add_time)=DATE(new.add_time);
ELSEIF(existflag=0)
	THEN INSERT INTO t_stock(good_id,num,stock_num,add_time) VALUES (new.good_id,new.num,last_stock_num+new.num,new.add_time);
END IF;

END//

DELIMITER ;

/*如果有报错两个触发器分开执行一下*/

DROP TRIGGER IF EXISTS decreaseStock_trigger;
DELIMITER //
CREATE TRIGGER decreaseStock_trigger
Before INSERT ON t_good_order
FOR EACH ROW BEGIN
DECLARE existflag INT DEFAULT 0;
DECLARE last_stock_num INT DEFAULT 0;

SELECT COUNT(*) INTO existflag FROM t_stock WHERE good_id=new.good_id AND DATE(add_time)=DATE(new.add_time) LIMIT 0,1;

SELECT stock_num INTO last_stock_num FROM t_stock WHERE good_id=new.good_id and new.add_time>add_time ORDER BY new.add_time-add_time ASC LIMIT 0, 1;


IF(existflag=1 AND last_stock_num >=new.num)
	THEN UPDATE t_stock SET num=num-new.num,add_time=new.add_time,stock_num=stock_num-new.num WHERE good_id=new.good_id
	AND DATE(add_time)=DATE(new.add_time);
ELSEIF(last_stock_num >=new.num)
	THEN INSERT INTO t_stock(good_id,num,stock_num,add_time) VALUES (new.good_id,0-new.num,last_stock_num-new.num,new.add_time);
ELSE
	signal SQLSTATE 'HY000' SET message_text='没有该商品的库存,销售失败';
END IF;
END//

# 建立模型
drop table if exists t_sales_forecast_model;
create table t_sales_forecast_model(
   id varchar(50) not null primary key ,
   k double ,
   b double,
   del_flag int default 0
)engine = innodb,charset =utf8;