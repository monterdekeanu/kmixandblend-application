package com.unit.kmixandblendapplication;

import java.sql.*;

public class JDBCObject {
    private static final String DATABASE_URL ="jdbc:mysql://localhost:3306/kmixdatabase?useSSL=false";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";
    private static final String SELECT_QUERY = "SELECT * FROM logincredentials WHERE adminID = ? AND adminPassword = ?";

    public boolean validateCredentials(String username, String password){
        //establish connection
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            //create statements
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }catch(SQLException err){
            printSQLException(err);
        }
        return false;
    }

    public Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            return connection;
        }catch(SQLException err){
            printSQLException(err);
        }
        return null;
    }

    public static void printSQLException(SQLException ex){
        for(Throwable e: ex){
            if(ex instanceof SQLException){
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException)e).getSQLState());
                System.err.println("Error Code: "+ ((SQLException)e).getErrorCode());
                System.err.println("Message: "+ ex.getMessage());
                Throwable t = ex.getCause();
                while(t != null){
                    System.err.println("Cause: "+t);
                    t = t.getCause();
                }
            }
        }
    }

}
