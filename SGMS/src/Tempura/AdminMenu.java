/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tempura;

import java.awt.Component;
import java.awt.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author steph
 */
public class AdminMenu extends javax.swing.JFrame implements JFrameDesign{

    /**
     * Creates new form LandingPage
     */
    String stud_id;
    Main main = new Main();
    public AdminMenu() {
        initComponents();
        scaleImage("C:\\Users\\Acer\\Desktop\\images\\exit_button.png", this);
         scaleImage("C:\\Users\\Acer\\Downloads\\Default Instagram pfp.jpeg", Student_img);
        main = new Main();
    }
 // methods for panel navigation -> displays the panel into the DisplayPanel 
    private void cardR(Component comp){
        displayPanel.removeAll();
        displayPanel.add(comp);
        displayPanel.repaint();
        displayPanel.revalidate();
    
    }
   
 // table methods
    // responsible for displaying the table info for the table in the Read Panel -> will be called by the Read button found in the button panel
     private void addData(JTable jTable) {
        try {
            DefaultTableModel table = (DefaultTableModel) jTable.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program, gpa, remarks FROM personal_info");           
            while (res_personal.next()) {         
 
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);

                String program = res_personal.getString(3);
                String gpa = res_personal.getString(4);
                String remarks = res_personal.getString(5);

                String[] x = {student_id, name, program, gpa, remarks};
                table.addRow(x);
                
            }
            studentCount.setText(table.getRowCount() + "");
            table.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
     }
     
