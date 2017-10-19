package io.jimeckerlein.jsshell;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DiscoverView extends FrameLayout {

    private static final List<DiscoverView> DISCOVER_VIEWS = new ArrayList<>();
    private String mPrefName;

    public DiscoverView(Context context) {
        super(context);
        init(null);
    }

    public DiscoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DiscoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_discover, this);
        TextView heading = findViewById(R.id.discover_heading);
        ListView description = findViewById(R.id.discover_description);

        description.setOnTouchListener((v, event) -> true);

        if(isInEditMode()) return;

        DISCOVER_VIEWS.add(this);
        SharedPreferences prefs = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
        if(null != attrs) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DiscoverView, 0, 0);
            try {
                heading.setText(a.getString(R.styleable.DiscoverView_heading));
                description.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_discover_description_item,
                        getContext().getResources().getStringArray(a.getResourceId(R.styleable.DiscoverView_description, R.array.discover_default_array))));
                mPrefName = "io.jimeckerlein.jsshell.DiscoverView_prefName_" + a.getString(R.styleable.DiscoverView_prefName) + "_viewed";
            } finally {
                a.recycle();
            }

            if(prefs.getBoolean(mPrefName, false)) {
                setVisibility(GONE);
            }
        }

        setOnTouchListener((v, event) -> true);

        // Ok Button
        String finalPrefName = mPrefName;
        findViewById(R.id.discover_ok).setOnClickListener(v -> {
            prefs.edit().putBoolean(finalPrefName, true).apply();
            setVisibility(GONE);
        });

        // Skip Button
        findViewById(R.id.discover_skip).setOnClickListener(v -> {
            for(DiscoverView view : DISCOVER_VIEWS) {
                prefs.edit().putBoolean(view.mPrefName, true).apply();
                view.setVisibility(GONE);
            }
            setVisibility(GONE);
        });
    }

    public static void resetSkipped(Context context) {
        for(DiscoverView view : DISCOVER_VIEWS) {
            ((Activity) context).getPreferences(Context.MODE_PRIVATE).edit().putBoolean(view.mPrefName, false).apply();
            view.setVisibility(VISIBLE);
        }
    }

}
