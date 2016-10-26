package com.example.rok.terroristinfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class DataHolder {
    private static Data[] data = null;

    private DataHolder() {}

    public static Data[] getData() {
        return data;
    }

    public static void setData(String stringData) {
        if (stringData == null || stringData.equals("")) {
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(stringData);

            data = new Data[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int b = jsonObject.getInt("notify");
                boolean notify = b == 1;
                data[i] = new Data(
                        (float)jsonObject.getInt("lat"), (float)jsonObject.getInt("lng"),
                        jsonObject.getString("icon"),
                        jsonObject.getString("location"),
                        jsonObject.getString("briefSummary"),
                        jsonObject.getString("eventInfo"),
                        jsonObject.getString("weekDay"),
                        jsonObject.getString("month"),
                        jsonObject.getInt("day"),
                        jsonObject.getString("year"),
                        notify,
                        jsonObject.getString("eventType")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
