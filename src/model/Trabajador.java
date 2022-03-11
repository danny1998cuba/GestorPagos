/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Utils.Utils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "trabajador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajador.findAll", query = "SELECT t FROM Trabajador t")
    , @NamedQuery(name = "Trabajador.findById", query = "SELECT t FROM Trabajador t WHERE t.id = :id")
    , @NamedQuery(name = "Trabajador.findByNombre", query = "SELECT t FROM Trabajador t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Trabajador.findByNombre2", query = "SELECT t FROM Trabajador t WHERE t.nombre2 = :nombre2")
    , @NamedQuery(name = "Trabajador.findByApellido1", query = "SELECT t FROM Trabajador t WHERE t.apellido1 = :apellido1")
    , @NamedQuery(name = "Trabajador.findByApellido2", query = "SELECT t FROM Trabajador t WHERE t.apellido2 = :apellido2")
    , @NamedQuery(name = "Trabajador.findByCi", query = "SELECT t FROM Trabajador t WHERE t.ci = :ci")})
public class Trabajador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nombre2")
    private String nombre2;
    @Basic(optional = false)
    @Column(name = "apellido1")
    private String apellido1;
    @Basic(optional = false)
    @Column(name = "apellido2")
    private String apellido2;
    @Basic(optional = false)
    @Column(name = "ci")
    private String ci;
    @JoinColumn(name = "id_clasificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clasificacion idClasificacion;
    @JoinColumn(name = "id_escala", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GrupoEscala idEscala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTrabajador")
    private List<Utilidades> utilidadesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTrabajador")
    private List<Pago> pagoList;

    public Trabajador() {
    }

    public Trabajador(Integer id) {
        this.id = id;
    }

    public Trabajador(Integer id, String nombre, String apellido1, String apellido2, String ci) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.ci = ci;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Clasificacion getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(Clasificacion idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public GrupoEscala getIdEscala() {
        return idEscala;
    }

    public void setIdEscala(GrupoEscala idEscala) {
        this.idEscala = idEscala;
    }

    @XmlTransient
    public List<Utilidades> getUtilidadesList() {
        return utilidadesList;
    }

    public void setUtilidadesList(List<Utilidades> utilidadesList) {
        this.utilidadesList = utilidadesList;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
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
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Utils.fullName(this);
    }

}
