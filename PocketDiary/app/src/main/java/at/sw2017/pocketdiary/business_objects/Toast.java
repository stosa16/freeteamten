package at.sw2017.pocketdiary.business_objects;

import android.content.Context;

/**
 * Created by marku on 26.05.2017.
 */

public class Toast {

    public Toast(){

    }

    public Toast(String msg, Context context){
        printToast(msg, context);
    }

    public void printToast(String msg, Context context){
        CharSequence text = msg;
        int duration = android.widget.Toast.LENGTH_SHORT;

        android.widget.Toast toast = android.widget.Toast.makeText(context, text, duration);
        toast.show();
    }
}
