package botInfo;
import org.openweathermap.api.DataWeatherClient;
import org.openweathermap.api.UrlConnectionDataWeatherClient;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.*;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeteoCall {
    private static final String API_KEY = "986a68cefa62de8fc7001b466db7ae6a";

    public static String getMeteoNow() {
        DataWeatherClient client = new UrlConnectionDataWeatherClient(API_KEY);
        CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
                .currentWeather()                   // get current weather
                .oneLocation()                      // for one location
                .byCityName("Toulouse")              // for Toulouse city
                .countryCode("FR")                  // in France
                .type(Type.ACCURATE)                // with Accurate search
                .language(Language.FRENCH)         // in English language
                .responseFormat(ResponseFormat.JSON)// with JSON response format
                .unitFormat(UnitFormat.METRIC)      // in metric units
                .build();
        CurrentWeather currentWeather = client.getCurrentWeather(currentWeatherOneLocationQuery);
        StringBuilder str=new StringBuilder();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        str.append("Il fait "+currentWeather.getMainParameters().getTemperature()+"°C à "+currentWeather.getCityName()+" le "+sdf.format(new Date()));
        return str.toString();
    }
}

