package com.kerboocorp.whenisit.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kerboocorp.whenisit.R;
import com.kerboocorp.whenisit.activities.EventActivity;
import com.kerboocorp.whenisit.managers.DatabaseManager;
import com.kerboocorp.whenisit.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chris on 17/12/14.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Event> eventList;
    private int rowLayout;
    private Context context;
    private DatabaseManager databaseManager;
    private FragmentManager fragmentManager;
    private ActionBarActivity activity;

    public EventAdapter(int rowLayout, Context context, FragmentManager fragmentManager, ActionBarActivity activity) {
        this.eventList = new ArrayList<Event>();
        this.rowLayout = rowLayout;
        this.context = context;
        databaseManager = new DatabaseManager(context);
        this.fragmentManager = fragmentManager;
        this.activity = activity;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void addEventList(List<Event> eventList) {
        this.eventList.addAll(eventList);
        notifyDataSetChanged();
    }

    public void addEvent(Event event) {
        eventList.add(event);
        notifyDataSetChanged();
    }

    public void removeEvent(Event event) {
        eventList.remove(event);
        notifyDataSetChanged();
    }

    public void updateEvent(Event event) {
        for (Event evt : eventList) {
            if (evt.getId() == event.getId()) {
                evt.setName(event.getName());
                evt.setDescription(event.getDescription());
                evt.setExpirationDate(event.getExpirationDate());
            }
        }
        notifyDataSetChanged();
    }

    public void clearEventList() {
        eventList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new EventViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof EventViewHolder) {

            EventViewHolder eventViewHolder = (EventViewHolder) viewHolder;
            Event event = eventList.get(i);

            eventViewHolder.name.setText(event.getName());

            Map<String, String> date = event.getDateDifference(new Date(), event.getExpirationDate());

            if (date.size() > 0) {
                if (!date.get("days").equals("0")) {
                    eventViewHolder.daysTextView.setText(date.get("days"));
                    eventViewHolder.hoursTextView.setVisibility(View.GONE);
                    eventViewHolder.hoursLabelTextView.setVisibility(View.GONE);
                    eventViewHolder.minutesTextView.setVisibility(View.GONE);
                    eventViewHolder.minutesLabelTextView.setVisibility(View.GONE);
                    eventViewHolder.secondsTextView.setVisibility(View.GONE);
                    eventViewHolder.secondsLabelTextView.setVisibility(View.GONE);
                } else {
                    eventViewHolder.daysTextView.setVisibility(View.GONE);
                    eventViewHolder.daysLabelTextView.setVisibility(View.GONE);
                    eventViewHolder.hoursTextView.setText(date.get("hours"));
                    eventViewHolder.minutesTextView.setText(date.get("minutes"));
                    eventViewHolder.secondsTextView.setText(date.get("seconds"));

                }

                eventViewHolder.context = context;
                eventViewHolder.event = event;
                eventViewHolder.eventAdapter = this;
                eventViewHolder.databaseManager = this.databaseManager;
                eventViewHolder.fragmentManager = fragmentManager;
            }

        }

    }

    @Override
    public int getItemCount() {
        return (eventList == null ? 0 : eventList.size());
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public TextView name;
        public TextView daysTextView;
        public TextView daysLabelTextView;
        public TextView hoursTextView;
        public TextView hoursLabelTextView;
        public TextView minutesTextView;
        public TextView minutesLabelTextView;
        public TextView secondsTextView;
        public TextView secondsLabelTextView;
        public Event event;
        public Context context;
        public EventAdapter eventAdapter;
        public DatabaseManager databaseManager;
        public FragmentManager fragmentManager;

        public EventViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            daysTextView = (TextView) itemView.findViewById(R.id.daysTextView);
            daysLabelTextView = (TextView) itemView.findViewById(R.id.daysLabelTextView);
            hoursTextView = (TextView) itemView.findViewById(R.id.hoursTextView);
            hoursLabelTextView = (TextView) itemView.findViewById(R.id.hoursLabelTextView);
            minutesTextView = (TextView) itemView.findViewById(R.id.minutesTextView);
            minutesLabelTextView = (TextView) itemView.findViewById(R.id.minutesLabelTextView);
            secondsTextView = (TextView) itemView.findViewById(R.id.secondsTextView);
            secondsLabelTextView = (TextView) itemView.findViewById(R.id.secondsLabelTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("event", (Parcelable)event);
            intent.putExtra("action", "update");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            EventAdapter.this.activity.startActivityForResult(intent, 2);
        }

        @Override
        public boolean onLongClick(View v) {
            new MaterialDialog.Builder(context)
                    .title(R.string.delete_event)
                    .content(R.string.delete_event_explication)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            eventAdapter.removeEvent(event);
                            databaseManager.deleteEvent(event);
                        }
                    })
                    .show();
            return true;
        }


    }



    public String getDateDifference(Date startDate, Date endDate){

        long different = endDate.getTime() - startDate.getTime();

        if (different < 0) {
            return "Expiré";
        } else {

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays > 0) {
                return "dans " + elapsedDays + " jours";
            } else {
                return "dans " + elapsedHours + " heures " + elapsedMinutes + " min " + elapsedSeconds + " sec";
            }

        }

    }

}