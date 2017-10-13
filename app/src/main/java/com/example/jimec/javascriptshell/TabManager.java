package com.example.jimec.javascriptshell;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RawRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.security.InvalidParameterException;

public class TabManager extends FragmentPagerAdapter implements TabLayout.OnTabSelectedListener {
    
    public static final int FILES_TAB_POSITION = 0;
    public static final int EDITOR_TAP_POSITION = 1;
    public static final int RUN_TAB_POSITION = 2;
    public static final int NUM_TABS = 3;
    
    private final ViewPager mViewPager;
    private final FilesTab mFilesTab = new FilesTab();
    private final EditorTab mEditorTab = new EditorTab();
    private final RunTab mRunTab = new RunTab();
    private final Activity mActivity;
    
    public TabManager(FragmentManager fm, ViewPager viewPager, Activity activity) {
        super(fm);
        mViewPager = viewPager;
        mActivity = activity;
        mEditorTab.setPager(viewPager);
        mFilesTab.setTabManager(this);
    }
    
    public void loadExample(String exampleName, @RawRes int id) {
        mEditorTab.loadExample(mActivity.getString(R.string.files_example_title, exampleName), id);
        mViewPager.setCurrentItem(EDITOR_TAP_POSITION);
    }
    
    public void loadFile(String filename, String content) {
        mEditorTab.loadFile(filename, content);
        mViewPager.setCurrentItem(EDITOR_TAP_POSITION);
    }
    
    public void startMultipleFileDeletion() {
        mFilesTab.startMultipleFileDeletion();
    }
    
    public void editorFormat() {
        mEditorTab.getEditor().format();
    }
    
    public void editorClear() {
        mEditorTab.getEditor().clear();
    }
    
    public void editorUndo() {
        mEditorTab.getEditor().undo();
    }
    
    public String getEditorCode() {
        return mEditorTab.getEditor().toString();
    }
    
    public void saveEditorFile() {
        if (!mEditorTab.currentFileIsExample() && mEditorTab.hasOpenedFile()) {
            mFilesTab.writeFile(mEditorTab.getCurrentFileName(), getEditorCode());
        }
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabManager.RUN_TAB_POSITION:
                mRunTab.clearOutput();
                mRunTab.launchV8(mEditorTab.getEditor().toString());
        }
        mActivity.invalidateOptionsMenu();
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            
            // Break execution:
            case TabManager.RUN_TAB_POSITION:
                mRunTab.stopV8();
                break;
            
            // Quit editor -> save file:
            case TabManager.EDITOR_TAP_POSITION:
                saveEditorFile();
        }
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
    
    @Override
    public int getCount() {
        return NUM_TABS;
    }
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FILES_TAB_POSITION:
                return mFilesTab;
            case EDITOR_TAP_POSITION:
                return mEditorTab;
            case RUN_TAB_POSITION:
                return mRunTab;
            default:
                throw new InvalidParameterException();
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FILES_TAB_POSITION:
                return mActivity.getString(R.string.files_tab_title);
            case EDITOR_TAP_POSITION:
                return mActivity.getString(R.string.editor_tab_title);
            case RUN_TAB_POSITION:
                return mActivity.getString(R.string.run_tab_title);
            default:
                throw new InvalidParameterException();
        }
    }
    
    public void loadSession() {
        String sessionFile = mActivity.getPreferences(Context.MODE_PRIVATE).getString(mActivity.getString(R.string.pref_session_file), null);
        if (sessionFile != null) {
            mFilesTab.openFile(sessionFile);
        }
        else {
            mFilesTab.openDefaultExample();
        }
    }
    
    public void saveSession() {
        SharedPreferences.Editor edit = mActivity.getPreferences(Context.MODE_PRIVATE).edit();
        if (!mEditorTab.currentFileIsExample()) {
            edit.putString(mActivity.getString(R.string.pref_session_file), mEditorTab.getCurrentFileName());
        }
        else {
            edit.putString(mActivity.getString(R.string.pref_session_file), null);
        }
        edit.apply();
    }
    
    /**
     * Try to handle a back-button key press.
     *
     * @return True if successfully processed, otherwise, false, which usually means app quit.
     */
    public boolean backPressed() {
        int currentTab = mViewPager.getCurrentItem();
        
        // Back key pressed inside file tab:
        if (FILES_TAB_POSITION == currentTab) {
            
            // Close file settings:
            if (mFilesTab.fileSettingsOpened()) {
                mFilesTab.hideFileSettings();
            }
            
            // End multiple file deletion:
            else if (mFilesTab.activeMultipleFileDeletion()) {
                mFilesTab.endMultipleFileDeletion();
            }
            
            // Return to editor tab:
            else {
                mViewPager.setCurrentItem(EDITOR_TAP_POSITION);
            }
            
        }
        
        // Back key pressed inside editor tab:
        else if (EDITOR_TAP_POSITION == currentTab) {
            
            // Hide keyboard:
            if (mEditorTab.isKeyboardVisible()) {
                mEditorTab.hideKeyboard();
            }

            // Return to files tab:
            else {
                mViewPager.setCurrentItem(FILES_TAB_POSITION);
            }
            
        }
        
        // Back key pressed inside run tab:
        else if (RUN_TAB_POSITION == currentTab) {
            
            // Return to editor tap:
            mRunTab.stopV8();
            mViewPager.setCurrentItem(EDITOR_TAP_POSITION);
            
        }
        
        else {
            return false;
        }
        
        return true;
    }
}
