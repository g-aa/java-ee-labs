/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyEntities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andrey
 */
@Entity
@Table(name = "EMPLOYEEHISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employeehistory.findAll", query = "SELECT e FROM Employeehistory e")
    , @NamedQuery(name = "Employeehistory.findById", query = "SELECT e FROM Employeehistory e WHERE e.id = :id")
    , @NamedQuery(name = "Employeehistory.findByPosition", query = "SELECT e FROM Employeehistory e WHERE e.position = :position")
    , @NamedQuery(name = "Employeehistory.findByManager", query = "SELECT e FROM Employeehistory e WHERE e.manager = :manager")
    , @NamedQuery(name = "Employeehistory.findByHire", query = "SELECT e FROM Employeehistory e WHERE e.hire = :hire")
    , @NamedQuery(name = "Employeehistory.findByDismiss", query = "SELECT e FROM Employeehistory e WHERE e.dismiss = :dismiss")})
public class Employeehistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "POSITION")
    private String position;
    @Column(name = "MANAGER")
    private Integer manager;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HIRE")
    @Temporal(TemporalType.DATE)
    private Date hire;
    @Column(name = "DISMISS")
    @Temporal(TemporalType.DATE)
    private Date dismiss;
    @JoinColumn(name = "CODE", referencedColumnName = "CODE")
    @ManyToOne
    private Employees code;

    public Employeehistory() {
    }

    public Employeehistory(Integer id) {
        this.id = id;
    }

    public Employeehistory(Integer id, String position, Date hire) {
        this.id = id;
        this.position = position;
        this.hire = hire;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public Date getHire() {
        return hire;
    }

    public void setHire(Date hire) {
        this.hire = hire;
    }

    public Date getDismiss() {
        return dismiss;
    }

    public void setDismiss(Date dismiss) {
        this.dismiss = dismiss;
    }

    public Employees getCode() {
        return code;
    }

    public void setCode(Employees code) {
        this.code = code;
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
        if (!(object instanceof Employeehistory)) {
            return false;
        }
        Employeehistory other = (Employeehistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MyEntities.Employeehistory[ id=" + id + " ]";
    }
    
}
