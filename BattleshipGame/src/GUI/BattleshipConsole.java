package GUI;

public class BattleshipConsole {

    public String boardToConsole(int[][] area) {
        StringBuilder builder = new StringBuilder();
        for (int[] row : area) {
            for (int cell : row)
                builder.append(cell).append("-");
            builder.append(" ");
        }
        return builder.toString();
    }

    public int[][] consoleToBoard(String response) {
        String[] rows = response.split(" ");
        String[][] cells = new String[rows.length][10];
        for (int i = 0; i < rows.length; i++)
            cells[i] = rows[i].split("-");
        int[][] area = new int[10][10];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++)
                area[i][j] = Integer.valueOf(cells[i][j]);
        }
        return area;
    }
}
