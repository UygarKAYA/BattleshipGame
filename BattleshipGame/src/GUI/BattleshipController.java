package GUI;

import GameLogic.GameLogic;
import GameModels.*;
import GameExceptions.*;
import GameNetwork.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static javax.swing.SwingConstants.HORIZONTAL;
import static javax.swing.SwingConstants.VERTICAL;

public class BattleshipController {

    private GameLogic playerBoard;
    private Client client;
    private NavalShips[] navalShips = { new Submarine(), new Battleship(), new Carrier(), new Destroyer() };
    private int navalShipsNumber = 0;

    private String clickSound;
    ButtonHandler buttonHandler = new ButtonHandler();
    SoundEffect soundEffect = new SoundEffect();

    BattleshipGame battleshipGame = new BattleshipGame(this);
    BattleshipConsole battleshipConsole = new BattleshipConsole();
    GameLogic opponentBoard = new GameLogic();

    public BattleshipController(GameLogic gameLogicBoard, Client client) {
        this.playerBoard = gameLogicBoard;
        this.client = client;

        Thread thread = new Thread(client);
        thread.start();
    }

    public void playerBoardClk(int x, int y, int direction) {
        try {
            NavalShips navalShip = navalShips[navalShipsNumber++];
            playerBoard.setNavalShip(x-1, y-1, direction, navalShip);
            if (direction == HORIZONTAL) {
                for (int i = y; i < y+navalShip.getSize(); i++)
                    battleshipGame.getPlayerButtons()[x][i].setText(navalShip.getSign());
            }
            else if (direction == VERTICAL) {
                for (int i = x; i < x+navalShip.getSize(); i++)
                    battleshipGame.getPlayerButtons()[i][y].setText(navalShip.getSign());
            }
            if (navalShipsNumber == 4) {
                Thread incomingMove = new Thread(new IncomingMove());
                incomingMove.start();
                for (JButton[] xx : battleshipGame.getPlayerButtons()) {
                    for (JButton cell : xx)
                        cell.setEnabled(false);
                }
                try {
                    client.getWriter().println(battleshipConsole.boardToConsole(playerBoard.getArea()) + "?");
                    System.out.println("You can send the massage now");
                    client.getWriter().flush();
                } catch (Exception exception) {
                    System.out.println("Connection message was not sent successfully");
                }
            }
        } catch (NavalShipExist exception) {
            navalShipsNumber--;
            JOptionPane.showMessageDialog(null, "You Have Already Placed The Naval Ship In This Cell, Please Try a New Cell");
        } catch (NavalShipFit exception) {
            navalShipsNumber--;
            JOptionPane.showMessageDialog(null, "The Naval Ship You Want to Place Does Not Fit The Field Dimensions, Please Try Another Area");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "You Have Placed All Your Naval Ships, Please Wait For Opponent to Place Their Naval Ships");
        }
    }

    public void opponentBoardClk(int x, int y) {
        try {
            opponentBoard.shot(--x, --y);
            try {
                client.getWriter().println(battleshipConsole.boardToConsole(opponentBoard.getArea()) + "|");
                System.out.println("You can send the massage now");
                client.getWriter().flush();
            } catch (Exception exception) { System.out.println("Connection message was not sent successfully"); }
            updateOpponentView(opponentBoard.getArea());
        } catch (Exception exception) { JOptionPane.showMessageDialog(null, "You Have Selected This Cell Before, Please Select a New Cell"); }
    }

    public class IncomingMove implements Runnable {
        private boolean isFirstTime = true;

        @Override
        public void run() {
            try {
                String message;
                while ((message = client.getMessage()) != null) {
                    if (message.endsWith("?") && isFirstTime) {
                        opponentBoard.setArea(battleshipConsole.consoleToBoard(message.substring(0, message.length() - 5)));
                        updateOpponentView(opponentBoard.getArea());
                        isFirstTime = false;
                    } else if (message.endsWith("|")) {
                        playerBoard.setArea(battleshipConsole.consoleToBoard(message.substring(0, message.length() - 5)));
                        updatePlayerView(playerBoard.getArea());
                    }
                }
            } catch (Exception e) {
                System.out.println("There is no new area");
            }
        }
    }

    public void battleshipGame() {
        battleshipGame.setTitle("Battleship Game with Local Area Network (LAN)");
        battleshipGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        battleshipGame.setSize(1075, 550);
        battleshipGame.setMinimumSize(new Dimension(1075, 550));

        battleshipGame.setLocationRelativeTo(null);
        battleshipGame.setResizable(false);
        battleshipGame.setVisible(true);
    }

    public class SoundEffect{
        Clip clip ;

        public void setFile(String soundFileName) {
            try{
                File file = new File(soundFileName);
                AudioInputStream sound  = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch(Exception e) { }
        }
        public void play() {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void updateOpponentView(int[][] area) {
        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[i].length; j++) {
                if (area[i][j] == -1) {
                    battleshipGame.getOpponentButtons()[i+1][j+1].addActionListener(buttonHandler);
                    clickSound = ".//rsc//miss.wav";
                    battleshipGame.getOpponentButtons()[i+1][j+1].setBackground(Color.RED);
                    battleshipGame.getOpponentButtons()[i+1][j+1].setText("X");
                }
                else if (area[i][j] == 1) {
                    battleshipGame.getOpponentButtons()[i+1][j+1].addActionListener(buttonHandler);
                    clickSound = ".//rsc//hit.wav";
                    battleshipGame.getOpponentButtons()[i+1][j+1].setBackground(Color.GREEN);
                    battleshipGame.getOpponentButtons()[i+1][j+1].setText("H");
                }
            }
        }
    }

    public void updatePlayerView(int[][] area) {
        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[i].length; j++) {
                if (area[i][j] == -1) {
                    battleshipGame.getOpponentButtons()[i+1][j+1].addActionListener(buttonHandler);
                    clickSound = ".//rsc//hit.wav";
                    battleshipGame.getPlayerButtons()[i+1][j+1].setText("X");
                    battleshipGame.getPlayerButtons()[i+1][j+1].setBackground(Color.GREEN);
                }
                else if (area[i][j] == 1) {
                    battleshipGame.getOpponentButtons()[i+1][j+1].addActionListener(buttonHandler);
                    clickSound = ".//rsc//miss.wav";
                    battleshipGame.getPlayerButtons()[i+1][j+1].setText("S");
                    battleshipGame.getPlayerButtons()[i+1][j+1].setBackground(Color.RED);
                }
            }
        }
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent event){
            soundEffect.setFile(clickSound);
            soundEffect.play();
        }
    }
}