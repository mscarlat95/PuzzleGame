package marius.scarlat.com.puzzlegame.game_design;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game {

    private static final String TAG = "Game";

    private boolean state;
    private ArrayList<Integer> numbers;

    public boolean getState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    public ArrayList<Integer> getNumbers() { return numbers; }
    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    public Game() {
        Log.d(TAG, "Game: Constructor was invoked!");
    }

    public void populateList() {
        Log.d(TAG, "populateList: Generating numbers");

        numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5,6 ,7 ,8));
        Collections.shuffle(numbers);
        Log.d(TAG, "populateList: Result" + numbers.toString());
    }

    public boolean isFinished() {
        Log.d(TAG, "isFinished: Method was invoked!");

        if (numbers != null && numbers.size() > 0) {
            /* Check if numbers are sorted */
            for (int i = 1; i < numbers.size(); ++i) {
                if (numbers.get(i) <= numbers.get(i - 1)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

}
