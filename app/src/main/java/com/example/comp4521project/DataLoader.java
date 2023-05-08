package com.example.comp4521project;

import android.content.Context;
import android.util.Log;

import com.example.comp4521project.util.IEvent;
import com.example.comp4521project.util.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    private HashMap<Integer, IEvent> idToEvent;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, List<Integer>>>> mapForDate;

    public DataLoader(Context context) {
        try {
            loadDate(context);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDate(Context context) throws JSONException {
        idToEvent = new HashMap<>();
        //Load All Events
        boolean isFilePresent = IOUtils.isFilePresent(context, "storage.json");
        if(isFilePresent) {
            String jsonString = IOUtils.read(context, "storage.json");
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                int id = Integer.parseInt(key);
                IEvent event = new IEvent(jsonObject.getJSONObject(key));
                idToEvent.put(id, event);
            }
        } else {
            IOUtils.create(context, "storage.json", "{}");
        }
        //End Loading
        mapForDate = new HashMap<>();
        for (int id : idToEvent.keySet()) {
            IEvent event = idToEvent.get(id);
            // Update mapForDate
            storeByDate(event);
        }
    }

    public boolean saveData(Context context) {
        JSONObject jsonObject = new JSONObject();
        for (int key : idToEvent.keySet()) {
            try {
                jsonObject.put(String.valueOf(key), idToEvent.get(key).toJSONObject());
            } catch (JSONException e) {
                //throw new RuntimeException(e);
                return false;
            }
        }
        return IOUtils.create(context, "storage.json", jsonObject.toString());
    }

    public boolean addEvent(Context context, IEvent event) {
        int id = (idToEvent.size()==0)? 0 : Collections.max(idToEvent.keySet()) + 1;
        event.setID(id);
        idToEvent.put(id,event);
        storeByDate(event);
        return saveData(context);
    }

    public boolean removeEvent(Context context, IEvent event) {
        idToEvent.remove(event.getID());
        removeByDate(event);
        return saveData(context);
    }

    public List<IEvent> getEventsByDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (!mapForDate.containsKey(year)) return null;
        if (!mapForDate.get(year).containsKey(month)) return null;
        if (!mapForDate.get(year).get(month).containsKey(day)) return null;
        List <IEvent> result = new ArrayList<>();
        for (int value : mapForDate.get(year).get(month).get(day)) {
            result.add(idToEvent.get(value));
        }
        if (result.size()==0) return null;
        return result;
    }

    public List<IEvent> getEventsByPeriod(LocalDate date1, LocalDate date2) {
        long numOfDays = ChronoUnit.DAYS.between(date1, date2);

        List<LocalDate> listOfDates = Stream.iterate(date1, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());

        List <IEvent> result = new ArrayList<>();
        for (LocalDate date : listOfDates) {
            List <IEvent> dayResult = getEventsByDate(date);
            result.addAll(dayResult);
        }
        if (result.size()==0) return null;
        return result;
    }

    /** HELPER FUNCTIONS **/

    private void storeByDate(IEvent event) {
        LocalDate date = event.getLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (!mapForDate.containsKey(year)) mapForDate.put(year, new HashMap<>());
        if (!mapForDate.get(year).containsKey(month)) mapForDate.get(year).put(month, new HashMap<>());
        if (!mapForDate.get(year).get(month).containsKey(day)) mapForDate.get(year).get(month)
                .put(day, new ArrayList<>());
        mapForDate.get(year).get(month).get(day).add(event.getID());
    }

    private void removeByDate(IEvent event) {
        LocalDate date = event.getLocalDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (!mapForDate.containsKey(year)) return;
        if (!mapForDate.get(year).containsKey(month)) return;
        if (!mapForDate.get(year).get(month).containsKey(day)) return;
        mapForDate.get(year).get(month).get(day).remove(event.getID());
    }
}