     // responsible for the table in the Grades Panel
     private void addDataGrades() {

        try {
            DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
            table.setRowCount(0);
            Main m = new Main();

    
            ResultSet res_academic = m.getStatement().executeQuery("SELECT * FROM academic_info");
            while (res_academic.next()) {       

                String course = res_academic.getString("course");
                String id = ""+res_academic.getInt("student_id");
                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");

                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                ResultSet res_personal = m.getStatement().executeQuery("Select program,section from personal_info where student_id=\'" + id + "\'");

                String set = "";
                String program = "";
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                }
                String[] x = {id, program, set, course, midterms, finals, gpa, remarks};

                table.addRow(x);

            }

        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }   
     // responsible for the table in the Course Panel -> display the student info
     private void addStudentCourseTable(){
         int id = 0;
         try {
             DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
             table.setRowCount(0);
             Main m = new Main();
             Statement statement = m.getStatement();
             Statement statement2 = m.getStatement();
             ResultSet res_personal = statement.executeQuery("SELECT student_id,name,program FROM personal_info");
             
             
             
             while (res_personal.next()) {          
                 ArrayList<String> student_info = new ArrayList<String>();
                 
                 String student_id = "" + res_personal.getInt(1);
                 String name = res_personal.getString(2);

                 String program = res_personal.getString(3);
                 String course = "";
                 ResultSet res_academic = statement2.executeQuery("select course_id from academic_info where student_id=\'" + student_id + "\'");
                 while (res_academic.next()) {
                      course = res_academic.getString("course_id");
                      student_info.add(course);
                     
                 }
                 String[] x = {student_id, name, program, String.join(",", student_info)};
                 table.addRow(x);
             }
             
            
         } catch (SQLException ex) {
             Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
        
     
     
     }
     // responsible for the table in the Course Panel -> displays the course info
     private void addDataQuickUpdate() {
        try {
            DefaultTableModel table = (DefaultTableModel) QuickUpdateTable.getModel();
            table.setRowCount(0);
            Main m = new Main();

            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program,section,email,year FROM personal_info");


            while (res_personal.next()) {         

                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);

                String program = res_personal.getString(3);
                String section = res_personal.getString(4);
                String username = res_personal.getString(5);
                String year =""+ res_personal.getInt(6);
                
                String[] x = {student_id, name, program, section, username,year};

                table.addRow(x);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
     }
     
//    The following methods are sort methods that will be used for sorting the table
     
    // the following methods are for the grade table found in the grade panel
    private void srtGradesByProgram(String program) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info ";
        try {
            ResultSet res_academic = main.getStatement().executeQuery(query);

            while (res_academic.next()) {

                int student_id = res_academic.getInt("student_id");
                
                String course = res_academic.getString("course");

                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                
                
                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                PreparedStatement prep  = main.getDBConnection().prepareStatement("select section,program from personal_info where student_id=? and program = ?");
                prep.setInt(1, student_id);
                prep.setString(2, program);
                ResultSet res_personal = prep.executeQuery();
                String set = "";
       
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    String[] x = {""+student_id, program, set, course, midterms, finals, gpa, remarks};
                    table.addRow(x);
                }

                
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesBySet(String set) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info";
        try {
            ResultSet res_academic = main.getStatement().executeQuery(query);
            while (res_academic.next()) {

             int student_id = res_academic.getInt("student_id");
                String course = res_academic.getString("course");
                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                
                PreparedStatement prep1 = main.getDBConnection().prepareStatement("select section,program from personal_info where student_id=? and section = ?");
                prep1.setInt(1, student_id);
                prep1.setString(2, set);
                ResultSet res_personal =prep1.executeQuery();
                                
                if (res_personal.next()) {
                    String program = res_personal.getString("program");
                    
                     String[] x = {student_id + "", program, set, course, midterms, finals, gpa, remarks};
                      table.addRow(x);
                }
  
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesByCourse(String course_id) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where course_id = \'" + course_id + "\'";
        try {
            ResultSet res_academic = main.getStatement().executeQuery(query);

            while (res_academic.next()) {

                String student_id = "" + res_academic.getInt("student_id");
              
                String course = res_academic.getString("course");
                 
                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                

                ResultSet res_personal = main.getStatement().executeQuery("select section,program from personal_info where student_id=\'" + student_id + "\'");
                String set = "";
                String program = "";
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                }

                String[] x = {student_id, program, set, course, midterms, finals, gpa, remarks};

                table.addRow(x);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    } 
    private void srtGradesBySearch(String student_id) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where student_id = \'" + student_id + "\'";
        try {
            ResultSet res_academic = main.getStatement().executeQuery(query);

            while (res_academic.next()) {
         
                student_id = "" + res_academic.getInt("student_id");

                String course = res_academic.getString("course");
              
                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
               double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                ResultSet res_personal = main.getStatement().executeQuery("select section,program from personal_info where student_id=\'" + student_id + "\'");
                String set = "";
                String program = "";
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                }

                String[] x = {student_id, program, set, course, midterms, finals, gpa, remarks};

                table.addRow(x);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesbyCourseNSet(String course_id, String set) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where course_id = ?";
        
        try {
            PreparedStatement prep = main.getDBConnection().prepareStatement(query);
            prep.setString(1, course_id);
           
            ResultSet res_academic = prep.executeQuery();
            
            while (res_academic.next()) {
                
                int student_id = res_academic.getInt("student_id");
                
                String course = res_academic.getString("course");
                
                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                
                PreparedStatement prep1 = main.getDBConnection().prepareStatement("select section,program from personal_info where student_id=? and section = ?");
                prep.setInt(1, student_id);
                prep.setString(2, set);
                ResultSet res_personal = prep1.executeQuery();
                
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    String program = res_personal.getString("program");
                    String[] x = {student_id + "", program, set, course, midterms, finals, gpa, remarks};    
                    table.addRow(x);
                
                
                }
                
                
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesbyCourseNProgram(String course_id, String program) {
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where course_id = ?";

        try {
            PreparedStatement prep = main.getDBConnection().prepareStatement(query);
            prep.setString(1, course_id);
            ResultSet res_academic = prep.executeQuery();

            while (res_academic.next()) {

                int student_id =res_academic.getInt("student_id");

                String course = res_academic.getString("course");

                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                
               double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                
                PreparedStatement prep1 = main.getDBConnection().prepareStatement("select * from personal_info where student_id=? and program = ?");
                prep1.setInt(1, student_id);
                prep1.setString(2, program);
                ResultSet res_personal = prep1.executeQuery();
                 
                String set = "";
                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                    String[] x = {student_id + "", program, set, course, midterms, finals, gpa, remarks};
                    table.addRow(x);
                }

                
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesbySetNProgram(String set, String program){
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where section = ?";

        try {
            PreparedStatement prep = main.getDBConnection().prepareStatement(query);
            prep.setString(1, set);
            ResultSet res_academic = prep.executeQuery();

            while (res_academic.next()) {

                int student_id = res_academic.getInt("student_id");

                String course = res_academic.getString("course");

                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
               double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                
                PreparedStatement prep1 = main.getDBConnection().prepareStatement("select section,program from personal_info where student_id=? and program = ?");
                prep1.setInt(1, student_id);
                prep1.setString(2, program);
                ResultSet res_personal = prep1.executeQuery();

                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                    String[] x = {student_id + "", program, set, course, midterms, finals, gpa, remarks};
                    table.addRow(x);
                }
 
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    private void srtGradesbyCourseNSetNProgram(String course_id, String set, String program){
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        table.setRowCount(0);
        String query = "select * from academic_info where course_id = ?";

        try {
            PreparedStatement prep = main.getDBConnection().prepareStatement(query);
            prep.setString(1, course_id);
            ResultSet res_academic = prep.executeQuery();

            while (res_academic.next()) {

                int student_id = res_academic.getInt("student_id");

                String course = res_academic.getString("course");

                String finals = "" + res_academic.getDouble("final_grade");
                String midterms = "" + res_academic.getDouble("midterm_grade");
                String gpa = "" + res_academic.getDouble("gpa");
                double remark = (Double.parseDouble(midterms) + Double.parseDouble(finals)) / 2;
                String remarks = "";
                if (remark > 75) {
                    remarks = "Passed";
                } else {
                    remarks = "Failed";
                }
                
                
                PreparedStatement prep1 = main.getDBConnection().prepareStatement("select section,program from personal_info where student_id=? and section = ? and program = ?");
                prep1.setInt(1, student_id);
                prep1.setString(2, set);
                prep1.setString(3, program);
                ResultSet res_personal = prep1.executeQuery();

                if (res_personal.next()) {
                    set = res_personal.getString("section");
                    program = res_personal.getString("program");
                    String[] x = {student_id + "", program, set, course, midterms, finals, gpa, remarks};
                    table.addRow(x);
                }

                
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
     
    
    // the following methods are for the student table in the course panel
    private void srtCourseByProgram(String program) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where program=\'" + program + "\'");
            ArrayList<String[]> student_info = new ArrayList<String[]>();

            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                String[] x = {student_id, name, program, ""};
                student_info.add(x);
            }
            for (String[] student : student_info) {

                String enrolled_courses = m.getEnrolledCourse(student[0]);
                student[3] = enrolled_courses;
                table.addRow(student);
            }

        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void srtCourseBySet(String set) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where section=\'" + set + "\'");
            ArrayList<String[]> student_info = new ArrayList<String[]>();

            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                String program = res_personal.getString(3);
                String[] x = {student_id, name, program, ""};
                student_info.add(x);
            }
            for (String[] student : student_info) {

                String enrolled_courses = m.getEnrolledCourse(student[0]);
                student[3] = enrolled_courses;
                table.addRow(student);
            }

        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void srtCourseByCourse(String course) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_academic = m.getStatement().executeQuery("SELECT student_id FROM academic_info where course_id=\'" + course + "\'");

            ArrayList<String> students = new ArrayList<String>();
            while (res_academic.next()) {
                String student_id = "" + res_academic.getInt(1);
                students.add(student_id);
            }

            ArrayList<String[]> student_info = new ArrayList<String[]>();
            for (String id : students) {
                ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where student_id=\'" + id + "\'");
                res_personal.next();
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                String program = res_personal.getString(3);
                String[] x = {student_id, name, program, ""};
                student_info.add(x);
            }

            for (String[] student : student_info) {

                String enrolled_courses = m.getEnrolledCourse(student[0]);
                student[3] = enrolled_courses;
                table.addRow(student);
            }

        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void srtCourseBySearch(String id) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where student_id=\'" + id + "\'");

            ArrayList<String> students = new ArrayList<String>();
            String enrolled_courses = "";
   
            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                String program = res_personal.getString(3);
                String course = "";
                ResultSet res_acad = m.getStatement().executeQuery("select course_id from academic_info where student_id =\'" + id + "\'");
                if (res_acad.next()) {
                    enrolled_courses = m.getEnrolledCourse(id);

                }

                String[] x = {student_id, name, program, enrolled_courses};
                table.addRow(x);

            }

        } catch (SQLException ex) {
            Logger.getLogger(displayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void srtCourseByProgramNSet(String program, String set) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where program=\'" + program + "\' and section= \'"+set+"\'");

            while(res_personal.next()){
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);           
                ResultSet course = m.getStatement().executeQuery("Select course_id from academic_info where student_id=\'"+student_id+"\'");
                String enrolled_courses = m.getEnrolledCourse(student_id);
                String[] x = {student_id, name, program, enrolled_courses};
                table.addRow(x);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     
    private void srtCourseByProgramNCourse(String program, String course) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
            boolean run= false;
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where program=\'" + program +"\'");

            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                ResultSet res = m.getStatement().executeQuery("select course_id from academic_info where student_id =\'"+student_id+"\' and course_id =\'" +course+"\'");
                if (res.next()) {
                    String enrolled_courses = m.getEnrolledCourse(student_id);
                    String[] x = {student_id, name, program, enrolled_courses};
                    table.addRow(x);
                    run = true;
                }               
            }
            
         
        } catch (SQLException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    private void srtCourseBySetNCourse(String course, String set) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
       
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where section=\'" + set +"\'");

            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String program = "" + res_personal.getString(3);
                String name = res_personal.getString(2);
                ResultSet res = m.getStatement().executeQuery("select course_id from academic_info where student_id =\'"+student_id+"\' and course_id =\'" +course+"\'");
                if (res.next()) {
                    String enrolled_courses = m.getEnrolledCourse(student_id);
                    String[] x = {student_id, name, program, enrolled_courses};
                    table.addRow(x);

                }               
            }
            
         
        } catch (SQLException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void srtCourseBySetNCourseNSet(String course, String set, String program) {
        try {
            DefaultTableModel table = (DefaultTableModel) tableStudent.getModel();
            table.setRowCount(0);
       
            Main m = new Main();
            ResultSet res_personal = m.getStatement().executeQuery("SELECT student_id,name,program FROM personal_info where section=\'" + set +"\'and program=\'"+program+"\'");

            while (res_personal.next()) {
                String student_id = "" + res_personal.getInt(1);
                String name = res_personal.getString(2);
                ResultSet res = m.getStatement().executeQuery("select course_id from academic_info where student_id =\'"+student_id+"\' and course_id =\'" +course+"\'");
                if (res.next()) {
                    String enrolled_courses = m.getEnrolledCourse(student_id);
                    String[] x = {student_id, name, program, enrolled_courses};
                    table.addRow(x);

                }               
            }
            
         
        } catch (SQLException ex) {
            Logger.getLogger(AdminMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// end of the sort methods
     
     
     
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayPanel = new ImagePanel("C:\\Users\\Acer\\Downloads\\SBP Participants\\GSU admin (2).png");
        HomePanel = new ImagePanel("C:\\Users\\Acer\\Downloads\\SBP Participants\\GSU admin (2).png");
        jLabel46 = new javax.swing.JLabel();
        EnrollPanel = new ImagePanel("C:\\Users\\Acer\\Downloads\\SBP Participants\\GSU admin (2).png");
        jLabel11 = new javax.swing.JLabel();
        ageField = new javax.swing.JSpinner();
        enter_addButton = new javax.swing.JButton();
        emailField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        contactField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        sexField = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        middleName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lastName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        firstname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        birthdayField = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        passField = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        programBoxEnroll = new javax.swing.JComboBox<>();
        setFieldEnroll = new javax.swing.JComboBox<>();
        enrollYearLvl = new javax.swing.JComboBox<>();
        numberOfStdField = new javax.swing.JTextField();
        quickEnrollBtn = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        programField = new javax.swing.JComboBox<>();
        DeletePanel = new javax.swing.JPanel();
        DisplayPanel = new ImagePanel("C:\\Users\\Acer\\Downloads\\SBP Participants\\GSU admin (2).png");
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        studentCount = new javax.swing.JLabel();
        UpdatePanel = new ImagePanel("C:\\Users\\Acer\\Downloads\\SBP Participants\\GSU admin (2).png");
        Student_img = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ageField1 = new javax.swing.JSpinner();
        UpdateEnterBtn = new javax.swing.JButton();
        contactField1 = new javax.swing.JTextField();
        addressField1 = new javax.swing.JTextField();
        emailField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        sexField1 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        middleName1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lastName1 = new javax.swing.JTextField();
        firstname1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        birthdayField1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        passField1 = new javax.swing.JTextField();
        Password = new javax.swing.JLabel();
        Birthday = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        programField1 = new javax.swing.JComboBox<>();
        GradePanel = new javax.swing.JPanel();
        saveChangesBtn_Grades = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        GradeTable = new javax.swing.JTable();
        Grades_programSortBox = new javax.swing.JComboBox<>();
        Grades_sortBtn = new javax.swing.JButton();
        Grades_setSortBox = new javax.swing.JComboBox<>();
        Grades_courseSortBox = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        searchBtn = new javax.swing.JButton();
        CoursePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableStudent = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        CourseTable = new javax.swing.JTable();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        addCourseBtn = new javax.swing.JButton();
        removeCourseBtn = new javax.swing.JButton();
        Course_courseSortBox = new javax.swing.JComboBox<>();
        Course_ProgramSortBox = new javax.swing.JComboBox<>();
        Course_setSortBox = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        LabelSort = new javax.swing.JLabel();
        Course_sortBtn = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        searchField_course = new javax.swing.JTextField();
        searchBtn_course = new javax.swing.JButton();
        QuickUpdatePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        QuickUpdateTable = new javax.swing.JTable();
        quickUpdateSave = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        readButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        exitbtn = new javax.swing.JButton();
        logoutbtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        homeBtn = new javax.swing.JButton();
        GradesBtn = new javax.swing.JButton();
        CourseBtn = new javax.swing.JButton();
        quickUpdateBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        displayPanel.setBackground(new java.awt.Color(255, 255, 255));
        displayPanel.setPreferredSize(new java.awt.Dimension(1536, 864));
        displayPanel.setLayout(new java.awt.CardLayout());

        HomePanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setFont(new java.awt.Font("Freeman", 1, 48)); // NOI18N
        jLabel46.setText("GSU Grades Management System");

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGap(333, 333, 333)
                .addComponent(jLabel46)
                .addContainerGap(351, Short.MAX_VALUE))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel46)
                .addContainerGap(767, Short.MAX_VALUE))
        );

        displayPanel.add(HomePanel, "card7");

        EnrollPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("Birthday");

        ageField.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        ageField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        enter_addButton.setBackground(new java.awt.Color(51, 102, 255));
        enter_addButton.setFont(new java.awt.Font("Futura Md BT", 1, 18)); // NOI18N
        enter_addButton.setForeground(new java.awt.Color(255, 255, 255));
        enter_addButton.setText("Enter");
        enter_addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enter_addButtonActionPerformed(evt);
            }
        });

        emailField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addressField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        contactField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Address");

        jLabel10.setText("Email Address");

        jLabel8.setText("Age");

        jLabel7.setText("Contact Info");

        sexField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));
        sexField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("Sex");

        middleName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Middle Name");

        jLabel4.setText("Last Name");

        lastName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel3.setText("Personal Info");

        firstname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("First Name");

        birthdayField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel27.setText("Password");

        passField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel31.setText("Program");

        jLabel36.setText("Fast Enroll");

        jLabel39.setText("Program");

        jLabel40.setText("Set:");

        jLabel41.setText("Number of Students:");

        jLabel42.setText("Year Level:");

        programBoxEnroll.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "BSIT", "BSIS", "BSCS", " " }));

        setFieldEnroll.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "A", "B", " " }));

        enrollYearLvl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "1", "2", "3", "4" }));

        quickEnrollBtn.setText("Quick Enroll");
        quickEnrollBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quickEnrollBtnActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Open Sans Condensed Medium", 0, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 102, 102));
        jLabel32.setText("* required");

        jLabel38.setFont(new java.awt.Font("Open Sans Condensed Medium", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 102, 102));
        jLabel38.setText("* required");

        jLabel43.setFont(new java.awt.Font("Open Sans Condensed Medium", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 102, 102));
        jLabel43.setText("* required");

        jLabel44.setFont(new java.awt.Font("Open Sans Condensed Medium", 0, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 102, 102));
        jLabel44.setText("* required");

        jLabel45.setFont(new java.awt.Font("Open Sans Condensed Medium", 0, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 102, 102));
        jLabel45.setText("* required");

        programField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BSIT", "BSIS", "BSCS", " " }));
        programField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EnrollPanelLayout = new javax.swing.GroupLayout(EnrollPanel);
        EnrollPanel.setLayout(EnrollPanelLayout);
        EnrollPanelLayout.setHorizontalGroup(
            EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnrollPanelLayout.createSequentialGroup()
                .addGap(262, 917, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(365, 365, 365))
            .addGroup(EnrollPanelLayout.createSequentialGroup()
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, EnrollPanelLayout.createSequentialGroup()
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(37, 37, 37)
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sexField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ageField, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(firstname, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                                    .addComponent(addressField)
                                    .addComponent(contactField)
                                    .addComponent(middleName)
                                    .addComponent(lastName, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                                    .addComponent(emailField)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, EnrollPanelLayout.createSequentialGroup()
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(EnrollPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addGap(68, 68, 68))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EnrollPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(45, 45, 45)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EnrollPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(45, 45, 45)))
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passField)
                                    .addComponent(birthdayField)
                                    .addComponent(programField, 0, 389, Short.MAX_VALUE))))
                        .addGap(8, 8, 8)
                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(EnrollPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(EnrollPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(quickEnrollBtn)
                                            .addGap(29, 29, 29)))))
                            .addGroup(EnrollPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(99, 99, 99)
                                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel41)
                                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(35, 35, 35)
                                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(programBoxEnroll, 0, 155, Short.MAX_VALUE)
                                            .addComponent(setFieldEnroll, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(enrollYearLvl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(numberOfStdField)))))))
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(jLabel3))
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(206, 206, 206)
                        .addComponent(enter_addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EnrollPanelLayout.setVerticalGroup(
            EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnrollPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addGap(46, 46, 46)
                .addComponent(jLabel36)
                .addGap(49, 49, 49)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39)
                    .addComponent(programBoxEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel43))
                .addGap(1, 1, 1)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(6, 6, 6)
                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(middleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(setFieldEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(sexField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(numberOfStdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(enrollYearLvl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(ageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38))
                    .addGroup(EnrollPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(quickEnrollBtn)))
                .addGap(32, 32, 32)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(birthdayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44)))
                .addGap(18, 18, 18)
                .addGroup(EnrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel45)
                    .addComponent(programField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110)
                .addComponent(enter_addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        displayPanel.add(EnrollPanel, "card2");

        javax.swing.GroupLayout DeletePanelLayout = new javax.swing.GroupLayout(DeletePanel);
        DeletePanel.setLayout(DeletePanelLayout);
        DeletePanelLayout.setHorizontalGroup(
            DeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1360, Short.MAX_VALUE)
        );
        DeletePanelLayout.setVerticalGroup(
            DeletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 864, Short.MAX_VALUE)
        );

        displayPanel.add(DeletePanel, "card4");

        DisplayPanel.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Name", "Program", "GPA", "Remarks"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel25.setText("Number of Students:");

        try{
            studentCount.setText("");
        }catch(SQLException ex){
            System.out.println(ex);
        }

        javax.swing.GroupLayout DisplayPanelLayout = new javax.swing.GroupLayout(DisplayPanel);
        DisplayPanel.setLayout(DisplayPanelLayout);
        DisplayPanelLayout.setHorizontalGroup(
            DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisplayPanelLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(DisplayPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(studentCount, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(191, Short.MAX_VALUE))
        );
        DisplayPanelLayout.setVerticalGroup(
            DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DisplayPanelLayout.createSequentialGroup()
                .addContainerGap(117, Short.MAX_VALUE)
                .addGroup(DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(studentCount))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        displayPanel.add(DisplayPanel, "card5");

        UpdatePanel.setBackground(new java.awt.Color(255, 255, 255));

        Student_img.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Birthday");

        ageField1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        UpdateEnterBtn.setText("Enter");
        UpdateEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateEnterBtnActionPerformed(evt);
            }
        });

        jLabel13.setText("Address");

        jLabel14.setText("Email Address");

        jLabel15.setText("Age");

        jLabel16.setText("Contact Info");

        sexField1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jLabel17.setText("Sex");

        jLabel18.setText("Middle Name");

        jLabel19.setText("Last Name");

        jLabel21.setText("First Name");

        jLabel20.setText("Student Name:");

        jLabel22.setText("Year:");

        jLabel23.setText("Set: ");

        Password.setText("Password");

        Birthday.setText("Program");

        programField1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BSIT", "BSIS", "BSCS" }));
        programField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UpdatePanelLayout = new javax.swing.GroupLayout(UpdatePanel);
        UpdatePanel.setLayout(UpdatePanelLayout);
        UpdatePanelLayout.setHorizontalGroup(
            UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UpdatePanelLayout.createSequentialGroup()
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UpdatePanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(UpdatePanelLayout.createSequentialGroup()
                                .addComponent(Student_img, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(UpdatePanelLayout.createSequentialGroup()
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(UpdatePanelLayout.createSequentialGroup()
                                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addGroup(UpdatePanelLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Password)
                                            .addComponent(jLabel12)
                                            .addComponent(Birthday))))
                                .addGap(37, 37, 37)
                                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(addressField1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sexField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ageField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(emailField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(birthdayField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(contactField1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(firstname1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(middleName1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lastName1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(passField1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(programField1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(UpdatePanelLayout.createSequentialGroup()
                        .addGap(242, 242, 242)
                        .addComponent(UpdateEnterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(711, Short.MAX_VALUE))
        );
        UpdatePanelLayout.setVerticalGroup(
            UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UpdatePanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(UpdatePanelLayout.createSequentialGroup()
                        .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)
                        .addGap(29, 29, 29))
                    .addComponent(Student_img, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(firstname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(23, 23, 23)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(middleName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(sexField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(contactField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(ageField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(addressField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(emailField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(birthdayField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Password))
                .addGap(18, 18, 18)
                .addGroup(UpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Birthday)
                    .addComponent(programField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addComponent(UpdateEnterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        displayPanel.add(UpdatePanel, "card3");

        GradePanel.setBackground(new java.awt.Color(255, 255, 255));

        saveChangesBtn_Grades.setText("Save Changes");
        saveChangesBtn_Grades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesBtn_GradesActionPerformed(evt);
            }
        });

        GradeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Student_ID", "Program", "Set", "Course", "Midterms", "Final ", "GPA", "Remarks"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        GradeTable.setColumnSelectionAllowed(true);
        jScrollPane5.setViewportView(GradeTable);
        GradeTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        Grades_programSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "BSIT", "BSIS", "BSCS" }));

        Grades_sortBtn.setText("Sort");
        Grades_sortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Grades_sortBtnActionPerformed(evt);
            }
        });

        Grades_setSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "1A", "1B", "2A", "2B", "3A", "3B", "4A", "4B" }));

        Grades_courseSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "IT121", "IT112", "IT111" }));

        jLabel28.setText("Program");

        jLabel29.setText("Set");

        jLabel30.setText("Course");

        jLabel24.setText("Search Student ID");

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout GradePanelLayout = new javax.swing.GroupLayout(GradePanel);
        GradePanel.setLayout(GradePanelLayout);
        GradePanelLayout.setHorizontalGroup(
            GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GradePanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveChangesBtn_Grades)
                    .addGroup(GradePanelLayout.createSequentialGroup()
                        .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Grades_programSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Grades_setSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(GradePanelLayout.createSequentialGroup()
                                .addComponent(Grades_courseSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Grades_sortBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)
                                .addGap(26, 26, 26)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(searchBtn))
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        GradePanelLayout.setVerticalGroup(
            GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GradePanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GradePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Grades_programSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Grades_setSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Grades_courseSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(searchBtn)
                    .addComponent(Grades_sortBtn))
                .addGap(69, 69, 69)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(saveChangesBtn_Grades, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(269, 269, 269))
        );

        displayPanel.add(GradePanel, "card8");

        CoursePanel.setBackground(new java.awt.Color(255, 255, 255));

        tableStudent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Name", "Program", "Enrolled Courses"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableStudent);

        CourseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"IT111", "Introduction to Computing", "TBA", "5"},
                {"IT112", "Computer Programming 1", "TBA", "5"},
                {"IT121", "Computer Programming 2", "TBA", "3"}
            },
            new String [] {
                "Course ID", "Course  Title", "Instructor", "Units"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(CourseTable);

        jLabel33.setText("Student Table:");

        jLabel34.setText("Course Table:");

        addCourseBtn.setText("Add Course to Student");
        addCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseBtnActionPerformed(evt);
            }
        });

        removeCourseBtn.setText("Remove Course from Student");
        removeCourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCourseBtnActionPerformed(evt);
            }
        });

        Course_courseSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "IT111", "IT112", "IT121" }));

        Course_ProgramSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "BSIT", "BSIS", "BSCS" }));

        Course_setSortBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "1A", "1B", "2A", "2B", "3A", "3B", "4A", "4B" }));

        jLabel35.setText("Program");

        LabelSort.setText("Set");

        Course_sortBtn.setText("Sort");
        Course_sortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Course_sortBtnActionPerformed(evt);
            }
        });

        jLabel37.setText("Course");

        jLabel47.setText("Search Student ID");

        searchField_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchField_courseActionPerformed(evt);
            }
        });

        searchBtn_course.setText("Search");
        searchBtn_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtn_courseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CoursePanelLayout = new javax.swing.GroupLayout(CoursePanel);
        CoursePanel.setLayout(CoursePanelLayout);
        CoursePanelLayout.setHorizontalGroup(
            CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursePanelLayout.createSequentialGroup()
                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CoursePanelLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CoursePanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CoursePanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addCourseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeCourseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CoursePanelLayout.createSequentialGroup()
                                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Course_ProgramSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Course_setSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelSort, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CoursePanelLayout.createSequentialGroup()
                                        .addComponent(Course_courseSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Course_sortBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel47)
                                        .addGap(26, 26, 26)
                                        .addComponent(searchField_course, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(searchBtn_course))
                                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(204, Short.MAX_VALUE))
        );
        CoursePanelLayout.setVerticalGroup(
            CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CoursePanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(LabelSort)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchField_course, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47)
                        .addComponent(searchBtn_course))
                    .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Course_ProgramSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Course_setSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Course_courseSortBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Course_sortBtn)))
                .addGap(18, 18, 18)
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(CoursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CoursePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel34)
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CoursePanelLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(addCourseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeCourseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(150, Short.MAX_VALUE))
        );

        displayPanel.add(CoursePanel, "card9");

        QuickUpdateTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Name", "Program", "Set", "Username", "Year"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(QuickUpdateTable);

        quickUpdateSave.setText("Save Changes");
        quickUpdateSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quickUpdateSaveActionPerformed(evt);
            }
        });

        updateButton.setText("Search & Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout QuickUpdatePanelLayout = new javax.swing.GroupLayout(QuickUpdatePanel);
        QuickUpdatePanel.setLayout(QuickUpdatePanelLayout);
        QuickUpdatePanelLayout.setHorizontalGroup(
            QuickUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuickUpdatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QuickUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quickUpdateSave))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        QuickUpdatePanelLayout.setVerticalGroup(
            QuickUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuickUpdatePanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(quickUpdateSave)
                .addContainerGap(282, Short.MAX_VALUE))
        );

        displayPanel.add(QuickUpdatePanel, "card10");

        buttonPanel.setBackground(new java.awt.Color(30, 59, 206));

        addButton.setText("Enroll");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        readButton.setText("Read");
        readButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        exitbtn.setText("Exit");
        exitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitbtnActionPerformed(evt);
            }
        });

        logoutbtn.setText("Logout");
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Admin Dashboard");

        homeBtn.setText("Home");
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });

        GradesBtn.setText("Grades");
        GradesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GradesBtnActionPerformed(evt);
            }
        });

        CourseBtn.setText("Course");
        CourseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CourseBtnActionPerformed(evt);
            }
        });

        quickUpdateBtn.setText("Quick Update");
        quickUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quickUpdateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1))
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(quickUpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(CourseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(GradesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(readButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(homeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exitbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(readButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GradesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CourseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(quickUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutbtn)
                .addGap(18, 18, 18)
                .addComponent(exitbtn)
                .addGap(88, 88, 88))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1360, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

// this method is for the Enroll Button found in the Button Panel. 
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        cardR(EnrollPanel);
        
    }//GEN-LAST:event_addButtonActionPerformed
    private void displayUpdateInfo(Student x) {
     
        ageField1.setValue(x.age);
        sexField1.setSelectedItem(x.gender);
        firstname1.setText(x.fname);
        this.lastName1.setText(x.lname);
        this.middleName1.setText(x.mname);
        addressField1.setText(x.address);
        contactField1.setText(x.contact_no);
        emailField1.setText(x.email);
        birthdayField1.setText(x.birthday);
        passField1.setText(x.getPassword());
        programField1.setSelectedItem(x.program);
        nameLabel.setText(x.getFormattedName(x.fname, x.lname, x.mname));
       
        
    }

// this method displays UpdatePanel into the DisplayPanel. This is used by the Update Butotn in the Button Panel.
// this method is used for specific update of one student.
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        quickUpdateSaveActionPerformed(evt);
        stud_id = JOptionPane.showInputDialog(this, "Please input student id");
        try {
            if (main.isStudentInDB(Integer.parseInt(stud_id))) {
                JOptionPane.showMessageDialog(this, "Student found");
                cardR(UpdatePanel);
                displayUpdateInfo(main.getStudent(stud_id));

            } else {
                JOptionPane.showMessageDialog(this, "Student not found", "", 2);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student not found", "", 2);
        }
    }//GEN-LAST:event_updateButtonActionPerformed

// this method is used for the Read Button found in the Button Panels. 
// Displays all the student found in the database, and it displays them in a table format
    private void readButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readButtonActionPerformed
         cardR(DisplayPanel);
         addData(jTable1);       
    }//GEN-LAST:event_readButtonActionPerformed

 //this method is for the delete button in the button panel. Deletes student from the database
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
       String text = JOptionPane.showInputDialog(this, "Please input student id");
       
        if (main.isStudentInDB(text)) {
            new Main().deletefromDB(Integer.parseInt(text));
            JOptionPane.showMessageDialog(this, "Student deleted successfully from the databse", "Information Message", 1);
        } else {
        JOptionPane.showMessageDialog(this, "Student not found in the database.", "Student not found" , 2);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed
// exits and stops the program
    private void exitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitbtnActionPerformed
       if (JOptionPane.showConfirmDialog(this, "Do you wish to proceed?") == 0) {
        this.dispose();
       }
    }//GEN-LAST:event_exitbtnActionPerformed
// logout the admin frame -> opens the LoginFrame
    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Do you wish to proceed?") == 0) {
            new LoginForm().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_logoutbtnActionPerformed

 // this method is for the Enter Button found on the Update Panel -> gets all the inputted data and updates the database
    private void UpdateEnterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateEnterBtnActionPerformed
        String firstName, lastName, middleName, address, contact_no, email, password, birthday, program;
        
        
        if (JOptionPane.showConfirmDialog(this, "Do you wish to proceed?", "Proceed?", JOptionPane.YES_NO_OPTION) == 0) {
            int age = (int) ageField1.getValue();
            String sex = (String) sexField1.getSelectedItem();

            firstName = firstname1.getText();
            lastName = this.lastName1.getText();
            middleName = this.middleName1.getText();
            address = addressField1.getText();
            contact_no = contactField1.getText();
            email = emailField1.getText();
            birthday = birthdayField1.getText();
            password = passField1.getText();
            program = ""+programField1.getSelectedItem();

            Student stu = new Student(firstName, lastName, middleName, address, email, contact_no, birthday, sex, password, program);
            stu.age = age;

            ageField1.setValue(0);
            sexField1.setSelectedItem("Male");
            firstname1.setText("");
            this.lastName1.setText("");
            this.middleName1.setText("");
            addressField1.setText("");
            contactField1.setText("");
            emailField1.setText("");
            birthdayField1.setText("");
            passField1.setText("");
            
            new Main().updatePersonalInfo(Integer.parseInt(stud_id), stu);
            JOptionPane.showMessageDialog(this, "Student updated successfully", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            cardR(QuickUpdatePanel);
            addDataQuickUpdate();
        }
        


    }//GEN-LAST:event_UpdateEnterBtnActionPerformed
// this method is for the Home Button found in the button panel -> displays the Home Panel in the DisplayPanel
    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        cardR(HomePanel);
    }//GEN-LAST:event_homeBtnActionPerformed
// for the grades button in the button panel -> displays the grade panel
    private void GradesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GradesBtnActionPerformed
        cardR(GradePanel);
        addDataGrades();
    }//GEN-LAST:event_GradesBtnActionPerformed
// for the course btn in the button panel -> displays the course panel
    private void CourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CourseBtnActionPerformed
        cardR(CoursePanel);
        addStudentCourseTable();
    }//GEN-LAST:event_CourseBtnActionPerformed

