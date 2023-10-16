package responses;
/**
 * Clear Response class, just a data container
 */
public class ClearResponse extends spark.Response{
    private String message;
    public ClearResponse() {}
    public ClearResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
