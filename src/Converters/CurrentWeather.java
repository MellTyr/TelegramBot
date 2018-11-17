package Converters;

import Interfaces.IConvertator;
import Weather.WeatherApiModel.Weather;
import Weather.WeatherApiModel.WeatherModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentWeather extends Converter implements IConvertator {
    //NumberFormat format=new DecimalFormat("#.00");

    @Override
    public String Convert(WeatherModel model) {
        StringBuilder sb=new StringBuilder();
        sb.append("Current weather:").append("\n").append("Temperatere: ").append(format.format( model.getMain().getTemp()-273)).append(" C°")
                .append("\n").append("Humidity: ").append(model.getMain().getHumidity()).append(" %")
                .append("\n").append("Speed of wind: ").append(format.format( model.getWind().getSpeed())).append("m/s ").append("direction: ").append(WindDegConvert(model))
                .append("\n").append("Pressure: ").append(format.format((model.getMain().getPressure())/1.33322)).append(" mm Hg")
                .append("\n").append("Cloudiness: ").append(model.getClouds().getAll()).append(" %")
                .append("\n").append("Precipitation: ").append(WeatherConvert(model))
                .append("\n").append("Sunrise: ").append(ConvertTime(model.getSys().getSunrise())).append(" Sunset: ").append(ConvertTime(model.getSys().getSunset()));
        return sb.toString();
    }

    /*
    String WeatherConvert(WeatherModel model){
        StringBuilder sb=new StringBuilder();
        for (Weather weater:model.getWeather()
             ) {sb.append(weater.getDescription()).append("\n");
        }
        return sb.toString();
    }
    String WindDegConvert(WeatherModel model){
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
    }*/
}
