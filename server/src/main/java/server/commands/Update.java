package server.commands;

import server.utils.Collection;
import shared.data.LabWork;
import shared.network.Response;

/**
 * Command to update element in collection by its id.
 */
public class Update extends Command {
    private final Collection collection;
    
    public Update(Collection collection) {
        super("update");
        this.collection = collection;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        long id;
        
        if (obj.getClass() != LabWork.class)
            return new Response(false, "Invalid object type. Expected LabWork.");

        try {
            id = Long.parseLong(args[0]);
        }
        catch (NumberFormatException e) {
            return new Response(false, "ID must be a long integer");
        }

        if (!collection.contains(id))
            return new Response(false, "No LabWork with such ID");

        collection.update(
            id,
            (LabWork) obj
        );

        return new Response(true, "Element with ID " + id + " has been updated!");
    }
}
