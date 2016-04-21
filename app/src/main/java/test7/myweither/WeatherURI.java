package test7.myweither;

/**
 * Created by 123 on 2016/3/5.
 */
public class WeatherURI {
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "test7.myweither";

    public static final String WEATHER_URI=CONTENT+AUTHORITY+"/"+WeatherSQL.TABLE_NAME;
}
