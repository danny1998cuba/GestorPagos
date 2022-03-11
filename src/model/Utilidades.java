/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "utilidades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utilidades.findAll", query = "SELECT u FROM Utilidades u")
    , @NamedQuery(name = "Utilidades.findById", query = "SELECT u FROM Utilidades u WHERE u.id = :id")
    , @NamedQuery(name = "Utilidades.findByTrimestre", query = "SELECT u FROM Utilidades u WHERE u.trimestre = :trimestre")
    , @NamedQuery(name = "Utilidades.findByPagoa", query = "SELECT u FROM Utilidades u WHERE u.pagoa = :pagoa")
    , @NamedQuery(name = "Utilidades.findByPagob", query = "SELECT u FROM Utilidades u WHERE u.pagob = :pagob")
    , @NamedQuery(name = "Utilidades.findByPagoc", query = "SELECT u FROM Utilidades u WHERE u.pagoc = :pagoc")
    , @NamedQuery(name = "Utilidades.findByPagod", query = "SELECT u FROM Utilidades u WHERE u.pagod = :pagod")
    , @NamedQuery(name = "Utilidades.findByPagoe", query = "SELECT u FROM Utilidades u WHERE u.pagoe = :pagoe")})
public class Utilidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "trimestre")
    private String trimestre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pagoa")
    private Double pagoa;
    @Column(name = "pagob")
    private Double pagob;
    @Column(name = "pagoc")
    private Double pagoc;
    @Column(name = "pagod")
    private Double pagod;
    @Column(name = "pagoe")
    private Double pagoe;
    @JoinColumn(name = "id_trabajador", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Trabajador idTrabajador;

    public Utilidades() {
    }

    public Utilidades(Integer id) {
        this.id = id;
    }

    public Utilidades(Integer id, String trimestre) {
        this.id = id;
        this.trimestre = trimestre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public Double getPagoa() {
        return pagoa;
    }

    public void setPagoa(Double pagoa) {
        this.pagoa = pagoa;
    }

    public Double getPagob() {
        return pagob;
    }

    public void setPagob(Double pagob) {
        this.pagob = pagob;
    }

    public Double getPagoc() {
        return pagoc;
    }

    public void setPagoc(Double pagoc) {
        this.pagoc = pagoc;
    }

    public Double getPagod() {
        return pagod;
    }

    public void setPagod(Double pagod) {
        this.pagod = pagod;
    }

    public Double getPagoe() {
        return pagoe;
    }

    public void setPagoe(Double pagoe) {
        this.pagoe = pagoe;
    }

    public Trabajador getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Trabajador idTrabajador) {
        this.idTrabajador = idTrabajador;
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
        if (!(object instanceof Utilidades)) {
            return false;
        }
        Utilidades other = (Utilidades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Utilidades[ id=" + id + " ]";
    }
    
}
