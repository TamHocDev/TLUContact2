package vn.edu.tlu.tlucontact.models;

public class User {
    private String uid;
    private String email;
    private String role;
    private String displayName;
    private String photoURL;
    private String phoneNumber;

    public User() {}

    public User(String uid, String email, String role, String displayName, String phoneNumber) {
        this.uid = uid;
        this.email = email;
        this.role = role;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
