package com.example.jimec.javascriptshell.files;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;


/**
 * Floating action button click listener, opens dialog to create a new file.
 * Calls onOk() with chosen file and with appended .js extension.
 * Ensures that filename is valid.
 */
public abstract class CreateFileDialogFab implements FloatingActionButton.OnClickListener {
    
    public static final int FILE_NAME_NO_ERROR = 0;
    public static final int FILE_NAME_ERROR_ALREADY_EXISTS = 1;
    public static final int FILE_NAME_ERROR_INVALID_CHARACTER = 2;
    private final Context mContext;
    private final FilesManager mFilesManager;
    private final Activity mActivity;
    
    public CreateFileDialogFab(Context context, FilesManager filesManager, Activity activity) {
        mContext = context;
        mFilesManager = filesManager;
        mActivity = activity;
    }
    
    /**
     * Checks for valid file name.
     * Appends the .js extension.
     *
     * @param filename Name of file to be created
     */
    private int isValidFilename(String filename) {
        if (!filename.matches("[a-zA-Z0-9 _-]*")) {
            return FILE_NAME_ERROR_INVALID_CHARACTER;
        }
        filename = filename + mContext.getString(R.string.files_extension);
        for (String existingFilename : mFilesManager.listFiles()) {
            if (existingFilename.equals(filename)) {
                return FILE_NAME_ERROR_ALREADY_EXISTS;
            }
        }
        return FILE_NAME_NO_ERROR;
    }
    
    @Override
    public void onClick(View v) {
        final View view = View.inflate(mContext, R.layout.view_file_create_dialog, null);
        final EditText filenameInput = view.findViewById(R.id.file_create_name);
        final TextView errorAlreadyExists = view.findViewById(R.id.file_create_error_already_exists);
        final TextView errorInvalidCharacter = view.findViewById(R.id.file_create_error_invalid_character);
        
        filenameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void afterTextChanged(Editable text) {
                switch (isValidFilename(text.toString())) {
                    case FILE_NAME_NO_ERROR:
                        errorAlreadyExists.setVisibility(View.INVISIBLE);
                        errorInvalidCharacter.setVisibility(View.INVISIBLE);
                        break;
                    case FILE_NAME_ERROR_ALREADY_EXISTS:
                        errorAlreadyExists.setVisibility(View.VISIBLE);
                        errorInvalidCharacter.setVisibility(View.INVISIBLE);
                        break;
                    case FILE_NAME_ERROR_INVALID_CHARACTER:
                        errorAlreadyExists.setVisibility(View.INVISIBLE);
                        errorInvalidCharacter.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    
        new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle(R.string.files_create_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    if (errorAlreadyExists.getVisibility() == View.INVISIBLE && errorInvalidCharacter.getVisibility() == View.INVISIBLE
                            && filenameInput.getText().length() > 0) {
                        // Ok clicked => create file
                        onOk(filenameInput.getText().toString() + mContext.getString(R.string.files_extension));
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .setOnDismissListener(dialog -> {
                    // Dialog closed => close soft keyboard
                    mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                })
                .show();
    }
    
    public abstract void onOk(String filename);
    
}
