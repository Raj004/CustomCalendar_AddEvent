package com.example.rajshekhar.calendarwithevents_sql;

import android.view.View;

/**
 * Created by rajshekhar on 11/6/18.
 */

public interface ClickListener {
    public void onClick(View view,int position);
    public void onLongClick(View view,int position);

}
