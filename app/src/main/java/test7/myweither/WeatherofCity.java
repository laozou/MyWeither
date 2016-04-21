package test7.myweither;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 123 on 2016/3/4.
 */
public class WeatherofCity {

    private RetData retData;
    public static final String CITY="city";
    public static final String CITYCODE="citycode";
    public static final String DATE="date";
    public static final String TIME="time";
    public static final String WEATHER="weather";
    public static final String L_TMP="l_tmp";
    public static final String H_TMP="h_tmp";
    public static final String WINDS="WS";

    public RetData getRetData() {
        return retData;
    }

    public void setRetData(RetData retData) {
        this.retData = retData;
    }


    public class RetData{
        @SerializedName("city")
        private String mCity;
        @SerializedName("citycode")
        private String mCityCode;
        @SerializedName("date")
        private String mDate;
        @SerializedName("time")
        private String mTime;
        @SerializedName("weather")
        private String mWeather;
        @SerializedName("l_tmp")
        private String mL_tmp;
        @SerializedName("h_tmp")
        private String mH_tmp;
        @SerializedName("WS")
        private String mWindS;

        public String getmCity(){
            return mCity;
        }

        public void setmCity(String city){
            mCity=city;
        }

        public String getmCityCode(){
            return mCityCode;
        }

        public void setmCityCode(String citycode){
            mCityCode=citycode;
        }

        public String getmDate(){
            return mDate;
        }

        public void setmDate(String date){
            mDate=date;
        }

        public String getmTime(){
            return mTime;
        }

        public void setmTime(String time){
            mTime=time;
        }

        public String getmWeather(){
            return mWeather;
        }

        public void setmWeather(String weather){
            mWeather=weather;
        }

        public String getmL_tmp(){
            return mL_tmp;
        }

        public void setmL_tmp(String l_tmp){
            mL_tmp=l_tmp;
        }

        public String getmH_tmp(){
            return mH_tmp;
        }

        public void setmH_tmp(String h_tmp){
            mH_tmp=h_tmp;
        }

        public String getmWindS() {
            return mWindS;
        }

        public void setmWindS(String windS) {
            mWindS = windS;
        }
    }


}
