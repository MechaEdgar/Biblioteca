//LibrosBD
package libreria;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;

public class LibrosBD {
    Connection connection;
    private boolean connectionFree = true;
    private ArrayList libros;

    public LibrosBD () throws Exception {
        try {
            InitialContext initialContext = new InitialContext ();
            Context envContext = (Context) initialContext.lookup ("java:comp/env");
            DataSource dataSource = (DataSource) envContext.lookup ("jdbc/biblioteca");
            this.connection = dataSource.getConnection ();
        }
        catch (Exception e) {
            throw new Exception ("No se pudo abrir la base de datos biblioteca: " + e.getMessage ());
        }
    }

    protected synchronized Connection getConnection () {
        while (this.connectionFree == false) {
            try {
                wait ();
            }
            catch (InterruptedException e) {
            }
        }
        this.connectionFree = false;
        notify ();
        return this.connection;
    }

    protected synchronized void releaseConnection () {
        while (this.connectionFree == true) {
            try {
                wait ();
            }
            catch (InterruptedException e) {
            }
        }
        this.connectionFree = true;
        notify ();
    }

    public Libro getLibro (String id) {
        try {
            this.getConnection ();
            PreparedStatement preparedStatement = this.connection.prepareStatement
              ("SELECT id, isbn, editorial, autor, categoria, titulo, ubicacion FROM libros" +
               " WHERE id = ?");
            preparedStatement.setString (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ()) {
                Libro libro = new Libro (
                    resultSet.getString (1), resultSet.getString (2), resultSet.getString (3),
                    resultSet.getString (4), resultSet.getString (5), resultSet.getString (6),
                    resultSet.getString (7)
                );
                preparedStatement.close ();
                this.releaseConnection ();
                return libro;
            }
            else {
                preparedStatement.close ();
                this.releaseConnection ();
                return null;
            }
        }
        catch (SQLException e) {
            this.releaseConnection ();
            return null;
        }
    }

    public int insertarLibro (Libro libro) {
        int rowsAffected = 0;
        try {
            this.getConnection ();
            PreparedStatement preparedStatement = this.connection.prepareStatement
                 ("INSERT INTO libros (isbn, editorial, autor, categoria, titulo, " +
                  " ubicacion) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString (1, libro.getIsbn ());
            preparedStatement.setString (2, libro.getEditorial ());
            preparedStatement.setString (3, libro.getAutor ());
            preparedStatement.setString (4, libro.getCategoria ());
            preparedStatement.setString (5, libro.getTitulo ());
            preparedStatement.setString (6, libro.getUbicacion ());
            rowsAffected = preparedStatement.executeUpdate ();
            preparedStatement.close ();
            this.releaseConnection ();
        }
        catch (SQLException e) {
            this.releaseConnection ();
            return 0;
        }
        return rowsAffected;
    }

    public int borrarLibro (String id){
        int rowsAffected = 0;
        try {
            this.getConnection ();
            PreparedStatement preparedStatement =
                this.connection.prepareStatement ("DELETE FROM libros WHERE id = ?");
            preparedStatement.setString (1, id);
            rowsAffected = preparedStatement.executeUpdate ();
            preparedStatement.close ();
            this.releaseConnection ();
        }
        catch (SQLException e) {
            this.releaseConnection ();
            return 0;
        }
        return rowsAffected;
    }

    public int modificarLibro (Libro libro) {
        int rowsAffected = 0;
        try {
            this.getConnection ();
            PreparedStatement preparedStatement =
              this.connection.prepareStatement ("UPDATE libros SET isbn=?, editorial=?," +
                 " autor=?, categoria=?, titulo=?, ubicacion=? WHERE id =?");
            preparedStatement.setString (1, libro.getIsbn ());
            preparedStatement.setString (2, libro.getEditorial ());
            preparedStatement.setString (3, libro.getAutor ());
            preparedStatement.setString (4, libro.getCategoria ());
            preparedStatement.setString (5, libro.getTitulo ());
            preparedStatement.setString (6, libro.getUbicacion ());
            preparedStatement.setString (7, libro.getId ());
            rowsAffected = preparedStatement.executeUpdate ();
            preparedStatement.close ();
            this.releaseConnection ();
        }
        catch (SQLException e) {
            this.releaseConnection ();
            return 0;
        }
        return rowsAffected;
    }

    public Collection getLibros () {
        libros = new ArrayList ();
        try {
            this.getConnection ();
            PreparedStatement preparedStatement = this.connection.prepareStatement
                  ("SELECT id, isbn, editorial, autor, categoria, titulo, ubicacion FROM libros");
            ResultSet resultSet = preparedStatement.executeQuery ();
            while (resultSet.next ()) {
                Libro libro = new Libro (
                    resultSet.getString (1), resultSet.getString (2), resultSet.getString (3),
                    resultSet.getString (4), resultSet.getString (5), resultSet.getString (6),
                    resultSet.getString (7)
                );
                libros.add (libro);
            }
            preparedStatement.close ();
        }
        catch (SQLException e) {
            return null;
        }
        this.releaseConnection ();

        return libros;
    }

    public void close () {
        try {
            this.connection.close ();
        }
        catch (SQLException e) {
            System.out.println (e.getMessage ());
        }
    }

}
