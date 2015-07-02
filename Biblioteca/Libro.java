//  Libro.java
//

package libreria;

public class Libro {
    private String id = null;
    private String isbn = null;
    private String editorial = null;
    private String autor = null;
    private String categoria = null;
    private String titulo = null;
    private String ubicacion = null;

    public Libro (String id, String isbn, String editorial,
       String autor, String categoria, String titulo, String ubicacion)  {
        this.id = id;
        this.isbn = isbn;
        this.editorial = editorial;
        this.autor = autor;
        this.categoria = categoria;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
    }

    public String getId () {
        return this.id;
    }

    public String getIsbn () {
        return this.isbn;
    }

    public String getEditorial () {
        return this.editorial;
    }

    public String getAutor () {
        return this.autor;
    }

    public String getCategoria () {
        return this.categoria;
    }

    public String getTitulo () {
        return this.titulo;
    }

    public String getUbicacion () {
        return this.ubicacion;
    }

}
//RED IS
//Identidad:admin_aregalado
//Identidad Anonima:
//Contraseña:4r3g4l4d0ML
//DEvf batch2
//devfbatch5
