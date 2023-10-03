package chess;

import java.util.Objects;

public class MyPosition implements ChessPosition{

    private final int row;
    private final int col;

    public MyPosition(int row, int col){
        this.row = row;
        this.col = col;
    }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPosition that = (MyPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "row=" + row +
                ", col=" + col;
    }
}
