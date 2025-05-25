package com.example.medisyncfrontend.Utils;

import com.example.medisyncfrontend.Controllers.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
public class DBUtils {

    public static void changeScene(ActionEvent event, String FXMLfile, String title, String username){

        Parent root=null;

        if (username != null) {
            try {
                FXMLLoader loader=new FXMLLoader(DBUtils.class.getResource((FXMLfile)));
                root=loader.load();

                LoginController logInController=loader.getController();
//                logInController.setUserInfo(username);


            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                root=FXMLLoader.load(DBUtils.class.getResource(FXMLfile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        Stage stage=(Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("title");
        stage.setScene(new Scene(root,700,500));
        stage.show();
    }

    public static void signUpUser(ActionEvent event,String username, String password){
        Connection connection=null;
        PreparedStatement psInsert=null;
        PreparedStatement psChechUserExists=null;
        ResultSet resultSet=null;
        ResultSet idresultSet=null;
        int userid=-1;
        try {
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/medisync","root","root");
            psChechUserExists=connection.prepareStatement("SELECT * FROM users WHERE user_name=?");
            psChechUserExists.setString(1,username);
            resultSet=psChechUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("already exists");
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username");
                alert.show();

            }else{
                psInsert=connection.prepareStatement("INSERT INTO users (user_name,password) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.executeUpdate();
                idresultSet=psInsert.getGeneratedKeys();
                if (idresultSet.next()) {
                    userid = idresultSet.getInt(1);
                    SessionManager.setLoggedInUserId(userid);
                    changeScene(event, "inventory.fxml", "Welcome", username);
                } else {

                    System.out.println("No generated keys retrieved.");
                }
                SessionManager.setLoggedInUserId(userid);
                changeScene(event,"inventory.fxml","Inventory Managment System",username);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(psChechUserExists!=null){
                try {
                    psChechUserExists.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert!=null){
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if(connection!=null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void logInUser(ActionEvent event,String username,String password){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;


        try {
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3307/javafxproj","root","root");
            preparedStatement=connection.prepareStatement("SELECT user_id, password From users WHERE user_name=?");
            preparedStatement.setString(1,username);
            resultSet=preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){
                System.out.println("user not found");
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provide correct credentials");
                alert.show();
            }else {
                while (resultSet.next()){
                    String retrievedPassword=resultSet.getString("password");
                if(retrievedPassword.equals(password)){
                    int id=resultSet.getInt("user_id");
                    SessionManager.setLoggedInUserId(id);
                    changeScene(event,"inventory.fxml","Inventory Managment System",username);
                }else {
                    System.out.println("passwords did not match");
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.show();
                }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addProduct(ActionEvent event,String product_name, int product_quantity,int user_id){

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try {
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3307/javafxproj","root","root");
            PreparedStatement selectStatment=connection.prepareStatement("SELECT product_id, product_Quantity FROM products Where product_name=? AND user_id=?");
            selectStatment.setString(1,product_name);
            selectStatment.setInt(2,user_id);
            resultSet=selectStatment.executeQuery();
            if(resultSet.next()){

                int existingProductId = resultSet.getInt("product_id");
                int existingQuantity=resultSet.getInt("product_Quantity");
                int newQuantity=existingQuantity+product_quantity;
                preparedStatement=connection.prepareStatement("UPDATE products SET product_Quantity=? WHERE product_id=?");
                preparedStatement.setInt(1,newQuantity);
                preparedStatement.setInt(2,existingProductId);
                preparedStatement.executeUpdate();

            }
            else{
                preparedStatement=connection.prepareStatement("INSERT INTO products (product_name, product_Quantity,user_id) VALUES (?, ?,?)");
                preparedStatement.setString(1,product_name);
                preparedStatement.setInt(2,product_quantity);
                preparedStatement.setInt(3,user_id);
                preparedStatement.executeUpdate();

                ;

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(preparedStatement == null){
                    preparedStatement.close();
                    if(connection==null){
                        connection.close();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void RemoveProduct(ActionEvent event, String product_name,int product_Quantity,int user_id){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3307/javafxproj","root","root");
            PreparedStatement selectStatment=connection.prepareStatement("SELECT product_id, product_Quantity FROM products Where product_name=? AND user_id=?");
            selectStatment.setString(1,product_name);
            selectStatment.setInt(2,user_id);
            resultSet=selectStatment.executeQuery();
            if(resultSet.next()){
                int productID=resultSet.getInt("product_id");
                int existingQuantity=resultSet.getInt("product_Quantity");
                int newQuantity = Math.max(existingQuantity - product_Quantity, 0);
                if(newQuantity==0){
                    preparedStatement=connection.prepareStatement("DELETE FROM products WHERE product_id=?");
                    preparedStatement.setInt(1,productID);

                    preparedStatement.executeUpdate();
                                  }
                else
                {
                    preparedStatement=connection.prepareStatement("UPDATE products SET product_Quantity=? WHERE product_id=?");
                  preparedStatement.setInt(1,newQuantity);
                  preparedStatement.setInt(2,productID);
                    preparedStatement.executeUpdate();
                }


            }
            else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("the "+product_name+" was not found in the inventory");
                alert.show();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }




}
