package marius.scarlat.com.puzzlegame;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Method was invoked!");

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.container);
        setupViewPager();
        setViewPager(Constants.MENU_FRAGMENT_POSITION);
    }

    private void setupViewPager() {
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
                        // TODO: get scores
                        break;

                    case Constants.GAME_FRAGMENT_POSITION:
                        Log.d(TAG, "onPageSelected: GAME_FRAGMENT_POSITION");
                        ((GameFragment) fragmentAdapter.getItem(position)).startGame();
                        break;

                    case Constants.SCORE_FRAGMENT_POSITION:
                        Log.d(TAG, "onPageSelected: SCORE_FRAGMENT_POSITION");
                        // TODO: get scores ; update user score
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
