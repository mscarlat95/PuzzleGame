package marius.scarlat.com.puzzlegame.game_design;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.views_adapters.GameRecyclerViewAdapter;

public class GameItemListener {
    private static final String TAG = "GameItemListener";

    /* In order to update recyclerview on data changed */
    private GameRecyclerViewAdapter adapter;
    private View view;

    public GameItemListener(GameRecyclerViewAdapter adapter, View view) {
        this.adapter = adapter;
        this.view = view;
    }

    public void addListeners() {
        Log.d(TAG, "addListeners: Method was invoked!");
        
        /* Add drag listener */
        view.setOnDragListener(new ItemDragListener());

        /* Add touch listener */
        view.setOnTouchListener(new ItemTouchListener());
    }

    private class ItemTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(TAG, "onTouch: Start dragging ...");

            if (!adapter.isGameAvailable()) {
                Log.d(TAG, "onTouch: Game is finished. Cannot perform drag and drop");
                return false;
            }

            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {

                /* Create a new ClipData.Item from the View object's tag */
                ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

                /*
                    Create a new ClipData using the tag as a label, the plain text MIME type, and
                    the already-created item. This will create a new ClipDescription object within
                    the ClipData, and set its MIME type entry to "text/plain"
                 */
                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData dragData = new ClipData(
                        (CharSequence)view.getTag(),
                        mimeTypes,
                        item);

                /* Instantiate the drag shadow builder */
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);

                /* Set moved item background */
                view.setBackground(ContextCompat.getDrawable(adapter.getContext(), R.drawable.custom_moved_item));

                /* Start dragging */
                view.startDrag(dragData,    /* Data to be dragged */
                            shadow,         /* Drag shadow */
                            view,           /* Local data about the drag and drop operation */
                            0);             /* No flags */

                return true;
            }

            return false;
        }
    }


    private class ItemDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch(action) {
                /* Start of a drag and drop operation */
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED");
                    /* Determines if this View can accept the dragged data */
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

                /* Drag point has entered the bounding box of the View. */
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED");
                    /* Set item background */
                    v.setBackgroundColor(Color.TRANSPARENT);
                    /* Return true; the return value is ignored. */
                    return true;

                /* Drag shadow is still within the View object's bounding box */
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION");
                    /* Return true; the return value is ignored. */
                    return true;

                /* Drag shadow outside the bounding box of the View */
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED");
                    /* Set item background */
                    v.setBackground(ContextCompat.getDrawable(adapter.getContext(), R.drawable.custom_item));
                    /* Returns true; the return value is ignored */
                    return true;

                /*
                    User has released the drag shadow, and the drag
                    point is within the bounding box of the View.
                */
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "onDrag: ACTION_DROP");

                    /* Set item background */
                    v.setBackground(ContextCompat.getDrawable(adapter.getContext(), R.drawable.custom_item));

                    /* Obtain data from both the dragged view and the destination view */
                    int dragVal = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                    int viewVal = Integer.parseInt(((TextView) v).getText().toString());

                    /* Arrange list displayed in recyclerview */
                    adapter.updateDataLogic(viewVal, dragVal);

                    /* Returns true. DragEvent.getResult() will return true. */
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED");
                    /* Set item background */
                    v.setBackground(ContextCompat.getDrawable(adapter.getContext(), R.drawable.custom_item));

                    /* Check the drop result */
                    if (event.getResult()) {
                        Log.d(TAG, "onDrag: The drop was handled!");
                    } else {
                        Log.d(TAG, "onDrag: The drop didn't work!");
                    }
                    /* Returns true; the return value is ignored */
                    return true;

                default:
                    Log.d(TAG, "onDrag: Unkown Event");
                    throw new RuntimeException("Unkown action event equals to " + action);
            }
        }
    }
}