//for the course table -> checks if the student is already enrolled in that course -> returns a boolean value
    // checks if the given array has recurring values or elements -> returns true if there is a recurring element
    private boolean checkDuplicate(String[] array) {
        ArrayList<String> courses = new ArrayList<String>();
        System.out.println(Arrays.toString(array));
        for (String course : array) {
            System.out.println("Course: "+course);
            for (String index : courses) {
                System.out.println("Index: " + index);
                if(course.equals(index)){
                    JOptionPane.showMessageDialog(this, "Duplicate Found. Please Try Again");
                    return true;
                }
                
            }
            courses.add(course);
        }
        return false;
    }
    // returns true if the target string can be found in the array
    private boolean checkDuplicate(String[] array, String target) {
        for (String course : array) {
           if(course.equals(target)){
           return true;
           }
        }
        return false;
    }

// in the Course Panel -> this method is used by the addCourseBtn 
    // Enrolls the student in the selected course
    private void addCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseBtnActionPerformed
        try {
            int course_row = CourseTable.getSelectedRow();
            int[] student_rows = tableStudent.getSelectedRows();
            
            if(student_rows.length <= 0 || course_row < 0){
            JOptionPane.showMessageDialog(this, "Error! Please select a row", "Error", 0);
            return;
            }
            
            for(int student_row: student_rows){
            String student_id = "" + tableStudent.getValueAt(student_row, 0);
            String course_id = "" + CourseTable.getValueAt(course_row, 0);
            String course_list =  "" + tableStudent.getValueAt(student_row, 3);
            String course_title = "" + CourseTable.getValueAt(course_row, 1);
                if(!checkDuplicate(course_list.split(","), course_id)){
                             
                    new Main().addtoAcademicInfo(student_id, new Course(course_id, course_title));
                    addStudentCourseTable();
                }
                else{
                JOptionPane.showMessageDialog(this, "Student is already enrolled in that course. Please Try Again");
                return;
                }
            
                        
            }
            JOptionPane.showMessageDialog(this, "Student successfully added to the course");
        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Please select a row in the Course Table and the Student Table", "Error!", 0);
        }
    }//GEN-LAST:event_addCourseBtnActionPerformed

