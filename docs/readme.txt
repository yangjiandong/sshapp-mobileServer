无线平台服务端
==============

2012.08.07
----------

   1. 增加移动端更改密码功能
   SystemController/updatePassword

2012.07.14
----------

   1. 修改包结构
   pm.approval:审批
   pm.mob:基础字典
   pm.nurse:移动护理
   pm.query:全院概况

2012.07.12
----------

   1. 增加记录手机操作信息
   mobLog
   --TODO,前台查看

2012.07.10
----------

   1. 增加ssh-mobileCommon 公共库支持,取消org.ssh.pm.mob.entity.UserDTO
   mvn eclipse:eclipse
   mvn war:inpalce

2012.07.04
----------

   1. 增加短信支持
   save/MODEM.zip
   pm/sms

   mvn eclipse:eclipse
   mvn war:inplace
   --没有硬件支持,暂时没实现短信功能
   --copy SMSDLL.dll to path

2012.06.27
----------

   1. 根据文档 save/开发环境说明,搭建环境

   2. 插入允许的系统模块

   insert into T_MySystem select 1, '系统管理'

   3. 第一次运行时需做下初始化

   http://localhost:8090/sshapp/common/init


   --END