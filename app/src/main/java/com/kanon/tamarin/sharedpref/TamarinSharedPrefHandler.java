package com.kanon.tamarin.sharedpref;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class TamarinSharedPrefHandler {

    //private static TamarinSharedPrefHandler tamarinSharedPrefHandler;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;


    /*public static TamarinSharedPrefHandler newInstance(Context context) {
        if (tamarinSharedPrefHandler == null)
            tamarinSharedPrefHandler = new TamarinSharedPrefHandler(context);

        return tamarinSharedPrefHandler;
    }*/

    public TamarinSharedPrefHandler(Context context) {
        //newInstance(context);
        sp =  context.getSharedPreferences("com.kanon.tamarin", MODE_PRIVATE);
    }

    public void InsertValue(String key, String value)
    {
        spEditor = sp.edit();
        spEditor.putString(key,value);
        spEditor.commit();
    }

    public String GetValue(String key)
    {
        return sp.contains(key)? sp.getString(key, null) : null;
    }

    public void RemoveValue(String key)
    {
        spEditor = sp.edit();
        spEditor.remove(key);
        spEditor.commit();
    }
}
