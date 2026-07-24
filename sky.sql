-- =====================================================
-- 苍穹外卖 数据库初始化脚本
-- 数据库: sky_take_out
-- =====================================================

DROP DATABASE IF EXISTS sky_take_out;
CREATE DATABASE sky_take_out DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sky_take_out;

-- =====================================================
-- 1. 员工表
-- =====================================================
CREATE TABLE employee (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)  NOT NULL COMMENT '姓名',
    username    VARCHAR(32)  NOT NULL COMMENT '用户名',
    password    VARCHAR(64)  NOT NULL COMMENT '密码',
    phone       VARCHAR(11)  NOT NULL COMMENT '手机号',
    sex         VARCHAR(2)   NOT NULL COMMENT '性别',
    id_number   VARCHAR(18)  NOT NULL COMMENT '身份证号',
    status      INT          NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    update_time DATETIME     NOT NULL COMMENT '更新时间',
    create_user BIGINT       NOT NULL COMMENT '创建人ID',
    update_user BIGINT       NOT NULL COMMENT '修改人ID',
    PRIMARY KEY (id),
    UNIQUE KEY idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息';

-- =====================================================
-- 2. 分类表 (菜品分类 + 套餐分类)
-- =====================================================
CREATE TABLE category (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    type        INT          DEFAULT NULL COMMENT '类型 1:菜品分类 2:套餐分类',
    name        VARCHAR(32)  NOT NULL COMMENT '分类名称',
    sort        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    status      INT          NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    update_time DATETIME     NOT NULL COMMENT '更新时间',
    create_user BIGINT       NOT NULL COMMENT '创建人ID',
    update_user BIGINT       NOT NULL COMMENT '修改人ID',
    PRIMARY KEY (id),
    UNIQUE KEY idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品及套餐分类';

-- =====================================================
-- 3. 菜品表
-- =====================================================
CREATE TABLE dish (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)  NOT NULL COMMENT '菜品名称',
    category_id BIGINT       NOT NULL COMMENT '菜品分类ID',
    price       DECIMAL(10,2) NOT NULL COMMENT '菜品价格',
    image       VARCHAR(255) NOT NULL COMMENT '图片路径',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述信息',
    status      INT          NOT NULL DEFAULT 1 COMMENT '状态 0:停售 1:起售',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    update_time DATETIME     NOT NULL COMMENT '更新时间',
    create_user BIGINT       NOT NULL COMMENT '创建人ID',
    update_user BIGINT       NOT NULL COMMENT '修改人ID',
    PRIMARY KEY (id),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品';

-- =====================================================
-- 4. 菜品口味表
-- =====================================================
CREATE TABLE dish_flavor (
    id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    dish_id BIGINT       NOT NULL COMMENT '菜品ID',
    name    VARCHAR(32)  NOT NULL COMMENT '口味名称',
    value   VARCHAR(255) DEFAULT NULL COMMENT '口味数据list',
    PRIMARY KEY (id),
    KEY idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品口味关系';

-- =====================================================
-- 5. 套餐表
-- =====================================================
CREATE TABLE setmeal (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    category_id BIGINT       NOT NULL COMMENT '套餐分类ID',
    name        VARCHAR(32)  NOT NULL COMMENT '套餐名称',
    price       DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
    image       VARCHAR(255) NOT NULL COMMENT '图片路径',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述信息',
    status      INT          NOT NULL DEFAULT 1 COMMENT '状态 0:停售 1:起售',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    update_time DATETIME     NOT NULL COMMENT '更新时间',
    create_user BIGINT       NOT NULL COMMENT '创建人ID',
    update_user BIGINT       NOT NULL COMMENT '修改人ID',
    PRIMARY KEY (id),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐';

-- =====================================================
-- 6. 套餐菜品关系表
-- =====================================================
CREATE TABLE setmeal_dish (
    id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    setmeal_id BIGINT       NOT NULL COMMENT '套餐ID',
    dish_id    BIGINT       NOT NULL COMMENT '菜品ID',
    name       VARCHAR(32)  NOT NULL COMMENT '菜品名称(冗余)',
    price      DECIMAL(10,2) NOT NULL COMMENT '菜品原价(冗余)',
    copies     INT          NOT NULL COMMENT '份数',
    PRIMARY KEY (id),
    KEY idx_setmeal_id (setmeal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐菜品关系';

-- =====================================================
-- 7. 用户表 (C端-微信用户)
-- =====================================================
CREATE TABLE user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    openid      VARCHAR(45)  NOT NULL COMMENT '微信用户唯一标识',
    name        VARCHAR(32)  DEFAULT NULL COMMENT '姓名',
    phone       VARCHAR(11)  DEFAULT NULL COMMENT '手机号',
    sex         VARCHAR(2)   DEFAULT NULL COMMENT '性别',
    id_number   VARCHAR(18)  DEFAULT NULL COMMENT '身份证号',
    avatar      VARCHAR(500) DEFAULT NULL COMMENT '头像',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY idx_openid (openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';

-- =====================================================
-- 8. 地址簿表
-- =====================================================
CREATE TABLE address_book (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id       BIGINT       NOT NULL COMMENT '用户ID',
    consignee     VARCHAR(50)  NOT NULL COMMENT '收货人',
    sex           VARCHAR(2)   NOT NULL COMMENT '性别',
    phone         VARCHAR(11)  NOT NULL COMMENT '手机号',
    province_code VARCHAR(12)  DEFAULT NULL COMMENT '省级区划编号',
    province_name VARCHAR(32)  DEFAULT NULL COMMENT '省级名称',
    city_code     VARCHAR(12)  DEFAULT NULL COMMENT '市级区划编号',
    city_name     VARCHAR(32)  DEFAULT NULL COMMENT '市级名称',
    district_code VARCHAR(12)  DEFAULT NULL COMMENT '区级区划编号',
    district_name VARCHAR(32)  DEFAULT NULL COMMENT '区级名称',
    detail        VARCHAR(200) NOT NULL COMMENT '详细地址',
    label         VARCHAR(100) DEFAULT NULL COMMENT '标签(家/公司/学校)',
    is_default    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否默认 0:否 1:是',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址簿';

-- =====================================================
-- 9. 购物车表
-- =====================================================
CREATE TABLE shopping_cart (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)  NOT NULL COMMENT '商品名称',
    image       VARCHAR(255) NOT NULL COMMENT '图片',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    dish_id     BIGINT       DEFAULT NULL COMMENT '菜品ID',
    setmeal_id  BIGINT       DEFAULT NULL COMMENT '套餐ID',
    dish_flavor VARCHAR(50)  DEFAULT NULL COMMENT '口味',
    number      INT          NOT NULL DEFAULT 1 COMMENT '数量',
    amount      DECIMAL(10,2) NOT NULL COMMENT '金额',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车';

-- =====================================================
-- 10. 订单表
-- =====================================================
CREATE TABLE orders (
    id                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    number              VARCHAR(50)  NOT NULL COMMENT '订单号',
    status              INT          NOT NULL DEFAULT 1 COMMENT '订单状态 1:待付款 2:待接单 3:已接单 4:派送中 5:已完成 6:已取消',
    user_id             BIGINT       NOT NULL COMMENT '用户ID',
    address_book_id     BIGINT       NOT NULL COMMENT '地址簿ID',
    order_time          DATETIME     NOT NULL COMMENT '下单时间',
    checkout_time       DATETIME     DEFAULT NULL COMMENT '结账时间',
    pay_method          INT          DEFAULT NULL COMMENT '支付方式 1:微信支付',
    pay_status          TINYINT      NOT NULL DEFAULT 0 COMMENT '支付状态 0:未支付 1:已支付',
    amount              DECIMAL(10,2) NOT NULL COMMENT '实收金额',
    remark              VARCHAR(100) DEFAULT NULL COMMENT '备注',
    phone               VARCHAR(11)  DEFAULT NULL COMMENT '手机号',
    address             VARCHAR(255) DEFAULT NULL COMMENT '地址(冗余)',
    user_name           VARCHAR(32)  DEFAULT NULL COMMENT '用户名称(冗余)',
    consignee           VARCHAR(32)  DEFAULT NULL COMMENT '收货人(冗余)',
    cancel_reason       VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    rejection_reason    VARCHAR(255) DEFAULT NULL COMMENT '拒单原因',
    cancel_time         DATETIME     DEFAULT NULL COMMENT '取消时间',
    estimated_delivery_time DATETIME DEFAULT NULL COMMENT '预计送达时间',
    delivery_status     TINYINT      NOT NULL DEFAULT 1 COMMENT '配送状态 1:立即送出',
    delivery_time       DATETIME     DEFAULT NULL COMMENT '送达时间',
    pack_amount         INT          DEFAULT NULL COMMENT '打包费',
    tableware_number    INT          DEFAULT NULL COMMENT '餐具数量',
    tableware_status    TINYINT      NOT NULL DEFAULT 1 COMMENT '餐具数量状态 1:按餐量提供',
    PRIMARY KEY (id),
    UNIQUE KEY idx_order_number (number),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- =====================================================
-- 11. 订单详情表
-- =====================================================
CREATE TABLE order_detail (
    id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name       VARCHAR(32)  NOT NULL COMMENT '商品名称',
    image      VARCHAR(255) NOT NULL COMMENT '商品图片',
    order_id   BIGINT       NOT NULL COMMENT '订单ID',
    dish_id    BIGINT       DEFAULT NULL COMMENT '菜品ID',
    setmeal_id BIGINT       DEFAULT NULL COMMENT '套装ID',
    dish_flavor VARCHAR(50) DEFAULT NULL COMMENT '口味',
    number     INT          NOT NULL DEFAULT 1 COMMENT '数量',
    amount     DECIMAL(10,2) NOT NULL COMMENT '金额',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细';

-- =====================================================
-- 插入默认管理员账号 (密码: 123456 -> MD5加密)
-- =====================================================
INSERT INTO employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
VALUES (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812345678', '1', '110101199001010011', 1, NOW(), NOW(), 1, 1);
