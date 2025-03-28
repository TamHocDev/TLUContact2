package vn.edu.tlu.tlucontact.models;

import java.io.Serializable;

public class Student implements Serializable {
    private String uid;

    private String unit;
    private String studentId;
    private String fullName;
    private String photoURL;
    private String phone;
    private String email;
    private String address;
    private String className;
    public Student() {}
    public Student(String uid, String studentId, String fullName, String photoURL, String phone, String email, String address, String className, String unit) {
        this.uid = uid;
        this.studentId = studentId;
        this.fullName = fullName;
        this.photoURL = photoURL;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.className = className;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
