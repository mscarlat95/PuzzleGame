package marius.scarlat.com.puzzlegame.views;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.network.NetworkConnection;
import marius.scarlat.com.puzzlegame.network.RetreiveScoreTask;
import marius.scarlat.com.puzzlegame.views_adapters.ScoreRecyclerViewAdapter;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    /* Android Views and Adapters */
    private RecyclerView recyclerView;
    private ScoreRecyclerViewAdapter scoreAdapter;
    private TextView infoTextView;

    public void displayScore() {

        if (NetworkConnection.isAvailable(getContext())) {
            new RetreiveScoreTask(getActivity(), recyclerView, scoreAdapter)
                    .execute(Constants.WEB_SERVICE_ADDR_GET + Constants.LIMIT_COUNT);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Method was invoked!");

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        view.setTag(TAG);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout rootLayout = view.findViewById(R.id.fragment_menu);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(Constants.GAME_FRAGMENT_POSITION);
            }
        });

        recyclerView = view.findViewById(R.id.highscore_recycler_view);
        infoTextView = view.findViewById(R.id.info_text_view);
        infoTextView.setText(R.string.start_game_info);

        /* Initialize adapter with no data */
        scoreAdapter = new ScoreRecyclerViewAdapter(getContext(), null, null);

        displayScore();
    }
}
