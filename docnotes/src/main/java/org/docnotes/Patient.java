package org.docnotes;

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
