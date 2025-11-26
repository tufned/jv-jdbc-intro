package mate.academy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mate.academy.dao.BookDao;
import mate.academy.lib.Injector;
import mate.academy.model.Book;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        BookDao bookDao = (BookDao) injector.getInstance(BookDao.class);

        Book book = new Book("Book 1", new BigDecimal("123.30"));
        Book book2 = new Book("Book 2", new BigDecimal("987.99"));
        book = bookDao.create(book);
        book2 = bookDao.create(book2);

        List<Book> booksList = bookDao.findAll();
        System.out.println(booksList.stream()
                .map(Book::toString)
                .collect(Collectors.joining()));

        Optional<Book> foundBookById = bookDao.findById(book.getId());
        System.out.println(foundBookById);

        book = new Book(book.getId(), "The Ultimate Book", new BigDecimal("99.9"));
        Book updatedBook = bookDao.update(book);
        System.out.println(updatedBook);

        bookDao.deleteById(updatedBook.getId());
    }
}
