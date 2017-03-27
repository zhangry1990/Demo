package com.zhangry.demo.ssh.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zhangry on 2017/3/15.
 */
@Entity
@Table(name = "demo_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "defaultGenerator")
    @GenericGenerator(name = "defaultGenerator", strategy = "uuid")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;
    @Column(name = "USERNAME", nullable = false, length = 128)
    private String username;
    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;
    @Column(name = "NAME", nullable = false, length = 128)
    private String name;
    @Column(name = "SEX", length = 1)
    private Integer sex;
    @Column(name = "AGE", length = 3)
    private Integer age;
    @Column(name = "USER_NO", length = 50)
    private String userNo;

    public User() {

    }

    public User(String username, String password, String name, Integer sex, Integer age, String userNo) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.userNo = userNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", userNo='" + userNo + '\'' +
                '}';
    }
}
