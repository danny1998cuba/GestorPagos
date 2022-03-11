/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findById", query = "SELECT p FROM Pago p WHERE p.id = :id")
    , @NamedQuery(name = "Pago.findByFecha", query = "SELECT p FROM Pago p WHERE p.fecha = :fecha")
    , @NamedQuery(name = "Pago.findByDiasTrabajados", query = "SELECT p FROM Pago p WHERE p.diasTrabajados = :diasTrabajados")
    , @NamedQuery(name = "Pago.findByPagoAdicional", query = "SELECT p FROM Pago p WHERE p.pagoAdicional = :pagoAdicional")
    , @NamedQuery(name = "Pago.findByEvaluacion", query = "SELECT p FROM Pago p WHERE p.evaluacion = :evaluacion")})
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "dias_trabajados")
    private double diasTrabajados;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pago_adicional")
    private Double pagoAdicional;
    @Basic(optional = false)
    @Column(name = "evaluacion")
    private int evaluacion;
    @JoinColumn(name = "id_trabajador", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Trabajador idTrabajador;

    public Pago() {
    }

    public Pago(Integer id) {
        this.id = id;
    }

    public Pago(Integer id, Date fecha, int diasTrabajados, int evaluacion) {
        this.id = id;
        this.fecha = fecha;
        this.diasTrabajados = diasTrabajados;
        this.evaluacion = evaluacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getDiasTrabajados() {
        return diasTrabajados;
    }

    public void setDiasTrabajados(double diasTrabajados) {
        this.diasTrabajados = diasTrabajados;
    }

    public Double getPagoAdicional() {
        return pagoAdicional;
    }

    public void setPagoAdicional(Double pagoAdicional) {
        this.pagoAdicional = pagoAdicional;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
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
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getFecha() + " ";
    }
    
}
