package top.cuizilin.mybatis.exception;

public class MyBuilderException extends RuntimeException{
    public MyBuilderException(String message){
        super(message);
    }
    public MyBuilderException(String message, Exception e){
        super(message, e);
    }
}
