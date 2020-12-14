package Server;

import Connection.To_Client;
import Connection.To_Database;
import Usages.Product;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    ObjectInputStream sois;
    ObjectOutputStream soos;
    static int kol;
    public User(ObjectInputStream sois, ObjectOutputStream soos, String account)
    {
        this.sois = sois;
        this.soos = soos;
        boolean Menu = true;
        while(Menu) {
            try {
                String selec1 = "Select COUNT(idadmin) as id from user.admin where valid = '+'";
                Statement statement2 = To_Database.connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(selec1);

                while (resultSet2.next()) {
                    kol = resultSet2.getInt("id");
                    //System.out.print(kol);
                }
                resultSet2.close();
                soos.writeObject(String.valueOf(kol));
                Product[] ob = new Product[kol];
                String select = "Select * from user.admin where valid = '+'";
                Statement statement3 = To_Database.connection.createStatement();
                ResultSet resultSet3 = statement3.executeQuery(select);
                int q = 0;
                while (resultSet3.next()) {
                    String productName = resultSet3.getString("productName");
                    double price = resultSet3.getDouble("price");
                    double weight = resultSet3.getDouble("weight");
                    double protein = resultSet3.getDouble("protein");
                    double fats = resultSet3.getDouble("fats");
                    double carbohydrates = resultSet3.getDouble("carbohydrates");
                    double nutritionalValue = resultSet3.getDouble("nutritionalValue");
                    String composition = resultSet3.getString("composition");

                    ob[q] = new Product(productName, price, weight, protein, fats, carbohydrates, nutritionalValue, composition);
                    soos.writeObject(productName);
                    soos.writeObject(String.valueOf(price));
                    soos.writeObject(String.valueOf(weight));
                    soos.writeObject(String.valueOf(protein));
                    soos.writeObject(String.valueOf(fats));
                    soos.writeObject(String.valueOf(carbohydrates));
                    soos.writeObject(String.valueOf(nutritionalValue));
                    soos.writeObject(composition);
                    q++;

                }
                resultSet3.close();
                String num = (String) sois.readObject();
                if (num.equals("1")) {
                    add(account);
                }
                else if (num.equals("2"))
                {
                    basket(account);
                }
                else if(num.equals("statistics"))
                {
                    statistic();
                }
                else if (num.equals("back")) {
                    To_Client.Connect(sois, soos);
                    Menu = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void statistic()
    {
        try {
            String select = "Select mark from user.customer where purchase = 'Молоко'";
            Statement statement = To_Database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int markMilk = 0;
            while (resultSet.next()) {
                if(resultSet.getInt("mark") != 0) {
                    if (markMilk == 0) {
                        markMilk = resultSet.getInt("mark");
                    } else if (markMilk != 0) {
                        markMilk = (markMilk + resultSet.getInt("mark")) / 2;
                    }
                }
            }
            resultSet.close();

            String select1 = "Select mark from user.customer where purchase = 'Творог'";
            Statement statement1 = To_Database.connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(select1);
            int markCottage = 0;
            while (resultSet1.next()) {
                if(resultSet1.getInt("mark") != 0) {
                    if (markCottage == 0) {
                        markCottage = resultSet1.getInt("mark");
                    } else if (markCottage != 0) {
                        markCottage = (markCottage + resultSet1.getInt("mark")) / 2;
                    }
                }
            }
            resultSet1.close();

            String select2 = "Select mark from user.customer where purchase = 'Сыр'";
            Statement statement2 = To_Database.connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(select2);
            int markCheese = 0;
            while (resultSet2.next()) {
                if(resultSet2.getInt("mark") != 0) {
                    if (markCheese == 0) {
                        markCheese = resultSet2.getInt("mark");
                    } else if (markCheese != 0) {
                        markCheese = (markCheese + resultSet2.getInt("mark")) / 2;
                    }
                }
            }
            resultSet2.close();

            FileReader fr = new FileReader("SurveyStatistic.txt");
            BufferedReader br = new BufferedReader(fr);
            String s;
            String[][] tokens = new String[100][3];
            int c = 0;
            while ((s = br.readLine()) != null)
            {
                tokens[c] = s.split(" ");
                c++;
            }
            //4. считать все сессии из файла и записать в массив, его отправить пользователю
            //5. на стороне пользователя записать их диаграмму и построить

            int id = Integer.parseInt(tokens[c-1][0]) + 1;
            soos.writeObject(String.valueOf(id));
            for(int i = 0;i < id;i++)
            {
                if(i == (id-1))
                {
                    soos.writeObject(String.valueOf(markMilk));
                    soos.writeObject(String.valueOf(markCottage));
                    soos.writeObject(String.valueOf(markCheese));
                }
                else {
                    soos.writeObject(tokens[i][1]);
                    soos.writeObject(tokens[i][2]);
                    soos.writeObject(tokens[i][3]);
                }
            }
            FileWriter writer = new FileWriter("SurveyStatistic.txt", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write( id + " " + markMilk + " " + markCottage + " " + markCheese + "\n");
            bufferWriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void add(String account)
    {
        try {
            String productName = (String) sois.readObject();

            String sel = "Select MAX(idcustomer) as id from user.customer";
            Statement statement2 = To_Database.connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(sel);

            while (resultSet2.next()) {
                kol = resultSet2.getInt("id");
                //System.out.print(kol);
            }
            resultSet2.close();

            String select = "Select price from user.admin where productName = '" + productName + "'";
            Statement statement3 = To_Database.connection.createStatement();
            ResultSet resultSet3 = statement3.executeQuery(select);
            int q = 0;
            double price = 0;
            while (resultSet3.next()) {
                price = resultSet3.getDouble("price");
            }
            resultSet3.close();

            String insert = "INSERT INTO user.customer (idcustomer, purchase, price, mark, purchaseID, account) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = To_Database.connection.prepareStatement(insert);
            pst.setInt(1, ++kol);
            pst.setString(2, productName);
            pst.setDouble(3, price);
            pst.setInt(4, 0);
            pst.setString(5, "NotPaid");
            pst.setString(6, account);
            pst.execute();
            pst.close();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void basket(String account)
    {
        boolean Menu1 = true;
        while(Menu1) {
            try {
                String select1 = "Select COUNT(idcustomer) as id from user.customer where account = '" + account + "'";
                Statement statement1 = To_Database.connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(select1);
                while (resultSet1.next()) {
                    kol = resultSet1.getInt("id");
                }
                resultSet1.close();
                soos.writeObject(String.valueOf(kol));

                String select = "Select purchase, price, purchaseID from user.customer where account = '" + account + "'";
                Statement statement = To_Database.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(select);

                double Price = 0;
                while (resultSet.next()) {
                    String purchase = resultSet.getString("purchase");
                    String price = String.valueOf(resultSet.getString("price"));
                    String status = resultSet.getString("purchaseID");
                    soos.writeObject(purchase);
                    soos.writeObject(price);
                    soos.writeObject(status);
                    if(status.equals("NotPaid"))
                    {
                        Price += resultSet.getDouble("price");
                    }
                }
                resultSet.close();

                String mes = (String) sois.readObject();
                if (mes.equals("sign"))
                {
                    String login = (String) sois.readObject();
                    String password = (String) sois.readObject();
                    BankAccount(login, password, Price, account);
                }
                else if (mes.equals("delete"))
                {
                    deleteProduct();
                }
                else if(mes.equals("survey"))
                {
                    UserSurvey();
                }
                else if (mes.equals("back"))
                {
                    Menu1 = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteProduct()
    {
        try {
            String productName = (String) sois.readObject();

            String select1 = "Select MAX(idcustomer) as id from user.customer";
            Statement statement = To_Database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select1);
            while(resultSet.next())
            {
                kol = resultSet.getInt("id");
            }
            resultSet.close();

            String select = "Select idcustomer as id from user.customer where purchase = '" + productName + "'";
            Statement statement4 = To_Database.connection.createStatement();
            ResultSet resultSet4 = statement4.executeQuery(select);
            int num = 0;
            while(resultSet4.next())
            {
                num = resultSet4.getInt("id");
            }
            resultSet4.close();

            String sel = "DELETE FROM user.customer where purchase = '" + productName + "'";
            PreparedStatement pst = To_Database.connection.prepareStatement(sel);
            pst.execute();
            pst.close();
            for (int i = num; i < kol; i++)
            {
                String sel2 = "UPDATE user.customer SET `idcustomer` = '" + i + "' WHERE (`idcustomer` = '" + (i + 1) + "')";
                PreparedStatement pst2 = To_Database.connection.prepareStatement(sel2);
                pst2.execute();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void UserSurvey()
    {
        try {
            String productName = (String) sois.readObject();
            String mark = (String) sois.readObject();

            String update = "UPDATE user.customer SET mark = '" + mark + "' where purchase = '" + productName + "'";
            PreparedStatement pst = To_Database.connection.prepareStatement(update);
            pst.execute();
            pst.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void BankAccount(String login, String password, double Price, String account)
    {
        try {
            String select = "Select bankaccount, bankpassword, cash from user.bank";
            Statement statement = To_Database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);

            int q = -1;
            while (resultSet.next()) {
                String Login = resultSet.getString("bankaccount");
                String Password = resultSet.getString("bankpassword");
                double Cash = resultSet.getDouble("cash");
                if(Login.equals(login) && Password.equals(password))
                {
                    soos.writeObject("ok");
                    soos.writeObject(String.valueOf(Price));
                    //Здесь написать функцию вычисления стоимости и оплаты
                    String mes = (String) sois.readObject();
                    if(mes.equals("pay")) {
                        Pay(Cash, Price, Login, account);
                    }
                    q = 1;
                    break;
                }

            }
            if(q == -1)
            {
                soos.writeObject("no");
            }
            resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void Pay(double Cash, double Price, String Login, String account)
    {
        try {
            if (Cash < Price) {
                soos.writeObject("not enough");
            }
            else
            {
                String update = "UPDATE `bank` SET `cash` = '" + (Cash - Price) + "' where bankaccount = '" + Login + "'";
                PreparedStatement pst2 = To_Database.connection.prepareStatement(update);
                pst2.execute();
                pst2.close();
                String update1 = "UPDATE user.customer SET purchaseID = 'Paid' where account = '" + account + "'";
                PreparedStatement pst = To_Database.connection.prepareStatement(update1);
                pst.execute();
                pst.close();
                soos.writeObject("enough");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

