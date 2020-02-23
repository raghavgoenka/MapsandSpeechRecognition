package com.example.mapsandspeechrecognition.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {
    public  static  final String Country_Key="country";
    public  static  final float Minimum_Confidence=0.4f;
    public  static  final String Default_Country="NEWZEALAND";
    //23.153717, 79.701729,-41.805587, 171.509357
    public  static  final double Default_Country_Latitude=-41.805587;
    public  static  final double Default_Country_Longitude=171.509357;
    public static  final  String Default_Message="Welcome!!";

    private Hashtable<String,String> countriesAndMessages;
    public CountryDataSource(Hashtable<String,String>countriesAndMessages){
        this.countriesAndMessages=countriesAndMessages;
    }


    public  String matchMinimumConfidence(ArrayList<String> userWordss,float[] confidenceLevel){

 if(userWordss==null || confidenceLevel==null){
     Log.i("outqqqqqqq","ert");
     return Default_Country;
 }
     int numberOfWords=userWordss.size();
        Enumeration<String> countries;
        for(int index1=0;index1<numberOfWords && index1<confidenceLevel.length;index1++){


            if(confidenceLevel[index1]<Minimum_Confidence){
                break;
            }

            String acceptedUserWord=userWordss.get(index1);
            Log.i("outqqqqqqq",acceptedUserWord);


            countries=countriesAndMessages.keys();

            while(countries.hasMoreElements()){
                String selectedCountry=countries.nextElement();
                if(acceptedUserWord.equalsIgnoreCase(selectedCountry)){

                    return acceptedUserWord;

                }
//                else{
//                    return Default_Country;
//                }

            }
           return acceptedUserWord;



        }
        return Default_Country;


 }

    public  String getInfoCountry(String country){


        return  countriesAndMessages.get(country);

    }



}
