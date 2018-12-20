# 集群搭建

1.创建目录 <br>
根据端口号分别创建名为6379，6380，6381，6382，6383，6384的文件夹。

2.修改配置文件 <br>
在解压文件夹中有一个 Redis 配置文件 redis.conf，其中一些默认的配置项需要修改（配置项较多，本文仅为举例，修改一些必要的配置）。以下仅以6379端口为例进行配置，6380，6381等端口配置操作类似。将修改后的配置文件分别放入6379~6384文件夹中。

![Image text](http://images.gitbook.cn/d35ae510-24ff-11e8-88b2-0151d97e2cb6)


3.创建必要启停脚本 <br>

```shell
start.sh

#!/bin/bash

../../redis-4.0.12/src/redis-server ../6379/redis.conf &
../../redis-4.0.12/src/redis-server ../6380/redis.conf &
../../redis-4.0.12/src/redis-server ../6381/redis.conf &
../../redis-4.0.12/src/redis-server ../6382/redis.conf &
../../redis-4.0.12/src/redis-server ../6383/redis.conf &
../../redis-4.0.12/src/redis-server ../6384/redis.conf &

stop.sh

#!/bin/bash

ps -ef|grep redis-server | grep -v grep | awk '{print $2}' | xargs kill-9
```

4.简单测试
```
查看进程
ps -ef|grep redis-server

./redis-cli -h 127.0.0.1 -p 6380
cluster nodes
cluster info
```

# Redis 集群创建的步骤有：
（1）相互感知，初步形成集群

在上文中，我们已经成功拉起了6个 redis-server 进程，每个进程视为一个节点，这些节点仍处于孤立状态，它们相互之间无法感知对方的存在，既然要创建集群，首先需要让这些孤立的节点相互感知，形成一个集群；

（2）分配 Slot 给期望的主节点

形成集群之后，仍然无法提供服务，Redis 集群模式下，数据存储于16384个 Slot 中，我们需要将这些 Slot 指派给期望的主节点。何为期望呢？我们有6个节点，3主3备，我们只能将 Slot 指派给3个主节点，至于哪些节点为主节点，我们可以自定义。

（3）设置从节点

Slot 分配完成后，被分配 Slot 的节点将成为真正可用的主节点，剩下的没有分到 Slot 的节点，即便状态标志为 Master，实际上也不能提供服务。接下来，处于可靠性的考量，我们需要将这些没有被指派 Slot 的节点指定为可用主节点的从节点（Slave）。

经过上述三个步骤，一个精简的3主3备 Redis 集群就搭建完成了。
