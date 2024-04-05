package stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description stream流
 * @Author wh
 * @Date 2024/4/5 13:47
 */
public class StreamDemo {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>(List.of("张三丰","张无忌","张翠山","王二麻子","张良","谢广坤"));
        list.stream()
                .filter(s -> s.startsWith("张"))
                .filter(s -> s.length() == 3)
                .forEach(s -> System.out.println(s));
        System.out.println("=======================");

        /**
         * 获取Stream流
         *    Collection体系集合：
         *       使用默认方法stream()生成流
         *    Map体系集合：
         *       把Map转成Set集合，间接的生成流
         *    数组：
         *       通过Arrays中的静态方法stream生成流
         *    同种数据类型的多个数据：
         *       通过Stream接口的静态方法of(T... values)生成流
         */

        // 集合获取Stream流
        ArrayList<Object> list1 = new ArrayList<>();
        Stream<Object> listStream = list1.stream();

        // Map获取流
        HashMap<Object, Object> map = new HashMap<>();
        Stream<Map.Entry<Object, Object>> entryStream = map.entrySet().stream();
        Stream<Object> keysetStream = map.keySet().stream();
        Stream<Object> valueStream = map.values().stream();

        // 数组获取流
        String[] arr = new String[]{"a", "b", "c"};
        Stream<String> arrStream = Arrays.stream(arr);

        // 同种类型的多个数据获取流
        Stream<String> stringStream = Stream.of("a", "b", "c");

        /**
         * Stream流操作中间方法
         *    filter: 过滤
         *    limit：截取前指定个数的数据
         *    skip：跳过指定参数个数的数据
         *    concat：合并两个流为一个流
         *    distinct：返回该流的不同元素（去重）
         *    map：转换流中的数据类型
         */
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("张三丰,12");
        stringList.add("张无忌,13");
        stringList.add("张翠山,15");
        stringList.add("王二麻子,16");
        stringList.add("张良,17");
        stringList.add("谢广坤,18");
        // 保留姓张的人
        stringList.stream().filter(s -> s.startsWith("张")).forEach(s -> System.out.println(s));
        System.out.println("==============");

        // 取出前三个数据输出
        stringList.stream().limit(3).forEach(s -> System.out.println(s));
        System.out.println("=======================");
        // 跳过三个，把剩下的数据打印
        stringList.stream().skip(3).forEach(s -> System.out.println(s));
        System.out.println("=======================");
        // 跳过2个元素，把剩下的元素中前2个在控制台输出
        stringList.stream().skip(2).limit(2).forEach(s -> System.out.println(s));
        System.out.println("=======================");


        Stream<String> s1 = stringList.stream().limit(4);
        Stream<String> s2 = stringList.stream().skip(2);
        // 合并两个流
//      Stream.concat(s1, s2).forEach(s -> System.out.println(s));

        // 去重，去掉流中重复的数据
        Stream.concat(s1, s2).distinct().forEach(s -> System.out.println(s));
        System.out.println("=======================");

        // 转换流中的数据类型
        stringList.stream().map(s -> Integer.parseInt(s.split(",")[1])).forEach(s -> System.out.println(s));
        System.out.println("+++++++++++++");


        /**
         * Stream流操作终结方法
         *      forEach：对流的每个元素执行操作
         *      count：统计流中元素的个数
         *      toArray：收集在数组中
         *      collect：把流中的数据收集在集合中（list, set, map）
         *
         */

        long count = stringList.stream().count();
        System.out.println(count);
        System.out.println("================");
        stringList.stream().forEach(s -> System.out.println(s));

        ArrayList<Integer> list2 = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list2.add(i);
        }

//        list2.add(10);
//        list2.add(10);
//        list2.add(10);
//        list2.add(10);
//        list2.add(10);

        // 收集偶数在集合中
        List<Integer> integerList = list2.stream().filter(item -> item % 2 == 0).collect(Collectors.toList());
        System.out.println(integerList);
        System.out.println("====================");

        // 收集偶数在set中
        Set<Integer> integerSet = list2.stream().filter(item -> item % 2 == 0).collect(Collectors.toSet());
        System.out.println(integerSet);
        System.out.println("=================");

        // 收集在数组中 value：元素个数  指定数组类型以及具体数据
        Integer[] array = list2.stream().toArray(value -> new Integer[value]);
        System.out.println(Arrays.toString(array));
        System.out.println("---");

        // 收集数据在Map中
        ArrayList<String> list3 = new ArrayList<>();
        list3.add("zhangsan,23");
        list3.add("lisi,24");
        list3.add("wangwu,25");
        list3.add("zhaoliu,26");
        // 保留年龄大于等于24岁的人，并将结果收集到Map集合中，姓名为键，年龄为值
        // 收集时指定Map中的key，value
        Map<String, Integer> map1 = list3.stream()
                .filter(item -> Integer.parseInt(item.split(",")[1]) > 24)
                .collect(Collectors.toMap(
                        item -> item.split(",")[0], // key
                        item -> Integer.parseInt(item.split(",")[1]))); // value
        System.out.println(map1);
        System.out.println("====================");




    }
}
