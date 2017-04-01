# Java-JVM Structure（内存结构）

![jvm-size](jvm-size.png)

控制参数

- -Xms 设置堆的最小空间大小。空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制。
- -Xmx 设置堆的最大空间大小。空余堆内存大于70%时，JVM就会减少堆直到-Xms的最小限制。
- -XX:NewSize 设置新生代最小空间大小。
- -XX:MaxNewSize 设置新生代最大空间大小。
- -XX:PermSize 设置永久代最小空间大小。
- -XX:MaxPermSize 设置永久代最大空间大小。
- -Xss 设置每个线程的堆栈大小。
- -XX:NewRatio=1:2 设置Young与Old的比例。
- -XX:SurvivorRatio=1:8 设置Eden与Survivor的比例。