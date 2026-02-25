CREATE TABLE IF NOT EXISTS clazz
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name  VARCHAR(100) NOT NULL COMMENT '班级名称',
    grade       VARCHAR(50)  NOT NULL COMMENT '年级',
    teacher     VARCHAR(50)           COMMENT '班主任',
    description VARCHAR(500)          COMMENT '描述',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '班级表';

CREATE TABLE IF NOT EXISTS student
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_no VARCHAR(50)  NOT NULL UNIQUE COMMENT '学号',
    name       VARCHAR(100) NOT NULL COMMENT '姓名',
    gender     TINYINT               COMMENT '性别（0-女, 1-男）',
    age        INT                   COMMENT '年龄',
    phone      VARCHAR(20)           COMMENT '手机号',
    email      VARCHAR(100)          COMMENT '邮箱',
    class_id   BIGINT                COMMENT '所属班级',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_student_clazz FOREIGN KEY (class_id) REFERENCES clazz (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '学生表';
