public class Car {
    // 依赖Framework类，层层依赖
    private Framework framework;

    public Car(int size) {
        framework = new Framework(size);
    }

    public void init() {
        System.out.println("do car");
        framework.init();
    }


    // 层层依赖，代码耦合性太高，一处代码修改，就需要改动多出代码
    // 依赖的生命周期在自己类中掌控（控制权在自己手里），需要降低耦合性，就需要将控制权反转（交给别人控制，自己需要时别人传递过来即可）
    public static void main(String[] args) {
        Car car = new Car(19);
        car.init();

    }
}
