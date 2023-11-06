package chess;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class PieceAdapter implements JsonDeserializer<ChessPiece> {

    @Override
    public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Type clas = null;
        if (jsonElement.toString().contains("PAWN")) {
            clas = Pawn.class;
        }
        if (jsonElement.toString().contains("KNIGHT")) {
            clas = Knight.class;
        }
        if (jsonElement.toString().contains("BISHOP")) {
            clas = Bishop.class;
        }
        if (jsonElement.toString().contains("ROOK")) {
            clas = Rook.class;
        }
        if (jsonElement.toString().contains("QUEEN")) {
            clas = Queen.class;
        }
        if (jsonElement.toString().contains("KING")) {
            clas = King.class;
        }
        assert clas != null;
        return new Gson().fromJson(jsonElement, clas);
    }
}
