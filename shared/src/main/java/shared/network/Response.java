package shared.network;

import java.io.Serializable;

public class Response implements Serializable {
  private static final long serialVersionUID = 1L;
  private boolean success;
  private String message;

  public Response(boolean success, String message) {
    this.message = message;
    this.success = success;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Response {success=" + success + ", message='" + message + "'}";
  }
}
