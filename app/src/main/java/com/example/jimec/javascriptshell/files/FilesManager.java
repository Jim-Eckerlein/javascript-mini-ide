package com.example.jimec.javascriptshell.files;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

// TODO: implement read->string, write(string), rename()
public class FilesManager {
    
    private static final String TOP_DIR = "javascript_user_files";
    
    private final Context mContext;
    private final File mDir;
    private final HashMap<String, File> mFiles = new HashMap<>();
    
    public FilesManager(Context context) {
        mContext = context;
        
        mDir = mContext.getDir(TOP_DIR, Context.MODE_PRIVATE);
        
        // Populate file list:
        for (File file : mDir.listFiles()) {
            mFiles.put(file.getName(), file);
        }
    }
    
    public void createFile(String name) throws IOException {
        File newFile = new File(mDir, name);
        if (!newFile.createNewFile()) {
            throw new FileAlreadyExistsException();
        }
    }
    
    public void deleteFile(String name) throws IOException {
        File file = mFiles.get(name);
        if (file == null) {
            throw new NoSuchFileException();
        }
        if (!file.delete()) {
            throw new CannotDeleteFileException();
        }
    }
    
    public String[] listFiles() {
        String[] array = new String[mFiles.size()];
        int i = 0;
        for (String key : mFiles.keySet()) {
            array[i++] = key;
        }
        return array;
    }
    
    public class FileAlreadyExistsException extends IOException {
    }
    
    public class CannotDeleteFileException extends IOException {
    }
    
    public class NoSuchFileException extends IOException {
    }
    
}
