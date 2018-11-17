package Converters;

import Interfaces.IConvertator;
import Weather.WeatherApiModel.WeatherModel;

public class Forecast extends Converter implements IConvertator {
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
}
