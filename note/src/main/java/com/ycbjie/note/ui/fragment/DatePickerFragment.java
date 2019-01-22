package com.ycbjie.note.ui.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import com.ycbjie.library.inter.listener.OnDatePickListener;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDatePickListener mOnDatePickListener;

    public void setOnDatePickListener(OnDatePickListener onDatePickListener) {
        this.mOnDatePickListener = onDatePickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.setSpinnersShown(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnDatePickListener != null) {
            mOnDatePickListener.onDatePickCancel();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mOnDatePickListener != null) {
            mOnDatePickListener.onDatePick(year, monthOfYear, dayOfMonth);
        }
    }

}
