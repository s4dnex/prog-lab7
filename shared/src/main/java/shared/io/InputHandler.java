package shared.io;

import java.io.IOException;

public interface InputHandler extends AutoCloseable {
    /**
     * Reads line and returns it.
     * @return line as {@link String}
     */
    public String readln();

    /**
     * Closes the input handler.
     * @throws IOException if an error occurs while closing the input handler
     */
    public void close() throws IOException;
}
