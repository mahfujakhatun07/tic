package com.mah.tictactoe;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount = 0;
    private TextView msg;
    private Button resetBtn, undoBtn, redoBtn;

    // Stacks to manage undo and redo functionality
    private Stack<Move> undoStack = new Stack<>();
    private Stack<Move> redoStack = new Stack<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Tic Tac Toe"); // Set the app title on the top of the app bar

        msg = findViewById(R.id.msg);
        resetBtn = findViewById(R.id.resetBtn);
        undoBtn = findViewById(R.id.undoBtn);
        redoBtn = findViewById(R.id.redoBtn);
        GridLayout gameBoard = findViewById(R.id.gameBoard);

        // Initialize the buttons in the grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + ((i * 3) + (j + 1));
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick((Button) v);
                    }
                });
            }
        }

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoMove();
            }
        });

        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redoMove();
            }
        });
    }

    private void onButtonClick(Button button) {
        if (!button.getText().toString().equals("")) {
            return;
        }

        String symbol = player1Turn ? "X" : "O";
        button.setText(symbol);

        int row = getButtonRow(button);
        int col = getButtonCol(button);
        undoStack.push(new Move(row, col, symbol));
        redoStack.clear(); // Clear redo stack since a new move was made

        roundCount++;

        if (checkForWin()) {
            showWinner(player1Turn ? "Player X wins!" : "Player O wins!");
        } else if (roundCount == 9) {
            showWinner("Draw!");
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void showWinner(String winner) {
        msg.setText(winner);
        msg.setVisibility(View.VISIBLE);
        disableButtons();
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        roundCount = 0;
        player1Turn = true;
        msg.setVisibility(View.GONE);
        undoStack.clear();
        redoStack.clear();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private void undoMove() {
        if (!undoStack.isEmpty()) {
            Move lastMove = undoStack.pop();
            buttons[lastMove.getRow()][lastMove.getCol()].setText("");
            redoStack.push(lastMove);
            player1Turn = !player1Turn;
            roundCount--;
            msg.setVisibility(View.GONE); // Hide the message if shown
        }
    }

    private void redoMove() {
        if (!redoStack.isEmpty()) {
            Move move = redoStack.pop();
            buttons[move.getRow()][move.getCol()].setText(move.getSymbol());
            undoStack.push(move);
            player1Turn = move.getSymbol().equals("X");
            roundCount++;
        }
    }

    private int getButtonRow(Button button) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    return i;
                }
            }
        }
        return -1; // Should never happen
    }

    private int getButtonCol(Button button) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    return j;
                }
            }
        }
        return -1; // Should never happen
    }

    // Inner class to represent a move in the game
    private static class Move {
        private final int row;
        private final int col;
        private final String symbol;

        public Move(int row, int col, String symbol) {
            this.row = row;
            this.col = col;
            this.symbol = symbol;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}