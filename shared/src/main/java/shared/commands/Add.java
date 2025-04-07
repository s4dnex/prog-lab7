package shared.commands;

public class Add extends CommandData {
    private static final long serialVersionUID = 2L;

    public Add() {
        super("add", new String[0], true, "Add a new element to the collection");
    }
}
