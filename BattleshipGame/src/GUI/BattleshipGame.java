package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static javax.swing.SwingUtilities.*;

public class BattleshipGame extends JFrame {

    private JButton[][] playerButtons;
    private JButton[][] opponentButtons;
    private JPanel playerPanel, playerGamePanel, playerNamePanel;
    private JPanel opponentPanel, opponentGamePanel, opponentNamePanel;

    private BattleshipController battleshipController;
    private char c = 'A';

    public BattleshipGame(final BattleshipController battleshipController) {
        this.playerButtons = new JButton[11][11];
        this.opponentButtons = new JButton[11][11];
        this.battleshipController = battleshipController;

        UIManager.put("OptionPane.minimumSize",new Dimension(150,150));
        JLabel label = new JLabel("Ozyegin University | CS 447 Project 1 | Welcome to Battleship Game with Local Area Network   ");
        label.setFont(new Font("serif", Font.BOLD + Font.ITALIC, 25));
        JOptionPane.showMessageDialog(null, label);

        UIManager.put("OptionPane.minimumSize",new Dimension(100,100));
        JOptionPane.showMessageDialog(null, " The Game Will be Played Among Two Players Over the Local Area Network"
                +"\n There Will be Four Ships which are Carrier (size of 5, sign by C), Battleship (size of 4, sign by B), Submarine (size of 3, sign by S), Destroyer (size of 2, sign by D)"
                +"\n The Left Grid is For The Player’s Own Ships and The Right Grid Will Show The Player’s Attempts to Sink His Opponent’s Ships"
                +"\n"
                +"\n You Can Set Your Naval Ships Horizontally With Left Click in the Player Panel"
                +"\n You Can Set Your Naval Ships Vertically With Right Click in the Player Panel"
                +"\n You Can Shot Naval Ships With Left Click in the Opponent Panel");
        UIManager.put("OptionPane.minimumSize",new Dimension(100,100));

        setLayout(new GridLayout(1, 1));
        playerPanel = new JPanel();
        playerPanel.setLayout(new BorderLayout());
        this.add(playerPanel);

        playerGamePanel = new JPanel();
        playerGamePanel.setSize(1075/2, 1075/2);
        playerPanel.add(playerGamePanel);

        playerNamePanel = new JPanel();
        playerNamePanel.setSize(1075/2, 550/13);
        playerNamePanel.add(new JLabel(JOptionPane.showInputDialog(null, "Enter Your Player Name", "You")));
        playerPanel.add(playerNamePanel, BorderLayout.NORTH);

        playerGamePanel.setLayout(new GridLayout(11, 11));

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JButton button = new JButton();
                playerButtons[i][j] = button;
                playerGamePanel.add(button);
                button.setBackground(Color.CYAN);
                button.setForeground(Color.BLACK);

                if (i == 0 && j == 0) {
                    button.setEnabled(false);
                    button.setBackground(Color.WHITE);
                }
                else if (i == 0) {
                    button.setText(Character.toString(c++));
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                } else if (j == 0) {
                    button.setText(Integer.toString(i));
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                } else {
                    int finalI = i;
                    int finalJ = j;
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent arg0) {
                        if (isLeftMouseButton(arg0))
                            battleshipController.playerBoardClk(finalI, finalJ, HORIZONTAL);
                        else if (isRightMouseButton(arg0))
                            battleshipController.playerBoardClk(finalI, finalJ, VERTICAL);
                        }
                    });
                }
            }
        }

        opponentPanel = new JPanel();
        opponentPanel.setLayout(new BorderLayout());
        this.add(opponentPanel);

        opponentGamePanel = new JPanel();
        opponentGamePanel.setSize(1075/2, 1075/2);
        opponentPanel.add(opponentGamePanel);

        opponentNamePanel = new JPanel();
        opponentNamePanel.setSize(1075/2, 550/13);
        opponentNamePanel.add(new JLabel("Opponent"));
        opponentPanel.add(opponentNamePanel, BorderLayout.NORTH);

        opponentGamePanel.setLayout(new GridLayout(11, 11));
        char c = 'A';
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JButton button = new JButton();
                opponentButtons[i][j] = button;
                opponentGamePanel.add(button);
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK);

                if (i == 0 && j == 0) {
                    button.setEnabled(false);
                    button.setBackground(Color.WHITE);
                }
                else if (i == 0) {
                    button.setText(Character.toString(c++));
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                }
                else if (j == 0) {
                    button.setText(Integer.toString(i));
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                }
                else {
                    int finalI = i;
                    int finalJ = j;
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent arg0) {
                        if (isLeftMouseButton(arg0))
                            battleshipController.opponentBoardClk(finalI, finalJ);
                        }
                    });
                }
            }
        }
    }

    public JButton[][] getPlayerButtons() { return playerButtons; }
    public JButton[][] getOpponentButtons() { return opponentButtons; }
}
