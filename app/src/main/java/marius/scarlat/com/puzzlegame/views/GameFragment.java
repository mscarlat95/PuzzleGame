package marius.scarlat.com.puzzlegame.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.game_design.Game;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.general.ConvertTime;
import marius.scarlat.com.puzzlegame.storage.SharedPref;
import marius.scarlat.com.puzzlegame.views_adapters.GameRecyclerViewAdapter;

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";

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
            Toast.makeText(getActivity(), "You cannot perform moves anymore. Game is finished!", Toast.LENGTH_SHORT).show();

            return Constants.INACTIVE;
        }
    }

    public boolean updateGame(ArrayList<Integer> numbers) {
        Log.d(TAG, "Updating game parameters");
        game.setNumbers(numbers);

        /* Check if game is finished */
        if (game.isFinished()) {
            Log.d(TAG, "updateGame: Game is finished");

            /* Stop chronometer */
            chronometer.stop();

            /* Update game state */
            game.setState(Constants.INACTIVE);

            /* Update UI */
            Toast.makeText(getActivity(), "Congratulations!", Toast.LENGTH_SHORT).show();
            infoTextView.setText(R.string.restart_game_msg);
            infoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {/* Initialize the game */
                    initGame();
                    initRecylerView();
                    startGame();
                }
            });

            displayFinishDialog();

            return true;
        }

        /* Game is still running */
        return false;
    }


    private void displayFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Game Over")
                .setMessage("Do you want to publish your score?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /* Save user score */
                        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        SharedPref.saveScore(ConvertTime.encodeTime(elapsedMillis));
                        Log.d(TAG, "User Score: " + SharedPref.getLastScore());

                        /* Display scores */
                        ((MainActivity) getActivity()).setViewPager(Constants.SCORE_FRAGMENT_POSITION);
                    }
                });

        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.BottomUpDialogAnim;
        alert.show();
    }


    /* Setup a new game */
    private void initGame() {
        Log.d(TAG, "Initializing a new game");

        game = new Game();
        game.setState(Constants.ACTIVE);
        game.populateList();
    }

    /* Set layout horizontal and populate it with random numbers */
    private void initRecylerView() {
        Log.d(TAG, "Initializing the RecylerView and its items");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GameRecyclerViewAdapter adapter = new GameRecyclerViewAdapter(getContext(), this, game.getNumbers());

        if (recyclerView == null || linearLayoutManager == null) {
            Log.d(TAG, "initRecylerView: RecyclerView or LinearLayoutManager is NULL");
            return;
        }

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initChronometer() {
        Log.d(TAG, "Initializing the Chronometer");

        /* Reset elapsed time to 0:0 */
        chronometer.setBase(SystemClock.elapsedRealtime());

        /* Start Chronometer */
        chronometer.start();
        chronometer.setFormat("- Elapsed Time: %s -");
    }

    public void startGame() {
        Log.d(TAG, "Starting a new game");

        /* Clear previous results */
        SharedPref.clear(Constants.PLAYER_SCORE);

        /* Initialize the game */
        initGame();

        /* Initialize and populate the RecyclerView */
        initRecylerView();

        /* Initialize bottom info */
        infoTextView.setText(R.string.sort_numbers_info);
        infoTextView.setOnClickListener(null);

        /* Start chronometer */
        initChronometer();
    }

    private void initViews(View view) {
        chronometer = view.findViewById(R.id.elapsed_time_chronometer);
        recyclerView = view.findViewById(R.id.game_recycler_view);
        infoTextView = view.findViewById(R.id.info_text_view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Method was invoked!");

        View view = inflater.inflate(R.layout.fragment_game, container, false);
        view.setTag(TAG);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: Method was invoked!");

        super.onViewCreated(view, savedInstanceState);

        /* Initialize Android views */
        initViews(view);
    }
}
