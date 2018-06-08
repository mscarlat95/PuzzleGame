package marius.scarlat.com.puzzlegame.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import marius.scarlat.com.puzzlegame.R;
import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.storage.SharedPref;
import marius.scarlat.com.puzzlegame.views_adapters.FragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private Toolbar toolbar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Method was invoked!");

        setContentView(R.layout.activity_main);

        /* Setup toolbar */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Setup View Pager */
        viewPager = findViewById(R.id.container);
        setupViewPager();
        setViewPager(Constants.MENU_FRAGMENT_POSITION);

        /* Setup shared preferences storage */
        SharedPref.setup(this);
    }


    private void initToolbar(CharSequence title, boolean displayHomeAsUp, boolean displayShowHome) {
        Log.d(TAG, "Initializing Toolbar");

        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUp);
        getSupportActionBar().setDisplayShowHomeEnabled(displayShowHome);

        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(Constants.MENU_FRAGMENT_POSITION);
            }
        });
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: Method was invoked!");

        final FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(new MenuFragment(), Constants.FRAGMENT_TITLES[Constants.MENU_FRAGMENT_POSITION]);
        fragmentAdapter.addFragment(new GameFragment(), Constants.FRAGMENT_TITLES[Constants.GAME_FRAGMENT_POSITION]);
        fragmentAdapter.addFragment(new HighScoreFragment(), Constants.FRAGMENT_TITLES[Constants.SCORE_FRAGMENT_POSITION]);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case Constants.MENU_FRAGMENT_POSITION:
                        Log.d(TAG, "onPageSelected: MENU_FRAGMENT_POSITION");
                        initToolbar (Constants.FRAGMENT_TITLES[position], false, true);
                        ((MenuFragment) fragmentAdapter.getItem(position)).displayScore();
                        break;

                    case Constants.GAME_FRAGMENT_POSITION:
                        Log.d(TAG, "onPageSelected: GAME_FRAGMENT_POSITION");
                        initToolbar (Constants.FRAGMENT_TITLES[position], true, true);
                        ((GameFragment) fragmentAdapter.getItem(position)).startGame();
                        break;

                    case Constants.SCORE_FRAGMENT_POSITION:
                        Log.d(TAG, "onPageSelected: SCORE_FRAGMENT_POSITION");
                        initToolbar (Constants.FRAGMENT_TITLES[position], true, true);
                        ((HighScoreFragment) fragmentAdapter.getItem(position)).displayScore();
                        break;

                    default:
                        throw new RuntimeException("onPageSelected: Received unkown position " + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }


    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
