package server.commands;

import server.utils.Collection;
import shared.network.Response;

/**
 * Command to print element's difficulties in ascending order.
 */
public class PrintFieldAscendingDifficulty extends Command {
    private final Collection collection;

    public PrintFieldAscendingDifficulty(Collection collection) {
        super("print_field_ascending_difficulty");
        this.collection = collection;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        var fields = collection.getAscendingDifficulty();

        if (fields.isEmpty()) {
            return new Response(true, "Collection is empty!");
        }

        StringBuilder msg = new StringBuilder("Difficulties in ascending order:\n");
        long i = 1;
        for (var field : fields) {
            msg.append((i++) + ". " + field + "\n");
        }
        return new Response(true, msg.toString());
    }
}
