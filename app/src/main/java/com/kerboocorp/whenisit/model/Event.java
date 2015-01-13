package com.kerboocorp.whenisit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Event implements Parcelable {
	
	private int id;
	private String name;
	private String description;
	private Date expirationDate;
	
	public Event() {}
	
	public Event(String name, String description, Date expirationDate) {
		this.name = name;
		this.description = description;
		this.expirationDate = expirationDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel data, int flags) {
        data.writeInt(id);
        data.writeString(name);
        data.writeString(description);
        data.writeLong(expirationDate.getTime());

    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in) {
        this.setId(in.readInt());
        this.name = in.readString();
        this.description = in.readString();
        this.expirationDate = new Date(in.readLong());
    }

    public Map<String, String> getDateDifference(Date startDate, Date endDate){

        Map<String, String> date = new HashMap<String, String>();

        long different = endDate.getTime() - startDate.getTime();

        if (different < 0) {
            return date;
        } else {

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            date.put("days", String.valueOf(elapsedDays));
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            date.put("hours", String.valueOf(elapsedHours));
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            date.put("minutes", String.valueOf(elapsedMinutes));
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;
            date.put("seconds", String.valueOf(elapsedSeconds));

            return date;
        }

    }
}
