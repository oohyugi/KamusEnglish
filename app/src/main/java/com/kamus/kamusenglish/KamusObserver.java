package com.kamus.kamusenglish;

import java.util.Observable;

/**
 * Created by koba on 11/23/15.
 */
public class KamusObserver extends Observable {
    public static String NEED_TO_REFRESH="refresh";

    public void refresh(){
        setChanged();
        notifyObservers(NEED_TO_REFRESH);
    }



}
