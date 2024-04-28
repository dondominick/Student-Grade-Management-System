/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tempura;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.*;

public class Main {
    private static Connection con = null;
    private static Statement statement = null;
    String driver_url = "jdbc:mysql://localhost:3306/studentdb?zeroDateTimeBehavior=CONVERT_TO_NULL";
    
    private Connection getDBConnection(){
    try{
    Class.forName("com.mysql.cj.jdbc.Driver");
    con = DriverManager.getConnection(driver_url,"root","root");
    
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
        ResultSet res = metadata.getTables(null , null , "personal_info", null);
        
        if (!res.next()) {
            String sql = "CREATE TABLE personal_info (  "
                    + "student_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) NULL,  "
                    + "age INT(2) NULL,"
                    + "gender VARCHAR(100) NULL,"
                    + "address VARCHAR(128) NULL,"
                    + "birthday DATE NULL,"
                    + "contact_number VARCHAR(15), "
                    + "email VARCHAR(128) NULL"
                    + ");";
             

            String sql_at = "CREATE TABLE academic_info ("
                    + "student_id INT(11) NOT NULL PRIMARY KEY,"
                    + "program VARCHAR(50) NULL,"
                    + "midterm_grade DOUBLE NULL,"
                    + "final_grade DOUBLE NULL,"
                    + "gpa DOUBLE NULL"
                    + ")";
                    
            statement.executeUpdate(sql);
            System.out.println("Table successfully created");
            statement.executeUpdate(sql_at);
            System.out.println("Table successfully created");
            statement.close();
            con.close();
          
        }
    
    
    }
    
    private Statement getStatement(){
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
    
    
    /*
    SUGGESTION:
        1. ADD AN ALGORITHM TO CHECK IF THE STUDENT ID IS ALREADY TAKEN AND CREATE A NEW STUDENT ID:
        2. IT SHOULD RETURN A STRING 
        3.  IT RETURNS A UNIQUE STUDENT ID FOR EACH STUDENT
    
    
    
    */
    
    /*
    STEPS OF THE PROGRAM:
        1. CHECKS IF AN ID IS ALREADY TAKEN, IF A GENERATED ID IS ALREADY TAKEN CREATE A NEW ONE
        2 IF FALSE, CREATE A NEW ONE:
        3 RETURN ID
    */
    String uniqueIDGenerator(){
    String id = "";
    
    
    return id;
    }
    void addtoPersonalInfo(Student x){
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO personal_info (student_id, name, age, gender, address, birthday, contact_number, email) VALUES (?,?,?,?,?,?,?,?)");
            prep.setInt(1, x.id);
            prep.setString(2, x.name);
            prep.setInt(3, x.age);
            prep.setString(4, x.gender);
            prep.setString(5, x.address);
            prep.setString(6, x.birthday);
            prep.setString(7, x.contact_no);
            prep.setString(8, x.email);
            prep.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data added successfully to the database");

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
                JOptionPane.showMessageDialog(null, "Data added successfully to the database");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public void deletefromDB(String id, String table){
        try{
            
        PreparedStatement prep = getDBConnection().prepareStatement("DELETE FROM ? WHERE student_id = ?");
        prep.setString(2, id);
        prep.setString(1, table);
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
    
    /**
     *
     * @return a list of string arrays
     * @throws SQLException
     */
    
    // this method returns a list of summarized data from the database
    
    // NOTE: error in this method
    /*
    ERROR: NULL.POINTER.EXCEPTION:
    POSSIBLE CAUSES: 
        1.    the table 'academic_info is empty' leading to this error:
        2. 
    POSSIBLE SOLUTIONS:
        1.      add data inside the table academic_info
    
    */
    public ArrayList<Student> displayAllStudentInfo() throws SQLException{
        
        ResultSet res_personal = statement.executeQuery("SELECT student_id,name FROM personal_info");
        ResultSet res_academic = statement.executeQuery("SELECT program,gpa,remarks FROM academic_info");
        ArrayList<Student> list = new ArrayList<Student>();
        
        
        while(res_personal.next()){          // index 0 = student id, 1 = name, 2 = program, 3 = gpa, 4 = remarks
           Student obj = new Student();
           obj.name = res_personal.getString("name");
           obj.student_id = res_personal.getString("student_id");
           obj.program = res_academic.getString("program");
           obj.gpa = res_academic.getDouble("gpa");
           obj.remarks = res_academic.getString("remarks");
                   
        
           list.add(obj);
        }
        return list;
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
        new AdminMenu().setVisible(true);
        try {
            new Main().createTable();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Database is not connected to the program. Failed to create the table.");
        }
        
    }
}

class Student{

    
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
    Student(String fname, String lname, String mname, String address, String email, String contact, String birthday, String gender,String id){
        
        setName(fname, lname, mname);
        this.address = address;
        this.email = email;
        contact_no = contact;
        this.birthday = birthday;
        this.gender = gender;
        this.student_id = id;
    }
    
    
    // 
    void setName(String fname, String lname, String mname){
    this.name = fname+'|'+lname+'|'+ mname;
    
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