package com.example.utkur.yukuzapp.Module;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class Person {
    private String first_name;
    private String last_name;
    private String email;
    private String ssn;
    private String phone_number;

    public Person(String first_name, String last_name, String ssn, String email, String phone_number) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setSsn(ssn);
        setPhone_number(phone_number);
        setEmail(email);
    }

    public Person() {
    }

    public Person getInstance() {
        return new Person();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