// in the Course Panel -> sorts the student table based on the given value
    private void Course_sortBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Course_sortBtnActionPerformed
         String program = ""+Course_ProgramSortBox.getSelectedItem();
        String set = "" + Course_setSortBox.getSelectedItem();
        String course = "" + Course_courseSortBox.getSelectedItem();
        
        if(!program.equals("Default") && !set.equals("Default") && !course.equals("Default")){srtCourseBySetNCourseNSet(course, set, program);}
        else if(!program.equals("Default") && !set.equals("Default") ){srtCourseByProgramNSet(program, set);}
        else if(!course.equals("Default") && !set.equals("Default") ){srtCourseBySetNCourse(course, set);}
        else if(!program.equals("Default") && !course.equals("Default") ){srtCourseByProgramNCourse(program, course);}
        else if(!program.equals("Default") && !course.equals("Default")){}
        else if(!program.equals("Default")){srtCourseByProgram(program);}
        else if(!set.equals("Default")){srtCourseBySet(set);}
        else if(!course.equals("Default")){srtCourseByCourse(course);}
        else{
        addStudentCourseTable();
        }
    }//GEN-LAST:event_Course_sortBtnActionPerformed

// in the Grade Panel, used by the save changes btn -> saves all the changes made by the user and updates the database.
    private void saveChangesBtn_GradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesBtn_GradesActionPerformed
        DefaultTableModel table = (DefaultTableModel) GradeTable.getModel();
        Main main = new Main();
        
        if(JOptionPane.showConfirmDialog(rootPane, "Do you wish to continue?","Proceed?",JOptionPane.YES_NO_OPTION) == 0){
         for (int i = 0; i < table.getRowCount(); i++) {
             int id = Integer.parseInt(""+GradeTable.getValueAt(i, 0));
      
             String set = "" + GradeTable.getValueAt(i, 2);
            String course = "" + GradeTable.getValueAt(i, 3);
            double midterm = Double.parseDouble(""+table.getValueAt(i, 4));
            double final_grade = Double.parseDouble(""+ table.getValueAt(i, 5));
          
           String remarks = ""+table.getValueAt(i,7);
            main.updateGrades(new Course(course, midterm, final_grade, id, set));
            main.updateGPAandRemarks(id);
            System.out.println("Update successful");
        }
        addDataGrades();
        }
       
        
    }//GEN-LAST:event_saveChangesBtn_GradesActionPerformed
