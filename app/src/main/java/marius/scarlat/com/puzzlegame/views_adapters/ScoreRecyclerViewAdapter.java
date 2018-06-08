package marius.scarlat.com.puzzlegame.views_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.general.ConvertTime;

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ScoreItemViewHolder> {

    private static final String TAG = "ScoreRecyclerAdapter";

    private Context context;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();

    /* Constructor */
    public ScoreRecyclerViewAdapter(Context context, ArrayList<String> users, ArrayList<Double> scores) {
        this.context = context;
        this.users = users;
        this.scores = scores;
    }

    /* Setters and Getters */
    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    public ArrayList<String> getUsers() { return users; }
    public void setUsers(ArrayList<String> users) { this.users = users; }

    public ArrayList<Double> getScores() { return scores; }
    public void setScores(ArrayList<Double> scores) { this.scores = scores; }

    /* Creates a new ScoreItemViewHolder that RecycleView can reuse */
    @Override
    public ScoreItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Method was invoked!");

        /* Inflates the provided layout */
        if (viewType == Constants.TYPE_SCORE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_score_item, parent, false);
            return new ScoreRecyclerViewAdapter.ScoreItemViewHolder(view);
        }

        throw new RuntimeException(TAG + ": Unkown view type item equals to " + viewType);
    }

    /* Called whenever a new item is added to list */
    @Override
    public void onBindViewHolder(ScoreItemViewHolder holder, int position) {
        /* Set score information (score value and username) */
        holder.scoreTextView.setText(ConvertTime.decodeTime(scores.get(position)));
        holder.playerNameEditText.setText(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) { return Constants.TYPE_SCORE_ITEM; }

    /* Item holder managed by RecyclerView */
    public class ScoreItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemLayout;
        private TextView scoreTextView;
        private EditText playerNameEditText;

        /* Setters and getters */
        public RelativeLayout getItemLayout() { return itemLayout; }
        public void setItemLayout(RelativeLayout itemLayout) { this.itemLayout = itemLayout; }

        public TextView getScoreTextView() { return scoreTextView; }
        public void setScoreTextView(TextView scoreTextView) { this.scoreTextView = scoreTextView; }

        public EditText getPlayerNameEditText() { return playerNameEditText; }
        public void setPlayerNameEditText(EditText playerNameEditText) { this.playerNameEditText = playerNameEditText; }

        /* Constructor */
        public ScoreItemViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.layout_score_item);
            scoreTextView = itemView.findViewById(R.id.score_text_view);
            playerNameEditText = itemView.findViewById(R.id.player_name_edit_text);
        }
    }

}
