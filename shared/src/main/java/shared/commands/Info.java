package shared.commands;

public class Info extends CommandData {
    private static final long serialVersionUID = 2L;
    
    public Info() {
        super("info", new String[0], false, "Display information about the collection");
    }
}
