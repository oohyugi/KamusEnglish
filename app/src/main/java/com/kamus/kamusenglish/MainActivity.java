package com.kamus.kamusenglish;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kamus.kamusenglish.model.KamusModel;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    KamusHelper kamusHelper;
    TextToSpeech speech;
    TextView tEnglish, tIndonesia;
    EditText inputText, terjemahan;
    ImageButton btChange;
    ImageButton btTerjemah, btSpeech;
    int cEnglish = 0, cIndonesia = 1;
    public static String KEY_CHANGE_TYPE = "type";
    String bInput, bTerjemahan;
    int changetype = 0;
    DatabaseHelper datakamus = null;

    private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        kamusHelper = new KamusHelper(MainActivity.this);
        kamusHelper.open();
        datakamus = new DatabaseHelper(this);
        db = datakamus.getWritableDatabase();
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.getDefault());

                }

            }
        });


        tEnglish = (TextView) findViewById(R.id.tEnglish);
        tIndonesia = (TextView) findViewById(R.id.tIndonesia);
        btChange = (ImageButton) findViewById(R.id.bChange);
        inputText = (EditText) findViewById(R.id.inputText);
        terjemahan = (EditText) findViewById(R.id.terjemahan);
        btTerjemah = (ImageButton) findViewById(R.id.btTerjemah);
        btSpeech = (ImageButton) findViewById(R.id.btSpeech);

        final Intent intent = getIntent();


        changetype = intent.getIntExtra(KEY_CHANGE_TYPE, 0);
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changetype == cEnglish) {
                    tEnglish.setText("Indonesia");
                    tIndonesia.setText("English");

                    btSpeech.setVisibility(View.INVISIBLE);

                    changetype = intent.getIntExtra(KEY_CHANGE_TYPE, 1);
                } else {
                    tEnglish.setText("English");
                    tIndonesia.setText("Indonesia");
                    changetype = intent.getIntExtra(KEY_CHANGE_TYPE, 0);
                    btSpeech.setVisibility(View.VISIBLE);
                   /**/

                }

            }
        });
        btTerjemah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bInput = inputText.getText().toString().trim();
                bTerjemahan = terjemahan.getText().toString().trim();
                KamusModel kamusModel = new KamusModel();

                terjemahan.requestFocus();


                if (changetype == cIndonesia) {
                    String result = "";
                    String result2 = "";
                    kamusCursor = db.rawQuery("SELECT _id, kata, arti " + "FROM kamus where kata = '" + bInput + "'ORDER BY kata", null);
                    btSpeech.setVisibility(View.VISIBLE);
                    //Membaca data dari kamusCursor dan menampungnya di result
                    //Membaca string di index 2 yaitu bahasa indonesia
                    if (kamusCursor.moveToFirst()) {
                        result = kamusCursor.getString(2);
                        for (; !kamusCursor.isAfterLast(); kamusCursor.moveToNext()) {
                            result = kamusCursor.getString(2);
                        }
                    }

                    //Membaca data dari kamusCursor dan menampungnya di result


                    //Menampilkan not found jika data tidak sesuai atau tidak ada di database
                    if (result.equals("")) {
                        result = "Terjemahan Not Found";
                    }



                    //Menampilkan datanya setelah ditampung
                    terjemahan.setText(result);


                } else {
                    String result = "";
                    String result2 = "";
                    kamusCursor = db.rawQuery("SELECT _id, kata, arti " + "FROM kamus where arti = '" + bInput + "'ORDER BY arti", null);

                    //Membaca data dari kamusCursor dan menampungnya di result
                    //Membaca string di index 1 yaitu bahasa inggris
                    if (kamusCursor.moveToFirst()) {
                        result = kamusCursor.getString(1);
                        for (; !kamusCursor.isAfterLast(); kamusCursor.moveToNext()) {
                            result = kamusCursor.getString(1);
                        }
                    }

                    //Membaca data dari kamusCursor dan menampungnya di result


                    //Menampilkan not found jika data tidak sesuai atau tidak ada di database
                    if (result.equals("")) {
                        result = "Terjemahan Not Found";
                    }
                    Log.d("Query", String.valueOf(kamusCursor));


                    //Menampilkan datanya setelah ditampung
                    terjemahan.setText(result);

                }
            }
        });


        btSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tSpeechEn = inputText.getText().toString();
                String tSpeech = terjemahan.getText().toString();
                if (changetype == cEnglish) {
                    speech.speak(tSpeechEn, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(), tSpeechEn, Toast.LENGTH_SHORT).show();
                } else {
                    speech.speak(tSpeech, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(), tSpeech, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lihatdata) {
            startActivity(new Intent(MainActivity.this, SemuaKata.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
