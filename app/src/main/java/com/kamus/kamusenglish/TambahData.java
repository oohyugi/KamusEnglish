package com.kamus.kamusenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kamus.kamusenglish.app.EnglishApplication;
import com.kamus.kamusenglish.model.KamusModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by koba on 11/23/15.
 */
public class TambahData extends AppCompatActivity implements Observer {
    Toolbar toolbar;
    EditText tkata,tarti;
    Button bAksi;
    public static int FORM_INPUT=0;
    public static int FORM_UDATE=1;
    public static String KEY_KAMUS_DATA="KamusData";
    public static String KEY_FORM_TYPE="type";

    private int formType=0;
    String kata,arti;
    private int id;
    private KamusModel kamusItem;
    private MenuItem itemDelete;
private EnglishApplication application;
    private KamusHelper kamusHelper;





    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.tambahdata);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tkata=(EditText)findViewById(R.id.tambahInd);
        tarti=(EditText)findViewById(R.id.tambahEN);
        bAksi=(Button)findViewById(R.id.bAksi);

        kamusHelper=new KamusHelper(TambahData.this);
        kamusHelper.open();
        application=(EnglishApplication)getApplication();
        application.getKamusObserver().addObserver(this);
        Intent intent =getIntent();
        formType=intent.getIntExtra(KEY_FORM_TYPE,0);
        String title=null;
        if (formType==FORM_INPUT){
            bAksi.setText("TAMBAH");
            title="Tambah Data";

        }else {
            bAksi.setText("UPDATE");
            title="Update Data";
            kamusItem=(KamusModel)intent.getSerializableExtra(KEY_KAMUS_DATA);
            kata= kamusItem.getKata();
            arti=kamusItem.getArti();
            id=kamusItem.getId();

            tkata.setText(kata);
            tarti.setText(arti);
        }
        getSupportActionBar().setTitle(title);


        bAksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word= tkata.getText().toString().trim();
                String meaning=tarti.getText().toString().trim();

                if (!word.equals("") && !meaning.equals("")){
                    if (formType==FORM_INPUT){
                        kamusHelper.insert(KamusModel.getKamusModel(word, meaning));
                        Toast.makeText(TambahData.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();

                    }else {
                        kamusHelper.update(KamusModel.getKamusModel(id, tkata.getText().toString().trim(), tarti.getText().toString().trim()));
                        Toast.makeText(TambahData.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();

                    }
                    application.getKamusObserver().refresh();
                    finish();
                }else {
                    Toast.makeText(TambahData.this,"Semua field wajid di isi !!! ",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
    protected void onDestroy(){
        super.onDestroy();
        kamusHelper.close();
    }

    public static void toTambahData(Activity activity){
        Intent intent=new Intent(activity,TambahData.class);
        intent.putExtra(TambahData.KEY_FORM_TYPE, FORM_INPUT);
        activity.startActivityForResult(intent, 0);


    }

    public static void toTambahData(Activity activity,KamusModel kamusModel){
        Intent intent=new Intent(activity,TambahData.class);
        intent.putExtra(TambahData.KEY_KAMUS_DATA,kamusModel);
        intent.putExtra(TambahData.KEY_FORM_TYPE, FORM_UDATE);
        activity.startActivityForResult(intent, 0);


    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==  android.R.id.home){
            finish();
        }
        if (item.getItemId()==R.id.action_delete){
            deleteData();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menuform, menu);
        itemDelete = menu.findItem(R.id.action_delete);
        if (formType == FORM_INPUT){
            itemDelete.setVisible(false);
        }else{
            itemDelete.setVisible(true);
        }
        return true;
    }
    private void deleteData() {
        kamusHelper.delete(id);
        Toast.makeText(TambahData.this,"Data berhasil dihapus",Toast.LENGTH_SHORT).show();
        application.getKamusObserver().refresh();
        finish();


    }
    public void update(Observable observable, Object o) {

    }
}
