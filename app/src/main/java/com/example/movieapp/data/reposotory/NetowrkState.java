package com.example.movieapp.data.reposotory;

public class NetowrkState {

    public NetowrkState(Status status, String msg) {
        NetowrkState loaded = new NetowrkState(Status.SUCCESS_, "Success");
        NetowrkState loading = new NetowrkState(Status.RUNNING_, "Running");
        NetowrkState error = new NetowrkState(Status.FAILED_, "Error");
    }
}

