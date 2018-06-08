package marius.scarlat.com.puzzlegame.views_adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import marius.scarlat.com.puzzlegame.game_design.GameItemListener;
import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.views.GameFragment;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.GameItemViewHolder>{

    private static final String TAG = "GameRecyclerViewAdapter";

    private GameFragment fragment;
    private Context context;
    private ArrayList<Integer> numbers = new ArrayList<>();

    /* Constructor */
    public GameRecyclerViewAdapter(Context context, GameFragment fragment, ArrayList<Integer> numbers) {
        this.context = context;
        this.fragment = fragment;
        this.numbers = numbers;
    }

    /* Setters and Getters */
    public Context getContext() { return  context; }
    public ArrayList<Integer> getNumbers() { return  numbers; }

    public void setContext(Context context) { this.context = context; }
    public void setNumbers(ArrayList<Integer> numbers) { this.numbers = numbers; }

    /* Creates a new GameItemViewHolder which the RecycleView can reuse */
    @Override
    public GameItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Method was invoked!");

        /* Inflates the provided layout */
        if (viewType == Constants.TYPE_GAME_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_game_item, parent, false);
            return new GameItemViewHolder(view);
        }

        throw new RuntimeException(TAG + ": Unkown view type item equals to " + viewType);
    }

    /* Called whenever a new item is added to list */
    @Override
    public void onBindViewHolder(GameItemViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: Method was invoked!");

        /* Set numbers values */
        String value = String.valueOf(numbers.get(position));
        holder.numberTextView.setText(value);
        holder.numberTextView.setTag(value);

        /* Initialize views and add listeners */
        new GameItemListener(this, holder.numberTextView).addListeners();
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_GAME_ITEM;
    }

    public boolean isGameAvailable() {
        return fragment.checkGameAvailable();
    }

    public void updateDataLogic(int viewValue, int dragValue) {
        int indexViewVal = numbers.indexOf(viewValue);
        int indexDragVal = numbers.indexOf(dragValue);

        Log.d(TAG, "updateDataLogic: Before " + numbers.toString());

        /* Rearrange the new list */
        if (indexViewVal < numbers.size() && indexDragVal < numbers.size()) {
            if (indexViewVal < indexDragVal) {
                for (int i = indexDragVal; i > indexViewVal; i--) {
                    Collections.swap(numbers, i, i - 1);
                    Log.d(TAG, "Step" + i + ": " + numbers);
                }
            } else {
                for (int i = indexDragVal; i < indexViewVal; i++) {
                    Collections.swap(numbers, i, i + 1);
                    Log.d(TAG, "Step" + i + ": " + numbers);
                }
            }
            /* Notify changes */
            notifyItemMoved(indexDragVal, indexViewVal);
        }
        Log.d(TAG, "updateDataLogic: After = " + numbers);

        fragment.updateGame(numbers);
    }

    /* Item holder managed by RecyclerView */
    public class GameItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemLayout;
        private TextView numberTextView;

        /* Getters and setters */
        public RelativeLayout getItemLayout() { return itemLayout; }
        public void setItemLayout(RelativeLayout itemLayout) { this.itemLayout = itemLayout; }

        public TextView getNumberTextView() { return numberTextView; }
        public void setNumberTextView(TextView numberTextView) { this.numberTextView = numberTextView; }

        /* Constructor */
        public GameItemViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.layout_game_item);
            numberTextView = itemView.findViewById(R.id.number_text_view);
        }
    }
}
