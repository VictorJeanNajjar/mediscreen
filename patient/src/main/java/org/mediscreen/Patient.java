package org.mediscreen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer patientId;
    @NotNull(message = "givenName field can't be blank")
    public String givenName;
    @NotNull(message = "familyName field can't be blank")
    public String familyName;
    @NotNull(message = "date of birth field can't be blank")
    @Past(message = "Date of Birth must be a past date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date dob;
    @NotNull(message = "gender field can't be blank")
    @Pattern(regexp = "^[MF]$")
    public String sex;
    @NotNull(message = "address field can't be blank")
    public String address;
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}|\\d+", message = "Must contain only numbers and hyphens in the format XXX-XXX-XXXX")
    @NotNull(message = "phoneNumber field can't be blank")
    public String phoneNumber;
    public Patient() {
    }
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
