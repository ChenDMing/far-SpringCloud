springboot整合redis有两种情况
    - 引入redis, 可以直接操作使用
    - 配合springboot中的缓存注解等使用
    
1. 引入redis可以使用StringRedisTemplate操作Redis数据, 也就是公司项目中的使用方式, 只要引入注解然后再application配置文件中配置
   redis的配置, 就可以直接使用.
2. 
    
    
redis集群:
    - 