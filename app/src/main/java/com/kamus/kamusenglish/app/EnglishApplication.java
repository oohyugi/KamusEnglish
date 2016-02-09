package com.kamus.kamusenglish.app;

import android.app.Application;

import com.kamus.kamusenglish.KamusObserver;

/**
 * Created by koba on 11/23/15.
 */
public class EnglishApplication extends Application {
    KamusObserver kamusObserver;
    @Override
    public void onCreate(){
        kamusObserver=new KamusObserver();

    }
    public KamusObserver getKamusObserver(){
        return kamusObserver;
    }
}
