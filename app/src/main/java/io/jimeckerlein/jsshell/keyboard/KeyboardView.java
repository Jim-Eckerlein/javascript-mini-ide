package io.jimeckerlein.jsshell.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.jimeckerlein.jsshell.R;
import io.jimeckerlein.jsshell.editor.CodeEditorView;

public class KeyboardView extends FrameLayout {
    
    public static float ALPHA_INACTIVE = 0.4f;
    public static float ALPHA_ACTIVE = 0.8f;
    private ShiftKeyView mShiftKey;
    private CodeEditorView mEditor;
    private KeyIndicatorView mKeyIndicatorView;
    private Runnable mOnHideKeyboardListener;
    
    public KeyboardView(Context context) {
        super(context);
        init();
    }
    
    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_keyboard, this);
    
        mKeyIndicatorView = findViewById(R.id.key_indicator);
    
        initKeys(this);
    
        // Hide keyboard animation:
        findViewById(R.id.hide_keyboard).setOnClickListener(v -> mOnHideKeyboardListener.run());
    }

    /**
     * Ran once to connect all keys with this keyboard.
     *
     * @param view Root view, this function runs recursively.
     */
    private void initKeys(View view) {
        if (view instanceof ShiftKeyView) {
            mShiftKey = ((ShiftKeyView) view);
        }
        if (view instanceof KeyboardKeyConnection) {
            ((KeyboardKeyConnection) view).setKeyboard(this);
    
            if (view instanceof KeyView) {
                ((KeyView) view).setKeyIndicatorView(mKeyIndicatorView);
            }
        }
        else if (view instanceof ViewGroup) {
            ViewGroup group = ((ViewGroup) view);
            for (int i = 0; i < group.getChildCount(); i++) {
                initKeys(group.getChildAt(i));
            }
        }
    }

    /**
     * Writes the given code considering upper-casing through the shift key.
     * @param code
     */
    public void write(String code) {
        mEditor.write(mShiftKey.process(code));
    }
    
    public void writeEnter() {
        mEditor.write("\n");
    }
    
    public void backspace() {
        mEditor.backspace();
    }
    
    public void setEditor(CodeEditorView editor) {
        mEditor = editor;
    }
    
    public void setOnHideKeyboardListener(Runnable onHideKeyboardListener) {
        mOnHideKeyboardListener = onHideKeyboardListener;
    }
    
}
