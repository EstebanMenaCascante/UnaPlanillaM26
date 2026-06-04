/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.unaplanillam26.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author emena
 */
@Entity
@Table(name = "PLAM_TIPOPLANILLAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPlanilla.findAll", query = "SELECT t FROM TipoPlanilla t"),
    @NamedQuery(name = "TipoPlanilla.findByTplaId", query = "SELECT t FROM TipoPlanilla t WHERE t.id = :id")})
public class TipoPlanilla implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PLAM_TIPOSPLANILLA_TP_ID_GENERATOR", sequenceName = "una.PLAM_TIPOPLANILLAS_SEQ01", allocationSize =  1)
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "PLAM_TIPOSPLANILLA_TP_ID_GENERATOR")
    @Basic(optional = false)
    @Column(name = "TPLA_ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "TPLA_CODIGO")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "TPLA_DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "TPLA_PLAXMES")
    private Long plaXMes;
    @Column(name = "TPLA_ANOULTPLA")
    private Integer anoultpla;
    @Column(name = "TPLA_MESULTPLA")
    private Integer mesultpla;
    @Column(name = "TPLA_NUMULTPLA")
    private Integer numultpla;
    @Basic(optional = false)
    @Column(name = "TPLA_ESTADO")
    private String estado;
    @Basic(optional = false)
    @Column(name = "TPLA_VERSION")
    private Long version;
    @JoinTable(name = "PLAM_EMPLEADOSPLANILLA", joinColumns = {
        @JoinColumn(name = "EXP_IDTPLA", referencedColumnName = "TPLA_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "EXP_IDEMP", referencedColumnName = "EMP_ID")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Empleado> empleados;

    public TipoPlanilla() {
    }

    public TipoPlanilla(TiposPlanillaDto tiposPlanillaDto) {
        this.id = tiposPlanillaDto.getId();
        actualizar(tiposPlanillaDto);
    }
    
    public void actualizar(TiposPlanillaDto tiposPlanillaDto) {
            this.codigo = tiposPlanillaDto.getCodigo();
        this.descripcion = tiposPlanillaDto.getDescripcion();
        this.plaXMes = Long.valueOf(tiposPlanillaDto.getPlanillasXMes());
        this.estado = tiposPlanillaDto.getActivo() ? "A" : "I";
        this.version = tiposPlanillaDto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getPlaxmes() {
        return plaXMes;
    }

    public void setPlaxmes(Long plaxmes) {
        this.plaXMes = plaxmes;
    }

    public Integer getAnoultpla() {
        return anoultpla;
    }

    public void setAnoultpla(Integer anoultpla) {
        this.anoultpla = anoultpla;
    }

    public Integer getMesultpla() {
        return mesultpla;
    }

    public void setMesultpla(Integer mesultpla) {
        this.mesultpla = mesultpla;
    }

    public Integer getNumultpla() {
        return numultpla;
    }

    public void setNumultpla(Integer numultpla) {
        this.numultpla = numultpla;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @XmlTransient
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TipoPlanilla)) {
            return false;
        }
        TipoPlanilla other = (TipoPlanilla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.unaplanillam26.model.TipoPlanilla[ tplaId=" + id + " ]";
    }

}