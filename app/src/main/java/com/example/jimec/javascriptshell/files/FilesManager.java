package com.example.jimec.javascriptshell.files;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
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
            throw new FileAlreadyExistsException(name);
        }
        mFiles.put(name, newFile);
    }
    
    public void deleteFile(String name) throws IOException {
        File file = mFiles.get(name);
        if (file == null) {
            throw new NoSuchFileException(name);
        }
        if (!file.delete()) {
            throw new CannotDeleteFileException(name);
        }
        mFiles.remove(name);
    }
    
    public String[] listFiles() {
        String[] array = new String[mFiles.size()];
        int i = 0;
        for (String key : mFiles.keySet()) {
            array[i++] = key;
        }
        return array;
    }
    
    public String readFile(String name) throws IOException {
        File file = mFiles.get(name);
        if (null == file) {
            throw new NoSuchFileException(name);
        }
        StringBuilder buffer = new StringBuilder();
        FileInputStream stream = new FileInputStream(file);
        
        while (stream.available() > 0) {
            buffer.append((char) stream.read());
        }
        
        return buffer.toString();
    }
    
    public class FileAlreadyExistsException extends IOException {
        FileAlreadyExistsException(String filename) {
            super("File already exists: " + filename);
        }
    }
    
    public class CannotDeleteFileException extends IOException {
        CannotDeleteFileException(String filename) {
            super("Cannot delete file: " + filename);
        }
    }
    
    public class NoSuchFileException extends IOException {
        NoSuchFileException(String filename) {
            super("No such file: " + filename);
        }
    }
    
}
