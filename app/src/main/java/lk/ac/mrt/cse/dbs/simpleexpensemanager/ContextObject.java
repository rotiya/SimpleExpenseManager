package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

/**
 * Created by Roshan on 12/5/2015.
 */
public class ContextObject {
    public static Context context;

    public static void setContext(Context ctx){
        context = ctx;
    }

    public static Context getContext(){
        return context;
    }
}
