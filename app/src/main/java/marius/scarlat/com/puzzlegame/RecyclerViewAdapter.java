package marius.scarlat.com.puzzlegame;


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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private ArrayList<Integer> numbers = new ArrayList<>();

    public Context getContext() { return  context; }
    public ArrayList<Integer> getNumbers() { return  numbers; }

    public void setContext(Context context) { this.context = context; }
    public void setNumbers(ArrayList<Integer> numbers) { this.numbers = numbers; }

    public RecyclerViewAdapter(Context context, ArrayList<Integer> numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    /* Creates a new ViewHolder which the RecycleView can reuse */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Method was invoked!");

        /* Inflates the provided layout */
        if (viewType == Constants.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_item, parent, false);
            return new ViewHolder(view);
        }

        throw new RuntimeException("Unkown view type item equals to " + viewType);
    }

    /* Called whenever a new item is added to list */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        return Constants.TYPE_ITEM;
    }

    public boolean isGameAvailable() {
        return ((MainActivity) context).checkGameAvailable();
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

        ((MainActivity) context).updateGame(numbers);
    }

    /* Item holder managed by RecyclerView */
    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemLayout;
        private TextView numberTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.layout_item);
            numberTextView = itemView.findViewById(R.id.number_text_view);
        }
    }
}
