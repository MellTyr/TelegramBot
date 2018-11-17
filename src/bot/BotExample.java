package bot;

import Converters.ConvertersFabric;
import Converters.CurrentWeather;
import Converters.Forecast;
import DataBase.PostgreSQL;
import Interfaces.IConvertator;
import Interfaces.ISender;
import Interfaces.IUser;
import User.UserModel;
import Weather.Weather;
import Weather.WeatherApiModel.WeatherForecastModel;
import Weather.WeatherApiModel.WeatherModel;
import Weather.WeatherApiModel.coord;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Level;


public class BotExample extends TelegramLongPollingBot {

    private static BotExample botExample;
    //UserModel user = new UserModel();

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
        //user.setChatId(update.getMessage().getChatId());
        if (update.hasMessage()) {
            PostgreSQL.NewUser(new UserModel(update.getMessage().getChatId()));
            IUser iUser = PostgreSQL.getUsetFromDB(update.getMessage().getChatId());
            UserModel user=getModelFromDB(iUser);
            if (update.getMessage().hasText()) {
                user.setCommand(update.getMessage().getText().replace("/",""));
                switch (update.getMessage().getText()) {
                    case "/start":
                        StringBuilder sb = new StringBuilder();
                        for (BotCommands str : BotCommands.values()
                        ) {
                            sb.append("/").append(str.toString()).append("\n\n");
                        }
                        this.SendToUser(user, sb.toString());
                        PostgreSQL.UpdateUser(user);
                        break;

                    case "/subscribe":
                        this.SendToUser(user, "Send me your location for subscribe");
                        PostgreSQL.UpdateUser(user);
                        break;

                    case "/unsubscribe":
                        this.SendToUser(user,"you have unsubscribed");
                        user.setSubscribe(false);
                        PostgreSQL.UpdateUser(user);
                        break;

                    case "/current":
                        this.SendToUser(user, "Send me your location for current");
                        PostgreSQL.UpdateUser(user);
                        break;
                    case "/forecast":
                        this.SendToUser(user, "Send me your location for forecast");
                        PostgreSQL.UpdateUser(user);
                        break;
                    default:
                        this.SendToUser(user,"Wrong command! try again!");
                        break;
                }
            }

            if (update.getMessage().hasLocation()) {
                user.setCoord(new coord(update.getMessage().getLocation().getLongitude(), update.getMessage().getLocation().getLatitude()) {
                });
                WeatherModel weatherModel = null;
                WeatherForecastModel weatherForecastModel = null;
                switch (user.getCommand().trim()) {
                    case "start":
                    case "current": {
                        try {
                            weatherModel = Weather.getWeather().GetCurrentWeather(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String current = ConvertersFabric.GetConvertator(user).Convert(weatherModel);
                        user.setCommand(BotCommands.start.toString());
                        SendToUser(user, current);
                        PostgreSQL.UpdateUser(user,true);
                        break;
                    }
                    case "forecast": {
                        try {
                            weatherForecastModel = Weather.getWeather().GetForecastWeater(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 3; i++) {
                            String forecast = ConvertersFabric.GetConvertator(user).Convert(weatherForecastModel.getList()[i]);
                            sb.append(forecast).append("\n");
                            sb.append(i);
                        }
                        sb.append("22!222");
                        user.setCommand(BotCommands.start.toString());
                        PostgreSQL.UpdateUser(user,true);
                        SendToUser(user, sb.toString());
                        break;
                    }
                    case "subscribe":
                        user.setSubscribe(true);
                        user.setCommand(BotCommands.start.toString());
                        PostgreSQL.UpdateUser(user);
                        SendToUser(user, "you subscribed to the newsletter at 8 am");
                        break;
                }
            }
        }
    }

    private static UserModel getModelFromDB(IUser iUser){
        UserModel userModel=new UserModel(iUser.getChatId());
        userModel.setCommand(iUser.getCommand());
        userModel.setCoord(iUser.getCoord());
        userModel.setSubscribe(iUser.isSubscribe());
        return userModel;
    }

    private void SendToUser(IUser user, String msg) {
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
