package vn.edu.tlu.tlucontact.models;

import java.io.Serializable;

public class Unit implements Serializable {
    private String unitId;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String fax;
    private String type;
    private String photoURL;
    private String parentId;
    private String code;

    private String description;

    public Unit() {
    }

    public Unit(String unitId, String name, String phone, String address, String email, String fax, String type, String photoURL, String parentId, String code, String description) {
        this.unitId = unitId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.fax = fax;
        this.type = type;
        this.photoURL = photoURL;
        this.parentId = parentId;
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
