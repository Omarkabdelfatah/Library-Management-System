package com.example.LibraryManagementSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Result<T>{


    T object ;
    List<T> objects;
    ResultStatus status;
    String errorMessage;

    public Result(T object, ResultStatus status) {
        this.object = object;
        this.status = status;
    }
    public Result(T object, ResultStatus status, String errorMessage) {
        this.object = object;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Result(List<T> objects,ResultStatus status, String errorMessage){
        this.objects = objects;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Result() {

    }
}


