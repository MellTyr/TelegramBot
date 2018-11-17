package Converters;

import Interfaces.IConvertator;
import User.UserModel;

public class ConvertersFabric {
    static public IConvertator GetConvertator(UserModel user){
        IConvertator convertator=null;
        switch (user.getCommand()){
            case "/forecast":
                convertator=new Forecast();
                break;
            case "/current":
                convertator=new CurrentWeather();
                break;
                default:
                    convertator=new CurrentWeather();
                    break;
        }
        return convertator;
    }
}
