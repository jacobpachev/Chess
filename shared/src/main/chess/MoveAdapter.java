package chess;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MoveAdapter implements JsonDeserializer<ChessMove> {
    @Override
    public MyChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(ChessPosition.class, new PosAdapter());
        return jsonBuilder.create().fromJson(jsonElement, MyChessMove.class);
    }
}
