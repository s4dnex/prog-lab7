package server.commands;

import server.utils.Collection;
import shared.network.Response;

/**
 * Command to remove element from collection by its id.
 */
public class RemoveById extends Command {
    private final Collection collection;

    public RemoveById(Collection collection) {
        super("remove_by_id");
        this.collection = collection;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        long id;
        
        try {
            id = Long.parseLong(args[0]);
        }
        catch (NumberFormatException e) {
            return new Response(false, "ID must be a long integer");
        }

        try {
            collection.remove(id);
        }
        catch (IllegalArgumentException e) {
            return new Response(false, "Element with ID " + id + " does not exist!");
        }

        return new Response(true, "Element with ID " + id + " has been removed!");
    }
}
