-- ----------------------------
-- 马拉松报名管理系统 - 摄像头管理增量脚本
-- 包含：摄像头信息表、计时打卡记录表新增字段、摄像头管理菜单
-- ----------------------------

-- ----------------------------
-- 1、摄像头信息表
-- ----------------------------
drop table if exists camera;
create table camera (
  id           bigint primary key auto_increment comment '摄像头ID',
  event_id     bigint not null comment '关联赛事',
  camera_id    varchar(50) not null comment '摄像头ID/点位编码（如 CP-05KM）',
  name         varchar(100) comment '摄像头名称（如 5公里计时点）',
  location     varchar(200) comment '安装位置描述',
  lng          decimal(10,6) comment '经度（大屏地图/热力图定位）',
  lat          decimal(10,6) comment '纬度（大屏地图/热力图定位）',
  status       tinyint default 1 comment '状态 0停用 1启用',
  create_time  datetime comment '创建时间',
  update_time  datetime comment '更新时间',
  unique key uk_event_camera (event_id, camera_id)
) engine=innodb default charset=utf8mb4 comment='摄像头信息表';

-- ----------------------------
-- 2、计时打卡记录表新增最初到达时间字段
-- ----------------------------
alter table pass_record add column first_arrive_time datetime comment '最初到达时间（该号码牌首次经过该摄像头的时刻，用于分段用时计算）' after pass_time;

-- ----------------------------
-- 3、摄像头管理菜单
-- ----------------------------
insert into sys_menu values('2050', '摄像头管理', '2000', '7', 'camera', 'business/camera/index', '', '', 1, 0, 'C', '0', '0', 'business:camera:list', 'video', 'admin', sysdate(), '', null, '摄像头管理菜单');

insert into sys_menu values('2051', '摄像头新增', '2050', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2052', '摄像头修改', '2050', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2053', '摄像头删除', '2050', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2054', '摄像头查询', '2050', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2055', '摄像头导出', '2050', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2056', '摄像头导入', '2050', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'business:camera:import', '#', 'admin', sysdate(), '', null, '');
