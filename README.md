# zhq!!!

#描述
一些自写模块,用于曾经/现在/未来的开发~<br/>
需要JDK 1.8 or later

#模块列表
###Core 
核心模块 主要用来提供共通的功能支持<br/>
###DBMeta 
数据库元信息 主要包装了jdbc一些获取数据库元信息的操作<br/>
主要对Table以及View进行获取<br/>
支持Postgresql、Oracle数据库 以后会逐渐增加
###Template
模板渲染 用来整合一个公共的渲染入口/出口 以便后续模块使用
###Template-velocity
对模板渲染提供velocity实现的支持
###Generator
模板生成器 依赖Template模块<br/>
提供一个生成参数以及文件扫描/生成的一个支持<br/>
实际使用时 需要依赖一个Template的实现<br/>
###Script 
脚本模块 主要包装了Java中对Javascript的支持<br/>
属于一时起兴项目 本来想同来统一校验以及配置文件的操作<br/>
不过经过测试 发现效率还是有问题<br/>
以后再说吧<br/>
###websocket-deploy
WebSocket动态部署类 主要对Java EE的WebSocket这块进行了适当修改<br/>
 可以不用注解 可以直接注入WEB容器中起作用<br/>
 由于该模块为一时起兴 基本可以放弃 以后考虑删除<br/>

#问题与支持
请用Issues进行提问<br/>
用Pull Requests提供支持<br/>

#许可
[Apache 2.0 licence](https://github.com/legend0702/zhq/blob/master/LICENSE)

#吐槽
反正没有人会用 我就自己慢慢折腾就好~