package io.jim_eckerlein.jsshell.files;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import io.jim_eckerlein.jsshell.R;

public class FileNameDialog {
    
    private FileNameDialog() {
    }
    
    public static void show(Context context, Mode mode, OnOkListener onOkListener, Runnable onCancelListener) {
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
                .setTitle(mode == Mode.CREATE ? R.string.files_create_title : R.string.files_rename_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    if (errorAlreadyExists.getVisibility() == View.INVISIBLE && errorInvalidCharacter.getVisibility() == View.INVISIBLE
                            && filenameInput.getText().length() > 0) {
                        // Ok clicked => create file
                        onOkListener.run(filenameInput.getText().toString() + context.getString(R.string.files_extension));
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    onCancelListener.run();
                    dialog.cancel();
                })
                .show();
    }
    
    public enum Mode {
        CREATE, RENAME
    }
    
    public interface OnOkListener {
        void run(String filename);
    }
    
}