// removes the courses from the student -> found in the Course Panel -> used by the Remove Course btn
    private void removeCourseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCourseBtnActionPerformed
        try {
            int course_row = CourseTable.getSelectedRow();
            int[] student_rows = tableStudent.getSelectedRows();

            if (JOptionPane.showConfirmDialog(rootPane, "Do you wish to continue?", "Proceed?", JOptionPane.YES_NO_OPTION) == 0) {
                for(int student_row: student_rows){
                String student_id = "" + tableStudent.getValueAt(student_row, 0);
                String course_id = "" + CourseTable.getValueAt(course_row, 0);

                new Main().removeStudentfromCourse(student_id, course_id);
                addStudentCourseTable();
                }
            }
        } catch(ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(this, "Please select a row in the Course Table and the Student Table", "Error!", 0);
        }
    }//GEN-LAST:event_removeCourseBtnActionPerformed

// sorts the Table based on the given value -> used by the sort Button on the Grade Panel
    private void Grades_sortBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Grades_sortBtnActionPerformed
        String program = ""+Grades_programSortBox.getSelectedItem();
        String set = "" + Grades_setSortBox.getSelectedItem();
        String course = "" + Grades_courseSortBox.getSelectedItem();
       
        if (!course.equals("Default") && !set.equals("Default") && !program.equals("Default")) {
            srtGradesbyCourseNSetNProgram(course, set, program);
        } else if (!course.equals("Default") && !set.equals("Default")) {
            srtGradesbyCourseNSet(course, set);
        } else if (!course.equals("Default") && !program.equals("Default")) {
            srtGradesbyCourseNProgram(course, program);
        } else if (!program.equals("Default") && !set.equals("Default")) {
            srtGradesbySetNProgram(set, program);
        } else if (!program.equals("Default")) {
            srtGradesByProgram(program);
        } else if (!set.equals("Default")) {
            srtGradesBySet(set);
        } else if (!course.equals("Default")) {
            srtGradesByCourse(course);
        } else {
            addDataGrades();
        }
    }//GEN-LAST:event_Grades_sortBtnActionPerformed

    // this method is for the enter btn found in the enroll panel -> collects all the inputted value on each text field and sends them 
    // into the database to be saved and stored
    private void enter_addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enter_addButtonActionPerformed
        String firstName, lastName, middleName, address, contact_no, email, password, birthday, program;
        
        int age = (int) ageField.getValue();
        String sex = (String) sexField.getSelectedItem();     
        firstName = firstname.getText();
        lastName = this.lastName.getText();
        middleName = this.middleName.getText();
        address = addressField.getText();
        contact_no = contactField.getText();
        email = emailField.getText();
        birthday = birthdayField.getText();
        password = passField.getText();
        program = ""+programField.getSelectedItem();
        
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || program.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please input all required given fields", "Error! Input all the required given fields", 2);
            return;
            // checks if the required text fields are empty or left blank
            // if so asks the user to input data to the required fields

        }

        int confirm = JOptionPane.showConfirmDialog(this, "Do you wish to proceed");// if the user selects 'YES' returns zero
        if (confirm == 0) { 
            // the program asks the user if he/she wishes to proceeed, if YES, runs this code, else skips this.
            Student stu = new Student(firstName, lastName, middleName, address, email, contact_no, birthday, sex, password, program);
            stu.age = age;

            ageField.setValue(0);
            sexField.setSelectedItem("Male");
            firstname.setText("");
            this.lastName.setText("");
            this.middleName.setText("");
            addressField.setText("");
            contactField.setText("");
            emailField.setText("");
            birthdayField.setText("");
            passField.setText("");
            programField.setSelectedItem("BSIT");
            new Main().addtoPersonalInfo(stu);
        }
    }//GEN-LAST:event_enter_addButtonActionPerformed

 // this method is for the quick enroll button in the Enroll Panel
    private void quickEnrollBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quickEnrollBtnActionPerformed
        String program = ""+programBoxEnroll.getSelectedItem();
        String set = "" + enrollYearLvl.getSelectedItem() + setFieldEnroll.getSelectedItem();
        String year = "" + enrollYearLvl.getSelectedItem();
        
        try{
        int count_num = Integer.parseInt(numberOfStdField.getText());
         for(int i = 1; i<=count_num; i++){
            Student x = new Student();
            x.program = program;
            x.set = set;
            x.year = year;
         main.addToDB(x);
        }
        }catch(NumberFormatException ex){ // this try-catch is used to warn and notify the user/admin to input an integer number
                                            // and not a non-integer value
        JOptionPane.showMessageDialog(this, "Please input a number", "Error! Wrong Value", 2);
        return;
        }
        
    }//GEN-LAST:event_quickEnrollBtnActionPerformed

