package org.assessments;

import java.util.Date;

public class Patient {
    public Integer patientId;
    public String givenName;
    public String familyName;
    public Date dob;
    public String sex;
    public String address;
    public String phoneNumber;
    public Patient() {
    }

    public Patient(Integer patientId, String givenName, String familyName, Date dob, String sex) {
        this.patientId = patientId;
        this.givenName = givenName;
        this.familyName = familyName;
        this.dob = dob;
        this.sex = sex;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Patient(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Date getDob() {
        return dob;
    }

    public String getSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
