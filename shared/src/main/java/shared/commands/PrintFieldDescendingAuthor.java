package shared.commands;

public class PrintFieldDescendingAuthor extends CommandData {
    private static final long serialVersionUID = 2L;

    public PrintFieldDescendingAuthor() {
        super("print_field_descending_author", new String[0], false, "Print the 'author' field of all elements in descending order");
    }
}
