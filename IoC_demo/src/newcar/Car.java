package newcar;

public class Car {
    private Framework framework;

    public Car(Framework framework) {
        this.framework = framework;
    }

    public void init() {
        System.out.println("do car");
        framework.init();
    }

    // Spring IoC 的做法（控制权反转）（降低代码耦合性）
    // 这里依赖的对象不在是自己控制了（不在自己类中实例），而是需要的时候，由别人传递过来即可，即控制权反转了
    public static void main(String[] args) {

        Tire tire = new Tire(19);
        Bottom bottom = new Bottom(tire);
        Framework framework = new Framework(bottom);
        Car car = new Car(framework);
        car.init();
    }
}
