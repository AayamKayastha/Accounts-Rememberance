package com.example.aayamk.accountsrememberance;

/**
 * Created by aayamk on 1/8/2017.
 */

public class AccountDetails {
    String userName,password,email,extras;
    int position;

    public AccountDetails(String userName, String password,String email,String extras,int position) {
        this.userName = userName;
        this.password = password;
        this.email=email;
        this.extras=extras;
        this.position=position;
    }


}
