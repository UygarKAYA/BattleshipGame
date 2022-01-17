package GUI;

import GameLogic.GameLogic;
import GameNetwork.Client;

public class BattleshipGUI {
    public static void main(String[] args) {
        BattleshipController battleshipController = new BattleshipController(new GameLogic(), new Client());
        battleshipController.battleshipGame();
    }
}
