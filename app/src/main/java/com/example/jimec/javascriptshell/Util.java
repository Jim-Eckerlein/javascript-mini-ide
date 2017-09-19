package com.example.jimec.javascriptshell;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

    private Util() {
    }

    static String readTextFile(Context ctx, int resId) {
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
     * Compare characters of string <code>a</code> at position
     * <code>start</code> to string <code>b</code>.
     *
     * Return false is string `b` is too long as the comparison could succeed.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Start position in string a.
     * @return True if comparision succeeded.
     */
    static boolean strcmp(String a, String b, int start) {
        return ((start + b.length()) <= a.length())
                && a.substring(start, start + b.length()).equals(b.substring(0, b.length()));
    }

    /**
     * Compare string `b` reversely character-wise to string `a` at position `start`.
     * Only as many characters are compared as string `b` is long.
     * Though the underlying comparison is done reversely, string be `b` is still given in the same
     * text direction as string `a`, since it's reversed by this function behind the scenes.
     *
     * Return false is string `b` is too long as the comparison could succeed.
     *
     * @param a     First string.
     * @param b     Second string.
     * @param start Position within first string to start comparison.
     * @return True if comparison succeeded.
     */
    static boolean strbackcmp(String a, String b, int start) {
        return (b.length() <= (start + 1))
                && (start < a.length())
                && new StringBuilder(a.substring(0, start + 1)).reverse().substring(0, b.length())
                .equals(new StringBuilder(b).reverse().toString());
    }

    static boolean containsString(String[] array, String src, int srcPosition) {
        for (String anArray : array) {
            if (strcmp(src, anArray, srcPosition)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Should be true:  " + strcmp("hello", "llo", 2));
        System.out.println("Should be false: " + strcmp("hello", "llo", 0));

        System.out.println("Should be true:  " + strbackcmp("hello", "llo", 4));
        System.out.println("Should be false: " + strbackcmp("hello", "llo", 2));
        System.out.println("Should be false: " + strbackcmp("hello", "lo", 3));
    }

}
