package Converters;

import Interfaces.IConvertator;
import Interfaces.IUser;
import User.UserModel;

public class ConvertersFabric {

    /*
    * получение конвертера в зависимости от команды юзера*/
    static public IConvertator GetConvertator(IUser user){
        IConvertator convertator=null;
        switch (user.getCommand()){
            case "forecast":
                convertator=new Forecast();
                break;
            case "current":
                convertator=new CurrentWeather();
                break;
                default:
                    convertator=new CurrentWeather();
                    break;
        }
        return convertator;
    }
}
