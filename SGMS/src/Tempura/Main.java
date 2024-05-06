/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tempura;

import java.awt.Image;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.*;

public class Main {
    private static Connection con = null;
    private static Statement statement = null;
    String driver_url = "jdbc:mysql://localhost:3306/studentdb?zeroDateTimeBehavior=CONVERT_TO_NULL";
    String username = "root";
    String pass = "";
    
    private Connection getDBConnection(){
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(driver_url,username,pass);
    
        System.out.println("Connected successfully with the database");
        
        
        }
    catch(SQLException ex){
        System.out.println("Error in connecting with the database");
        System.err.println(ex);
       
    
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    return con;
    }
    
    private void createTable() throws SQLException{
        DatabaseMetaData metadata = getDBConnection().getMetaData();
        getStatement();
        ResultSet res = metadata.getTables(null , null , "academic_info", null);
        
        if (!res.next()) {
            String sql = "CREATE TABLE personal_info ("
                    + "student_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) NULL,  "
                    + "age INT(2) NULL,"
                    + "gender VARCHAR(100) NULL, "
                    + "address VARCHAR(128) NULL,"
                    + "birthday DATE,"
                    + "contact_number VARCHAR(15) NULL,  "
                    + "email VARCHAR(128) NULL, "
                    + "remarks VARCHAR(100) NULL);" ;
            
            String sql_ = "CREATE TABLE academic_info ("
                    + "student_id int NOT NULL, "
                    + "program VARCHAR(50) NULL,"
                    + "midterm_grade DOUBLE NULL,"
                    + "final_grade DOUBLE NULL, "
                    + "gpa Double null, "
                    + "remarks varchar(120) null,"
                    + "foreign key (student_id) references personal_info(student_id));";
             
            String sql_credentials = "Create table login_credentials("
                    + "id int primary key not null,"
                    + "username varchar(50) unique key null,"
                    + "password varchar(100) null);";
            statement.executeUpdate(sql);
            statement.executeUpdate(sql_);
            statement.executeUpdate(sql_credentials);
            System.out.println("Table successfully created");
            
        
          
        }
        
      
         statement.close();
         con.close();
    
    
    }
    
    Statement getStatement(){
        try{
                statement = getDBConnection().createStatement();
                System.out.println("Statement successfully created");

        }
        catch(SQLException ex){
            System.out.println("Error in creating a statement");
            System.out.println(ex);
        }
        return statement;
    }
    
    public int getMaxInt()throws SQLException{
    ResultSet res = getStatement().executeQuery("Select max(student_id) from personal_info");
    int id = 0;
    res.next();
    return res.getInt(1);
    }
    
