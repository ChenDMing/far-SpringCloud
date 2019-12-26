Shiro:
    1. 自定义filter实现认证和授权
    2. 实现realm重写认证和授权方法
    3. 继承jwt, 生成token 编码token 解码token
    4. springboot定义全局异常拦截器, 正确处理Shiro认证和授权错误时抛出的异常
    
Shiro主要作用的3件事:
    1. 用户登录时做用户密码的校验
    2. 用户登录后收到请求时做 JWT Token 校验
    3. 用户权限的校验
    
Shiro流程:
    1. 自定义的过滤器 isAccessAllowed() 方法, 这个方法是 Shiro 过滤器最先进入的方法, 方法内部调用重写的 isLoginAttempt() 方法
    2. isLoginAttempt() 方法内部判断是否需要进行登录, 登录返回true, 然后 isAccessAllowed() 方法中调用重写的 executeLogin() 方法
    3. executeLogin() 方法内部实现登录, 具体是 getSubject(request, response) 得到 Shiro 的 Subject 对象
    4. subject.login(token) 此方法最终会调用到项目中实现了 Realm 中的 (认证)doGetAuthenticationInfo(){} 方法

项目中Shiro流程:
    1. 登录时 isAccessAllowed() 方法中调用的 isLoginAttempt() 中返回false, 表示不需要认证, 逻辑判断请求直接放行到 controller 的登录接口里
    2. 登录接口中手动获取 Subject 对象主动调用 login() 方法进行认证, 认证通过将 access_token + 角色权限响应到前端
    3. 前端每次请求都会携带 access_token, 此时 isLoginAttempt() 中返回true, 表示需要认证, 认证通过请求继续执行, 而角色权限用于控制
       显示的页面
    4. 认证和授权错误 Shiro 会抛出特定的异常, springboot中的全局异常拦截器可以捕获, 响应特定的状态码和错误信息

Shiro中Realm的逻辑(认证):
    1. 登陆的controller方法中 jwtToken set() 了 username, password, 而过滤器中的 jwtToken set() 了 access_token - 可以凭借这个区分
       是登录请求还是其他请求
    2. 如果 token == null, 说明是登录, 使用前台传递的 username 查询出用户, 前台传递的 username+pwd 生成 token 加上查询出的 password 调用
       jwt 的校验方法, 校验成功查询用户对应角色以及角色对应的权限, 然后将用户放入缓存中
    3. 如果 token != null, 说明是其他请求, 从缓存中获取 adminUser, 获取到了直接刷新一次 token, 获取不到则通过前台的 access_token 走 2 的流程, 
       没出问题就不用用户再次登录
springboot 4.1新特性:
    1. ResponseBodyAdvice + @ControllerAdvice 可以在响应的 response 中追加数据 实例:ResponseTokenAdvice
    
客户端token校验:
    1. 用户注册或登录生成token, 将token和其他数据响应到前端, 再将token根据user_id放入缓存, 没有过期时间, token中只有user_id数据
    2. 用户访问时前端请求头中会包含token
    3. 后台根据自定义注解加上拦截器控制是否校验token, 目的是为了游客也可以访问的页面, 商品展示等
    4. 拦截器通过反射确定是否controller上是否有自定义注解, 如有注解且属性isCheck=true时会校验token, 校验不通过则请求不通过
    5. 通过缓存中的token可以确定用户是不是在其他地方登录了, 每次登录生成的token不一样, 缓存中取出的token和请求的token不一致, 且请求
       的token校验成功, 则是用户在其他地方登录了.
    6. 如果校验通过则取出token中的user_id, 放入ThreadLocal中以供后续使用
    7(额外). jwt的签名是根据头部和载荷 + 加密算法生成, 哪一部分数据有改动都会校验不成功 