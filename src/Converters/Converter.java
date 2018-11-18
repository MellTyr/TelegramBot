package Converters;

import Interfaces.IConvertator;
import Weather.WeatherApiModel.Weather;
import Weather.WeatherApiModel.WeatherModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*начальный класс с общими методами для создания конвертеров моделей, полученных от api погоды*/
public abstract class Converter implements IConvertator {


    public abstract String Convert(WeatherModel model);


    String WeatherConvert(WeatherModel model){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<model.getWeather().length;i++){
            sb.append(model.getWeather()[i].getDescription());
            if(i<model.getWeather().length-1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    NumberFormat format=new DecimalFormat("#.00");

    String WindDegConvert (WeatherModel model){
        double grad=model.getWind().getDeg();
        if((grad>=0&&grad<=22.5)||(337.5<grad&&grad<=360))
            return "N";
        else if(22.5<grad&&grad<=67.5)
            return "NE";
        else if(67.5<grad&&grad<=112.5)
            return "E";
        else if(112.5<grad&&grad<=157.5)
            return "SE";
        else if(157.5<grad&&grad<=202.5)
            return "S";
        else if(202.5<grad&&grad<=247.5)
            return "SW";
        else if(247.5<grad&&grad<=292.5)
            return "W";
        else if(292.5<grad&&grad<=337.5)
            return "NW";
        return "NEWS";
    }


    String ConvertTime(long unixTime){
        Date time=new Date(unixTime*1000);
        SimpleDateFormat formatoToHMS=new SimpleDateFormat("HH:mm:ss");
        return formatoToHMS.format(time);
    }
}
