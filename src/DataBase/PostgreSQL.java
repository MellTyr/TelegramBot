package DataBase;

import Interfaces.IUser;
import User.UserModel;
import Weather.WeatherApiModel.Sys;
import Weather.WeatherApiModel.coord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PostgreSQL {
    static final String CHAT_ID_COLUMN = "userchatid";
    static final String SUBSCRIBE_COLUMN = "subscribe";
    static final String LAT_COLUMN = "lat";
    static final String LON_COLUMN = "lon";
    static final String COMMAND_COLUMN = "command";

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/UserTelegram";
    static final String USER = "postgres";
    static final String PASS = "A11enWalker";

    private static Boolean ExistInDB(IUser user, Connection connection) {
        Statement statement = null;
        String getUser = "SELECT userchatid FROM public.users WHERE userchatid=" + user.getChatId();
        try {
            connection = getDBConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getUser);
            while (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void TestExistInDB() {
        Connection connection = getDBConnection();
        Boolean exist = ExistInDB(new IUser() {
            @Override
            public String getCommand() {
                return null;
            }

            @Override
            public long getChatId() {
                return 0;
            }

            @Override
            public coord getCoord() {
                return null;
            }

            @Override
            public boolean isSubscribe() {
                return false;
            }
        }, connection);
        System.out.println(exist);
    }

    public static void NewUser(IUser user) {
        Connection connection = getDBConnection();
        Statement statement = null;
        if (!ExistInDB(user, connection)) {
            String insertNewUser = "INSERT INTO public.users (userchatid) VALUES (%s)";
            try {
                statement = connection.createStatement();
                statement.executeUpdate(String.format(insertNewUser, user.getChatId()));
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void UpdateUser(IUser user) {
        Connection connection = getDBConnection();
        Statement statement = null;
        if (!ExistInDB(user, connection)) {
            NewUser(user);
        }
        try {
            statement = connection.createStatement();
            String queryToFormat = "UPDATE public.users SET command = '%s', subscribe = '%s', lat = '%s', lon = '%s' WHERE userchatid ='%s'";
            String qq = String.format(queryToFormat, user.getCommand(), user.isSubscribe(), user.getCoord().getLat(), user.getCoord().getLon(), user.getChatId());
            statement.execute(qq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateUser(IUser user, boolean onlyCommand) {
        Connection connection = getDBConnection();
        Statement statement = null;
        if (!ExistInDB(user, connection)) {
            NewUser(user);
        }
        String queryToFormat;
        String query;
        if (!onlyCommand) {
            queryToFormat = "UPDATE public.users SET command = '%s', subscribe = '%s', lat = '%s', lon = '%s' WHERE userchatid ='%s'";
            query = String.format(queryToFormat, user.getCommand(), user.isSubscribe(), user.getCoord().getLat(), user.getCoord().getLon(), user.getChatId());
        } else {
            queryToFormat = "UPDATE public.users SET command = '%s' WHERE userchatid='%s'";
            query = String.format(queryToFormat, user.getCommand(), user.getChatId());
        }

        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<IUser> getUsersWithSubscribe() {
        Connection connection = getDBConnection();
        String selectQuery = "SELECT userchatid, subscribe, lat, lon, command FROM public.users WHERE subscribe = 'true'";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(selectQuery);
            return UsersFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);
            return dbConnection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    public static IUser getUsetFromDB(long chatId) {
        Connection connection = getDBConnection();
        String selectQuery = "SELECT userchatid, subscribe, lat, lon, command FROM public.users WHERE userchatid = '%s'";
        //IUser iUser;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format(selectQuery, chatId));
            return UsersFromRS(rs).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<IUser> UsersFromRS(ResultSet rs){
        List<IUser> users = new ArrayList<>();
        try{
            while (rs.next()) {
                UserModel userModel=new UserModel();
                userModel.setCommand(rs.getString(COMMAND_COLUMN));
                userModel.setSubscribe(rs.getBoolean(SUBSCRIBE_COLUMN));
                userModel.setCoord(new coord(rs.getDouble(LON_COLUMN), rs.getDouble(LAT_COLUMN)));
                userModel.setChatId(rs.getLong(CHAT_ID_COLUMN));
                users.add(userModel);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    private void TestConnection() {
        System.out.println("Testing connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM public.users");

            while (resultSet.next()) {
                // String str=resultSet.getString("userid")+" :"+resultSet.getString("lat");
                System.out.println(resultSet.getString(2) + "qweqwe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
