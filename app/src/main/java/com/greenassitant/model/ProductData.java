package com.greenassitant.model;

/**
 * Created by Saul on 14/09/2014.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class ProductData {

    public static JSONArray getJsonFromAssets(String fileName, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();

        String jsonString = new String(data);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static String[] getNamesByRegex(String query, Context context) {

        String[] autocomplete_array = new String[0];
        try {
            JSONArray jsonArray = getJsonFromAssets("productdata-generic.json", context);
            autocomplete_array = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                autocomplete_array[i] = jsonObject.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return autocomplete_array;
    }

    /**
     * Dummy search query
     */
    public static BasketItem getByName(String name, Context context) {

        JSONArray jsonArray = null;
        try {
            jsonArray = getJsonFromAssets("productdata-generic.json", context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Search dummy products db
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getString("name").toLowerCase().startsWith(name.toLowerCase())) {
                    return new BasketItem(jsonObject.getString("name").toLowerCase(), jsonObject.getDouble("co2"), jsonObject.getDouble("amount"), jsonObject.getString("unit"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // If product not found, generate one with random values
        int greenScore = 1 + (int) (Math.random() * 3);
        return new BasketItem(name, greenScore, 0.1, "kg");
    }
}
