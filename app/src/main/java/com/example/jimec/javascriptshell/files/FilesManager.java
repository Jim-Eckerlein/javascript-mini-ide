package com.example.jimec.javascriptshell.files;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Singleton class managing files in internal memory.
 * Since the file manager relies on a context object,
 * you need to call the static initialize function once.
 * <p>
 * TODO: implement read->string, write(string), rename()
 */
public class FilesManager {
    
    private static final String TOP_DIR = "javascript_user_files";
    private static FilesManager INSTANCE;
    private final File mDir;
    private final HashMap<String, File> mFiles = new HashMap<>();
    
    private FilesManager(Context context) {
        mDir = context.getDir(TOP_DIR, Context.MODE_PRIVATE);
        
        // Populate file list:
        for (File file : mDir.listFiles()) {
            mFiles.put(file.getName(), file);
        }
    }
    
    public static FilesManager getInstance() {
        if (null == INSTANCE) {
            throw new IllegalStateException("Files manager has not been initialized yet");
        }
        return INSTANCE;
    }
    
    public static void initialize(Context context) {
        if (null != INSTANCE) {
            throw new IllegalStateException("Files manager has been already initialized");
        }
        INSTANCE = new FilesManager(context);
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
    
        stream.close();
        return buffer.toString();
    }
    
    public class FileAlreadyExistsException extends IOException {
        private static final long serialVersionUID = 6207807659709679937L;
    
        FileAlreadyExistsException(String filename) {
            super("File already exists: " + filename);
        }
    }
    
    public class CannotDeleteFileException extends IOException {
        private static final long serialVersionUID = -6641564767073976105L;
    
        CannotDeleteFileException(String filename) {
            super("Cannot delete file: " + filename);
        }
    }
    
    public class NoSuchFileException extends IOException {
        private static final long serialVersionUID = 8512223856488578532L;
    
        NoSuchFileException(String filename) {
            super("No such file: " + filename);
        }
    }
    
}
