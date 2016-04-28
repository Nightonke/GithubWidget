package com.nightonke.githubwidget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Weiping on 2016/4/28.
 */

public class LimitedEditText extends EditText {

    /**
     * Max lines to be present in editable text field
     */
    private int maxLines = 2;

    /**
     * Max characters to be present in editable text field
     */
    private int maxCharacters = Integer.MAX_VALUE;

    /**
     * application context;
     */
    private Context context;

    private OnTextChangedListener onTextChangedListener;

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    public LimitedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public LimitedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LimitedEditText(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextWatcher watcher = new TextWatcher() {

            private String text;
            private int beforeCursorPosition = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (onTextChangedListener != null) {
                    onTextChangedListener.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                text = s.toString();
                beforeCursorPosition = start;
            }

            @Override
            public void afterTextChanged(Editable s) {

            /* turning off listener */
                removeTextChangedListener(this);

            /* handling lines limit exceed */
                if (LimitedEditText.this.getLineCount() > maxLines) {
                    LimitedEditText.this.setText(text);
                    LimitedEditText.this.setSelection(beforeCursorPosition);
                }

            /* handling character limit exceed */
                if (s.toString().length() > maxCharacters) {
                    LimitedEditText.this.setText(text);
                    LimitedEditText.this.setSelection(beforeCursorPosition);
                }

            /* turning on listener */
                addTextChangedListener(this);

            }
        };

        this.addTextChangedListener(watcher);
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }
}
