package bot;

import DataBase.PostgreSQL;
import Interfaces.IUser;
import SchedulerForWhether.SenderByTime;
import Weather.Weather;
import Weather.WeatherApiModel.Sys;
import Weather.WeatherApiModel.WeatherForecastModel;
import Weather.WeatherApiModel.coord;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.*;

public class Main {


    public static void main(String[] args) {
        Register();
        SenderByTime senderByTime=SenderByTime.getSenderByTime();
        senderByTime.senders();
    }

    private static void Register() {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "127.0.0.1");
        System.getProperties().put("socksProxyPort", "9150");

        //Initialize Api Context

        ApiContextInitializer.init();

        //Instantiate Telegram Bots Api
        TelegramBotsApi botsapi = new TelegramBotsApi();

        //Register our bot
        try {
            botsapi.registerBot(BotExample.getBotExample());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
