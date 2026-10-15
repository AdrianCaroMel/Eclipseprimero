package di;
import java.io.Serializable;

public class Libro implements Serializable {
    private int id;
    private String titulo;
    private String autor;
    private float precio;
    private boolean eliminado;

    public Libro(int id, String titulo, String autor, float precio, boolean eliminado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.eliminado = eliminado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public float getPrecio() { return precio; }
    public void setPrecio(float precio) { this.precio = precio; }
    
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    @Override
    public String toString() {
        return "ID: " + id
                + ", Titulo: " + titulo
                + ", Autor: " + autor
                + ", Precio: " + precio
                + ", Eliminado: " + eliminado;
    }
}