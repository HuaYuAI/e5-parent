# e5-parent
mysqld的windows安装（从数据库）：（https://blog.csdn.net/bfqs1988/article/details/80102981）
	去官网下载：https://dev.mysql.com/downloads/mysql/：
	下载完后在自己需要的文件下解压。
	设置环境变量：在Path内容中加入：D:\mysql\mysql-5.7.22-winx64\bin;（自己安装解压的位置）。
	在mysql-5.7.20-winx64的文件夹下创建一个名为data的空文件夹。
	创建一个my.ini的文件，放在bin目录里面(同时放在C:\Windows下一份作为系统服务)，内容如下
	[mysql]
	# 设置mysql客户端默认字符集
	default-character-set=utf8 
	[mysqld]
	#设置3306端口
	port = 3306 
	# 设置mysql的安装目录
	basedir=D:/mysql-5.7.12-winx64
	# 设置mysql数据库的数据的存放目录
	datadir=D:/mysql-5.7.12-winx64/data
	# 允许最大连接数
	max_connections=200
	# 服务端使用的字符集默认为8比特编码的latin1字符集
	character-set-server=utf8
	# 创建新表时将使用的默认存储引擎
	default-storage-engine=INNODB
	接下来在bin文件夹下进入dos窗口里面输入：
	mysqld --initialize-insecure --user=mysql
	mysqld install
	最后输入：net start mysql     #启动mysql服务
	设置密码：mysqladmin -u root password 123456
	登录mysql -uroot -p
	允许所有：mysql>grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
  mysql安装：
	1.下载文件：
		[root@localhost ~]# wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
	2.安装源文件
		yum localinstall -y mysql57-community-release-el7-11.noarch.rpm
		查看Mysql源是否安装成功
		[root@localhost ~]# yum repolist enabled | grep "mysql.*-community.*"
		mysql-connectors-community/x86_64        MySQL Connectors Community           42
		mysql-tools-community/x86_64             MySQL Tools Community                55
		mysql57-community/x86_64                 MySQL 5.7 Community Server          227
		[root@localhost ~]#
		可以修改vim /etc/yum.repos.d/mysql-community.repo源，改变默认安装的mysql版本。比如要安装5.6版本，将5.7源的enabled=1改成enabled=0。然后再将5.6源的enabled=0改成enabled=1即可。改完之后的效果如下所示：
	3.安装Mysql服务
		[root@localhost ~]# yum install -y mysql-community-server
		查看是否安装成功
		[root@localhost ~]# systemctl status mysqld
	4.启动Mysql
		[root@localhost ~]# systemctl start mysqld	（可能需要较长时间请耐心等待）	
	5.获取root默认密码
		[root@localhost ~]# grep 'temporary password' /var/log/mysqld.log
		root@localhost:密码
	6.关闭Mysql密码校验规则，允许设置简单密码
		在Mysql配置文件最后加入：validate_password = off
		[root@localhost ~]# vi /etc/my.cnf
		重启服务生效：systemctl restart mysqld
	7.登录mysql
		[root@localhost ~]# mysql -uroot -p
		设置密码mysql>alter user 'root'@'localhost' identified by '123456';
	8.配置远程用户登录：
		指定IP：mysql>grant all privileges on *.* to 'root'@'192.168.1.102' identified by '123456' with grant option;
		允许所有：mysql>grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
		退出mysql：mysql>quit
		如果远程还是连接不上查看防火墙是否关闭。
	9.设置开机启动
		systemctl enable mysqld
		systemctl daemon-reload
		取消开机自启动：[root@localhost ~]# chkconfig --del mysql
	启动systemctl start mysqld
	关闭systemctl stop mysqld
	10.已配置远程访问权限，依然不能登录？请检查系统是否开启了防火墙。
		CentOS关闭防火墙
		systemctl stop firewalld.service
		禁止防火墙开机启动
		systemctl disable firewalld.service

