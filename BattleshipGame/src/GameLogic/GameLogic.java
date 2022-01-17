package GameLogic;

import GameModels.NavalShips;
import GameExceptions.NavalShipFit;
import GameExceptions.NavalShipExist;

public class GameLogic {
    private int[][] area;

    public GameLogic() {
        area = new int[10][10];
        int x = 0 ;
        int y = 0 ;
        while(x < 10) {
            while(y < 10) {
                area[x][y] = 0;
                y++;
            }
            y = 0;
            x++ ;
        }
    }

    public void setNavalShip(int i, int j, int direction, NavalShips navalShips) throws NavalShipFit, NavalShipExist {
        if (availableSize(i, j, direction, navalShips))
            throw new NavalShipFit();
        if (shipConflict(i, j, direction, navalShips))
            throw new NavalShipExist();
        if (direction == 0) {
            int x = j;
            while (x < j + navalShips.getSize()) {
                area[i][x] = navalShips.getSize();
                x++;
            }
        }
        else {
            int y = i;
            while (y < i + navalShips.getSize()) {
                area[y][j] = navalShips.getSize();
                y++;
            }
        }
    }

    public void shot(int x, int y) throws Exception {
        if (area[x][y] == -1 || area[x][y] == 1) throw new Exception();
        if (area[x][y] == 0) area[x][y] = -1;
        else area[x][y] = 1;
    }

    private boolean availableSize(int i, int j, int direction, NavalShips navalShips) {
        return (direction == 0) ? (j + navalShips.getSize() > 10) || (i + 1 > 10) : (j + 1 > 10) || (i + navalShips.getSize() > 10);
    }

    private boolean shipConflict(int x, int y, int direction, NavalShips navalShips) {
        if (direction == 0) {
            int i = y;
            while (i < y + navalShips.getSize()) {
                if (area[x][i] != 0)
                    return true;
                i++;
            }
        }
        else {
            int i = x;
            while (i < x + navalShips.getSize()) {
                if (area[i][y] != 0)
                    return true;
                i++;
            }
        }
        return false;
    }

    public int[][] getArea() { return area; }
    public void setArea(int[][] field) { this.area = field; }
}