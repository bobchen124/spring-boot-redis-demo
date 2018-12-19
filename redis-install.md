# redis 安装

官网下载安装如下：
$ wget http://download.redis.io/releases/redis-5.0.3.tar.gz

$ tar xzf redis-5.0.3.tar.gz <br>
$ cd redis-5.0.3 <br>
$ make <br>

安装成功，在 src 目录下可以看到生成的 bin 文件 redis-server 和 redis-cli。

# Run Redis

$ src/redis-server

# Redis client
'
$ src/redis-cli

redis> set foo bar

OK

redis> get foo

"bar"
'
