package com.example.jimec.javascriptshell.files;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Singleton class managing files in internal memory.
 * Since the file manager relies on a context object,
 * you need to call the static initialize function once.
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
    
    /**
     * Get the static files manager.
     * Throws if no call to initialize has occurred yet.
     *
     * @return Static files manager object.
     */
    public static FilesManager getInstance() {
        if (null == INSTANCE) {
            throw new IllegalStateException("Files manager has not been initialized yet");
        }
        return INSTANCE;
    }
    
    /**
     * Initialize the static files manager object with the given context.
     *
     * @param context Context of files manager. Usually the activity itself.
     */
    public static void initialize(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new FilesManager(context);
        }
    }
    
    /**
     * Create a new file.
     *
     * @param filename Name of new file.
     * @throws IOException If file cannot be created, e.g. if that file already exists.
     */
    public void create(String filename) throws IOException {
        File newFile = new File(mDir, filename);
        if (!newFile.createNewFile()) {
            throw new FileAlreadyExistsException(filename);
        }
        mFiles.put(filename, newFile);
    }
    
    /**
     * Delete a file.
     *
     * @param filename Name of file.
     * @throws IOException If file cannot be deleted, e.g. if that file does not exist.
     */
    public void delete(String filename) throws IOException {
        File file = mFiles.get(filename);
        if (file == null) {
            throw new NoSuchFileException(filename);
        }
        if (!file.delete()) {
            throw new CannotDeleteFileException(filename);
        }
        mFiles.remove(filename);
    }
    
    public void rename(String oldFilename, String newFilename) throws IOException {
        create(newFilename);
        write(newFilename, read(oldFilename));
        delete(oldFilename);
    }
    
    /**
     * List all file names without preceding path.
     *
     * @return String array containing all file names.
     */
    public String[] listFiles() {
        String[] array = new String[mFiles.size()];
        int i = 0;
        for (String key : mFiles.keySet()) {
            array[i++] = key;
        }
        return array;
    }
    
    /**
     * Read file content.
     * Preserves white-space.
     *
     * @param filename Name of file.
     * @return File content as string.
     * @throws IOException If file cannot be read.
     */
    public String read(String filename) throws IOException {
        File file = mFiles.get(filename);
        if (null == file) {
            throw new NoSuchFileException(filename);
        }
        StringBuilder buffer = new StringBuilder();
        FileInputStream stream = new FileInputStream(file);
        
        while (stream.available() > 0) {
            buffer.append((char) stream.read());
        }
    
        stream.close();
        return buffer.toString();
    }
    
    /**
     * Write file content.
     * Preserves white-space.
     * Replaces whole old file content.
     *
     * @param filename Name of file
     * @param content  Content to be written into file.
     * @throws IOException If file cannot be written.
     */
    public void write(String filename, String content) throws IOException {
        File file = mFiles.get(filename);
        if (null == file) {
            throw new NoSuchFileException(filename);
        }
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(content.getBytes());
        stream.close();
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
