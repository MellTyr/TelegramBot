package User;

import Interfaces.HaveCoordinate;
import Interfaces.IUser;
import Weather.WeatherApiModel.coord;

public class UserModel implements HaveCoordinate, IUser {
    private long ChatId;
    private coord coord;
    private String command;
    private boolean subscribe;

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getChatId() {
        return ChatId;
    }

    public void setChatId(long chatId) {
        ChatId = chatId;
    }

    public Weather.WeatherApiModel.coord getCoord() {
        return coord;
    }

    public void setCoord(Weather.WeatherApiModel.coord coord) {
        this.coord = coord;
    }

    public UserModel() {
        this.command = "/start";
        this.coord=new coord();
    }
    public UserModel(long userChatId) {
        this.command = "/start";
        this.coord=new coord();
        this.ChatId=userChatId;
    }
}
