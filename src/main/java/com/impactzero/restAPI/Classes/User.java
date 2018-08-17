package com.impactzero.restAPI.Classes;

import javax.persistence.*;

//Class purpose: Declare Table User and its fields
//Inform Hibernate that this is a table
@Entity
@Table(name = "USER")
public class User{

    //Declare the indexing variable as an Id as Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Table Fields
    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    //Constructors

    //Empty constructor
    public User(){

        name = null;
        username = null;
        password = null;
        email = null;

    }

    //Constructor without the param id for creation of local users
    public User(String name, String username, String password, String email) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Constructor with all params to receive data from mySQL
    public User(int id, String name, String username, String password, String email){

        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;

    }


    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setData(String name, String username, String password, String email) {

        if(name != null){
            this.name = name;
        }

        if(username != null){
            this.username = username;
        }

        if(password != null){
            this.password = password;
        }

        if(email != null){
            this.email = email;
        }

    }

    public boolean equals(User user){
        if(
            this.getName().equals(user.getName()) &&
            this.getUsername().equals(user.getUsername()) &&
            this.getPassword().equals(user.getPassword()) &&
            this.getEmail().equals(user.getEmail())
        ){
            return true;
        }else{
            return false;
        }
    }
}