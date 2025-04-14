import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {
    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[] buttons = new JButton[9];
    JButton restartButton = new JButton("Restart");
    boolean player1_turn;
    boolean playWithAI = false;

    TicTacToe() {
        showGameModeSelection();
    }

    private void showGameModeSelection() {
        JFrame modeFrame = new JFrame("Select Game Mode");
        modeFrame.setSize(400, 200);
        modeFrame.setLayout(new GridLayout(2, 1));

        JButton playerVsPlayer = new JButton("Two Players");
        JButton playerVsAI = new JButton("Play vs AI");

        playerVsPlayer.addActionListener(e -> {
            playWithAI = false;
            modeFrame.dispose();
            setupGame();
        });

        playerVsAI.addActionListener(e -> {
            playWithAI = true;
            modeFrame.dispose();
            setupGame();
        });

        modeFrame.add(playerVsPlayer);
        modeFrame.add(playerVsAI);
        modeFrame.setLocationRelativeTo(null);
        modeFrame.setVisible(true);
    }

    private void setupGame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);

        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        restartButton.setFont(new Font("Ink Free", Font.BOLD, 40));
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> resetGame());
        title_panel.add(restartButton, BorderLayout.EAST);
        title_panel.add(textfield, BorderLayout.CENTER);

        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);

        firstTurn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i] && buttons[i].getText().equals("")) {
                if (player1_turn) {
                    buttons[i].setText("X");
                    buttons[i].setForeground(Color.RED);
                    buttons[i].setBackground(new Color(200, 200, 200));
                    player1_turn = false;
                    textfield.setText("O turn");
                } else {
                    buttons[i].setText("O");
                    buttons[i].setForeground(Color.BLUE);
                    buttons[i].setBackground(new Color(200, 200, 200));
                    player1_turn = true;
                    textfield.setText("X turn");
                }
                check();
                if (!player1_turn && playWithAI) {
                    aiMove();
                }
            }
        }
    }

    public void firstTurn() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player1_turn = true;
        textfield.setText("X turn");
    }

    private void check() {
        int[][] winConditions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] condition : winConditions) {
            if (!buttons[condition[0]].getText().equals("") &&
                    buttons[condition[0]].getText().equals(buttons[condition[1]].getText()) &&
                    buttons[condition[0]].getText().equals(buttons[condition[2]].getText())) {
                highlightWinner(condition);
                return;
            }
        }
        boolean full = true;
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                full = false;
                break;
            }
        }
        if (full) {
            textfield.setText("Draw!");
        }
    }

    private void highlightWinner(int[] condition) {
        for (int index : condition) {
            buttons[index].setBackground(Color.GREEN);
        }
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
        textfield.setText(buttons[condition[0]].getText() + " wins!");
    }

    private void aiMove() {
        java.util.List<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                availableMoves.add(i);
            }
        }
        if (!availableMoves.isEmpty()) {
            int aiChoice = availableMoves.get(random.nextInt(availableMoves.size()));
            buttons[aiChoice].setText("O");
            buttons[aiChoice].setForeground(Color.BLUE);
            buttons[aiChoice].setBackground(new Color(200, 200, 200));
            player1_turn = true;
            textfield.setText("X turn");
            check();
        }
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setBackground(null);
            button.setEnabled(true);
        }
        firstTurn();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
