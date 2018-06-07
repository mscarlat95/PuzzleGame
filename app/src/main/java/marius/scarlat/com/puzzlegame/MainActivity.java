package marius.scarlat.com.puzzlegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /* Game Logic */
    private Game game;

    /* Android Views */
    private Chronometer chronometer;
    private RecyclerView recyclerView;
    private TextView infoTextView;

    public boolean checkGameAvailable() {
        if (game.getState() == Constants.ACTIVE) {
            Log.d(TAG, "checkGameAvailable: Game is active");
            return Constants.ACTIVE;
        } else {
            Log.d(TAG, "checkGameAvailable: Game is inactive");

            /* Display UI message */
            Toast.makeText(MainActivity.this, "You cannot perform moves anymore. Game is finished!", Toast.LENGTH_SHORT).show();

            return Constants.INACTIVE;
        }
    }

    public boolean updateGame(ArrayList<Integer> numbers) {
        Log.d(TAG, "updateGame: Updating game parameters");
        game.setNumbers(numbers);

        /* Check if game is finished */
        if (game.isFinished()) {
            Log.d(TAG, "updateGame: Game is finished");

            /* Update game state */
            game.setState(Constants.INACTIVE);

            /* Stop chronometer */
            chronometer.stop();

            /* Display UI message */
            Toast.makeText(MainActivity.this, "Congrats!", Toast.LENGTH_SHORT).show();

            return true;
        }

        /* Game is still running */
        return false;
    }

    /* Setup a new game */
    private void initGame() {
        Log.d(TAG, "initGame: Setup a new game");

        game = new Game();
        game.setState(Constants.ACTIVE);
        game.populateList();
    }

    /* Set layout horizontal and populate it with random numbers */
    private void initRecylerView() {
        Log.d(TAG, "initRecylerView: Method was invoked!");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, game.getNumbers());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
   }

   private void initChronometer() {
       chronometer.start();
       chronometer.setFormat("- %s -");
   }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Method was invoked!");

        setContentView(R.layout.activity_main);

        /* Initialize Android views */
        chronometer = findViewById(R.id.elapsed_time_chronometer);
        recyclerView = findViewById(R.id.recycler_view);
        infoTextView = findViewById(R.id.info_text_view);

        /* Initialize the game */
        initGame();

        /* Initialize and populate the RecyclerView */
        initRecylerView();

        /* Start chronometer */
        initChronometer();
    }

}
