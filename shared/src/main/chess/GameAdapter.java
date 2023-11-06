package chess;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GameAdapter implements JsonDeserializer<ChessGame> {

    @Override
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());
        return jsonBuilder.create().fromJson(jsonElement, GameImpl.class);
    }
}
