package server.commands;

import server.utils.Collection;
import shared.data.LabWork;
import shared.network.Response;

/**
 * Command to remove every element from collection if it is lower than given.
 */
public class RemoveLower extends Command {
    private final Collection collection;
    
    public RemoveLower(Collection collection) {
        super("remove_lower");
        this.collection = collection;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        if (obj.getClass() != LabWork.class) {
            return new Response(false, "Invalid object type. Expected LabWork.");
        }

        if (collection.getSize() == 0) {
            return new Response(false, "Collection is empty!" );
        }

        long amount = collection.removeLower((LabWork) obj);

        if (amount > 1)
            return new Response(true, amount + " elements have been removed!");
        else if (amount == 1)
            return new Response(true, "1 element has been removed!");
        else
            return new Response(true, "No elements have been removed!");
    }
}
