package shared.commands;


public class Help extends CommandData {
    private static final long serialVersionUID = 2L;

    public Help() {
        super("help", new String[0], false, "Display available server-side commands");
    }
}
