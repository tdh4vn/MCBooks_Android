package vn.mcbooks.mcbooks.exception;

/**
 * Created by hungtran on 6/16/16.
 */
public class SetBookTypeException extends Exception {
    public SetBookTypeException() {
        super("Book type khong dung dinh dang. hot, new, coming");
    }
}
