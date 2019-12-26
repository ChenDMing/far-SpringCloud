1. mybatis升级成了mybatis-plus
    - mybatis-plus 是 mybatis 的进一步封装框架, 里面添加了对单表的增删改查等操作以及分页查询
2. 使用了日志 logback
    - springboot 默认日志为 slf4j+logback, 只需要 resource 文件中加入 logback.xml 文件即可, 不需要任何依赖
3. 拦截器的使用
    - 自定义类实现 HandlerInterceptor 重写方法, WebMvcConfigurer 中添加新增的拦截器
    - ThreadLocal 中记得 remove 删除线程变量
    - 有自定义拦截器时, springboot 中不能再使用拦截器配置跨域, springboot 会先执行自定义的拦截器再执行 springboot 自带的跨域拦截器, 
      请求就会报跨域问题. 使用 springboot 中配置跨域过滤器, 保证最先执行跨域请求.
4. 全局异常拦截处理类
    - 使用 @RestControllerAdvice 注解, 方法上添加 @ExceptionHandler 注解
5. springboot 整合 redis
    - 引入 redis 依赖直接就可以使用
    
    
    
几点
    1. springboot中配置拦截器方式的跨域问题, 项目中有其他的拦截器此时会出现顺序问题, 请求先被其他拦截器拦截就会出现跨域问题
    2. 项目中频繁的插入操作可以通过延迟队列来实现批量插入