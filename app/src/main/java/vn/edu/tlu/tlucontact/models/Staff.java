package vn.edu.tlu.tlucontact.models;


import java.io.Serializable;

public class Staff implements Serializable {
    private String uid;
    private String staffId;
    private String fullName;
    private String position;
    private String phone;
    private String email;
    private String unit;
    private String photoURL;

    public Staff() {}

    public Staff(String uid, String staffId, String fullName, String position, String phone, String email, String unit, String photoURL) {
        this.uid = uid;
        this.staffId = staffId;
        this.fullName = fullName;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.unit = unit;
        this.photoURL = photoURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
