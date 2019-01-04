package com.example.rajshekhar.calendarwithevents_sql;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajshekhar.calendarwithevents_sql.database.Event;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Event>events;
    private Activity activity;
    int mLastPostion =0;
    private RemoveClickListner mListner;

    public MyAdapter(MainActivity mainActivity, List<Event> events, RemoveClickListner listner) {
        this.events=events;
        this.activity=mainActivity;
        mListner=listner;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =activity.getLayoutInflater();

        final View view =inflater.inflate(R.layout.item_recycler, parent, false);

        ViewHolder holder = new ViewHolder(view, new RemoveClickListner() {
            @Override
            public void OnRemoveClick(int index) {
                mListner.OnRemoveClick(index);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final  ViewHolder holder1=(ViewHolder)holder;
        holder1.setItemDetails(events.get(position),position);
        Log.e("DAT-ISrAJ",events.get(position).getDate());


        mLastPostion=position;
        holder1.name1.setText(events.get(position).getName());
        holder1.date1.setText(events.get(position).getDate());


        holder1.container.setOnClickListener(onClickListener(position));


    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.item_recycler);
                dialog.setTitle("Postion"+position);
                dialog.setCancelable(true);
                TextView name =(TextView)dialog.findViewById(R.id.name);
                setDataToView(name,position);
                dialog.show();



            }
        };

    }

    private void setDataToView(TextView name, int position) {
        name.setText(events.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (null !=events?events.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name1;
        private  TextView date1;
        private View container;
        private LinearLayout mainLayout;
        private RemoveClickListner mListner;

        public ViewHolder(View itemView, RemoveClickListner removeClickListner) {
            super(itemView);
            Context context;
            this.mListner=removeClickListner;
            imageView=(ImageView)itemView.findViewById(R.id.image);
            name1=(TextView)itemView.findViewById(R.id.nameEvent);
            date1=(TextView)itemView.findViewById(R.id.dateEvent);
            container=itemView.findViewById(R.id.card_view);
            mainLayout=(LinearLayout)itemView.findViewById(R.id.mainLayout);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Toast.makeText(view.getContext(),"Position:"+Integer.toString(getPosition()),Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setItemDetails(Event event,final  int position){
            name1.setText(event.getName());
            date1.setText(event.getDate());

            imageView.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.OnRemoveClick(position);
                }
            });
        }

    }
}
