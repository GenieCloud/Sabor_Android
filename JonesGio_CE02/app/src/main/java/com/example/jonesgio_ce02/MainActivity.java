// Gio Jones
// GPL - 202603
// MainActivity.java
package com.example.jonesgio_ce02;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final EditText[] guessFields = new EditText[4];
    private final int[] targetNumbers = new int[4];
    private  int guessesRemaining = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        generateTargetNumbers();

        guessFields[0] = findViewById(R.id.edit_guess_1);
        guessFields[1] = findViewById(R.id.edit_guess_2);
        guessFields[2] = findViewById(R.id.edit_guess_3);
        guessFields[3] = findViewById(R.id.edit_guess_4);
        Button submitBtn = findViewById(R.id.button_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    checkGuess();
                }
            }
        });
    }

    private void generateTargetNumbers() {
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            targetNumbers[i] = random.nextInt(10);
        }
    }

    private boolean validateFields() {
        for (EditText field : guessFields) {

            if (field.getText().toString().trim().isEmpty()) {

                Toast.makeText(this, R.string.error_all_fields_required, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void checkGuess() {
        int correctCount = 0;

        for (int i = 0; i < guessFields.length; i++) {

            int userGuess = Integer.parseInt(guessFields[i].getText().toString());
            int target = targetNumbers[i];

            if (userGuess == target) {

                guessFields[i].setTextColor(Color.GREEN);
                correctCount++;
            } else if (userGuess < target) {

                guessFields[i].setTextColor(Color.BLUE);
            } else {

                guessFields[i].setTextColor(Color.RED);
            }
        }


        if (correctCount == 4) {
            showWinDialog();
        } else {
            guessesRemaining--;
            if (guessesRemaining > 0) {

                String message = getString(R.string.guesses_left) + " " + guessesRemaining;
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                showLossDialog();
            }
        }
    }

    private void showWinDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.win_title)
                .setMessage(R.string.win_message)
                .setCancelable(false) // User must click the button
                .setPositiveButton(R.string.btn_restart, (dialog, which) -> resetGame())
                .show();
    }

    private void showLossDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.loss_title)
                .setMessage(R.string.loss_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_restart, (dialog, which) -> resetGame())
                .show();
    }

    private void resetGame() {

        guessesRemaining = 4;


        generateTargetNumbers();


        for (EditText field : guessFields) {
            field.setText("");
            field.setTextColor(Color.BLACK);
        }
    }

}