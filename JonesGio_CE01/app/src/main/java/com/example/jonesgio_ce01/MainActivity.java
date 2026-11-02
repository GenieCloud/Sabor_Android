// Gio Jones
// GPL - 202603
// MainActivity.java

package com.example.jonesgio_ce01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Button[] buttons = new Button[16];
    private int guessCount = 0;
    private Button firstClicked = null;
    private Button secondClicked = null;
    private int pairsFound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i < 16; i++) {
            String buttonID = "btn_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            buttons[i].setOnClickListener(this);
        }

        findViewById(R.id.btn_restart).setOnClickListener(this);

        resetGame();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_restart) {
            resetGame();
        } else {
            handleCardClick((Button) v);
        }
    }

    private void handleCardClick(Button tappedButton) {
        if (firstClicked != null && secondClicked != null) {
            firstClicked.setText("");
            secondClicked.setText("");
            firstClicked = null;
            secondClicked = null;
        }

        String letter = (String) tappedButton.getTag();
        tappedButton.setText(letter);

        if (firstClicked == null) {
            firstClicked = tappedButton;
        } else {
            if (tappedButton == firstClicked) return;

            secondClicked = tappedButton;
            guessCount++;
            updateGuessDisplay();

            if (firstClicked.getText().equals(secondClicked.getText())) {
                firstClicked.setVisibility(View.INVISIBLE);
                secondClicked.setVisibility(View.INVISIBLE);
                firstClicked = null;
                secondClicked = null;
                pairsFound++;

                if (pairsFound == 8) {
                    showRestartButton();
                }
            }
        }
    }


    private void updateGuessDisplay() {
        TextView tv = findViewById(R.id.textView_guesses);
        String display = getString(R.string.guesses_label) + guessCount;
        tv.setText(display);
    }

    private void resetGame() {
        guessCount = 0;
        pairsFound = 0;
        updateGuessDisplay();

        List<String> letters = new ArrayList<>(Arrays.asList("A","A","B","B","C","C","D","D","E","E","F","F","G","G","H","H"));
        Collections.shuffle(letters);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setVisibility(View.VISIBLE);
            buttons[i].setText("");
            buttons[i].setTag(letters.get(i));
        }

        findViewById(R.id.btn_restart).setVisibility(View.GONE);
    }

    private void showRestartButton() {
        findViewById(R.id.btn_restart).setVisibility(View.VISIBLE);
    }
}