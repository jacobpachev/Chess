package responses;
/**
 * Logout Response class, just a data container
 */
public class LogoutResponse {
    private String message;

    public LogoutResponse() {}

    public LogoutResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
