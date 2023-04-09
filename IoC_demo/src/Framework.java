public class Framework {
    private Bottom bottom;
    public Framework(int size) {
        bottom = new Bottom(size);
    }

    public void init() {
        System.out.println("do framework");
        bottom.init();
    }
}
