package stream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description 不可变集合 只能查询，不能修改
 * @Author wh
 * @Date 2024/4/5 13:27
 */
public class FinalCollective {


    public static void main(String[] args) {
        // 不可变list集合
        // 只能查询不能修改
        List<String> list = List.of("张三", "李四", "王五", "赵六");
        System.out.println(list.get(0));

        System.out.println("============================");

        // 不可变Set集合
        // 注意：数据必须保证唯一性
        Set<String> set = Set.of("张三", "李四", "王五", "赵六");
        for (String s : set) {
            System.out.println(s);
        }

        System.out.println("============================");

        // 不可变Map集合（参数个数小于10个）
        // 注意：
        //    key不能重复
        //    of方法参数有上限，最多只能包含10个键值对
        Map<String, String> map = Map.of("张三", "南京", "张三1", "北京", "王五", "上海",
                "赵六", "广州", "孙七", "深圳", "周八", "杭州",
                "吴九", "宁波", "郑十", "苏州", "刘一", "无锡",
                "陈二", "嘉兴");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for(Map.Entry<String, String> entry : entries) {
            System.out.println(entry);
        }

        // 不可变Map集合（参数个数大于10个）
        // 使用ofEntries方法创建
        HashMap<String, String> hm = new HashMap<>();
        hm.put("张三", "南京");
        hm.put("李四", "北京");
        hm.put("王五", "上海");
        hm.put("赵六", "北京");
        hm.put("孙七", "深圳");
        hm.put("周八", "杭州");
        hm.put("吴九", "宁波");
        hm.put("郑十", "苏州");
        hm.put("刘一", "无锡");
        hm.put("陈二", "嘉兴");
        hm.put("aaa", "111");


        System.out.println("============================");

        // 可变参数底层就是数组，这里获取entry数组
        Map<Object, Object> objectMap = Map.ofEntries(hm.entrySet().toArray(new Map.Entry[0]));
        System.out.println(objectMap);
        // Java8之后可以使用copyOf方法创建不可变Map
        Map<String, String> map1 = Map.copyOf(hm);
        System.out.println(map1);

    }


}
