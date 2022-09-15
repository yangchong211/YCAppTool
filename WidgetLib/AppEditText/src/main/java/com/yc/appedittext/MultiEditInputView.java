package com.yc.appedittext;


import android.annotation.SuppressLint;
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

    private final Context mContext;
    private EditText idEtInput;
    private TextView idTvInput;
    private final int MAX_COUNT;
    private String hintText;
    private final boolean ignoreCnOrEn;
    private String contentText;
    private final float contentHeight;
    LinearLayout idLlMulti;
    private final TextWatcher mTextWatcher;

    public MultiEditInputView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MultiEditInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiEditInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int editStart = MultiEditInputView.this.idEtInput.getSelectionStart();
                int editEnd = MultiEditInputView.this.idEtInput.getSelectionEnd();
                MultiEditInputView.this.idEtInput.removeTextChangedListener(MultiEditInputView.this.mTextWatcher);
                if (MultiEditInputView.this.ignoreCnOrEn) {
                    while(MultiEditInputView.this.calculateLengthIgnoreCnOrEn(editable.toString()) > MultiEditInputView.this.MAX_COUNT) {
                        editable.delete(editStart - 1, editEnd);
                        --editStart;
                        --editEnd;
                    }
                } else {
                    while(MultiEditInputView.this.calculateLength(editable.toString()) > (long)MultiEditInputView.this.MAX_COUNT) {
                        editable.delete(editStart - 1, editEnd);
                        --editStart;
                        --editEnd;
                    }
                }

                MultiEditInputView.this.idEtInput.setSelection(editStart);
                MultiEditInputView.this.idEtInput.addTextChangedListener(MultiEditInputView.this.mTextWatcher);
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
        typedArray.recycle();
        this.init();
    }

    private void init() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.view_multi_edit_input, this);
        this.idLlMulti = (LinearLayout)view.findViewById(R.id.id_ll_multi);
        this.idLlMulti.setBackgroundResource(R.drawable.view_selector_edit_text_multi);
        this.idEtInput = (EditText)view.findViewById(R.id.id_et_input);
        this.idTvInput = (TextView)view.findViewById(R.id.id_tv_input);
        this.idEtInput.addTextChangedListener(this.mTextWatcher);
        this.idEtInput.setHint(this.hintText);
        this.idEtInput.setText(this.contentText);
        this.idEtInput.setHeight((int)this.contentHeight);
        this.idTvInput.requestFocus();
        this.configCount();
        this.idEtInput.setSelection(this.idEtInput.length());
        this.idEtInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                MultiEditInputView.this.idLlMulti.setSelected(b);
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

    @SuppressLint("SetTextI18n")
    private void configCount() {
        if (this.ignoreCnOrEn) {
            int nowCount = this.calculateLengthIgnoreCnOrEn(this.idEtInput.getText().toString());
            this.idTvInput.setText(this.MAX_COUNT - nowCount + "/" + this.MAX_COUNT);
        } else {
            long nowCount = this.calculateLength(this.idEtInput.getText().toString());
            this.idTvInput.setText((long)this.MAX_COUNT - nowCount + "/" + this.MAX_COUNT);
        }

    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5F);
    }

    public void setContentText(String content) {
        this.contentText = content;
        if (this.idEtInput != null) {
            this.idEtInput.setText(this.contentText);
        }
    }

    public String getContentText() {
        if (this.idEtInput != null) {
            this.contentText = this.idEtInput.getText() == null ? "" : this.idEtInput.getText().toString();
        }

        return this.contentText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        this.idEtInput.setHint(hintText);
    }

    public String getHintText() {
        if (this.idEtInput != null) {
            this.hintText = this.idEtInput.getHint() == null ? "" : this.idEtInput.getHint().toString();
        }

        return this.hintText;
    }
}

