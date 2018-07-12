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
