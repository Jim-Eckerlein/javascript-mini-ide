package com.example.jimec.javascriptshell;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {
    
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    
    /**
     * Compare characters of string <code>a</code> at position
     * <code>start</code> to string <code>b</code>.
     * <p>
     * Return false is string `b` is too long as the comparison could succeed.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Start position in string a.
     * @return True if comparision succeeded.
     */
    public static boolean strcmp(String a, String b, int start) {
        if ((start + b.length()) > a.length()) {
            return false;
        }
        for (int i = 0; i < b.length(); i++) {
            if (a.charAt(start + i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Compare string `b` reversely character-wise to string `a` at position `start`.
     * Only as many characters are compared as string `b` is long.
     * Though the underlying comparison is done reversely, string be `b` is still given in the same
     * text direction as string `a`, since it's reversed by this function behind the scenes.
     * <p>
     * Return false is string `b` is too long as the comparison could succeed.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Position within first string to start comparison.
     * @return True if comparison succeeded.
     */
    public static boolean strbackcmp(String a, String b, int start) {
        if (b.length() > (start + 1) || (start >= a.length())) {
            return false;
        }
        for (int i = 0; i < b.length(); i++) {
            if (a.charAt(start - i) != b.charAt(b.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Reads a raw text file as is.
     * That means that line breaks are preserved and *not* automatically added.
     * '\r' are removed.
     *
     * @param ctx Context
     * @param resId Raw resource id
     * @return File content as string
     */
    public static String readTextFile(Context ctx, @RawRes int resId) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getResources().openRawResource(resId)));
        StringBuilder sb = new StringBuilder();
        try {
            int i = reader.read();
            for (; i != -1; i = reader.read()) {
                if ('\r' == i) {
                    continue;
                }
                sb.append((char) i);
            }
        } catch (IOException ignored) {
        }
        return sb.toString();
    }
    
    public static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
