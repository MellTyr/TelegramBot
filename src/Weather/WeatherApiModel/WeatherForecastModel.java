package Weather.WeatherApiModel;

public class WeatherForecastModel {
    private City city;

    private WeatherModel[] list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public WeatherModel[] getList() {
        return list;
    }

    public void setList(WeatherModel[] list) {
        this.list = list;
    }
}