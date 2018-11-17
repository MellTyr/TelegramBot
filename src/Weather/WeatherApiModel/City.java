package Weather.WeatherApiModel;

public class City {
    private long id;
    private String name;
    private coord coord;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public coord getCoord() {
        return coord;
    }

    public void setCoord(coord coord) {
        this.coord = coord;
    }*/

}
