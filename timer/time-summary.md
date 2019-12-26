深拷贝浅拷贝:
    1. 浅拷贝: 对于基本数据类型, 浅拷贝会直接进行值传递, 结果是两份不同的数据, 对其中一个对象进行修改不影响另一个. 
              对于引用数据类型, 浅拷贝会把引用值(地址)赋值一份给新对象, 对其中一个进行修改会影响另一个.
    2. 深拷贝: 对于复杂的类可以通过序列化实现深拷贝, 避免修改大量的类
list中的subList(startInt, endInt) 方法得到的数据是内存共享的, 修改其中一个另一个也会变动

BigInteger: 理论上无穷大, 用于大整数之间的运算

mybatis-plus:分页使用
    1. 注入分页插件: 在自定义配置类 MyBatisPlusConfig 中注入 mybatis-plus 插件
    2. service层: 详情见 JobAndTriggerServiceImpl 的 getDataByPaging() 方法中实现分页