create DATABASE shihu;
use shihu;

CREATE TABLE `Role` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
  `role_name` VARCHAR(64) NOT NULL UNIQUE COMMENT '角色名称',
  `description` VARCHAR(255) COMMENT '角色描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';


CREATE TABLE `User` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
  `role_id` INT NOT NULL COMMENT '角色ID',
  `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
  `email` VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱',
  `register_time` DATETIME NOT NULL COMMENT '注册时间',
  `last_login` DATETIME COMMENT '最后登录时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
   FOREIGN KEY (`role_id`) REFERENCES `Role`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

CREATE TABLE `Post` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
  `title` VARCHAR(255) NOT NULL COMMENT '帖子标题',
  `content` TEXT NOT NULL COMMENT '帖子内容',
  `user_id` INT COMMENT '发帖用户ID',
  `create_time` DATETIME NOT NULL COMMENT '发帖时间',
  `update_time` DATETIME COMMENT '最后编辑时间',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `reply_count` INT DEFAULT 0 COMMENT '回复数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '帖子状态：0-已删除，1-正常',
  FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子信息表';

CREATE TABLE `Post_Image` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
  `post_id` INT NOT NULL COMMENT '关联的帖子ID',
  `image_url` VARCHAR(512) NOT NULL COMMENT '图片URL',
  `upload_time` DATETIME NOT NULL COMMENT '上传时间',
  FOREIGN KEY (`post_id`) REFERENCES `Post`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片信息表';

CREATE TABLE `Reply` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '回复ID',
  `content` TEXT NOT NULL COMMENT '回复内容',
  `post_id` INT COMMENT '所属帖子ID',
  `user_id` INT COMMENT '回复用户ID',
  `create_time` DATETIME NOT NULL COMMENT '回复时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '回复状态：0-已删除，1-正常',
  FOREIGN KEY (`post_id`) REFERENCES `Post`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回复信息表';

CREATE TABLE `User_Like` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞记录ID',
  `user_id` INT COMMENT '点赞用户ID',
  `post_id` INT COMMENT '被点赞的帖子ID',
  `is_liked` TINYINT NOT NULL DEFAULT 1 COMMENT '点赞状态：1-点赞，0-取消点赞',
  UNIQUE (`user_id`, `post_id`) COMMENT '联合唯一索引避免重复点赞',
  FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`post_id`) REFERENCES `Post`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞信息表';

CREATE TABLE `Report` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '举报ID',
  `reporter_id` INT COMMENT '举报人ID',
  `post_id` INT COMMENT '被举报的帖子ID',
  `reason` VARCHAR(255) NOT NULL COMMENT '举报理由',
  `create_time` DATETIME NOT NULL COMMENT '举报时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0-未处理，1-已处理',
  FOREIGN KEY (`reporter_id`) REFERENCES `User`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`post_id`) REFERENCES `Post`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报信息表';

CREATE TABLE `Operation_Log` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '操作日志ID',
  `user_id` INT COMMENT '操作用户ID',
  `operation_type` ENUM('CREATE', 'READ', 'UPDATE', 'DELETE') NOT NULL COMMENT '操作类型',
  `target_table` VARCHAR(64) NOT NULL COMMENT '操作涉及的表名',
  `target_id` INT COMMENT '操作对象的ID',
  `details` TEXT COMMENT '操作详情',
  `create_time` DATETIME NOT NULL COMMENT '操作时间',
  FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

INSERT INTO `Role` (`role_name`, `description`)
VALUES ('admin', '管理员，拥有所有权限'),
       ('moderator', '版主，负责帖子审核和管理'),
       ('user', '普通用户');

INSERT INTO `User` (`username`, `password`, `role_id`, `email`, `register_time`, `status`)
VALUES ('admin', 'hashed_admin_password', 1, 'admin@example.com', NOW(), 1),
       ('user1', 'hashed_user1_password', 1, 'user1@example.com', NOW(), 1);

-- 假设admin用户ID为1，user1用户ID为2
INSERT INTO `Post` (`title`, `content`, `user_id`, `create_time`, `status`)
VALUES ('欢迎来到论坛', '这里是论坛的第一篇帖子，希望你喜欢！', 1, NOW(), 1),
       ('关于技术分享', '我想分享一些编程技巧，敬请期待...', 2, NOW(), 1);

-- 假设第一个帖子ID为1，第二个帖子ID为2
INSERT INTO `Reply` (`content`, `post_id`, `user_id`, `create_time`, `status`)
VALUES ('非常赞同！', 1, 2, NOW(), 1),
       ('谢谢分享，期待更多内容。', 2, 1, NOW(), 1);

-- 假设用户1给帖子1点赞，用户2给帖子2点赞
INSERT INTO `User_Like` (`user_id`, `post_id`, `is_liked`)
VALUES (1, 1, 1),
       (2, 2, 1);

-- 假设用户2举报了一个不存在的违规行为
INSERT INTO `Report` (`reporter_id`, `post_id`, `reason`, `create_time`, `status`)
VALUES (2, 1, '疑似违规内容', NOW(), 0);

INSERT INTO `Operation_Log` (`user_id`, `operation_type`, `target_table`, `target_id`, `details`, `create_time`)
VALUES (1, 'CREATE', 'Post', 1, '创建了一篇名为“欢迎来到论坛”的帖子', NOW()),
       (1, 'UPDATE', 'User', 2, '更新了用户2的资料', NOW()),
       (1, 'DELETE', 'Reply', 1, '删除了一条回复', NOW());