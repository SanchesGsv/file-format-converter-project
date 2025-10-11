package com.gsanches.file_format_converter.exceptions;

public class InvalidDestinyException extends RuntimeException{

    public InvalidDestinyException(){
        super("Invalid destination");
    }

    public InvalidDestinyException(String message){
        super(message);
    }

}
