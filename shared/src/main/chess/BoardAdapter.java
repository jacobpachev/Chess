package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BoardAdapter implements JsonDeserializer<ChessBoard> {

    @Override
    public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var els = jsonElement.toString().split(":");
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
        var json = gsonBuilder.create();
        var piecesStr = new StringBuilder();
        els = Arrays.toString(els).split(",");
        var pieces = new ArrayList<ChessPiece>();
        var positions = new ArrayList<ChessPosition>();
        for(int j = 0; j+10 < els.length; j+=10) {
            var row = 0;
            var col = 0;
            for(int k = 1+j; k <= 2+j; k++) {
                var n = Integer.valueOf(els[k].replaceAll("[^0-9]", ""));
                if(k % 2 != 0) {
                    row = n;
                }
                else {
                    col = n;
                }
            }
            for (int i = 3+j; i <= 10+j; i++) {
                piecesStr.append(els[i]);
                if (i % 2 != 0) {
                    piecesStr.append(":");
                } else if (i != 10+j) {
                    piecesStr.append(",");
                }
            }
            if(piecesStr.substring(piecesStr.length()-3, piecesStr.length()).equals("}}]")) {
                piecesStr.delete(piecesStr.length()-3, piecesStr.length());
            }

            pieces.add(json.fromJson(String.valueOf(piecesStr), ChessPiece.class));
            positions.add(new MyPosition(row, col));
            piecesStr = new StringBuilder();
        }
        var board = new HashMap<ChessPosition, ChessPiece>();
        for(int i = 0; i < pieces.size(); i++) {
            board.put(positions.get(i), pieces.get(i));
        }

        return new MyBoard(board);
    }
}

