package Converters;

import Interfaces.IConvertator;
import Weather.WeatherApiModel.WeatherModel;

public class Forecast extends Converter implements IConvertator {
    @Override
    public String Convert(WeatherModel model) {
        StringBuilder sb=new StringBuilder();
        sb.append("Time: ").append(ConvertTime(model.getDt()))
                .append("Temperatere: ").append(format.format( model.getMain().getTemp()-273)).append(" C°")
                .append("\n").append("Humidity: ").append(model.getMain().getHumidity()).append(" %")
                .append("\n").append("Speed of wind: ").append(format.format( model.getWind().getSpeed())).append("m/s ").append("direction: ").append(WindDegConvert(model))
                .append("\n").append("Precipitation: ").append(WeatherConvert(model));
        return sb.toString();
    }
}
