/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.unaplanillam26.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author emena
 */
public class TiposPlanillaDto {

    private StringProperty id;
    private StringProperty codigo;
    private StringProperty descripcion;
    private StringProperty planillasXMes;
    private BooleanProperty activo;
    private Long version;
    private ObservableList<EmpleadoDto> empleados;
    private List<EmpleadoDto> empleadosEliminados;

    public TiposPlanillaDto() {
        this.id = new SimpleStringProperty("");
        this.codigo = new SimpleStringProperty("");
        this.descripcion = new SimpleStringProperty("");
        this.planillasXMes = new SimpleStringProperty("");
        this.activo = new SimpleBooleanProperty(true);
        this.empleados = FXCollections.observableArrayList();
        this.empleadosEliminados = new ArrayList<>();
        version = (long) 0;
    }

    public TiposPlanillaDto(TipoPlanilla tipoPlanilla) {

        this.id = new SimpleStringProperty(tipoPlanilla.getId().toString());
        this.codigo = new SimpleStringProperty(tipoPlanilla.getCodigo());
        this.descripcion = new SimpleStringProperty(tipoPlanilla.getDescripcion());
        this.planillasXMes = new SimpleStringProperty(tipoPlanilla.getPlaxmes().toString());
        this.activo = new SimpleBooleanProperty(tipoPlanilla.getEstado().equals("A"));
        this.empleados = FXCollections.observableArrayList();
        this.empleadosEliminados = new ArrayList<>();
        this.version = tipoPlanilla.getVersion();

        if (tipoPlanilla.getEmpleados() != null) {
            for (Empleado emp : tipoPlanilla.getEmpleados()) {
                EmpleadoDto dto = new EmpleadoDto(emp);
                dto.setModificado(false);
                this.empleados.add(dto);
            }
        }

    }

    public void setEmpleados(ObservableList<EmpleadoDto> empleados) {
        this.empleados.clear();
        if (empleados != null) {
            this.empleados.addAll(empleados);
        }
    }

    public ObservableList<EmpleadoDto> getEmpleados() {
        return empleados;
    }

    // Getters
    public Long getId() {
        if (this.id.get() != null && !this.id.get().isBlank()) {
            return Long.valueOf(this.id.get());
        }
        return null;
    }

    public String getCodigo() {
        return codigo.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public String getPlanillasXMes() {
        return planillasXMes.get();
    }

    public Boolean getActivo() {
        return activo.get();
    }

    public StringProperty getIdProperty() {
        return id;
    }

    public StringProperty getCodigoProperty() {
        return codigo;
    }

    public StringProperty getDescripcionProperty() {
        return descripcion;
    }

    public StringProperty getPlanillasXMesProperty() {
        return planillasXMes;
    }

    public BooleanProperty getActivoProperty() {
        return activo;
    }

    public Long getVersion() {
        return version;
    }

    /*
        public Integer getPlanillasXMes() {
        try {
            String val = planillasXMes.get();
            if (val == null || val.isBlank()) {
                return null;
            }
            return Integer.valueOf(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }
     */
    
    public void setId(Long id) {
        if (id == null) {
            this.id.set("");
            return;
        }
        this.id.set(id.toString());
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public void setPlanillasXMes(String planillasXMes) {
        this.planillasXMes.set(planillasXMes);
    }

    /* 
    public void setPlanillasXMes(Integer planillasXMes) {
        if (planillasXMes == null) {
            this.planillasXMes.set("");
        } else {
            this.planillasXMes.set(planillasXMes.toString());
        }
    }
     */
    public void setActivo(Boolean activo) {
        this.activo.set(activo);
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<EmpleadoDto> getEmpleadosEliminados() {
        return empleadosEliminados;
    }

    public void setEmpleadosEliminados(List<EmpleadoDto> empleadosEliminados) {
        this.empleadosEliminados = empleadosEliminados != null ? empleadosEliminados : new ArrayList<>();
    }
}
