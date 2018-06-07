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
        setViewPager(0);
    }

    private void setupViewPager() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(new MenuFragment(), "Menu Fragment");
        fragmentAdapter.addFragment(new GameFragment(), "Game Fragment");

//        fragmentAdapter.addFragment(new ScoreFragment(), "Score Fragment");

        viewPager.setAdapter(fragmentAdapter);
    }


    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
