package bot;

import Converters.ConvertersFabric;
import Converters.CurrentWeather;
import Converters.Forecast;
import Interfaces.IConvertator;
import Interfaces.ISender;
import User.UserModel;
import Weather.Weather;
import Weather.WeatherApiModel.WeatherForecastModel;
import Weather.WeatherApiModel.WeatherModel;
import Weather.WeatherApiModel.coord;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Level;


public class BotExample extends TelegramLongPollingBot {

    private static BotExample botExample;
    UserModel user = new UserModel();

    public static BotExample getBotExample() {
        if (botExample == null) {
            botExample = new BotExample();
        }
        return botExample;
    }

    private BotExample() {
    }

    /**
     * Метод для приема сообщений
     *
     * @param update Содержит сообщение от пользователя
     */

    @Override
    public void onUpdateReceived(Update update) {


        //todo
        //проверка существование ползователя в базе данных
        //

        user.setChatId(update.getMessage().getChatId());


        if (update.hasMessage()) {

            if (update.getMessage().hasText()) {
                switch (update.getMessage().getText()) {
                    case "/start":
                        StringBuilder sb = new StringBuilder();
                        for (BotCommands str : BotCommands.values()
                        ) {
                            sb.append("/").append(str.toString()).append("\n\n");
                        }
                        this.SendToUser(user, sb.toString());
                        break;

                    case "/subscribe":
                        //todo
                        //метод создания в БД юзера по айди
                        this.SendToUser(user,"Send me your location for subscribe");
                        //todo
                        //переделать на БД
                        user.setCommand("subscribe");
                        break;

                    case "/current":
                        this.SendToUser(user,"Send me your location for current");
                        //todo
                        //переделать на БД
                        user.setCommand("current");
                        break;
                    case "/forecast":
                        this.SendToUser(user,"Send me your location for forecast");
                        //todo
                        //переделать на БД
                        user.setCommand("forecast");
                        break;
                }
            }

            if (update.getMessage().hasLocation()) {
                user.setCoord(new coord(update.getMessage().getLocation().getLongitude(), update.getMessage().getLocation().getLatitude()) {
                });
                WeatherModel weatherModel = null;
                WeatherForecastModel weatherForecastModel = null;
                switch (user.getCommand()) {
                    case "start":
                    case "current": {
                        try {
                            weatherModel = Weather.getWeather().GetCurrentWeather(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "forecast": {
                        try {
                            weatherForecastModel = Weather.getWeather().GetForecastWeater(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                switch (user.getCommand()) {
                    case "subscribe":
                        //todo
                        //реализовать подписку
                        //
                        SendToUser(user, "Вы подписались на ежедневную рассылку");
                        break;
                    case "start":
                    case "current":
                        String current = ConvertersFabric.GetConvertator(user).Convert(weatherModel);
                        //System.out.println("qweasd");
                        SendToUser(user, current);
                        break;
                    case "forecast":{
                        StringBuilder sbq=new StringBuilder();
                        for(int i=0;i<3;i++) {
                            String forecast = ConvertersFabric.GetConvertator(user).Convert(weatherForecastModel.getList()[i]);
                            sbq.append(forecast).append("\n");
                            sbq.append(i);
                        }
                        sbq.append("22!222");

                        SendToUser(user,sbq.toString());
                        break;
                    }
                }
            }
        }
    }

    private void SendToUser(UserModel user, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId())
                .setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String MsgFromWeatherModel(WeatherModel model) {
        StringBuilder sb = new StringBuilder();
        sb.append("Current temperature").append(model.getMain().getTemp());
        return sb.toString();
    }

    @Override
    public String getBotUsername() {
        return "whethebot";
    }

    @Override
    public String getBotToken() {
        return "722282223:AAFYD-LqrCe77NGiQOHLTsllGY1U7IOP7LM";
    }

    public void Send() {
        String messageText = "From computer";
        //мой
        long chatId = 214866159;
        //ася
        //long chatId=600971067;
        org.telegram.telegrambots.meta.api.methods.send.SendMessage message = new org.telegram.telegrambots.meta.api.methods.send.SendMessage()
                .setChatId(chatId)
                .setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
