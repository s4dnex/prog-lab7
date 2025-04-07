package server.commands;

import server.utils.Collection;
import shared.network.Response;
import shared.utils.Formatter;

/**
 * Command to show info about the collection.
 */
public class Info extends Command {
    private final Collection collection;
    
    // CONSTRUCTORS

    public Info(Collection collection) {
        super("info");
        this.collection = collection;
    }

    // METHODS

    @Override
    public Response execute(String[] args, Object obj) {
        StringBuilder msg = new StringBuilder();
        msg.append("Type: " + collection.getType() + "\n");
        msg.append("Size: " + collection.getSize() + "\n");
        msg.append("Creation date: " + collection.getCreationDate().format(Formatter.DATE_FORMAT) + "\n");
        return new Response(true, msg.toString());
    }

}
