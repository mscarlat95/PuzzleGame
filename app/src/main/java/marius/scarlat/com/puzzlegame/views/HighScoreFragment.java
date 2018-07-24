package marius.scarlat.com.puzzlegame.views;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.general.ConvertTime;
import marius.scarlat.com.puzzlegame.network.NetworkConnection;
import marius.scarlat.com.puzzlegame.network.PublishScoreTask;
import marius.scarlat.com.puzzlegame.network.RetreiveScoreTask;
import marius.scarlat.com.puzzlegame.storage.SharedPref;
import marius.scarlat.com.puzzlegame.views_adapters.ScoreRecyclerViewAdapter;

public class HighScoreFragment extends Fragment {

    private static final String TAG = "HighScoreFragment";

    /* Android Views and adapters */
    private RecyclerView recyclerView;
    private ScoreRecyclerViewAdapter scoreAdapter;
    private TextView infoTextView;
    private TextView playerScoreTextView;
    private EditText playerNameEditText;

    private boolean scorePosted = false;

    public void displayScore() {

        if (NetworkConnection.isAvailable(getContext())) {
            new RetreiveScoreTask(getActivity(), recyclerView, scoreAdapter).
                    execute(Constants.WEB_SERVICE_ADDR_GET + Constants.LIMIT_COUNT);
        }

        showUserScore();
    }

    private void showUserScore() {
        String user = SharedPref.getLastPlayer();
        double score = SharedPref.getLastScore();

        playerScoreTextView.setText(ConvertTime.decodeTime(score));
        if (!user.equals(Constants.UNSET)) {
            playerNameEditText.setText(user);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Method was invoked!");

        View view = inflater.inflate(R.layout.fragment_highscore, container, false);
        view.setTag(TAG);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: Method was invoked!");

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.highscore_recycler_view);

        playerScoreTextView = view.findViewById(R.id.score_text_view);
        playerNameEditText = view.findViewById(R.id.player_name_edit_text);
        playerNameEditText.requestFocus();

        infoTextView = view.findViewById(R.id.info_text_view);
        infoTextView.setText(R.string.publish_score_msg);
        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (scorePosted) {
                    Toast.makeText(getActivity(), "Your score has already been posted!", Toast.LENGTH_SHORT).show();
                    return;
                }


                final String playerName = playerNameEditText.getText().toString();
                final double playerScore = SharedPref.getLastScore();

                if (playerName.isEmpty() || playerName.equals(Constants.UNSET)) {
                    Toast.makeText(getActivity(), "Please enter your username before publishing your score", Toast.LENGTH_SHORT).show();

                    /* Request focus and activate the keyboard */
                    playerNameEditText.requestFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(playerNameEditText, InputMethodManager.SHOW_IMPLICIT);
                    return;
                }

                if (playerScore < 0) {
                    Toast.makeText(getActivity(), "Invalid Score. Play again in order to publish your score!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /* Save player name for the next time */
                SharedPref.savePlayer(playerName);

                /* Publish score on the web server address */
                new PublishScoreTask(getContext()).execute(Constants.WEB_SERVICE_ADDR_POST, playerName, String.valueOf(playerScore));
                scorePosted = true;

            }
        });

        /* Initialize adapter with no data */
        scoreAdapter = new ScoreRecyclerViewAdapter(getContext(), null, null);
    }
}
