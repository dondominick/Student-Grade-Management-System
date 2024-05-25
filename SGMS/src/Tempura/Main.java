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
import javax.swing.ImageIcon;

public class Main {
    private static Connection con;
    private static Statement statement; 
    String driver_url = "jdbc:mysql://localhost:3306/studentdb?zeroDateTimeBehavior=CONVERT_TO_NULL";
    String username = "root";
    String pass = "";
    
    Connection getDBConnection(){
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(driver_url,username,pass);
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
            String sql = "CREATE TABLE personal_info ("
                    + "student_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) NULL,  "
                    + "age INT(2) NULL,"
                    + "gender VARCHAR(100) NULL, "
                    + "address VARCHAR(128) NULL,"
                    + "birthday varchar(27) NULL,"
                    + "contact_number VARCHAR(15) NULL,"
                    + "email VARCHAR(128) NULL, "
                    + "remarks VARCHAR(100) NULL,"
                    + "_password varchar(64) NULL,"
                    + "gpa double null,"
                    + "program varchar(15) NULL,"
                    + "section varchar(5) NULL,"
                    + "year INT NULL"
                    + ");";
            
            String sql_ = "CREATE TABLE academic_info ("
                    + "student_id int NOT NULL,"
                    + "course VARCHAR(50) NULL,"
                    + "course_id varchar(6) null,"
                    + "midterm_grade DOUBLE NULL,"
                    + "final_grade DOUBLE NULL, "
                    + "gpa Double null,"
                    + "remarks varchar(120) null,"
                    + "semester varchar(10) null,"
                    + "foreign key (student_id) references personal_info(student_id));";
             

            
            statement.executeUpdate(sql);
            statement.executeUpdate(sql_);
            System.out.println("Table successfully created");
            
        
          
        }
        
      
         statement.close();
         con.close();
    
    
    }
    
    Statement getStatement(){
        try{
                statement = getDBConnection().createStatement();
               

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

    void addToDB(Student x){
        
        try {
            addtoPersonalInfo(x);
            x.id = getMaxInt();
            addtoAcademicInfo(x);
            JOptionPane.showMessageDialog(null,"Student added succesfully to the database" );
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Student failed to be added in the database\nError: " + ex, "Error! Something went wrong", 0);
        }

    }
    void addtoPersonalInfo(Student x) {
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO personal_info (name, age, gender, address, birthday, contact_number, email, program,"
                    + "_password, year) VALUES (?,?,?,?,?,?,?,?,?,?)");

            prep.setString(1, x.name);
            prep.setInt(2, x.age);
            prep.setString(3, x.gender);
            prep.setString(4, x.address);
            prep.setString(5, x.birthday);
            prep.setString(6, x.contact_no);
            prep.setString(7, x.email);
            prep.setString(8, x.program);
            prep.setString(9, x.getPassword());
            prep.setInt(10, Integer.parseInt(x.year));
           

            prep.executeUpdate();

            System.out.println("Data added succssfully to table personal info");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addtoAcademicInfo(Student x) {
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO academic_info(student_id, course, midterm_grade, final_grade, gpa) VALUES (?,?,?,?,?)");

            prep.setInt(1, x.id);

            prep.setString(2, "null");
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
    public void addtoAcademicInfo(String id, Course course) {
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("INSERT INTO academic_info(student_id, course, course_id) VALUES (?,?,?)");

            prep.setInt(1, Integer.parseInt(id));
            prep.setString(2, course.course_title);
            prep.setString(3, course.course_id);


            prep.executeUpdate();
            System.out.println("data added successfully to table academic info");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
// delete methods 
    public void deletefromDB(int id) {
        try {
            PreparedStatement prep = getDBConnection().prepareStatement("DELETE FROM academic_info WHERE student_id = ?");
            PreparedStatement prep2 = getDBConnection().prepareStatement("DELETE FROM personal_info WHERE student_id = ?");
            prep.setInt(1, id);
            prep2.setInt(1, id);
            prep.executeUpdate();
            prep2.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data deleted successfully from the database");
        } catch (SQLException ex) {
            System.out.println("Error! " + ex);
        }
    }
    
    public void removeStudentfromCourse(String id, String course_id) {
        try {
            String query = "DELETE FROM academic_info where student_id = ? and course_id  =?";
            PreparedStatement prep = con.prepareStatement(query);
            prep.setInt(1, Integer.parseInt(id));
            prep.setString(2, course_id);
            prep.executeUpdate();
            System.out.println("Course deleted successfully");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
// update methods
    
    
    public void updatePersonalInfo(int x, Student student) {
        try {
            String query = "update personal_info "
                    + "set name =?, age=?, gender =?, address = ?, "
                    + "birthday =?, contact_number =?, email = ?, "
                    + "remarks=?, program=?, birthday=?, _password=?"
                    + "where student_id =?";

            PreparedStatement prep = getDBConnection().prepareStatement(query);

            prep.setString(1, student.name);
            prep.setInt(2, student.age);
            prep.setString(3, student.gender);
            prep.setString(4, student.address);
            prep.setString(5, student.birthday);
            prep.setString(6, student.contact_no);
            prep.setString(7, student.email);
            prep.setString(8, student.remarks);
            prep.setString(9, student.program);
            prep.setString(10, student.birthday);
            prep.setString(11, student.getPassword());
            prep.setInt(12, x);

            prep.executeUpdate();

            System.out.println("update successful");
            JOptionPane.showMessageDialog(null, "Data updated successfully from the database");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Data cannot be added into the database");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void updateAcademicTable(Course course) {
        String query = "update academic_info set course=?, midterm_grade=?, final_grade=?,gpa=?,remarks=? where student_id = ?";
        getDBConnection();
        try {
            PreparedStatement prep = con.prepareStatement(query);
            prep.setString(1, course.course_title);
            prep.setDouble(2, course.midterm_grade);
            prep.setDouble(3, course.final_grade);
            prep.setDouble(4, course.getGPA(course.midterm_grade, course.final_grade));
            prep.setString(5, course.setRemarks(course.gpa));
            prep.setInt(6, course.student_id);

            prep.executeUpdate();
            System.out.println("Update Successful");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void updateGrades(Course course) {
        String query = "update academic_info set midterm_grade=?, final_grade=?,gpa=?,remarks=? where student_id = ? and course =?";
        getDBConnection();
        try {
            PreparedStatement prep = con.prepareStatement(query);
            prep.setDouble(1, course.midterm_grade);
            prep.setDouble(2, course.final_grade);
            prep.setDouble(3, course.getGPA(course.midterm_grade, course.final_grade));
            prep.setString(4, course.setRemarks(course.getGPA(course.midterm_grade, course.final_grade)));
            prep.setInt(5, course.student_id);
            prep.setString(6, course.course_title);


            prep.executeUpdate();
            System.out.println("Update Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void quickUpdate(Student x){
            String query = "update personal_info set name=?, program=?,section=?,email=? ,year=?where student_id=?";
            
        try {
            PreparedStatement prep = con.prepareStatement(query);
            
            
            prep.setString(1, x.name);
            prep.setString(2, x.program);
            prep.setString(3, x.set);
            prep.setString(4, x.email);
            prep.setString(5, x.year);
            prep.setString(6, x.student_id);
            prep.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    
    }
// auxilliary methods
    public boolean isStudentInDB(int id) {
        try {
            getStatement();
            String sql = "Select * from personal_info where student_id =" + id;

            ResultSet res = statement.executeQuery(sql);

            if (res.next()) {
                res.close();
                return true;
            }
        } catch (SQLException ex) {

        }

        System.out.println("user cannot be found");
        return false;
    }

    public boolean isStudentInDB(String id) {
        try {
            getStatement();
            String sql = "Select * from personal_info where student_id =" + id;

            ResultSet res = statement.executeQuery(sql);

            if (res.next()) {
                return true;
            }
        } catch (SQLException ex) {

        }

        System.out.println("user cannot be found");
        return false;
    }
    public boolean isStudentInDBemail(String email) {
        try {
            getStatement();
            String sql = "Select * from personal_info where email =\'" + email+"\'";

            ResultSet res = statement.executeQuery(sql);

            if (res.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        System.out.println("user cannot be found");
        return false;
    }

    // this method returns all the information related or connected to the given student id:
    public Student displayStudentInfo(String id) {
        Student x = new Student();
        try {
            getStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM personal_info WHERE student_id=" + id);
            //       

            while (res.next()) {

                x.student_id = "" + res.getInt("student_id");
                x.name = res.getString("name");
                x.address = res.getString("address");
                x.age = res.getInt("age");
                x.gender = res.getString("gender");
                x.contact_no = res.getString("contact_number");
                x.email = res.getString("email");

            }
            ResultSet acad = statement.executeQuery("SELECT * FROM academic_info WHERE student_id=" + id);

            while (acad.next()) {

                x.program = acad.getString("program");
                x.mid_grade = acad.getDouble("midterm_grade");
                x.final_grade = acad.getDouble("final_grade");
                x.gpa = acad.getDouble("gpa");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }


    public Student getStudent(String id) {
        Student x = new Student();
        try {
            getStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM personal_info WHERE student_id=\'" + id+"\'");
            //       

            while (res.next()) {
                
                x.student_id = "" + res.getInt("student_id");
                try{
                String[] name = res.getString("name").split(",");
                x.fname = name[0];
                x.lname = name[1];
                x.mname = name[2];
                }catch(ArrayIndexOutOfBoundsException ex){
//                x.fname = "";
//                x.lname = "";
//                x.mname = "";
                }
                
                x.address = res.getString("address");
                x.age = res.getInt("age");
                x.gender = res.getString("gender");
                x.contact_no = res.getString("contact_number");
                x.email = res.getString("email");
                x.program = res.getString("program");
                x.setPassword(res.getString("_password"));
                x.birthday = res.getString("birthday");
                

            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }
    public Student getStudentByEmail(String email) {
        Student x = new Student();
        try {
            getStatement();
            String sql = "SELECT * FROM personal_info WHERE email=\'"+email+"\'";

            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {

                x.student_id = "" + res.getInt("student_id");
               String[] name = res.getString("name").split(",");
                x.fname = name[0];
                x.lname = name[1];
                x.mname = name[2];
                
                x.address = res.getString("address");
                x.age = res.getInt("age");
                x.gender = res.getString("gender");
                x.contact_no = res.getString("contact_number");
                x.email = res.getString("email");
                x.program = res.getString("program");
                x.setPassword(res.getString("_password"));
                x.year = res.getString("year");
                x.birthday = res.getString("birthday");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }

    public Course getCourse(String course_id, String student_id) {
        Course course = new Course();
        try {
            getStatement();

            ResultSet res = statement.executeQuery("Select * from academic_info where course_id = ? and student_id = ?");
            
            while(res.next()){
            
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return course;
    }

    // useless method -> can be deleted
    public String getEnrolledCourse(String id) {
        try {
            String query = "select course_id from academic_info where student_id="+id;
            ResultSet res = statement.executeQuery(query);
            ArrayList<String> course_list = new ArrayList<String>();
            
            
                while(res.next()){
                     String course_id = res.getString(1);
                     course_list.add(course_id);               
                }
           
            return String.join(",", course_list);
        } catch (SQLException ex){
            System.out.println(ex);
        }
        return "null";
    }

    public String getPassword(String email){
        String sql = "select _password from personal_info where email = "+ email;

        try {
            ResultSet res = getStatement().executeQuery(sql);
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    public String getPasswordID(String id){
        String sql = "select _password from personal_info where student_id = \'"+ id+"\'";

        try {
            ResultSet res = getStatement().executeQuery(sql);
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    public String getPasswordIDbyEmail(String email){
        String sql = "select _password from personal_info where email = \'"+ email+ "\'";

        try {
            ResultSet res = getStatement().executeQuery(sql);
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    public Double getTotalGPA(int id){
     String sql = "select gpa from personal_info where student_id = \'"+ id+"\'";

        try {
            ResultSet res = getStatement().executeQuery(sql);
            if(res.next()){
                return res.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    
    }
    public String getRemarks(int id){
     String sql = "select remarks from personal_info where student_id = \'"+ id+"\'";

        try {
            ResultSet res = getStatement().executeQuery(sql);
            if(res.next()){
                return res.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    
    }
    void updateGPAandRemarks(int id) {
        getStatement();
        String sql = "select gpa from academic_info where student_id = \'"+id+"\'";
        try{
        ResultSet res = statement.executeQuery(sql);
        double final_gpa=0.0;
        int counter = 0;
        while(res.next()){
        double gwa = res.getDouble(1);
        final_gpa+= gwa;
        counter++;
        }
        final_gpa = final_gpa/counter;
        String remarks = "";
        if(final_gpa >= 75){
        remarks = "Passed";
        }else{
        remarks = "Failed";
        }
        PreparedStatement prep= con.prepareStatement("Update personal_info set gpa=?, remarks=? where student_id=?");
        prep.setDouble(1, final_gpa);
        prep.setString(2, remarks);
        prep.setInt(3, id);
        prep.executeUpdate();
        
        
        }catch(SQLException ex){
            System.out.println("Errror");
        }
    }
    public static void main(String[] args) {
        
        try {
            new Main().createTable();
            new LoginForm().setVisible(true);
        } catch (SQLException ex) {
            
            System.out.println("Database is not connected to the program. Failed to create the table.");
            System.out.println(ex);
        }        
    }

    
}

class Student{
    
    
    
    // personal info attributes
    String fname, lname, mname, address, contact_no, email, student_id, birthday, gender;
    int age;
    String name = getName();
    String formatted_name;
    
    // academic info attributes
    int id = 0;
    String year;
    String program, remarks, set;
    int semester;
    
     // course = {course_id, course_title, midterm_grade, final_grade, gpa}
    private List<Course> course_list = new ArrayList<Course>();
    double gpa, mid_grade, final_grade;
    
    // login credentials
    private String username = email ;
    private String password;
    
    Student(){
    
    
    }
    Student(String fname, String lname, String mname, String address, String email, String contact, String birthday, String gender, String password, String program){
        
        setName(fname, lname, mname);
        this.address = address;
        this.email = email;
        contact_no = contact;
        this.birthday = birthday;
        this.gender = gender;
        this.program = program;
        this.gender = gender; 
        this.password = password;
    }
    
    
    // 
    void setName(String fname, String lname, String mname){
    this.name = fname+','+lname+','+ mname;
    
    }
 
    String getPassword(){
    return password;
    }
   void setPassword(String pass){
    this.password = pass;
    }
    String getUsername(){
    return username;
    }
    String getFormattedName(String fname, String lname, String mname){
    
        return fname + " " + lname +" "+ mname;
    }
    
    void addtoCourseList(Course course){
        course_list.add(course);
    }
    String getName(){
    return fname+','+lname+','+ mname;
    }
    List<Course> getCourseList(){
    return course_list;
    }
}


class Course{

    String course_id, course_title;
    int student_id;
    double midterm_grade = 0, final_grade = 0, gpa = (midterm_grade + final_grade) / 2;
    String set;
    
    String remarks = setRemarks(getGPA(midterm_grade, final_grade));
    Course() {

    }
    Course(String id, String title){
    this.course_id = id;
    this.course_title = title;
    }

    Course(String course_title, String id, String remarks, double mgrade, double fgrade) {
        this.course_title = course_title;
        this.course_id = id;
        this.remarks = remarks;
        midterm_grade = mgrade;
        fgrade = final_grade;
    }

    Course(String title, double midterm, double final_grade, int id, String set) {
        this.course_title = title;
        this.student_id = id;
        midterm_grade = midterm;
        this.final_grade = final_grade;
        this.set = set;
        setRemarks(getGPA(midterm_grade, final_grade));
        setGPA();
    }

    void setGPA() {
        gpa = (midterm_grade + final_grade) / 2;
    }
    
    double getGPA(double midterm_grade, double final_grade)
    {
    return (midterm_grade + final_grade) / 2;
    }
     String setRemarks(double gpa) {
        if (gpa >= 75) {
            return "Passed";
        } else {
            return "Failed";
        }
        
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
    default void scaleImage(String url, JFrame container){
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();
        Image imageScale = image.getScaledInstance(container.getWidth(), container.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imageScale);
        container.setIconImage(imageScale);
    }
}


