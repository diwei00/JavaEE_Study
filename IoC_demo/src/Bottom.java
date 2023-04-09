public class Bottom {
    private Tire tire;
    public Bottom(int size) {
        tire = new Tire(size);
    }

    public void init() {
        System.out.println("do bottom");
        tire.init();
    }
}
