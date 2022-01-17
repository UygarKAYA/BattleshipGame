package GameModels;

public class Battleship extends NavalShips {
    private static final int size = 4;
    private static final String sign = "B";

    public int getSize() {
        return size;
    }
    public String getSign() {
        return sign;
    }
}
