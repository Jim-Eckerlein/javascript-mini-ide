package com.example.jimec.javascriptshell.files;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jimec.javascriptshell.FilesTab;
import com.example.jimec.javascriptshell.R;


/**
 * Floating action button click listener, opens dialog to create a new file
 */
public abstract class CreateFileDialogFab implements FloatingActionButton.OnClickListener {
    
    public static final int FILE_NAME_NO_ERROR = 0;
    public static final int FILE_NAME_ERROR_ALREADY_EXISTS = 1;
    public static final int FILE_NAME_ERROR_INVALID_CHARACTER = 2;
    private final Context mContext;
    private final FilesManager mFilesManager;
    private final FilesTab mFilesTab;
    private final Activity mActivity;
    
    public CreateFileDialogFab(Context context, FilesManager filesManager, FilesTab filesTab, Activity activity) {
        mContext = context;
        mFilesManager = filesManager;
        mFilesTab = filesTab;
        mActivity = activity;
    }
    
    /**
     * Checks for valid file name.
     * Appends the .js extension.
     *
     * @param filename Name of file to be created
     */
    public int isValidFilename(String filename) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
        
        builder.setView(view);
        builder.setTitle(R.string.files_create_title);
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ok clicked => create file
                if (errorAlreadyExists.getVisibility() != View.INVISIBLE
                        || errorInvalidCharacter.getVisibility() != View.INVISIBLE) {
                    // Current file name is erroneous => ignore
                    return;
                }
                onOk(filenameInput.getText().toString());
            }
        });
        
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Dialog closed => close soft keyboard
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    public abstract void onOk(String filename);
    
}