    void addtoDB(Student x) throws SQLException{
        addtoPersonalInfo(x);
        
        int id = getMaxInt();
        x.id = id;
        addtoAcademicInfo(x);
        addtoCredentials(x);
    
    }
    void addtoCredentials(Student x){
     try {
                PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO login_credentials(id, username, password) VALUES (?,?,?)");
                
                prep.setInt(1, x.id);
                prep.setString(2, x.username);
                prep.setString(3, x.password);
                prep.executeUpdate();
            System.out.println("data added successfully to table login credentials");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    void addtoPersonalInfo(Student x){
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO personal_info (name, age, gender, address, birthday, contact_number, email) VALUES (?,?,?,?,?,?,?)");

            prep.setString(1, x.name);
            prep.setInt(2, x.age);
            prep.setString(3, x.gender);
            prep.setString(4, x.address);
            prep.setString(5, x.birthday);
            prep.setString(6, x.contact_no);
            prep.setString(7, x.email);
            prep.executeUpdate();
            
            System.out.println("Data added succssfully to table personal info");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void addtoAcademicInfo(Student x){
        try {
                PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO academic_info(student_id, program, midterm_grade, final_grade, gpa) VALUES (?,?,?,?,?)");
                
                prep.setInt(1, x.id);
                prep.setString(2, x.program);
                prep.setDouble(3, x.mid_grade);
                prep.setDouble(4, x.final_grade);
                prep.setDouble(5, x.gpa);
                
                prep.executeUpdate();
            System.out.println("data added successfully to table academic info");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public void deletefromDB(int id){
        try{
            
        PreparedStatement prep = getDBConnection().prepareStatement("DELETE FROM personal_info, academic_info WHERE student_id = ?");
       
        
         prep.setInt(1, id);
        prep.executeUpdate();
        }
        catch(SQLException ex){
        
            System.out.println("Error! " + ex);
            
        }
        JOptionPane.showMessageDialog(null, "Data deleted successfully from the database");
    }
    
    public void updateDB(int x, Student student){
        // x = 1 : update a data from the table personal info
        // x = 2 : update a data from the table academic info
        try{
            String query = "";
             PreparedStatement prep = null ;
                JOptionPane.showMessageDialog(null, "Data added successfully to the database");
        switch(x){
            case 1:
                query = "UPDATE personal_info"
                        + "SET name"
                        + "WHERE student_id = ?";
                
                prep = getDBConnection().prepareStatement(query);
                break;
            case 2:
                query = "UPDATE academic_info"
                        + "SET name"
                        + "WHERE student_id = ?";
                
                prep = getDBConnection().prepareStatement(query);
                break;
            default:
                break;
                
        }
        }
         catch(SQLException ex){
                 JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                 
                 }
        
        JOptionPane.showMessageDialog(null, "Data updated successfully from the database");
    }
 
        
      
  
    // this method returns all the information related or connected to the given student id:
    public Student displayStudentInfo(String id) throws SQLException{
        ResultSet res = statement.executeQuery("SELECT * FROM personal_info WHERE id=" + id);
        ResultSet acad = statement.executeQuery("SELECT * FROM academic_info WHERE id=" + id);
        Student x = new Student();
        
        while(res.next()){
            x.student_id = res.getString("student_id");
            x.name = res.getString("name");
            x.address = res.getString("address");
            x.age = res.getInt("age");
            x.gender = res.getString("gender");
            x.contact_no = res.getString("contact_no");
            x.email = res.getString("email");

            x.program = acad.getString("program");
            x.mid_grade = acad.getDouble("midterm_grade");
            x.final_grade = acad.getDouble("final_grade");
            x.gpa = acad.getDouble("gpa");
        }
        
        return x;
    
    }
    
    public static void main(String[] args) {
        new LoginForm().setVisible(true);
        try {
            new Main().createTable();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Database is not connected to the program. Failed to create the table.");
        }
        
    }
}

class Student{
    
    String username;
    String password;
    
    // personal info attributes
    String fname, lname, mname, address, contact_no, email, student_id, birthday, gender;
    int age;
    String name = fname+'|'+lname+'|'+ mname;
    
    // academic info attributes
    int id = 0;
    int year = 1;
    String program, remarks;
    Course course = new Course(); // course = {course_id, course_title, midterm_grade, final_grade, gpa}
    double gpa, mid_grade, final_grade;
    
    Student(){
    
    
    }
    Student(String fname, String lname, String mname, String address, String email, String contact, String birthday, String gender){
        
        setName(fname, lname, mname);
        this.address = address;
        this.email = email;
        contact_no = contact;
        this.birthday = birthday;
        this.gender = gender;
     
    }
    
    
    // 
    void setName(String fname, String lname, String mname){
    this.name = fname+' '+lname+' '+ mname;
    
    }
 
    
    
}


class Course{

    String course_id, course_title, remarks;
    double midterm_grade = 0, final_grade = 0, gpa = (midterm_grade+final_grade)/2;

    Course(){
    
    }
    
    Course(String course_title, String id, String remarks, double mgrade, double fgrade){
    this.course_title = course_title;
    this.course_id = id;
    this.remarks = remarks;
    midterm_grade = mgrade;
    fgrade = final_grade;
    }
    
    
}

interface JFrameDesign{

    default void scaleImage(String url, JLabel container) {
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();
        Image imageScale = image.getScaledInstance(container.getWidth(), container.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imageScale);
        container.setIcon(scaledIcon);

    }
    default void scaleImage(String url, JButton container) {
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();
        Image imageScale = image.getScaledInstance(container.getWidth(), container.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imageScale);
        container.setIcon(scaledIcon);

    }
}


