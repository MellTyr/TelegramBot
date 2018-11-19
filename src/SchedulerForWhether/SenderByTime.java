package SchedulerForWhether;

import Converters.Converter;
import Converters.ConvertersFabric;
import DataBase.PostgreSQL;
import Interfaces.IConvertator;
import Interfaces.IUser;
import Weather.Weather;
import Weather.WeatherApiModel.WeatherModel;
import Weather.WeatherApiModel.coord;
import bot.BotCommands;
import bot.BotExample;
import it.sauronsoftware.cron4j.Scheduler;

import java.util.List;

public class SenderByTime {

    private static SenderByTime senderByTime;

    private SenderByTime() {}

    public static SenderByTime getSenderByTime() {
        if(senderByTime==null){
            senderByTime=new SenderByTime();
        }
        return senderByTime;
    }

    /*запуск планировщика по шаблону*/
    public void senders(){
        Scheduler scheduler=new Scheduler();
        scheduler.schedule("00 08 * * *", new Runnable() {
            @Override
            public void run() {
                send();
                System.out.println("Send ok!");
            }
        });
        scheduler.start();
    }

    /*отправка прогноза на 12 часов каждому из списка полученного из бд*/
    private void send(){
       List<IUser> users=PostgreSQL.getUsersWithSubscribe();
       for(IUser user:users){
           IConvertator converter= ConvertersFabric.GetConvertator(new IUser() {
               @Override
               public String getCommand() {
                   return BotCommands.forecast.toString();
               }

               @Override
               public boolean isSubscribe() {
                   return false;
               }

               @Override
               public long getChatId() {
                   return 0;
               }

               @Override
               public coord getCoord() {
                   return null;
               }
           });
           Weather weather=Weather.getWeather();
           WeatherModel[] weatherModel=weather.GetForecastWeater(user).getList();
           for(int i=0;i<4;i++){
               BotExample.getBotExample().SendToUser(user,converter.Convert(weatherModel[i]));
           }
       }
    }
}
