package com.example.rajshekhar.calendarwithevents_sql;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajshekhar.calendarwithevents_sql.database.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@TargetApi(3)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String tag = "MainActivity";
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    TextView tv_no_event;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormat = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    String date_month_year;
    String flag = "abc";
    RelativeLayout main_relative;
    ClickListener clickListener=null;
    RecyclerView recyclerView;
    private ArrayList<Event>eventList;
    private MyAdapter my_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventList=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        //notesList.addAll(db.getAllNotes());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,recyclerView,
                new RecyclerTouchListener.ClickListener(){
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }));



        tv_no_event=(TextView)findViewById(R.id.empty_notes_view);
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);

        selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

// Initialised
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);



        my_adapter = new MyAdapter(this, eventList, new RemoveClickListner() {
            @Override
            public void OnRemoveClick(int index) {
                eventList.remove(index);
                my_adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(my_adapter);

    }




    private View.OnClickListener onCancelListener(final Dialog dialog){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }
    private View.OnClickListener onConfirmListener(final EditText name, final Dialog dialog){
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Event friend = new Event(name.getText().toString().trim());
                friend.setDate(date_month_year.toString());
                Log.e("DATE1111111",date_month_year.toString());





                if(name.getText().toString().matches("")){
                    Toast.makeText(v.getContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                eventList.add(friend);



                my_adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        };
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View …");
        super.onDestroy();
    }
    @Override
    public void onClick(View view) {
        if (view == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter123: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (view == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter123: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
    }

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener{
        private final Context _context;
         private final List<String>list;
         private static final int DAY_OFFSET =1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        public Button gridcell;
        private TextView num_events_per_day;
        private final HashMap<String,Integer>eventsPerMonthMap;
        private ClickListener clickListener =null;

        public GridCellAdapter(Context context,int textViewResourceId,int month,int year){
            super();
            this._context=context;
            this.list= new ArrayList<String>();
            Calendar calendar=Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            printMonth(month,year);
            eventsPerMonthMap=findNumberOfEventsPerMonth(year,month);

        }

        private HashMap<String,Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String,Integer>map=new HashMap<String, Integer>();
            return map;
        }

        private void printMonth(int mm, int yy) {
            int trailingSpaces=0;
            int daysInPrevMonth=0;
            int prevMonth=0;
            int prevYear=0;
            int nextMonth=0;
            int nextYear=0;

            int currentMonth =mm-1;
            daysInMonth=getNumberOfDaysOfMonth(currentMonth);
            GregorianCalendar cal= new GregorianCalendar(yy,currentMonth,1);
            if (currentMonth==11){
                prevMonth=currentMonth-1;
                daysInPrevMonth=getNumberOfDaysOfMonth(prevMonth);
                nextMonth=0;
                prevYear=yy;
                nextYear=yy+1;
            }else if(currentMonth==0){
                prevMonth=11;
                prevYear=yy-1;
                nextYear=yy;
                daysInPrevMonth=getNumberOfDaysOfMonth(prevMonth);
                nextMonth=1;
            }
            else {
                prevMonth=currentMonth-1;
                nextMonth=currentMonth+1;
                nextYear=yy;
                prevYear=yy;
                daysInPrevMonth=getNumberOfDaysOfMonth(prevMonth);
            }
            int currentWeekDay=cal.get(Calendar.DAY_OF_WEEK)-1;
            trailingSpaces=currentWeekDay;
            if(cal.isLeapYear(cal.get(Calendar.YEAR))&&mm==1){
                ++daysInMonth;
            }
            for (int i=0;i<trailingSpaces;i++){
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY"
                        + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }
            for (int i=1;i<=daysInMonth;i++){
                if(i==getCurrentDayOfMonth())
                    list.add(String.valueOf(i)+"-BLUE"+""+getMonthAsString(currentMonth)+"-"+yy);
                else
                    list.add(String.valueOf(i) + "-RED" + "-" + getMonthAsString(currentMonth) + "-" + yy);
            }
            for (int i=0;i<list.size()%7;i++){
             //   list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) +"-"+nextYear);
            }
        }

        public int getCurrentDayOfMonth(){
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth){
            this.currentDayOfMonth = currentDayOfMonth;
        }
        public int getCurrentWeekDay(){
            return currentWeekDay;
        }
        private String getMonthAsString(int i){
            return months[i];
        }
        private int getNumberOfDaysOfMonth(int i){
            return daysOfMonth[i];
        }

        private void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay=currentWeekDay;
        }

        @Override
        public void onClick(View view) {
            date_month_year=(String)view.getTag();
            flag="Date selected..";
            selectedDayMonthYearButton.setText("Selected:"+date_month_year);

        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int postion) {
            return list.get(postion);
        }

        @Override
        public long getItemId(int position) {
            return position;

        }
        @Override
        public View getView(final int position, View conevrtView, ViewGroup parent) {
            final ViewHolder holder;
            if (conevrtView == null){
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                conevrtView =  inflater.inflate(R.layout.screen_gridcell,null);
                holder = new ViewHolder(conevrtView);
                conevrtView.setTag(holder);
            }else {
                holder=(ViewHolder)conevrtView.getTag();
            }
           holder.btn_cell.setOnClickListener(this);
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
//            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)){
                if (eventsPerMonthMap.containsKey(theday)){

                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    holder.tv_cell.setText(numEvents.toString());
                }

            }
            // Set the Day GridCell
            holder.btn_cell.setText(theday);
            holder.btn_cell.setTag(theday + "-" + themonth + "-");
            if (day_color[1].equals("GRAY"))
                holder.btn_cell.setTextColor(Color.RED);
          //  holder.text.setTextColor(Color.argb(0,200,0,0));

            holder.btn_cell.setTextColor(Color.GREEN);

            if(day_color[1].equals("RED"))
                holder.btn_cell.setTextColor(Color.RED);
                if (day_color[1].equals("BLUE"))
                    holder.btn_cell.setTextColor(Color.DKGRAY);
                holder.btn_cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        date_month_year=(String)view.getTag();
                        flag="Date selected..";
                        selectedDayMonthYearButton.setText("Selected:"+date_month_year);
                        Toast.makeText(_context,date_month_year, Toast.LENGTH_SHORT).show();






                    }
                });

                holder.btn_cell.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        date_month_year=(String)view.getTag();
                        flag="Date selected..";
                        selectedDayMonthYearButton.setText("Selected:"+date_month_year);
                        Toast.makeText(_context,date_month_year, Toast.LENGTH_SHORT).show();
                        Log.e("LONG-CLICK",date_month_year+"");

                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.dialog);
                        dialog.setTitle("Add a new friend");
                        dialog.setCancelable(false);
                        EditText name =(EditText)dialog.findViewById(R.id.name);

                        View btnAdd =dialog.findViewById(R.id.btn_ok);
                        View btnCancel = dialog.findViewById(R.id.btn_cancel);




                        btnAdd.setOnClickListener(onConfirmListener(name, dialog));
                        btnCancel.setOnClickListener(onCancelListener(dialog));

                        dialog.show();
                        return false;
                    }
                });


                return conevrtView;
        }


        public void setClickListener(ClickListener clickListener) {
            this.clickListener=clickListener;
        }

        private class ViewHolder {
            public Button btn_cell;
            public TextView tv_cell;
            public RelativeLayout rl_cell;

            public ViewHolder(View v) {
                btn_cell=(Button)v.findViewById(R.id.calendar_day_gridcell);
                tv_cell= (TextView)v.findViewById(R.id.num_events_per_day);
                rl_cell=(RelativeLayout)v.findViewById(R.id.main);

            }
        }
    }
}


















