package webSocketMessages.serverMessages;

public class Error extends ServerMessage{
    String errorMsg;

    public Error(String errorMsg) {
        super(ServerMessageType.ERROR);
        this.errorMsg = errorMsg;
    }
}
