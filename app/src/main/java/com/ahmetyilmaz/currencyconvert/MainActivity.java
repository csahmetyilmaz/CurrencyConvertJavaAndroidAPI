package com.ahmetyilmaz.currencyconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tryText;
    TextView usdText;
    TextView cadText;
    TextView jpyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText=findViewById(R.id.tryText);
        usdText=findViewById(R.id.usdText);
        cadText=findViewById(R.id.cadText);
        jpyText=findViewById(R.id.jpyText);
    }

    public void getRates(View view){

        DownloadData downloadData =new DownloadData();
        try {
            String url ="http://data.fixer.io/api/latest?access_key=75f3a396fa77cd45201ad8542ef37b00&format=1";
            downloadData.execute(url);
        }catch (Exception exc){
            exc.printStackTrace();
        }


    }

    private class DownloadData extends AsyncTask<String,Void,String>{


        //ask data with URL -> get data as String
        @Override
        protected String doInBackground(String... strings) {

            String result ="";
            URL url;
            HttpURLConnection httpURLConnection;

            try{
                url = new URL(strings[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                //getting data character par character
                while (data>0){
                    char character =(char) data;
                    result += character;
                    data=inputStreamReader.read();

                }

                return result;

            }catch (Exception exc){
                exc.printStackTrace();
                return null;
            }


        }

        //what to do with json data
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           // -test// System.out.println("got data : "+s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                //getting base currency
                String base=jsonObject.getString("base");
                System.out.println("base: "+base);

                String rates=jsonObject.getString("rates");
                //System.out.println("rates: "+rates);

                //to get only dollar FROM RATES
                JSONObject jsonObject1=new JSONObject(rates);

                String usDollar=jsonObject1.getString("USD");
                usdText.setText("USD: "+usDollar);

                String cad=jsonObject1.getString("CAD");
                cadText.setText("CAD: "+cad);

                String jpy=jsonObject1.getString("JPY");
                jpyText.setText("JPY: "+jpy);

                String trLira=jsonObject1.getString("TRY");
                tryText.setText("TRY: "+trLira);

            }catch (Exception exc){
                exc.printStackTrace();
            }

        }

    }
}
