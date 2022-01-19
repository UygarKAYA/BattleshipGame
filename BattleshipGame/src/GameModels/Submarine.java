package GameModels;

public class Submarine extends GameModels.NavalShips {
    private static final int size = 3;
    private static final String sign = "S";

    public int getSize() {
        return size;
    }
    public String getSign() {
        return sign;
    }
}
