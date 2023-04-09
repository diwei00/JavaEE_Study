package newcar;

public class Framework {
    private Bottom bottom;

    public Framework(Bottom bottom) {
        this.bottom = bottom;
    }
    public void init() {
        System.out.println("do framework");
        bottom.init();
    }
}
