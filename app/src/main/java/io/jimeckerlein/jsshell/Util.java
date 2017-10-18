package io.jimeckerlein.jsshell;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RawRes;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {
    
    public static final int ANIMATION_DURATION = 200;
    public static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();
    
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    
    /**
     * Reads a raw text file as is.
     * That means that line breaks are preserved and *not* automatically added.
     * '\r' are removed.
     *
     * @param ctx   Context
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
