package io.jimeckerlein.jsshell;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button mNextButton;
    private Button mLetsGoButton;
    private static final int TAB_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return TAB_COUNT;
            }
        });

        TabLayout tabLayout = ((TabLayout) findViewById(R.id.tab_layout));
        tabLayout.setupWithViewPager(mViewPager, true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == TAB_COUNT - 1) {
                    // Last tab selected, show lets-go button:
                    mNextButton.setVisibility(View.GONE);
                    mLetsGoButton.setVisibility(View.VISIBLE);
                }
                else {
                    // Show skip button on every other tab:
                    mNextButton.setVisibility(View.VISIBLE);
                    mLetsGoButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mNextButton = (Button) findViewById(R.id.on_boarding_next);
        mNextButton.setOnClickListener(v -> mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1));

        mLetsGoButton = (Button) findViewById(R.id.on_boarding_lets_go);
        mLetsGoButton.setOnClickListener(v -> finish());
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String TAB_POSITION = "io.jimeckerlein.jsshell.OnBoardingActivity.tab_position";

        public static PlaceholderFragment newInstance(int position) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_on_boarding, container, false);
            int pos = getArguments().getInt(TAB_POSITION);
            ((TextView) rootView.findViewById(R.id.on_boarding_text)).setText(getResources().getStringArray(R.array.on_boarding_texts)[pos]);
            ((TextView) rootView.findViewById(R.id.on_boarding_headline)).setText(getResources().getStringArray(R.array.on_boarding_headlines)[pos]);
            @DrawableRes int id = 0;
            switch (pos) {
                case 0:
                    id = R.drawable.intro_robot;
                    break;
                case 1:
                    id = R.drawable.intro_code;
                    break;
                case 2:
                    id = R.drawable.intro_circuit;
                    break;
            }
            ((ImageView) rootView.findViewById(R.id.on_boarding_image)).setImageDrawable(ContextCompat.getDrawable(getContext(), id));
            return rootView;
        }
    }
}
