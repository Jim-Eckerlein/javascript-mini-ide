package com.example.jimec.javascriptshell;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

    private Util() {
    }

    public static String readTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }

    /**
     * Compares characters of string <code>a</code> at position
     * <code>start</code> to string <code>b</code>.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Start position in string a.
     * @return True if comparision succeeded.
     */
    public static boolean strncmp(String a, String b, int start) {
        return strncmp(a, b, start, b.length());
    }

    /**
     * Compares <code>count</code> characters of string <code>a</code> at position
     * <code>start</code> to the first <code>count</code> characters of string <code>b</code>.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Start position in string a.
     * @param count Count of characters.
     * @return True if comparision succeeded.
     */
    public static boolean strncmp(String a, String b, int start, int count) {
        return a.substring(start, start + count).equals(b.substring(0, count));
    }

    public static void main(String[] args) {
        System.out.println(strncmp("hello", "llo", 2));
        System.out.println(strncmp("hello", "llo", 0));
    }

}
