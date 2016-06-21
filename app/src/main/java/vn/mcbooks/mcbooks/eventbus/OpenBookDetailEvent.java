package vn.mcbooks.mcbooks.eventbus;

import vn.mcbooks.mcbooks.model.Book;

/**
 * Created by hungtran on 6/22/16.
 */
public class OpenBookDetailEvent {
    private Book book;

    public OpenBookDetailEvent() {
    }

    public OpenBookDetailEvent(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
