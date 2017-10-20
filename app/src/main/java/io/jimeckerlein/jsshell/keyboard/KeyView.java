package io.jimeckerlein.jsshell.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.jimeckerlein.jsshell.R;

public class KeyView extends LinearLayout implements KeyboardKeyConnection {

    private KeyIndicatorView mKeyIndicatorView;
    private TextView mText;
    private String mPrimary, mSecondary;
    private SpannableString mSpannableString;
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
        mText = findViewById(R.id.key_text);

        if (null != attrs) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.KeyView, 0, 0);
            try {
                setText(a.getString(R.styleable.KeyView_primary), a.getString(R.styleable.KeyView_secondary));
            } finally {
                a.recycle();
            }
        }

        setOnTouchListener(new TouchListener());
    }

    /**
     * Sets the text of this key
     *
     * @param primary   Primary text, mustn't be null
     * @param secondary Secondary text, null means no secondary text
     */
    public void setText(String primary, String secondary) {
        mPrimary = primary;
        mSecondary = secondary;
        if (secondary != null) {
            // Key has secondary text
            primary = primary + ' ';
            mSpannableString = new SpannableString(primary + secondary);
            mSpannableString.setSpan(new SuperscriptSpan(), primary.length(), primary.length() + secondary.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new RelativeSizeSpan(0.6f), primary.length(), primary.length() + secondary.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mText.setText(mSpannableString);
        }
        else {
            // Key does not have secondary text
            mSpannableString = new SpannableString(primary);
            mText.setText(primary);
        }
    }

    public String getPrimaryText() {
        return mPrimary;
    }

    public String getSecondaryText() {
        return mSecondary;
    }

    public SpannableString getText() {
        return mSpannableString;
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
            mKeyIndicatorView.show(KeyView.this, getText());
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
