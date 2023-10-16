package responses;

public class JoinResponse extends spark.Response{
    private String message;

    /**
     * Empty constructor
     */
    public JoinResponse() {}

    /**
     * Constructor if no color specified
     * @param message error message
     */
    public JoinResponse(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
