# bg_erp

兵哥铝业的erp网站
##项目介绍
- 兵哥铝业是一家传统型生产公司，主要是负责轻奢极简风格的家居型材的生产。我也是借此机会，为兵哥铝业编写了一款erp系统，旨在提升生产效率。
- 本项目是基于微服务架构的前后端分离项目。前台使用的是VUE-ELEMENT-ADMIN开源项目作为管理界面，后台使用springboot+springcloud为基础进行开发
- 本项目是此erp系统的后端项目，前端的项目我单独放到了一个库里面，地址：https://github.com/xiahang111/vue_bingo_admin
##目录介绍

- config_center  SpringCloudConfig项目，保存各个环境的配置文件
- erp_base 存放父类、枚举类等
- erp_commons 存放与mysql对应的实体类
- erp_config 存放系统的相关配置类
- erp_eureka eureka的启动项目，用于服务的发现与注册
- erp_gateway 本系统的网关启动项目，使用的是SpringCloudGateway
- erp_person 提供erp公司资源相关的接口与服务
- erp_sms 消息服务，用于异步更新redis，以及监听绑定rabbitmq消息队列并提供相应服务
- erp_utils 存放一些工具类
- erp_web 提供web服务接口与主要功能接口
- erp_xo 存放service层、Dao层
- excel 存放excel生成的模板文件