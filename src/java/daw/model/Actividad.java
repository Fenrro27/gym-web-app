
package daw.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;


@Entity
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findById", query = "SELECT a FROM Actividad a WHERE a.id = :id"),
    @NamedQuery(name = "Actividad.findMonitores", query = "SELECT DISTINCT a.monitor FROM Actividad a WHERE a.monitor IS NOT NULL"),
    @NamedQuery(name = "Actividad.deleteById", query = "DELETE FROM Actividad a WHERE a.id = :id"),
    @NamedQuery(name = "Actividad.findByUsuarioIdAndActividadId", query = "SELECT a FROM Actividad a JOIN a.usuarios u WHERE u.id = :usuarioId AND a.id = :actividadId"),

})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Atributos de la clase
    @Column(unique = true)
    String nombre;
    String descripcion;

    @OneToOne
    @JoinColumn(name = "monitor") 
    Usuario monitor;

    double precio;

    @ManyToMany
    @JoinTable(
            name = "usuario_actividad",
            joinColumns = @JoinColumn(name = "actividad_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios;

    public Actividad() {

    }

    public Actividad(String nombre, String descripcion, Usuario monitor, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monitor = monitor;
        this.precio = precio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getMonitor() {
        return monitor;
    }

    public void setMonitor(Usuario monitor) {
        this.monitor = monitor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "daw.model.Actividad[ id=" + id + " ]";
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
