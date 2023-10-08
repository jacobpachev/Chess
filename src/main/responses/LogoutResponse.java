package responses;
/**
 * Logout Response class, just a data container
 */
public class LogoutResponse extends spark.Response{
    private String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
