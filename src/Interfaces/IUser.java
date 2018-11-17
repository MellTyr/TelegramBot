package Interfaces;

public interface IUser {
    String getCommand();
    boolean isSubscribe();
    long getChatId();

    Weather.WeatherApiModel.coord getCoord();

}
