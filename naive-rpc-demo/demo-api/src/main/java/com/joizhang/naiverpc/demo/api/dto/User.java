package com.joizhang.naiverpc.demo.api.dto;

public class User {

    private String username;
    private byte gender;
    private short age;
    private int version;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", version=" + version +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
