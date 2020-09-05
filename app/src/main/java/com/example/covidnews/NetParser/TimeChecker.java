package com.example.covidnews.NetParser;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeChecker {
    public static String CheckString(String Time){
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tm = Time.split(" ")[0];
        try {
            formatter.parse(tm);
            return Time;
        } catch (ParseException e) {
            String[] s = Time.split("/");
            StringBuilder f = new StringBuilder(s[0]);
            for(int i = 1; i <= 2; i++){
                f.append("-");
                f.append(s[i]);
            }
            return f.toString();
        }
    }
}
