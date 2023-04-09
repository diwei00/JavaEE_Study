package newcar;

public class Bottom {
    private Tire tire;

    public Bottom(Tire tire) {
        this.tire = tire;

    }
    public void init() {
        System.out.println("do bottom");
        tire.init();
    }
}
