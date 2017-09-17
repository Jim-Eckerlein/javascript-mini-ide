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

}
