# Java - Servlet

## 生命周期

启动时：filter init --> 

运行时：servlet init（第一次访问） --> filter doFilter --> servlet service --> 

关闭时：servlet destroy --> filter destroy

#### *Servlet*

- 单例
- 当客户端第一次访问Servlet的时候，服务器就会创建Servlet实例，执行init方法
- 每次请求，服务器会开一个新的线程访问Servlet中得service方法
- 当服务器关闭时候，Servlet对象就会被销毁，执行destory方法

#### *Filter*

- 单例
- 当服务器启动的时候，服务器就会创建Filter实例
- 当Filter拦截到资源的时候，就会执行doFilter方法
- 当服务器关闭的时候，Filter对象就会被销毁，执行destroy方法

## Jetty

官网：https://www.eclipse.org/jetty

*PS：本文使用的是servlet-3.1*