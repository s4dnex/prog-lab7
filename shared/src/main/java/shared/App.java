package shared;

public class App {
    public String getGreeting() {
        return "shared!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
