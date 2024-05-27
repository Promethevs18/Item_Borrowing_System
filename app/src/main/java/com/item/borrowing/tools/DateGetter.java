package com.item.borrowing.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateGetter {

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
    String date = dateFormat.format(new Date());


    public String postDate(){
        return date;
    }
}
