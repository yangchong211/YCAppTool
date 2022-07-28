package com.yc.appedittext;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MultiEditInputView extends LinearLayout {

    private Context mContext;
    private EditText id_et_input;
    private TextView id_tv_input;
    private int MAX_COUNT;
    private String hintText;
    private boolean ignoreCnOrEn;
    private String contentText;
    private float contentHeight;
    LinearLayout id_ll_multi;
    private TextWatcher mTextWatcher;

    public MultiEditInputView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MultiEditInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiEditInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTextWatcher = new TextWatcher() {
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                this.editStart = MultiEditInputView.this.id_et_input.getSelectionStart();
                this.editEnd = MultiEditInputView.this.id_et_input.getSelectionEnd();
                MultiEditInputView.this.id_et_input.removeTextChangedListener(MultiEditInputView.this.mTextWatcher);
                if (MultiEditInputView.this.ignoreCnOrEn) {
                    while(MultiEditInputView.this.calculateLengthIgnoreCnOrEn(editable.toString()) > MultiEditInputView.this.MAX_COUNT) {
                        editable.delete(this.editStart - 1, this.editEnd);
                        --this.editStart;
                        --this.editEnd;
                    }
                } else {
                    while(MultiEditInputView.this.calculateLength(editable.toString()) > (long)MultiEditInputView.this.MAX_COUNT) {
                        editable.delete(this.editStart - 1, this.editEnd);
                        --this.editStart;
                        --this.editEnd;
                    }
                }

                MultiEditInputView.this.id_et_input.setSelection(this.editStart);
                MultiEditInputView.this.id_et_input.addTextChangedListener(MultiEditInputView.this.mTextWatcher);
                MultiEditInputView.this.configCount();
            }
        };
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiEditInputView);
        this.MAX_COUNT = typedArray.getInteger(R.styleable.MultiEditInputView_maxCount, 240);
        this.ignoreCnOrEn = typedArray.getBoolean(R.styleable.MultiEditInputView_IgnoreCnOrEn, true);
        this.hintText = typedArray.getString(R.styleable.MultiEditInputView_hintText);
        this.contentText = typedArray.getString(R.styleable.MultiEditInputView_contentText);
        this.contentHeight = typedArray.getDimension(R.styleable.MultiEditInputView_contentHeight, 140.0F);
        this.init();
    }

    private void init() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.view_multi_edit_input, this);
        this.id_ll_multi = (LinearLayout)view.findViewById(R.id.id_ll_multi);
        this.id_ll_multi.setBackgroundResource(R.drawable.view_selector_edit_text_multi);
        this.id_et_input = (EditText)view.findViewById(R.id.id_et_input);
        this.id_tv_input = (TextView)view.findViewById(R.id.id_tv_input);
        this.id_et_input.addTextChangedListener(this.mTextWatcher);
        this.id_et_input.setHint(this.hintText);
        this.id_et_input.setText(this.contentText);
        this.id_et_input.setHeight((int)this.contentHeight);
        this.id_tv_input.requestFocus();
        this.configCount();
        this.id_et_input.setSelection(this.id_et_input.length());
        this.id_et_input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                MultiEditInputView.this.id_ll_multi.setSelected(b);
            }
        });
    }

    private long calculateLength(CharSequence c) {
        double len = 0.0D;

        for(int i = 0; i < c.length(); ++i) {
            int tmp = c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5D;
            } else {
                ++len;
            }
        }

        return Math.round(len);
    }

    private int calculateLengthIgnoreCnOrEn(CharSequence c) {
        int len = 0;

        for(int i = 0; i < c.length(); ++i) {
            ++len;
        }

        return len;
    }

    private void configCount() {
        if (this.ignoreCnOrEn) {
            int nowCount = this.calculateLengthIgnoreCnOrEn(this.id_et_input.getText().toString());
            this.id_tv_input.setText(this.MAX_COUNT - nowCount + "/" + this.MAX_COUNT);
        } else {
            long nowCount = this.calculateLength(this.id_et_input.getText().toString());
            this.id_tv_input.setText((long)this.MAX_COUNT - nowCount + "/" + this.MAX_COUNT);
        }

    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5F);
    }

    public void setContentText(String content) {
        this.contentText = content;
        if (this.id_et_input != null) {
            this.id_et_input.setText(this.contentText);
        }
    }

    public String getContentText() {
        if (this.id_et_input != null) {
            this.contentText = this.id_et_input.getText() == null ? "" : this.id_et_input.getText().toString();
        }

        return this.contentText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        this.id_et_input.setHint(hintText);
    }

    public String getHintText() {
        if (this.id_et_input != null) {
            this.hintText = this.id_et_input.getHint() == null ? "" : this.id_et_input.getHint().toString();
        }

        return this.hintText;
    }
}

