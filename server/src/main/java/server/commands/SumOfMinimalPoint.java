package server.commands;

import server.utils.Collection;
import shared.network.Response;

/**
 * Command to print sum of minimal points of every element.
 */
public class SumOfMinimalPoint extends Command {
    private final Collection collection;

    public SumOfMinimalPoint(Collection collection) {
        super("sum_of_minimal_point");
        this.collection = collection;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        return new Response(true, "Sum of the minimal points: " + collection.sumOfMinimalPoint());
    }

}