// this method is for the quick update button in the button panels -> shows the QuickUpdatePanel in the Display Panel
    private void quickUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quickUpdateBtnActionPerformed
        cardR(QuickUpdatePanel);
        addDataQuickUpdate();
    }//GEN-LAST:event_quickUpdateBtnActionPerformed

    
 // this method is for the quickUpdate Button found in the QuickUpdate Panel -> saves the data inputted in the table into the database
    private void quickUpdateSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quickUpdateSaveActionPerformed
        DefaultTableModel table = (DefaultTableModel) QuickUpdateTable.getModel();
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to save all changes?");
        if (confirm == 0) {

            for (int i = 0; i < table.getRowCount(); i++) { // for each row in the table -> get the value of each row starting from the top the bottom
                Student x = new Student();
                
                x.student_id = "" + QuickUpdateTable.getValueAt(i, 0);
                x.name = "" + QuickUpdateTable.getValueAt(i, 1);
                x.program = "" + QuickUpdateTable.getValueAt(i, 2);
                x.set = "" + QuickUpdateTable.getValueAt(i, 3);
                x.email = "" + QuickUpdateTable.getValueAt(i, 4);
                x.year = "" + QuickUpdateTable.getValueAt(i, 5);
                
                main.quickUpdate(x);    //every iteration of the loop, it sends all the data collected each row to the database 
                                        // and update each row of the database                       
            }
            addDataQuickUpdate(); // refreshes the table
        }
    }//GEN-LAST:event_quickUpdateSaveActionPerformed

    private void programField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_programField1ActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        String student_id = searchField.getText();
        srtGradesBySearch(student_id);
    }//GEN-LAST:event_searchBtnActionPerformed

    private void searchBtn_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtn_courseActionPerformed
        String student_id = searchField_course.getText();
        srtCourseBySearch(student_id);
    }//GEN-LAST:event_searchBtn_courseActionPerformed

    private void searchField_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchField_courseActionPerformed
        
    }//GEN-LAST:event_searchField_courseActionPerformed

    private void programFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_programFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Birthday;
    private javax.swing.JButton CourseBtn;
    private javax.swing.JPanel CoursePanel;
    private javax.swing.JTable CourseTable;
    private javax.swing.JComboBox<String> Course_ProgramSortBox;
    private javax.swing.JComboBox<String> Course_courseSortBox;
    private javax.swing.JComboBox<String> Course_setSortBox;
    private javax.swing.JButton Course_sortBtn;
    private javax.swing.JPanel DeletePanel;
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JPanel EnrollPanel;
    private javax.swing.JPanel GradePanel;
    private javax.swing.JTable GradeTable;
    private javax.swing.JButton GradesBtn;
    private javax.swing.JComboBox<String> Grades_courseSortBox;
    private javax.swing.JComboBox<String> Grades_programSortBox;
    private javax.swing.JComboBox<String> Grades_setSortBox;
    private javax.swing.JButton Grades_sortBtn;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel LabelSort;
    private javax.swing.JLabel Password;
    private javax.swing.JPanel QuickUpdatePanel;
    private javax.swing.JTable QuickUpdateTable;
    private javax.swing.JLabel Student_img;
    private javax.swing.JButton UpdateEnterBtn;
    private javax.swing.JPanel UpdatePanel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addCourseBtn;
    private javax.swing.JTextField addressField;
    private javax.swing.JTextField addressField1;
    private javax.swing.JSpinner ageField;
    private javax.swing.JSpinner ageField1;
    private javax.swing.JTextField birthdayField;
    private javax.swing.JTextField birthdayField1;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextField contactField;
    private javax.swing.JTextField contactField1;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField emailField1;
    private javax.swing.JComboBox<String> enrollYearLvl;
    private javax.swing.JButton enter_addButton;
    private javax.swing.JButton exitbtn;
    private javax.swing.JTextField firstname;
    private javax.swing.JTextField firstname1;
    private javax.swing.JButton homeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lastName;
    private javax.swing.JTextField lastName1;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JTextField middleName;
    private javax.swing.JTextField middleName1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField numberOfStdField;
    private javax.swing.JTextField passField;
    private javax.swing.JTextField passField1;
    private javax.swing.JComboBox<String> programBoxEnroll;
    private javax.swing.JComboBox<String> programField;
    private javax.swing.JComboBox<String> programField1;
    private javax.swing.JButton quickEnrollBtn;
    private javax.swing.JButton quickUpdateBtn;
    private javax.swing.JButton quickUpdateSave;
    private javax.swing.JButton readButton;
    private javax.swing.JButton removeCourseBtn;
    private javax.swing.JButton saveChangesBtn_Grades;
    private javax.swing.JButton searchBtn;
    private javax.swing.JButton searchBtn_course;
    private javax.swing.JTextField searchField;
    private javax.swing.JTextField searchField_course;
    private javax.swing.JComboBox<String> setFieldEnroll;
    private javax.swing.JComboBox sexField;
    private javax.swing.JComboBox sexField1;
    private javax.swing.JLabel studentCount;
    private javax.swing.JTable tableStudent;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
