
package daw.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = "Usuario.findAllExcept", query = "SELECT p FROM Usuario p WHERE p.id != :id"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT p FROM Usuario p WHERE p.id =:id"),
    @NamedQuery(name = "Usuario.findByEmailAndPassword", query = "SELECT u FROM Usuario u WHERE u.email = :email AND u.contrasenna = :contrasenna"), // Nueva consulta
    @NamedQuery(name = "Usuario.findActividadesByUsuarioId", query = "SELECT a FROM Actividad a JOIN a.usuarios u WHERE u.id = :usuarioId"),
    @NamedQuery(name = "Usuario.deleteById", query = "DELETE FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByTipo", query = "SELECT u FROM Usuario u WHERE u.tipo = :tipo"),
    @NamedQuery(name = "Usuario.findByDNI", query = "SELECT u FROM Usuario u WHERE u.DNI = :dni")
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributos de la tabla
    String name;
    @Column(unique = true)
    String DNI;
    Date fechaNacimiento;
    int telefono;
    @Column(unique = true)
    String email;
    Date fechaEntrada;
    String contrasenna;
    String tipo;

    @ManyToMany(mappedBy = "usuarios", cascade = CascadeType.ALL)
    private List<Actividad> actividades;

    
    
    public Usuario() {

    }

    public Usuario(String name, String DNI, Date fechaNacimiento, int telefono, String email, Date fechaEntrada, String contrasenna, String tipo) {
        this.name = name;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.fechaEntrada = fechaEntrada;
        this.contrasenna = contrasenna;
        this.tipo = tipo;

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

}
