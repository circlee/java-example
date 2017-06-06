# Java - Jxls Excel报表生成工具

> [Jxls](http://jxls.sourceforge.net)是一个生成Excel的工具包。它采用[Apache Jexl](http://commons.apache.org/proper/commons-jexl/reference/syntax.html)标签的方法，类似Jsp标签，写一个Excel模板，然后生成报表，非常灵活，简单。

先看一下Excel模板模板吧。

![user.xlsx](user.xlsx.png)

- A1单元格中的批注 `jx:area(lastCell="C3")` 表示模板的区域为A1:C3，在这区域内的标签才会被解析。
- A3单元格中的批注 `jx:each(var="user" items="users" lastCell="C3")` 用于遍历一个列表 `users` ，lastCell表示遍历的区域为A3:C3。

接下来再看看Jxls是怎么使用这个模板文件吧。

```java
public class JxlsTest {
    @Test
    public void test() throws Exception {
        List<User> users = listUser();
        try (InputStream input = new FileInputStream(getTemplate("user.xlsx"))) {
            try (OutputStream output = new FileOutputStream(getOutput("user.xlsx"))) {
                Context context = new Context();
                context.putVar("users", users);
                JxlsHelper.getInstance().processTemplate(input, output, context);
            }
        }
    }
    ...
}
```
 
 最后我再介绍下Jxls是怎么自定义标签函数。
 
 ![user-util.xls](user-util.xlsx.png)
 
*PS：本文使用的是jxls-2.4.0*