package mate.academy.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.db.DbConnectionManager;
import mate.academy.lib.Dao;
import mate.academy.model.Book;

@Dao
public class BookDaoImpl implements BookDao {
    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO books (title, price) VALUES (?, ?)";
        try (Connection connection = DbConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Unable to insert");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                book.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute query", e);
        }
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DbConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long entityId = resultSet.getLong(Columns.ID.getColumn());
                String title = resultSet.getString(Columns.TITLE.getColumn());
                BigDecimal price =
                        resultSet.getObject(Columns.PRICE.getColumn(), BigDecimal.class);
                return Optional.of(new Book(entityId, title, price));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute query", e);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        try (Connection connection = DbConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<Book> booksList = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong(Columns.ID.getColumn());
                String title = resultSet.getString(Columns.TITLE.getColumn());
                BigDecimal price =
                        resultSet.getObject(Columns.PRICE.getColumn(), BigDecimal.class);
                booksList.add(new Book(id, title, price));
            }
            return booksList;
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute query", e);
        }
    }

    @Override
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, price = ? WHERE id = ?";
        try (Connection connection = DbConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setLong(3, book.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Unable to update");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute query", e);
        }
        return book;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DbConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Unable to delete");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute query", e);
        }
    }

    private enum Columns {
        ID("id"),
        TITLE("title"),
        PRICE("price");

        private final String column;

        Columns(String column) {
            this.column = column;
        }

        public String getColumn() {
            return column;
        }
    }
}
