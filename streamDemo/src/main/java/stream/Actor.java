package stream;

/**
 * @Description 测试实体类
 * @Author wh
 * @Date 2024/4/5 15:30
 */
public class Actor {
   private String name;
   private Integer age;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getAge() {
      return age;
   }

   public void setAge(Integer age) {
      this.age = age;
   }

   public Actor(String name, Integer age) {
      this.name = name;
      this.age = age;
   }

   @Override
   public String toString() {
      return "Actor{" +
              "name='" + name + '\'' +
              ", age=" + age +
              '}';
   }
}
