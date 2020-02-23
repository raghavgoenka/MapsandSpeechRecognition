package com.example.mapsandspeechrecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapsandspeechrecognition.model.CountryDataSource;

import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int SPEAK_REQUEST=10;
    TextView textView;
    Button button;
    public  static  CountryDataSource countryDataSource;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.value);
        button=findViewById(R.id.button);

//        CookieHandler.setDefault(new MainActivity(null,CookiePolicy.ACCEPT_ALL));
//
//        for (int i = 0; i < IDs.size(); i++) {
//            InputStream inputStream = new URL("https://awebsite/" + IDs.get(i)).openStream();
//            inputStream.close();
//        }

        button.setOnClickListener(MainActivity.this);

        Hashtable<String,String> countriesAndMessages=new Hashtable<>();
       // countriesAndMessages.put("India","Welcome to India.Happy Visiting");
        countriesAndMessages.put("USA","Welcome to USA.Happy Visiting");
       // countriesAndMessages.put("India","Welcome to USA.Happy Visiting");
        //countriesAndMessages.put("Aditya","Welcome to USA.Happy Visiting");

        countryDataSource=new CountryDataSource(countriesAndMessages);

        // tells whether our phone support speech recogniton or not!
        PackageManager packageManager=this.getPackageManager();

        List<ResolveInfo>list=packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(list.size()>0){
            Toast.makeText(MainActivity.this,"your device support speech recognition",Toast.LENGTH_SHORT).show();
            listenToUserVoice();
        }
        else{

            Toast.makeText(MainActivity.this,"your device does not support speech recognition",Toast.LENGTH_SHORT).show();
        }
    }


    private void listenToUserVoice(){
        // This is to get speech recogniton using intent object and to accept that in any language!
        Intent voiceIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Talk To Me!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
        startActivityForResult(voiceIntent,SPEAK_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPEAK_REQUEST && resultCode==RESULT_OK){
    // getting user voice words and setting it to text view!
            ArrayList<String>voiceWords=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            float[] confidLevels=data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES );

            int index=0;
            for(String userWords:voiceWords){

                if(confidLevels!=null && index<confidLevels.length){
                    textView.setText(userWords+"-"+confidLevels[index]);
                }
            }

         String countryMachedWithuserWord=countryDataSource.matchMinimumConfidence(voiceWords,confidLevels);
         Intent map=new Intent(MainActivity.this , MapsActivity.class);
         try{
         map.putExtra(CountryDataSource.Country_Key,countryMachedWithuserWord);
            Log.i("outqqqqqqq",countryMachedWithuserWord);
         startActivity(map);}catch (Exception e){
             Log.i("Error", "error");
         }


        }
    }

    @Override
    public void onClick(View view){

        listenToUserVoice();
    }
}
