package edu.pitt.mycontacts;

/**
 * Created by ARUNKUMAR on 26-03-2017.
 */

public class Contact {

    private String _name, _phone, _email, _address;
    private int _id;

    public Contact(int id, String name, String phone, String email, String address){
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;


    }

    public int getId() { return _id; }
    public String getName(){
        return _name;
    }
    public String getPhone(){
        return _phone;
    }
    public String getEmail(){
        return _email;
    }
    public String getAddress(){
        return _address;
    }
}
