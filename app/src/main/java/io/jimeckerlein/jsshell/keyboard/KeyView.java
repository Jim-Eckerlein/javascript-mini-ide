package io.jimeckerlein.jsshell.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.jimeckerlein.jsshell.R;

public class KeyView extends LinearLayout implements KeyboardKeyConnection {
    
    private KeyIndicatorView mKeyIndicatorView;
    private TextView mPrimary;
    private TextView mSecondary;
    private KeyboardView mKeyboard;
    
    public KeyView(Context context) {
        super(context);
        init(null);
    }
    
    public KeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    public KeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    
    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_key, this);
        mPrimary = findViewById(R.id.primary);
        mSecondary = findViewById(R.id.secondary);

        if(null != attrs) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.KeyView, 0, 0);
            try {
                setPrimaryText(a.getString(R.styleable.KeyView_primary));
                setSecondaryText(a.getString(R.styleable.KeyView_secondary));
            } finally {
                a.recycle();
            }
        }

        setOnTouchListener(new TouchListener());
    }

    public String getPrimaryText() {
        return mPrimary.getText().toString();
    }

    public void setPrimaryText(String text) {
        mPrimary.setText(text);
        invalidate();
        requestLayout();
    }

    public String getSecondaryText() {
        return mSecondary.getText().toString();
    }

    public void setSecondaryText(String text) {
        mSecondary.setText(text);
        invalidate();
        requestLayout();
    }

    public void writePrimaryText() {
        mKeyboard.write(getPrimaryText());
    }

    public void writeSecondaryText() {
        mKeyboard.write(getSecondaryText());
    }

    @Override
    public void setKeyboard(KeyboardView keyboard) {
        mKeyboard = keyboard;
    }
    
    public void setKeyIndicatorView(KeyIndicatorView keyIndicatorView) {
        mKeyIndicatorView = keyIndicatorView;
    }
    
    private class TouchListener extends TapListener {
    
        @Override
        public void onDown() {
            mKeyIndicatorView.show(KeyView.this, getPrimaryText());
        }
    
        @Override
        public void onUp() {
            mKeyIndicatorView.hide();
        }
    
        @Override
        public void onTap() {
            writePrimaryText();
        }

        @Override
        public void onLongTap() {
            if (getSecondaryText() != null && getSecondaryText().length() > 0) {
                writeSecondaryText();
            }
            else {
                writePrimaryText();
            }
        }
    }

}
