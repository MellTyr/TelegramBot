package bot;

import Converters.ConvertersFabric;
import DataBase.PostgreSQL;
import Interfaces.IUser;
import User.UserModel;
import Weather.Weather;
import Weather.WeatherApiModel.WeatherForecastModel;
import Weather.WeatherApiModel.WeatherModel;
import Weather.WeatherApiModel.coord;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class BotExample extends TelegramLongPollingBot {

    private static BotExample botExample;

    public static BotExample getBotExample() {
        if (botExample == null) {
            botExample = new BotExample();
        }
        return botExample;
    }

    private BotExample() {
    }

    /*
    * выполняется при поступлении сообщения из телеграмма
    * каждое сообщение обрабатывается в отдельном потоке
    * тк идет общение к БД и к api погоды*/

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Dialog(update);
                }
            });
            thread.run();
        }
    }
    /*
    * основной распределитель логики
    * 1. взять из бд пользователя если он там есть, или создать нового
    * 2. в зависимости от существования команды или координат выполняется:
    *   2.1 установка команды ползователя
    *   2.2 обращения к api погода
    * 3. ответ пользователю
    * */
    private void Dialog(Update update) {
        PostgreSQL.NewUser(new UserModel(update.getMessage().getChatId()));
        IUser iUser = PostgreSQL.getUsetFromDB(update.getMessage().getChatId());
        UserModel user = getModelFromDB(iUser);
        if (update.getMessage().isCommand()) {
            user.setCommand(update.getMessage().getText().replace("/", ""));
            this.SetCommand(update.getMessage().getText(), user);
        }
        if (update.getMessage().hasLocation()) {

            user.setCoord(
                    new coord(
                            update.getMessage().getLocation().getLongitude(),
                            update.getMessage().getLocation().getLatitude())
            );
            SendWhether(user);
        }
    }
    /*
    * выбор типа погоды,
     * обращение к api погоды,
      * конвертация json в string,
      * отправка сообщения пользователю
      * обращение в бд для установки начальной команды и подписки
      * */
    private void SendWhether(UserModel user) {
        WeatherModel weatherModel = null;
        WeatherForecastModel weatherForecastModel = null;
        switch (user.getCommand().trim()) {
            case "start":
            case "current": {
                user.setCommand(BotCommands.current.toString());
                try {
                    weatherModel = Weather.getWeather().GetCurrentWeather(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String current = ConvertersFabric.GetConvertator(user).Convert(weatherModel) + "\n/start\n";
                user.setCommand(BotCommands.start.toString());
                SendToUser(user, current);
                PostgreSQL.UpdateUser(user, true);
                break;
            }
            case "forecast": {
                user.setCommand(BotCommands.forecast.toString());
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
                sb.append("/start\n");
                user.setCommand(BotCommands.start.toString());
                PostgreSQL.UpdateUser(user, true);
                SendToUser(user, sb.toString());
                break;
            }
            case "subscribe":
                user.setSubscribe(true);
                user.setCommand(BotCommands.start.toString());
                PostgreSQL.UpdateUser(user,false);
                SendToUser(user, "you subscribed to the newsletter at 8 am\n/start\n");
                break;
        }
    }
    /*
    * установка команды для пользователя в БД
    * */
    private void SetCommand(String command, UserModel user) {
        switch (command) {
            case "/start":
                StringBuilder sb = new StringBuilder();
                for (BotCommands str : BotCommands.values()
                ) {
                    sb.append("/").append(str.toString()).append("\n\n");
                }
                this.SendToUser(user, sb.toString());
                PostgreSQL.UpdateUser(user, true);
                break;

            case "/subscribe":
                this.SendToUser(user, "Send me your location for subscribe\n/start\n");
                PostgreSQL.UpdateUser(user, true);
                break;

            case "/unsubscribe":
                this.SendToUser(user, "you have unsubscribed\n/start\n");
                user.setSubscribe(false);
                PostgreSQL.UpdateUser(user,false);
                break;

            case "/current":
                this.SendToUser(user, "Send me your location for current\n/start\n");
                PostgreSQL.UpdateUser(user, true);
                break;

            case "/forecast":
                this.SendToUser(user, "Send me your location for forecast\n/start\n");
                PostgreSQL.UpdateUser(user, true);
                break;

            default:
                this.SendToUser(user, "Wrong command! try again!\n/start\n");
                break;
        }
    }
    /*создание модели пользователя из информации полученной от БД*/
    private static UserModel getModelFromDB(IUser iUser) {
        UserModel userModel = new UserModel(iUser.getChatId());
        userModel.setCommand(iUser.getCommand());
        userModel.setCoord(iUser.getCoord());
        userModel.setSubscribe(iUser.isSubscribe());
        return userModel;
    }
    /*отправка сообщения пользователю*/
    /*как можно создать такой же метод в другом классе без наследования от бота?*/
    public void SendToUser(IUser user, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId())
                .setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "whethebot";
    }

    @Override
    public String getBotToken() {
        return "722282223:AAFYD-LqrCe77NGiQOHLTsllGY1U7IOP7LM";
    }

}
