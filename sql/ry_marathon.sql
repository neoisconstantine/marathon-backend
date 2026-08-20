-- ----------------------------
-- 马拉松报名管理系统业务表
-- 包含：参赛用户、赛事、报名、报警、成绩、资讯、计时打卡记录
-- ----------------------------

-- ----------------------------
-- 1、参赛用户表
-- ----------------------------
drop table if exists person;
create table person (
  id                bigint(20)      not null auto_increment    comment '用户ID',
  openid            varchar(64)     default null               comment '微信openid',
  name              varchar(50)     not null default ''         comment '姓名',
  gender            tinyint(1)      default 0                  comment '性别（0未知 1男 2女）',
  birth_date        date            default null               comment '出生日期',
  phone             varchar(20)     default null               comment '手机号码',
  id_card           varchar(18)     default null               comment '身份证号',
  emergency_name    varchar(50)     default null               comment '紧急联系人姓名',
  emergency_phone   varchar(20)     default null               comment '紧急联系人电话',
  status            tinyint(1)      default 0                  comment '账号状态（0正常 1禁用）',
  create_time       datetime        default null               comment '创建时间',
  update_time       datetime        default null               comment '更新时间',
  primary key (id),
  unique key uk_openid (openid),
  key idx_phone (phone)
) engine=innodb default charset=utf8mb4 comment = '参赛用户表';

-- ----------------------------
-- 2、赛事表
-- ----------------------------
drop table if exists `event`;
create table `event` (
  id                bigint(20)      not null auto_increment    comment '赛事ID',
  name              varchar(100)    not null                   comment '赛事名称',
  location          varchar(200)    default null               comment '比赛地点',
  start_time        datetime        default null               comment '比赛开始时间',
  signup_start      datetime        default null               comment '报名开始时间',
  signup_end        datetime        default null               comment '报名截止时间',
  total_quota       int(4)          not null default 0         comment '报名总名额',
  registered        int(4)          default 0                  comment '已报名人数',
  signup_open       tinyint(1)      default 1                  comment '报名开关（0关闭 1开启）',
  status            tinyint(1)      default 0                  comment '赛事状态（0未发布 1报名中 2进行中 3已结束）',
  fee               decimal(10,2)   default 0         comment '报名费用（元）',
  cover_url         varchar(500)    default null               comment '封面图地址',
  intro             text            default null               comment '赛事介绍',
  create_time       datetime        default null               comment '创建时间',
  update_time       datetime        default null               comment '更新时间',
  primary key (id)
) engine=innodb default charset=utf8mb4 comment = '赛事表';

-- ----------------------------
-- 3、报名表
-- ----------------------------
drop table if exists registration;
create table registration (
  id                bigint(20)      not null auto_increment    comment '报名ID',
  person_id         bigint(20)      not null                   comment '参赛用户ID',
  event_id          bigint(20)      not null                   comment '赛事ID',
  bib               varchar(20)     default null               comment '参赛号码布',
  status            tinyint(1)      default 0                  comment '报名状态（0已报名 1已审核 2已退赛）',
  create_time       datetime        default null               comment '创建时间',
  primary key (id),
  unique key uk_person_event (person_id, event_id)
) engine=innodb default charset=utf8mb4 comment = '报名表';

-- ----------------------------
-- 4、报警表
-- ----------------------------
drop table if exists alarm;
create table alarm (
  id                bigint(20)      not null auto_increment    comment '报警ID',
  type              varchar(30)     not null                   comment '报警类型',
  level             tinyint(1)      default 1                  comment '报警级别（1提示 2警告 3严重）',
  title             varchar(200)    default null               comment '报警标题',
  content           text            default null               comment '报警内容',
  event_id          bigint(20)      default null               comment '赛事ID',
  status            tinyint(1)      default 0                  comment '处理状态（0未处理 1已处理）',
  create_time       datetime        default null               comment '创建时间',
  primary key (id)
) engine=innodb default charset=utf8mb4 comment = '报警表';

-- ----------------------------
-- 5、成绩表
-- ----------------------------
drop table if exists `result`;
create table `result` (
  id                bigint(20)      not null auto_increment    comment '成绩ID',
  event_id          bigint(20)      not null                   comment '赛事ID',
  person_id         bigint(20)      not null                   comment '参赛用户ID',
  registration_id   bigint(20)      default null               comment '报名ID',
  bib               varchar(20)     default null               comment '参赛号码布',
  gun_time          time(2)         default null               comment '枪声成绩',
  net_time          time(2)         default null               comment '净成绩',
  avg_pace          varchar(10)     default null               comment '平均配速',
  total_rank        int(4)          default null               comment '总排名',
  status            tinyint(1)      default 0                  comment '成绩状态（0未完赛 1已完赛 2成绩无效）',
  source            tinyint(1)      default 0                  comment '成绩来源（0系统计算 1外部推送）',
  create_time       datetime        default null               comment '创建时间',
  primary key (id),
  unique key uk_event_person (event_id, person_id)
) engine=innodb default charset=utf8mb4 comment = '成绩表';

