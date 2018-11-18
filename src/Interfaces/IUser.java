package Interfaces;

public interface IUser extends HaveCoordinate {
    String getCommand();
    boolean isSubscribe();
    long getChatId();

   // Weather.WeatherApiModel.coord getCoord();

}
