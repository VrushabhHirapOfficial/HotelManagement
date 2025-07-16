//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
Title : Hotel Reservation System

Description : user be able to 1) Add new reservation
                              2) Check their reservations
                              3) Get room number
                              4) Update Reservations
                              5) Delete reservations
                              6) Exit
              All this functionalities should be able to perform by user.

Schema of the app : name of the db = HotelManagementSystem
                    Table name = Reservations
                    Schema =
                           1) Reservation id that will be auto increment
                           2) Guest name varchar and that will be not null
                           3) Room number INT that will be not null
                           4) Contact number varchar and that will be not null
                           5) reservation date timestamp that will be default
 */

//import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;

//import static java.lang.System.exit;
//import static java.lang.System.setOut;

public class HotelManagementSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/HotelManagementSystem";
    private static final String username = "root";
    private static final String password = "vruhirapsh9981";

    public static void main(String[] args)  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();
            while (true){
                System.out.println();
                System.out.println("Hotel Management System ");
                Scanner sc = new Scanner(System.in);
                System.out.println("""
                        1) Add new reservation
                        2) Check their reservations
                        3) Get room number
                        4) Update Reservations
                        5) Delete reservations
                        6) Exit""");
                System.out.println("Enter Your choice :");
                int choice = sc.nextInt();
                switch (choice){
                    case 1 :
                        reserveRoom(connection,sc,statement);
                        break;
                    case 2 :
                        checkReservation(connection,statement);
                        break;
                    case 3 :
                        getRoomNo(connection,sc,statement);
                        break;
                    case 4 :
                        updateRoomNumber(connection,sc,statement);
                        break;
                    case 5 :
                        deleteReservation(connection,sc,statement);
                        break;
                    case 6 :
                        exit();
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid Choice Try Again !!");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException r){
            throw new RuntimeException(r);
        }
    }

    public static void exit()throws InterruptedException{
        System.out.println("Turning off System");
        int i=5;
        while(i != 0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println();
        System.out.println("Thank you for Using Hotel Management System !!!");

    }

    private static void deleteReservation(Connection connection, Scanner sc,Statement statement)throws SQLException {
        try{
            System.out.println("Enter Id of the Customer :");
            int id = sc.nextInt();

            if(!reservationexists(connection,id,statement)){
                System.out.println("Reservation not found for the given id !!!");
                return;
            }

            String sql = "delete from Reservations where Reservation_id = "+id;

            int affectedrows = statement.executeUpdate(sql);

            if(affectedrows>0){
                System.out.println("Data deleted Successfully !!");
            } else {
                System.out.println("Failed for deletion from table!!");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static boolean reservationexists(Connection connection,int Reservation_id,Statement statement){
        try {
            String sql = "Select Reservation_id from Reservations where Reservation_id ="+Reservation_id;

            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void updateRoomNumber(Connection connection, Scanner sc,Statement statement) throws  SQLException{
        try{
            System.out.println("Enter Reservation ID to Update Room No :");
            int id = sc.nextInt();

            if(!reservationexists(connection,id,statement)){
                System.out.println("Reservation not found for the given id !!!");
                return;
            }

            System.out.println("Enter new Guest First Name :");
            String newname = sc.next();
            String newlastname = sc.next();

            System.out.println("Enter new room number :");
            int roomno = sc.nextInt();

            System.out.println("Enter new Contact Number :");
            String contactno = sc.next();

            String sql = "UPDATE Reservations SET GuestFirstName = '" + newname + "', " +
                    "GuestLastName = '" + newlastname + "', " +
                    "RoomNo = " + roomno + ", " +
                    "ContactNo = " + contactno + " " +
                    "WHERE Reservation_id = " + id;

            try {
                int affectedrows = statement.executeUpdate(sql);

                if (affectedrows>0){
                    System.out.println("Reservation updated Successfully !!");
                }else {
                    System.out.println("Reservation updated Failed !!");
                }
            }catch (SQLException t){
                System.out.println(t.getMessage());
            }

        }catch (Exception u){
            System.out.println(u.getMessage());
        }
    }

    private static void getRoomNo(Connection connection, Scanner sc,Statement statement) throws SQLException {
        try{
            System.out.println("Enter Reservation Id :");
            int reservationid = sc.nextInt();

            System.out.println("Enter First Name and last name of the Customer :");
            String firstname = sc.next();
            String lastname = sc.next();

            String sql = "SELECT RoomNo FROM Reservations WHERE Reservation_id = " + reservationid +
                    " AND GuestFirstName = '" + firstname + "' AND GuestLastName = '" + lastname + "'";


            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                int roomno = resultSet.getInt("RoomNo");

                System.out.println("The Room number of the Customer is :"+roomno);
            } else{
                System.out.println("Reservation is not found!!!!");
            }
        } catch (Exception y){
            System.out.println(y.getMessage());
        }
    }

    private static void checkReservation(Connection connection,Statement statement) throws SQLException{
        //String sql = "Select (Reservation_id,GuestFirstName,GuestLastName,RoomNo,ContactNo,Reservation_Date) from Reservations;";
        String sql = "SELECT Reservation_id, GuestFirstName, GuestLastName, RoomNo, ContactNo, Reservation_Date FROM Reservations;";

        try {
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println();
            System.out.println("Current Reservations!!!!");
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Reservation_id\tFirstName\tLastName\tRoomNo\tContactNo\tDateAndTime");
            System.out.println("-------------------------------------------------------------------");


            while(resultSet.next()){
                int Reservation_id = resultSet.getInt("Reservation_id");
                String firstname = resultSet.getString("GuestFirstName");
                String lastname = resultSet.getString("GuestLastName");
                int roomno = resultSet.getInt("RoomNo");
                String contactno = resultSet.getString("ContactNo");
                String reservation_date = resultSet.getTimestamp("Reservation_Date").toString();

                System.out.println("\t"+Reservation_id+"\t\t\t"+firstname+"\t"+lastname+"\t\t"+roomno+"\t\t"+contactno+"\t"+reservation_date);
            }
            System.out.println("-------------------------------------------------------------------");
        } catch (SQLException y){
            System.out.println(y.getMessage());
        }
    }

    private static void reserveRoom(Connection connection, Scanner sc,Statement statement) {
        try {
            System.out.println("Enter Guest First Name :");
            String guestFirstname = sc.next();
            System.out.println("Enter Guest Last Name :");
            String guestLastname = sc.next();

            System.out.println("Enter room number :");
            int roomno = sc.nextInt();

            System.out.println("Enter contact number :");
            String contactno = sc.next();

            String sql = "INSERT INTO Reservations (GuestFirstName, GuestLastName, RoomNo, ContactNo) " +
                    "VALUES ('" + guestFirstname + "','" + guestLastname + "','" + roomno + "','" + contactno + "')";

            try {
                int affectedrows = statement.executeUpdate(sql);

                if (affectedrows > 0) {
                    System.out.println("Reservation is successfully !!");
                } else {
                    System.out.println("Reservation failed !!");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }catch (Exception t){
            System.out.println(t.getMessage());
        }
    }


}