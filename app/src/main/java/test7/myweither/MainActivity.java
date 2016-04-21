package test7.myweither;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String HTTPURL = "http://apis.baidu.com/apistore/weatherservice/cityid?cityid=101010100";
    private static final int MESSAGE=666;
    private static String text=null;
    private TextView mTextView;
    private SQLiteDatabase mSQLiteDatabase;
    class MyTread implements Runnable{
        @Override
        public void run() {
            text=parseJSONWithGSON(request(HTTPURL));
        }
    }
    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyTread myTread =new MyTread();
            new Thread(myTread).start();
            if(text!=null) {
                mTextView.setText(text);
                Toast.makeText(MainActivity.this, "刚刚更新了一下", Toast.LENGTH_LONG).show();
            }

            removeCallbacks(myTread);
            sendEmptyMessageDelayed(MESSAGE, 30000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.json);
        Button mButton=(Button) findViewById(R.id.get);
        mButton.setOnClickListener(this);

        WeatherSQL weatherSQL = new WeatherSQL(this);
        mSQLiteDatabase = weatherSQL.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MESSAGE);
    }

    @Override
    public void onClick(View v) {
        new RequestNetworkDataTask().execute(HTTPURL);
    }

    class RequestNetworkDataTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[] params) {

            return  parseJSONWithGSON(request(params[0]));

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mTextView.setText(result);
            mHandler.sendEmptyMessageDelayed(MESSAGE,30000);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private String parseJSONWithGSON(String jsonData) {
        Gson gson =new Gson();
        WeatherofCity weatherofcity = gson.fromJson(jsonData,WeatherofCity.class);
        addData(WeatherofCity.CITY, weatherofcity.getRetData().getmCity());
        addData(WeatherofCity.CITYCODE, weatherofcity.getRetData().getmCityCode());
        addData(WeatherofCity.DATE, weatherofcity.getRetData().getmDate());
        addData(WeatherofCity.TIME, weatherofcity.getRetData().getmTime());
        addData(WeatherofCity.WEATHER, weatherofcity.getRetData().getmWeather());
        addData(WeatherofCity.H_TMP, weatherofcity.getRetData().getmH_tmp());
        addData(WeatherofCity.L_TMP, weatherofcity.getRetData().getmL_tmp());
        addData(WeatherofCity.WINDS, weatherofcity.getRetData().getmWindS());

        return searchWeatherSQL(WeatherURI.WEATHER_URI);

    }

    public static String request(String urlString) {
        BufferedReader reader ;
        String result = null;
        StringBuilder sbf = new StringBuilder();


        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "875fa0ed0f1d7e6aa2d87b3ae0aa062c");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead ;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void addData(String title, String content) {

        Cursor datacursor = mSQLiteDatabase.query(WeatherSQL.TABLE_NAME, null, null, null, null, null, null);
        ContentValues contentValues = new ContentValues();
        if(datacursor.moveToFirst()){
            if(!searchSameName(datacursor,title)){
                putDataInSQL(title,content,mSQLiteDatabase);

            }else {
                contentValues.put(WeatherSQL.CONTENT, content);
                String whereClauseString = "title=?";
                String[] whereArgsString = {title};
                mSQLiteDatabase.update(WeatherSQL.TABLE_NAME, contentValues, whereClauseString, whereArgsString);
            }
        }else {
            putDataInSQL(title,content,mSQLiteDatabase);
        }

        datacursor.close();

    }

    private  void putDataInSQL(String title, String content,SQLiteDatabase mSQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherSQL.TITLE,title);
        contentValues.put(WeatherSQL.CONTENT,content);

        mSQLiteDatabase.insert(WeatherSQL.TABLE_NAME, null, contentValues);

    }

    private static boolean searchSameName(Cursor cursor, String name){
        int count = cursor.getCount();
        for(int i=0;i<count;i++){
            if(cursor.getString(cursor.getColumnIndexOrThrow(WeatherSQL.TITLE)).equals(name)){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }


    private String searchWeatherSQL(String stringURI){
        Uri uri = Uri.parse(stringURI);
        ContentResolver contentResolver =getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        String weatherofcity=null;
        if(cursor != null){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                if(weatherofcity==null){
                    weatherofcity=cursor.getString(cursor.getColumnIndex(WeatherSQL.CONTENT));
                }else {
                    weatherofcity=weatherofcity+"\n"+cursor.getString(cursor.getColumnIndex(WeatherSQL.CONTENT));
                }

                cursor.moveToNext();
            }

        }else {
            return null;
        }

        cursor.close();

        return weatherofcity;
    }
}
