## 使用说明:
这是一个展示如何使用ServiceComb来集成Shiro框架做安全认证和权限校验，Shiro是一个简单、
功能强大的Java安全框架，详细可以参考[Apache Shiro官网](https://shiro.apache.org/)
### 本地启动、部署说明
1、首先启动服务中心，服务中心[下载地址](http://servicecomb.apache.org/release/service-center-downloads/)，
下载后解压运行脚本start-service-center.bat    
2、确保已经安全JDK和Maven    
3、使用IDE导入本工程    
4、执行mvn install    
5、单独启动服务，查看运行效果
### 项目结构简介
本项目主要包含两个工程：servicecomb-shiro-simple和servicecomb-shiro-jwt，其中servicecomb-shiro-simple
是展示简单的集成Shiro身份认证，servicecomb-shiro-jwt则主要是结合微服务的特征，演示了分布式场景下如何使用Shiro做
身份认证和权限校验。

## License:

 Apache 2.0 license.See [LICENSE](LICENSE) file for details.
