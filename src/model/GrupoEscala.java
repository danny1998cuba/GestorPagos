/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "grupo_escala")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoEscala.findAll", query = "SELECT g FROM GrupoEscala g")
    , @NamedQuery(name = "GrupoEscala.findById", query = "SELECT g FROM GrupoEscala g WHERE g.id = :id")
    , @NamedQuery(name = "GrupoEscala.findBySalario", query = "SELECT g FROM GrupoEscala g WHERE g.salario = :salario")
    , @NamedQuery(name = "GrupoEscala.findByDescripcion", query = "SELECT g FROM GrupoEscala g WHERE g.descripcion = :descripcion")})
public class GrupoEscala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "salario")
    private double salario;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEscala")
    private List<Trabajador> trabajadorList;

    public GrupoEscala() {
    }

    public GrupoEscala(Integer id) {
        this.id = id;
    }

    public GrupoEscala(Integer id, double salario) {
        this.id = id;
        this.salario = salario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Trabajador> getTrabajadorList() {
        return trabajadorList;
    }

    public void setTrabajadorList(List<Trabajador> trabajadorList) {
        this.trabajadorList = trabajadorList;
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
        if (!(object instanceof GrupoEscala)) {
            return false;
        }
        GrupoEscala other = (GrupoEscala) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
    
}
