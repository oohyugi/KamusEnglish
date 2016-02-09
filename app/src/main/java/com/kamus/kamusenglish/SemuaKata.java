package com.kamus.kamusenglish;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamus.kamusenglish.adapter.ListKataAdapter;
import com.kamus.kamusenglish.app.EnglishApplication;
import com.kamus.kamusenglish.data.DefaultData;
import com.kamus.kamusenglish.model.KamusModel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by koba on 11/23/15.
 */
public class SemuaKata extends AppCompatActivity implements Observer {

    private ListView listView;
    private FloatingActionButton fab;
    private ArrayList<KamusModel>listKata;
    private KamusHelper kamusHelper;
    public ListKataAdapter adapter;
    private EnglishApplication application;
    private Toolbar toolbar;
    LinearLayout linerror;
    TextView jumlahKata;
    DatabaseHelper datakamus = null;

    private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.semuakata);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
linerror=(LinearLayout)findViewById(R.id.lineError);
        linerror.setVisibility(View.INVISIBLE);
        listView=(ListView)findViewById(R.id.listKata);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        listKata=new ArrayList<KamusModel>();
        jumlahKata=(TextView)findViewById(R.id.jumlahKata);
        application=(EnglishApplication)getApplication();
        application.getKamusObserver().addObserver(this);
        kamusHelper=new KamusHelper(SemuaKata.this);
        kamusHelper.open();
        listKata=kamusHelper.getAllData();

        datakamus = new DatabaseHelper(this);
        db = datakamus.getWritableDatabase();



        if (listKata.size()>0){
            bindData();
        }else {
           //insertDefaultData();
            linerror.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahData.toTambahData(SemuaKata.this);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showMeaningDialog(SemuaKata.this, listKata.get(i));
            }
        });
    }

    private void showMeaningDialog(Activity semuaKata, KamusModel kamusModel) {
        TambahData.toTambahData(semuaKata,kamusModel);
    }




    private void insertDefaultData() {
        new StoreDefaultData().execute();
    }
    public void update(Observable observable, Object o) {
        if (o.equals(KamusObserver.NEED_TO_REFRESH)){
            bindData();
        }
    }






    private class StoreDefaultData extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(SemuaKata.this);
            mProgressDialog.setTitle("Input Darta");
            mProgressDialog.setMessage("Please Wait");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            for (int i = 0; i < DefaultData.defaultData.length; i++) {
                kamusHelper.insert(KamusModel.getKamusModel(DefaultData.defaultData[i][0],
                        DefaultData.defaultData[i][1]));
            }

            listKata = kamusHelper.getAllData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            adapter = new ListKataAdapter(SemuaKata.this, listKata);
            listView.setAdapter(adapter);
        }

    }
    private void bindData() {
linerror.setVisibility(View.INVISIBLE);
        if (listKata.size()>0) {
            listKata.clear();
        }
        listKata = kamusHelper.getAllData();
        adapter = new ListKataAdapter(SemuaKata.this, listKata);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        kamusCursor = db.rawQuery("SELECT *  FROM kamus ", null);
        jumlahKata.setText(String.valueOf(kamusCursor.getCount()));
    }
    @Override
    protected void onDestroy() {
        if (kamusHelper != null){
            kamusHelper.close();
        }
        super.onDestroy();
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==  android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
