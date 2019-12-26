概念:     数据库       表/集合       数据/文档    字段/域    索引     表连接    主键
mongodb   database    table         row         column    index   支持
mysql     database    collection    document    field     index   不支持    自动将_id字段设置成主键

1. mongodb 中同一个集合可以插入不同类型的数据, 而关系型数据库表格中数据是固定的


springBoot+DB的常用注解:
1. @Id: 主键, 自带索引不可重复, 有次注解的文档需要自己维护不重复约束, Mongodb 不在主动生成主键, 效率要远低于Mongodb自己生成主键的效率
2. @Document: 类似于 Mybatis 中的 @TableName("dsc_users") @Document(collection = "d_user")
3. @Indexed: 声明该字段需要加索引, 大幅度提高条件检索的速度
4. @CompoundIndex: 复合索引注解, 使用在类上
5. @Field: 不加默认以属性名为列名
6. @Transient: 忽略此字段的映射
7. 


索引:
注: Mongodb 中索引的顺序, 建立索引时 1 和 -1 分别表示正序好倒序
1. 单字段索引 = @Indexed 也是Mongodb默认建立的索引类型, 文档默认创建的 _id 也是这种类型的索引
2. 复合索引: 与 MySQL 中组合索引类似, 提高多条件查询的效率, 同时可以满足匹配索引前缀的查询 (age, name) 可以满足单个 age 字段的查询
    - 复合索引创建技巧: 首先按照前缀字段单个查询的可能, 其次考虑字段值得分布
      字段值分布: 如果没有外界因素影响, 根据age和name创建索引, 此时就考虑 age 和 name 字段重复的可能, 显然 age 字段会出现大量的重复
      那就按照 (name, age) 创建索引, 查询时会根据 name 准确缩小查询范围
   多key索引: Mongodb中对数组建立索引时, 会对数组中每个元素建立一条索引, 比如用户表中有 hobby(爱好) 字段, 此时建立多key索引, 可以再查询
             时找出爱好相同的数据