package shared.commands;


public class Help extends CommandData {
    private static final long serialVersionUID = 2L;
    public static final Help instance = new Help();

    private Help() {
        super("help", new String[0], false, "Display available server-side commands");
    }
}
