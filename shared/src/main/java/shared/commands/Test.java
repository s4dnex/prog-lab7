package shared.commands;


public class Test extends CommandData {
    private static final long serialVersionUID = 2L;
    public static final Test instance = new Test();

    private Test() {
        super("test", new String[0], false, "Test connection between client and server");
    }
}
