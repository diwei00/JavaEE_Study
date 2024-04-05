package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @Author wh
 * @Date 2024/4/5 15:32
 */
public class Test {
   public static void main(String[] args) {
      // 存放男演员
      ArrayList<String> list1 = new ArrayList<>();
      // 存放女演员
      ArrayList<String> list2 = new ArrayList<>();
      list1.add("aaaa,23");
      list1.add("aaa,23");
      list1.add("aaaa,23");
      list1.add("aaa,23");
      list1.add("aaa,23");
      list1.add("aaaa,23");
      list2.add("杨bb,25");
      list2.add("bbb,25");
      list2.add("bbb,25");
      list2.add("杨bb,25");
      list2.add("bbb,25");
      list2.add("杨bb,25");
      Stream<String> stream1 = list1.stream().filter(s -> s.split(",")[0].length() == 3).limit(2);
      Stream<String> stream2 = list2.stream().filter(s -> s.startsWith("杨")).skip(1);
      List<Actor> list = Stream.concat(stream1, stream2)
              .map(item -> new Actor(item.split(",")[0], Integer.parseInt(item.split(",")[1])))
              .collect(Collectors.toList());
      System.out.println(list);


   }
}
