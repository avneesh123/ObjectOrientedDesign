package com.library.librarymanagement.core;

import com.library.librarymanagement.references.BookFormat;
import com.library.librarymanagement.references.BookStatus;

import java.util.Date;

public class BookItem extends Book {
    public String getBarCode() {
        return barCode;
    }

    private String barCode;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Date dateOfPurchase;
    private Date publicationDate;
    private Rack placedAt;

    public boolean checkout(String memberId) {
        if(isReferenceOnly) {
            ShowError("This book is Reference only and can't be issued");
            return false;
        }
        if(!BookLending.lendBook(this.getBarCode(), memberId)){
            return false;
        }
        this.updateBookItemStatus(BookStatus.LOANED);
        return true;
    }

    private void updateBookItemStatus(BookStatus loaned) {
    }

    private void ShowError(String s) {
    }

    public String getBarcode() {
        return null;
    }
}
