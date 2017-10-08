package com.example.jimec.javascriptshell.files;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.jimec.javascriptshell.R;

public class FileNameDialog {
    
    private FileNameDialog() {
    }
    
    public static void show(Context context, OnOkListener onOkListener, OnCancelListener onCancelListener) {
        final View view = View.inflate(context, R.layout.view_file_create_dialog, null);
        final FileNameEditText filenameInput = view.findViewById(R.id.file_create_name);
        final TextView errorAlreadyExists = view.findViewById(R.id.file_create_error_already_exists);
        final TextView errorInvalidCharacter = view.findViewById(R.id.file_create_error_invalid_character);
    
        filenameInput.setErrorListener(new FileNameEditText.ErrorListener() {
            @Override
            public void onFileNameIsOk() {
                errorAlreadyExists.setVisibility(View.INVISIBLE);
                errorInvalidCharacter.setVisibility(View.INVISIBLE);
            }
        
            @Override
            public void onFileNameAlreadyExists() {
                errorAlreadyExists.setVisibility(View.VISIBLE);
                errorInvalidCharacter.setVisibility(View.INVISIBLE);
            }
        
            @Override
            public void onFileNameContainsInvalidCharacter() {
                errorAlreadyExists.setVisibility(View.INVISIBLE);
                errorInvalidCharacter.setVisibility(View.VISIBLE);
            }
        });
    
        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.files_create_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    if (errorAlreadyExists.getVisibility() == View.INVISIBLE && errorInvalidCharacter.getVisibility() == View.INVISIBLE
                            && filenameInput.getText().length() > 0) {
                        // Ok clicked => create file
                        onOkListener.onOk(filenameInput.getText().toString() + context.getString(R.string.files_extension));
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    onCancelListener.onCancel();
                    dialog.cancel();
                })
                .show();
    }
    
    public interface OnOkListener {
        void onOk(String filename);
    }
    
    public interface OnCancelListener {
        void onCancel();
    }
    
}
