package com.kamus.kamusenglish.model;

import android.view.View;

import java.io.Serializable;

/**
 * Created by koba on 11/23/15.
 */
public class KamusModel implements Serializable {
    int id;
    String kata;
    String arti;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;

    }
    public String getKata(){
        return kata;
    }
    public void setKata(String kata){
        this.kata=kata;
    }
    public String getArti(){
        return arti;
    }
    public void setArti(String arti){
        this.arti=arti;
    }
     public static KamusModel getKamusModel(String kata,String arti){
         KamusModel kamusModel=new KamusModel();
         kamusModel.setKata(kata);
         kamusModel.setArti(arti);

         return kamusModel;

     }
    public static KamusModel getKamusModel(int id,String kata,String arti){
        KamusModel kamusModel=new KamusModel();
        kamusModel.setKata(kata);
        kamusModel.setArti(arti);

        kamusModel.setId(id);
        return kamusModel;

    }


}
