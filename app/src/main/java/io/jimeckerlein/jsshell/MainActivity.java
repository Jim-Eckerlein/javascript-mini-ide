package io.jimeckerlein.jsshell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.security.InvalidParameterException;

import io.jimeckerlein.jsshell.files.FilesManager;

public class MainActivity extends AppCompatActivity {

    private static final String ON_BOARDED_PREF = "io.jimeckerlein.jsshell.ON_BOARDED_PREF";
    private ViewPager mPager;
    private TabManager mTabManager;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize files manager:
        FilesManager.initialize(this);

        // Toolbar:
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPager = (ViewPager) findViewById(R.id.pager);
        mTabManager = new TabManager(getSupportFragmentManager(), mPager, this);
        mPager.setAdapter(mTabManager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);
        tabLayout.addOnTabSelectedListener(mTabManager);

        mPager.setCurrentItem(TabManager.EDITOR_TAP_POSITION);
        mPager.post(() -> mTabManager.loadSession());

        // Eventually launch on-boarding activity:
        final Util.FinalRef<Boolean> onBoarded = new Util.FinalRef<>(getPreferences(MODE_PRIVATE).getBoolean(ON_BOARDED_PREF, false));
        if (!onBoarded.get()) {
            startActivity(new Intent(this, OnBoardingActivity.class));
            findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                if(onBoarded.get()) return;
                DiscoverView.showAll(this);
                getPreferences(MODE_PRIVATE).edit().putBoolean(ON_BOARDED_PREF, true).apply();
                onBoarded.set(true);
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        @MenuRes int res;

        switch (mPager.getCurrentItem()) {
            case TabManager.FILES_TAB_POSITION:
                res = R.menu.toolbar_files;
                break;
            case TabManager.EDITOR_TAP_POSITION:
                res = R.menu.toolbar_editor;
                break;
            case TabManager.RUN_TAB_POSITION:
                res = R.menu.toolbar_run;
                break;
            default:
                throw new InvalidParameterException("No such tab");
        }

        getMenuInflater().inflate(res, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mTabManager.backPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save file:
        mTabManager.saveEditorFile();

        // Save session:
        mTabManager.saveSession();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        @IdRes int id = item.getItemId();

        switch (id) {
            case R.id.action_format_code:
                mTabManager.editorFormat();
                return true;

            case R.id.action_clear_code:
                mTabManager.editorClear();
                return true;

            case R.id.action_about:
                // Start About activity:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;

            case R.id.action_replay_on_boarding:
                // Start On-Boarding activity:
                startActivity(new Intent(this, OnBoardingActivity.class));
                return true;

            case R.id.action_help:
                // Start Help activity:
                DiscoverView.showAll(this);
                return true;

            case R.id.action_share:
                // Share code:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTabManager.getEditorCode());
                sendIntent.setType("application/javascript");
                startActivity(sendIntent);
                return true;

            case R.id.action_delete_multiple_files:
                // Delete multiple files from file tab
                mTabManager.startMultipleFileDeletion();
                return true;

            default:
                return false;
        }
    }
}
