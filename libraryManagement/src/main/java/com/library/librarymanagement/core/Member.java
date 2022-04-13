package com.library.librarymanagement.core;

import com.library.librarymanagement.Constants;
import com.library.librarymanagement.references.BookStatus;
import com.library.librarymanagement.references.ReservationStatus;

import java.util.Date;

public class Member extends Account {
    private Date dateOfMembership;
    private int totalBooksCheckedOut;

    public int getTotalBooksCheckedOut() {
        return 0;
    }

    public boolean reserveBookItem(BookItem bookItem) {
        return false;
    }

    private void incrementTotalBooksCheckedOut() {

    }

    public boolean checkoutBookItem(BookItem bookItem) {
        if (this.getTotalBooksCheckedOut() >= Constants.MAX_BOOKS_ISSUED_TO_A_USER) {
            ShowError("The user has already checked-out maximum number of books");
            return false;
        }
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (bookReservation != null && bookReservation.getMemberId() != this.getId()) {
            // book item has a pending reservation from another user
            ShowError("This book is reserved by another member");
            return false;
        } else if (bookReservation != null) {
            // book item has a pending reservation from the give member, update it
            bookReservation.updateStatus(ReservationStatus.COMPLETED);
        }

        if (!bookItem.checkout(this.getId())) {
            return false;
        }

        this.incrementTotalBooksCheckedout();
        return true;
    }

    private void incrementTotalBooksCheckedout() {
    }

    private String getId() {
    }

    private void ShowError(String s) {
    }

    private void checkForFine(String bookItemBarcode) {
        BookLending bookLending = BookLending.fetchLendingDetails(bookItemBarcode);
        Date dueDate = bookLending.getDueDate();
        Date today = new Date();
        // check if the book has been returned within the due date
        if (today.compareTo(dueDate) > 0) {
            long diff = todayDate.getTime() - dueDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            Fine.collectFine(memberId, diffDays);
        }
    }

    public void returnBookItem(BookItem bookItem) {
        this.checkForFine(bookItem.getBarcode());
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (bookReservation != null) {
            // book item has a pending reservation
            bookItem.updateBookItemStatus(BookStatus.RESERVED);
            bookReservation.sendBookAvailableNotification();
        }
        bookItem.updateBookItemStatus(BookStatus.AVAILABLE);
    }

    public bool renewBookItem(BookItem bookItem) {
        this.checkForFine(bookItem.getBarcode());
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        // check if this book item has a pending reservation from another member
        if (bookReservation != null && bookReservation.getMemberId() != this.getMemberId()) {
            ShowError("This book is reserved by another member");
            member.decrementTotalBooksCheckedout();
            bookItem.updateBookItemState(BookStatus.RESERVED);
            bookReservation.sendBookAvailableNotification();
            return false;
        } else if (bookReservation != null) {
            // book item has a pending reservation from this member
            bookReservation.updateStatus(ReservationStatus.COMPLETED);
        }
        BookLending.lendBook(bookItem.getBarCode(), this.getMemberId());
        bookItem.updateDueDate(LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS));
        return true;
    }

    private String getMemberId() {
    }

    @Override
    public boolean resetPassword() {
        return false;
    }
}
