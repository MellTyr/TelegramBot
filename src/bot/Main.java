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

    private static void TestDB() {
        PostgreSQL.NewUser(new IUser() {
            @Override
            public String getCommand() {
                return null;
            }

            @Override
            public long getChatId() {
                return 5;
            }

            @Override
            public coord getCoord() {
                return null;
            }

            @Override
            public boolean isSubscribe() {
                return false;
            }
        });
        PostgreSQL.UpdateUser(new IUser() {
            @Override
            public boolean isSubscribe() {
                return true;
            }

            @Override
            public String getCommand() {
                return null;
            }

            @Override
            public long getChatId() {
                return 7;
            }

            @Override
            public coord getCoord() {
                return new coord(1,2);
            }
        });


        IUser user=PostgreSQL.getUsetFromDB(6);
        System.out.println(user.getChatId());
        System.out.println(user.getCoord().getLat());
        System.out.println(user.getCommand());
        System.out.println(user.isSubscribe());
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
