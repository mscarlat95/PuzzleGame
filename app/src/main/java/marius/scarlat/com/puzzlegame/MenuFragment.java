package marius.scarlat.com.puzzlegame;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    /* Android Views */
    private RecyclerView recyclerView;
    private TextView infoTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Method was invoked!");

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        /* Initialize android views */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setViewPager(1);
            }
        });

        /* TODO: add scores */
        recyclerView = view.findViewById(R.id.recycler_view);

        infoTextView = view.findViewById(R.id.info_text_view);
        infoTextView.setText(R.string.start_game_info);

        return view;
    }
}
