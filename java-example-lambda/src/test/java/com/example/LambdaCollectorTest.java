package com.example;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LambdaCollectorTest {

    @Test
    public void testCollector() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa"));
        users.add(new User(2L, "bbb"));
        users.add(new User(3L, "bbb"));
        users.add(new User(4L, "ddd"));

        // 获取用户Id
        List<Long> list = users.stream().map(User::getId).collect(Collectors.toList());
        list = users.stream().map(User::getId).collect(Collectors.toCollection(ArrayList::new));// 指定List实现类
    }

    @Test
    public void testCollector2() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa"));
        users.add(new User(2L, "bbb"));
        users.add(new User(3L, "bbb"));
        users.add(new User(4L, "ddd"));

        // 获取用户名称
        Set<String> set = users.stream().map(User::getUsername).collect(Collectors.toSet());
        set = users.stream().map(User::getUsername).collect(Collectors.toCollection(LinkedHashSet::new));// 指定Set实现类
    }

    @Test
    public void testCollector3() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa"));
        users.add(new User(2L, "bbb"));
        users.add(new User(3L, "bbb"));
        users.add(new User(4L, "ddd"));

        // users --> userId: user
        Map<Long, User> map = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        map = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));// 简化
        map = users.stream().collect(Collectors.toMap(User::getId, Function.identity(), (u1, u2) -> u2));// 解决Key冲突
        map = users.stream().collect(Collectors.toMap(User::getId, Function.identity(), (u1, u2) -> u2, LinkedHashMap::new));// 指定Map实现类
    }

    @Test
    public void testCollector4() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa"));
        users.add(new User(2L, "bbb"));
        users.add(new User(3L, "bbb"));
        users.add(new User(4L, "ddd"));

        // 取最大值
        User user = users.stream().collect(Collectors.collectingAndThen(Collectors.maxBy((o1, o2) -> o1.getId().compareTo(o2.getId())), Optional::get));// 列表不能为空
        user = users.stream().max((o1, o2) -> o1.getId().compareTo(o2.getId())).get();// 列表不能为空
        // 取最小值
        user = users.stream().collect(Collectors.collectingAndThen(Collectors.minBy((o1, o2) -> o1.getId().compareTo(o2.getId())), Optional::get));// 列表不能为空
        user = users.stream().min((o1, o2) -> o1.getId().compareTo(o2.getId())).get();// 列表不能为空
        // 合并内容
        String title = users.stream().map(User::getUsername).collect(Collectors.joining(","));// 列表为空时，是空字符串
        System.out.println(title);
    }

    @Test
    public void testCollector5() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa"));
        users.add(new User(2L, "bbb"));
        users.add(new User(3L, "bbb"));
        users.add(new User(4L, "ddd"));

        // parallel 并行，默认是串行
        // 根据用户名称分组
        Map<String, List<User>> map = users.stream().parallel().collect(Collectors.groupingBy(User::getUsername));
        // 根据用户名称分组，然后再统计名称出现的次数
        Map<String, Long> map2 = users.stream().collect(Collectors.groupingBy(User::getUsername, Collectors.counting()));
        map2.forEach((k, v) -> System.out.println(String.format("%s ==>> %d", k, v)));
    }

    @Test
    public void testCollector6() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "aaa", Arrays.asList("a1", "a2", "a3")));
        users.add(new User(2L, "bbb", Arrays.asList("a1", "a2", "a4")));
        users.add(new User(3L, "bbb", Arrays.asList("a2")));
        users.add(new User(4L, "ddd", Arrays.asList("a1")));

        Map<String, List<User>> map = users.stream()
                .flatMap(user -> user.getLoves().stream().map((t) -> new AbstractMap.SimpleEntry<>(t, user)))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())));
    }
}