-- ----------------------------
-- 6、资讯表
-- ----------------------------
drop table if exists content;
create table content (
  id                bigint(20)      not null auto_increment    comment '资讯ID',
  type              tinyint(1)      not null                   comment '资讯类型（1轮播图 2公告 3常见问题）',
  title             varchar(200)    default null               comment '标题',
  image_data        mediumblob      default null               comment '图片数据',
  image_type        varchar(20)     default null               comment '图片类型',
  summary           varchar(500)    default null               comment '摘要',
  detail            longtext        default null               comment '详情内容',
  sort              int(4)          default 0                  comment '显示顺序',
  status            tinyint(1)      default 1                  comment '状态（0下架 1上架）',
  start_time        datetime        default null               comment '上架时间',
  end_time          datetime        default null               comment '下架时间',
  create_time       datetime        default null               comment '创建时间',
  update_time       datetime        default null               comment '更新时间',
  primary key (id)
) engine=innodb default charset=utf8mb4 comment = '资讯表';

-- ----------------------------
-- 7、计时打卡记录表
-- ----------------------------
drop table if exists pass_record;
create table pass_record (
  id                bigint(20)      not null auto_increment    comment '打卡记录ID',
  event_id          bigint(20)      not null                   comment '赛事ID',
  camera_id         varchar(50)     not null                   comment '计时点设备编号',
  bib               varchar(20)     not null                   comment '参赛号码布',
  person_id         varchar(64)     default null               comment '参赛用户ID',
  pass_time         datetime        not null                   comment '打卡时间',
  speed             decimal(5,2)    default null               comment '通过速度',
  camera_lng        decimal(10,6)   default null               comment '计时点经度',
  camera_lat        decimal(10,6)   default null               comment '计时点纬度',
  extra             json            default null               comment '扩展信息',
  create_time       datetime        default null               comment '创建时间',
  primary key (id),
  unique key uk_camera_bib_time (camera_id, bib, pass_time)
) engine=innodb default charset=utf8mb4 comment = '计时打卡记录表';

-- ----------------------------
-- 一级菜单（菜单ID从2000开始，避开系统菜单）
-- ----------------------------
insert into sys_menu values('2000', '马拉松管理', '0', '5', 'marathon', null, '', '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), '', null, '马拉松管理目录');
-- 二级菜单
insert into sys_menu values('2001', '赛事管理', '2000', '1', 'event',        'business/event/index',        '', '', 1, 0, 'C', '0', '0', 'business:event:list',        'date',     'admin', sysdate(), '', null, '赛事管理菜单');
insert into sys_menu values('2002', '参赛用户', '2000', '2', 'person',       'business/person/index',       '', '', 1, 0, 'C', '0', '0', 'business:person:list',       'peoples',  'admin', sysdate(), '', null, '参赛用户菜单');
insert into sys_menu values('2003', '报名管理', '2000', '3', 'registration', 'business/registration/index', '', '', 1, 0, 'C', '0', '0', 'business:registration:list', 'form',     'admin', sysdate(), '', null, '报名管理菜单');
insert into sys_menu values('2004', '成绩管理', '2000', '4', 'result',       'business/result/index',       '', '', 1, 0, 'C', '0', '0', 'business:result:list',       'chart',    'admin', sysdate(), '', null, '成绩管理菜单');
insert into sys_menu values('2005', '资讯管理', '2000', '5', 'content',      'business/content/index',      '', '', 1, 0, 'C', '0', '0', 'business:content:list',      'message',  'admin', sysdate(), '', null, '资讯管理菜单');
insert into sys_menu values('2006', '报警管理', '2000', '6', 'alarm',        'business/alarm/index',        '', '', 1, 0, 'C', '0', '0', 'business:alarm:list',        'warn',     'admin', sysdate(), '', null, '报警管理菜单');

-- 赛事管理按钮
insert into sys_menu values('2007', '赛事新增', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:event:add',    'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2008', '赛事修改', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:event:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2009', '赛事删除', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:event:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2010', '赛事查询', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:event:query',  '#', 'admin', sysdate(), '', null, '');
-- 参赛用户按钮
insert into sys_menu values('2011', '用户新增', '2002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:add',    'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2012', '用户修改', '2002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2013', '用户删除', '2002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2014', '用户查询', '2002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:query',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2015', '用户导入', '2002', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:import', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2016', '用户导出', '2002', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'business:person:export', '#', 'admin', sysdate(), '', null, '');
-- 报名管理按钮
insert into sys_menu values('2017', '报名新增', '2003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:add',     'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2018', '报名修改', '2003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:edit',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2019', '报名删除', '2003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:remove',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2020', '报名查询', '2003', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:query',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2021', '报名导出', '2003', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:export',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2022', '报名审核', '2003', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:review',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2023', '报名退赛', '2003', '7', '', '', '', '', 1, 0, 'F', '0', '0', 'business:registration:refund',  '#', 'admin', sysdate(), '', null, '');
-- 成绩管理按钮
insert into sys_menu values('2024', '成绩新增', '2004', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:result:add',     'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2025', '成绩修改', '2004', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:result:edit',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2026', '成绩删除', '2004', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:result:remove',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2027', '成绩查询', '2004', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:result:query',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2028', '成绩确认', '2004', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'business:result:confirm', '#', 'admin', sysdate(), '', null, '');
-- 资讯管理按钮
insert into sys_menu values('2029', '资讯新增', '2005', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:content:add',    'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2030', '资讯修改', '2005', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:content:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2031', '资讯删除', '2005', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:content:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2032', '资讯查询', '2005', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'business:content:query',  '#', 'admin', sysdate(), '', null, '');
-- 报警管理按钮
insert into sys_menu values('2033', '报警新增', '2006', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'business:alarm:add',    'btn-add', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2034', '报警修改', '2006', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'business:alarm:edit',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2035', '报警删除', '2006', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'business:alarm:remove', '#', 'admin', sysdate(), '', null, '');
