package com.viddoer.attendence.Models;

public class ClassTestHelper {

    String Date, Name, Registration, ClassTest1, ClassTest2, ClassTest3, ClassTest4;

    public ClassTestHelper(String date, String name, String registration, String classTest1, String classTest2, String classTest3, String classTest4) {
        Date = date;
        Name = name;
        Registration = registration;
        ClassTest1 = classTest1;
        ClassTest2 = classTest2;
        ClassTest3 = classTest3;
        ClassTest4 = classTest4;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRegistration() {
        return Registration;
    }

    public void setRegistration(String registration) {
        Registration = registration;
    }

    public String getClassTest1() {
        return ClassTest1;
    }

    public void setClassTest1(String classTest1) {
        ClassTest1 = classTest1;
    }

    public String getClassTest2() {
        return ClassTest2;
    }

    public void setClassTest2(String classTest2) {
        ClassTest2 = classTest2;
    }

    public String getClassTest3() {
        return ClassTest3;
    }

    public void setClassTest3(String classTest3) {
        ClassTest3 = classTest3;
    }

    public String getClassTest4() {
        return ClassTest4;
    }

    public void setClassTest4(String classTest4) {
        ClassTest4 = classTest4;
    }
}
