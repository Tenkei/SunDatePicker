package com.afkar.sundatepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.Calendar;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
 * Created by Alireza Afkar on 2/11/16 AD.
 */

public class MainActivity extends FragmentActivity implements
    OnClickListener, DateSetListener {
    private Button mEnd;
    private Button mStart;
    private CheckBox mDark;
    private CheckBox mFuture;

    private Date mEndDate;
    private Date mStartDate;

    public MainActivity() {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale("fa");

        setContentView(R.layout.activity_main);

        mEnd = (Button) findViewById(R.id.endDate);
        mStart = (Button) findViewById(R.id.startDate);
        mDark = (CheckBox) findViewById(R.id.darkTheme);
        mFuture = (CheckBox) findViewById(R.id.future);

        mEndDate = new Date();
        mStartDate = new Date();

        mEnd.setOnClickListener(this);
        mStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId() == R.id.startDate ? 1 : 2;
        @StyleRes int theme = mDark.isChecked()
            ? R.style.DarkDialogTheme
            : R.style.DialogTheme;

        DatePicker.Builder builder = new DatePicker
            .Builder()
            .id(id)
            .theme(theme)
            //.showYearFirst(true)
            //.closeYearAutomatically(true)
            //.minYear(1393)
            .future(mFuture.isChecked())
            .enableToday(true);

        if (v.getId() == R.id.startDate)
            builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());
        else
            builder.date(mEndDate.getCalendar());

        builder.build(MainActivity.this)
            .show(getSupportFragmentManager(), "");
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (id == 1) {
            mStartDate.setDate(day, month, year);
            mStart.setText(mStartDate.getDate());
        } else {
            mEndDate.setDate(day, month, year);
            mEnd.setText(mEndDate.getDate());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());
    }

    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                "%d/%d/%d (%d/%d/%d)",
                getYear(), getMonth(), getDay(),
                calendar.get(Calendar.YEAR),
                +calendar.get(Calendar.MONTH) + 1,
                +calendar.get(Calendar.DAY_OF_MONTH));
        }
    }
}
