import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons;
    private char[][] board;
    private char currentPlayer;
    private int boardSize;
    private boolean gameFinished;
    private boolean playWithCPU;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainMenuPanel = createMainMenuPanel();
        add(mainMenuPanel, BorderLayout.CENTER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Keep the window maximized
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        });
    }

    public JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        mainMenuPanel.setBackground(Color.BLUE);

        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.black);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainMenuPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Color.RED);

        JButton playerVsPlayerButton = createGameButton("Player vs Player");
        playerVsPlayerButton.addActionListener(e -> showBoardSizeOptions(false));
        buttonPanel.add(playerVsPlayerButton);

        JButton playerVsCPUButton = createGameButton("Player vs CPU");
        playerVsCPUButton.addActionListener(e -> showBoardSizeOptions(true));
        buttonPanel.add(playerVsCPUButton);

        mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

        return mainMenuPanel;
    }

    public void showBoardSizeOptions(boolean cpuMode) {
        getContentPane().removeAll();

        JPanel boardSizePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        boardSizePanel.setBackground(Color.BLUE);

        JButton button3x3 = createGameButton("3x3");
        button3x3.addActionListener(e -> startGame(3, cpuMode));
        boardSizePanel.add(button3x3);

        JButton button4x4 = createGameButton("4x4");
        button4x4.addActionListener(e -> startGame(4, cpuMode));
        boardSizePanel.add(button4x4);

        JButton button5x5 = createGameButton("5x5");
        button5x5.addActionListener(e -> startGame(5, cpuMode));
        boardSizePanel.add(button5x5);

        add(boardSizePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public JButton createGameButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.GRAY);
        button.setFocusPainted(false);
        return button;
    }

    public void startGame(int size, boolean playWithCPU) {
        getContentPane().removeAll();
        setTitle("Tic Tac Toe " + size + "x" + size);
        setLayout(new GridLayout(size, size));
        setResizable(true);

        boardSize = size;
        buttons = new JButton[size][size];
        board = new char[size][size];
        currentPlayer = 'X';
        gameFinished = false;
        this.playWithCPU = playWithCPU;

        initializeBoard();
        createButtons();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        if (playWithCPU && currentPlayer == 'O') {
            makeCPUMove();
        }
    }

    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void createButtons() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 80));

                Dimension buttonSize = new Dimension(120, 120);
                button.setPreferredSize(buttonSize);

                button.addActionListener(new ButtonListener(i, j));
                buttons[i][j] = button;
                add(button);
            }
        }
    }

    public boolean isWinningMove(int row, int col) {
        // Check row
        boolean rowWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[row][i] != currentPlayer) {
                rowWin = false;
                break;
            }
        }

        // Check column
        boolean colWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][col] != currentPlayer) {
                colWin = false;
                break;
            }
        }

        // Check diagonal
        boolean diagWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != currentPlayer) {
                diagWin = false;
                break;
            }
        }

        // Check anti-diagonal
        boolean antiDiagWin = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][boardSize - 1 - i] != currentPlayer) {
                antiDiagWin = false;
                break;
            }
        }

        return rowWin || colWin || diagWin || antiDiagWin;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public void displayResult(String result) {
        String message;
        if (result.equals("draw")) {
            message = "It's a draw! Do you want to play again?";
        } else {
            if (playWithCPU && currentPlayer == 'O') {
                message = "CPU wins! Do you want to play again?";
            } else {
                message = result + " Do you want to play again?";
            }
        }

        int option = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }

        gameFinished = true;
    }

    public void resetGame() {
        getContentPane().removeAll();
        getContentPane().add(createMainMenuPanel());
        pack();
        setLocationRelativeTo(null);
        gameFinished = false;
    }

    public void makeCPUMove() {
        if (!gameFinished) {
            Random random = new Random();
            int row, col;

            do {
                row = random.nextInt(boardSize);
                col = random.nextInt(boardSize);
            } while (board[row][col] != ' ');

            buttons[row][col].setText(String.valueOf(currentPlayer));
            board[row][col] = currentPlayer;

            if (isWinningMove(row, col)) {
                displayResult("Player X wins!");
            } else if (isBoardFull()) {
                displayResult("It's a draw!");
            } else {
                switchPlayer();

            }
        }
    }

    private class ButtonListener implements ActionListener {
        private int row;
        private int col;

        public ButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameFinished && board[row][col] == ' ') {
                buttons[row][col].setText(String.valueOf(currentPlayer));
                board[row][col] = currentPlayer;

                if (isWinningMove(row, col)) {
                    displayResult("Player " + currentPlayer + " wins!");
                } else if (isBoardFull()) {
                    displayResult("It's a draw!");
                } else {
                    switchPlayer();

                    if (playWithCPU && currentPlayer == 'O') {
                        makeCPUMove();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}