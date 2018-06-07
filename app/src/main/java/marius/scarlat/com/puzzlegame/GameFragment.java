package marius.scarlat.com.puzzlegame;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

            /* Update game state */
            game.setState(Constants.INACTIVE);

            /* Stop chronometer */
            chronometer.stop();

            /* Update UI */
            Toast.makeText(getActivity(), "Congratulations!", Toast.LENGTH_SHORT).show();
            infoTextView.setText(R.string.restart_game_msg);
            infoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame();
                }
            });
            return true;
        }

        /* Game is still running */
        return false;
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), this, game.getNumbers());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initChronometer() {
        Log.d(TAG, "Initializing the Chronometer");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        chronometer.setFormat("- Elapsed Time: %s -");
    }

    public void startGame() {
        Log.d(TAG, "Starting a new game");
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
        recyclerView = view.findViewById(R.id.recycler_view);
        infoTextView = view.findViewById(R.id.info_text_view);
    }

    private void initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setViewPager(Constants.MENU_FRAGMENT_POSITION);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Method was invoked!");

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        /* Initialize toolbar */
        initToolbar(view);

        /* Initialize Android views */
        initViews(view);

        return view;
    }
}
