/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.unaplanillam26.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.unaplanillam26.model.EmpleadoDto;
import cr.ac.una.unaplanillam26.model.TiposPlanillaDto;
import cr.ac.una.unaplanillam26.service.EmpleadoService;
import cr.ac.una.unaplanillam26.service.TipoPlanillaService;
import cr.ac.una.unaplanillam26.util.FlowController;
import cr.ac.una.unaplanillam26.util.Formato;
import cr.ac.una.unaplanillam26.util.Mensaje;
import cr.ac.una.unaplanillam26.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author emena
 */
public class TiposPlanillaController extends Controller implements Initializable {

    @FXML
    private MFXTextField txfId;
    @FXML
    private MFXCheckbox chkActivo;
    @FXML
    private MFXTextField txfCodigo;
    @FXML
    private MFXTextField txfDescripcion;
    @FXML
    private MFXTextField txfPlanillasPorMes;
    @FXML
    private MFXTextField txfIdEmpleado;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private TableView<EmpleadoDto> tbvEmpleados;
    @FXML
    private TableColumn<EmpleadoDto, String> colId;
    @FXML
    private TableColumn<EmpleadoDto, String> colNombre;
    @FXML
    private TableColumn<EmpleadoDto, Boolean> colEliminar;
    @FXML
    private MFXButton btnNuevo;

    private TiposPlanillaDto tipoPlanilla;
    private ObjectProperty<TiposPlanillaDto> tipoPlanillaProperty; //= new SimpleObjectProperty<>();
    private ArrayList<Node> requeridos = new ArrayList<>();

    private EmpleadoDto empleado;
    private ObjectProperty<EmpleadoDto> empleadoProperty = new SimpleObjectProperty<>();
    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnGuardar;

    // Declaraciones
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txfId.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        //txfCodigo.delegateSetTextFormatter(Formato.getInstance().letrasFormat(10));
        txfDescripcion.delegateSetTextFormatter(Formato.getInstance().letrasFormat(50));
        txfPlanillasPorMes.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txfIdEmpleado.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txfNombre.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));
        this.tipoPlanilla = new TiposPlanillaDto();
        tipoPlanillaProperty = new SimpleObjectProperty<>();
        tipoPlanillaProperty.set(tipoPlanilla);
        tbvEmpleados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bindTipoPlanilla();
        this.empleado = new EmpleadoDto();
        bindEmpleado();
        cargarValoresDefecto();
        indicarRequeridos();

        colId.setCellValueFactory(cd -> cd.getValue().getIdProperty());
        colNombre.setCellValueFactory(cd -> cd.getValue().getNombreProperty());
        colEliminar.setCellValueFactory(cd -> new SimpleBooleanProperty(cd.getValue() != null));

        colEliminar.setCellFactory(p -> new ButtonCell());
        tbvEmpleados.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                this.empleado = newValue;
                this.empleadoProperty.setValue(this.empleado);
            }
        });
    }

    @Override
    public void initialize() {

    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isBlank()) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Guardar Planilla", getStage(), invalidos);
            } else {
                TipoPlanillaService planillaService = new TipoPlanillaService();
                Respuesta respuesta = planillaService.guardarTipoPlanilla(this.tipoPlanilla);
                if (respuesta.getEstado()) {
                    this.tipoPlanilla = (TiposPlanillaDto) respuesta.getResultado("TipoPlanilla");
                    this.tipoPlanillaProperty.set(this.tipoPlanilla);
                    validarRequeridos();
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar Planilla", getStage(), "La planilla se guardo correctamente");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error guardando la planilla", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Planilla", getStage(), "Ocurrio un error al guardar la planilla");
        }
    }

    private void bindTipoPlanilla() {
        try {
            tipoPlanillaProperty.addListener((obs, oldVal, newVal) -> {
                if (oldVal != null) {
                    txfId.textProperty().unbindBidirectional(oldVal.getIdProperty());
                    chkActivo.selectedProperty().unbindBidirectional(oldVal.getActivoProperty());
                    txfCodigo.textProperty().unbindBidirectional(oldVal.getCodigoProperty());
                    txfDescripcion.textProperty().unbindBidirectional(oldVal.getDescripcionProperty());
                    txfPlanillasPorMes.textProperty().unbindBidirectional(oldVal.getPlanillasXMesProperty());
                }
                if (newVal != null) {
                    txfId.textProperty().bindBidirectional(newVal.getIdProperty());
                    chkActivo.selectedProperty().bindBidirectional(newVal.getActivoProperty());
                    txfCodigo.textProperty().bindBidirectional(newVal.getCodigoProperty());
                    txfDescripcion.textProperty().bindBidirectional(newVal.getDescripcionProperty());
                    txfPlanillasPorMes.textProperty().bindBidirectional(newVal.getPlanillasXMesProperty());
                }
            });
        } catch (Exception e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error al realizar el bindeo", getStage(), "Ocurrio un error al realizar el bindeo");
        }
    }

    private void cargarValoresDefecto() {
        tipoPlanilla = new TiposPlanillaDto();
        tipoPlanilla.setActivo(true);
        tipoPlanilla.setCodigo("");
        tipoPlanilla.setDescripcion("");
        tipoPlanilla.setPlanillasXMes("");
        tipoPlanillaProperty.set(tipoPlanilla);
        txfId.clear();
        txfId.requestFocus();
        limpiarEmpleado();
        cargarEmpleados();
    }

    private void limpiarEmpleado() {
        tbvEmpleados.getSelectionModel().select(null);
        this.empleado = new EmpleadoDto();
        this.empleadoProperty.setValue(this.empleado);
        txfIdEmpleado.clear();
        txfIdEmpleado.requestFocus();
    }

    private void cargarEmpleados() {
        tbvEmpleados.getItems().clear();
        tbvEmpleados.setItems(this.tipoPlanilla.getEmpleados());
        tbvEmpleados.refresh();
    }

    private void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txfCodigo, txfDescripcion, txfPlanillasPorMes));

    }

    private String validarRequeridos() {
        Boolean validos = true;
        String invalidos = "";
        for (Node node : requeridos) {
            if (node instanceof MFXTextField && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXTextField) node).getFloatingText();
                }
                validos = false;
            }
        }
        if (validos) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidos + "].";
        }
    }

    public void cargarPlanilla(Long id) {
        try {
            TipoPlanillaService planillaService = new TipoPlanillaService();
            Respuesta respuesta = planillaService.getTipoPlanilla(id);
            if (respuesta.getEstado()) {
                this.tipoPlanilla = (TiposPlanillaDto) respuesta.getResultado("TipoPlanilla");
                this.tipoPlanillaProperty.set(this.tipoPlanilla);
                validarRequeridos();
                cargarEmpleados();
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Planilla", getStage(), respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error Consultando la planilla", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar Planilla", getStage(), "Ocurrio un error consultando la planilla");
        }
    }

    private void cargarEmpleado(Long id) {
        try {
            EmpleadoService empleadoService = new EmpleadoService();
            Respuesta respuesta = empleadoService.getEmpleado(id);
            if (respuesta.getEstado()) {
                this.empleado = (EmpleadoDto) respuesta.getResultado("Empleado");
                this.empleadoProperty.set(this.empleado);
                validarRequeridos();
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar empleado", getStage(), respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error Consultando el empleado", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar Empleado", getStage(), "Ocurrio un error consultando el empleado");
        }
    }

    private void bindEmpleado() {
        try {
            empleadoProperty.addListener((ov, oldVal, newVal) -> {
                if (oldVal != null) {
                    txfIdEmpleado.textProperty().unbind();
                    txfNombre.textProperty().unbindBidirectional(oldVal.getNombreProperty());

                }
                if (newVal != null) {
                    if (newVal.getIdProperty().get() != null && !newVal.getIdProperty().get().isBlank()) {
                        txfIdEmpleado.textProperty().bind(newVal.getIdProperty());

                    }
                    txfNombre.textProperty().bindBidirectional(newVal.getNombreProperty());

                }
            });
        } catch (Exception e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error al realizar el bindeo", getStage(), "Ocurrio un error al realizar el bindeo");

        }
    }

    @FXML
    private void onKeyPressedTxfIdEmpleado(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfIdEmpleado.getText().isBlank()) {
            cargarEmpleado(Long.valueOf(txfIdEmpleado.getText()));
        }
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) {
        if (this.empleado.getId() == null && this.empleado.getNombre().isBlank()) {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Agregar Empleado", getStage(),
                    "Es necesario cargar un empleado para agregarlo a la lista");
        } else {
            if (this.tipoPlanilla.getId() == null) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Agregar Empleado", getStage(),
                        "Debe cargar el tipo de planilla que desea agregar empleados.");
            } else if (tbvEmpleados.getItems() == null || tbvEmpleados.getItems().stream().noneMatch((e) -> e.equals(this.empleado))) {
                this.empleado.setModificado(true);
                tbvEmpleados.getItems().add(this.empleado);
                tbvEmpleados.refresh();
            }
        }
    }

    @FXML
    private void onKeyPressedTxfIdPlanilla(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfId.getText().isBlank()) {
            cargarPlanilla(Long.valueOf(txfId.getText()));
        }
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("BusquedaView", getStage(), true);
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        try {
            if (this.tipoPlanilla.getId() == null) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Elminiar Planilla", getStage(), "Favor consultar la planilla a eliminar");
            } else {
                String invalidos = validarRequeridos();
                if (!invalidos.isBlank()) {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Eliimnar Planilla", getStage(), invalidos);
                } else {
                    TipoPlanillaService planillaService = new TipoPlanillaService();
                    Respuesta respuesta = planillaService.eliminarTipoPlanilla(this.tipoPlanilla.getId());
                    if (respuesta.getEstado()) {
                        cargarValoresDefecto();
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Planilla", getStage(), "La planilla se elimino correctamente");
                    } else {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Planilla", getStage(), respuesta.getMensaje());
                    }
                }
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Planilla", getStage(), "La planilla se elimino correctamente");
            }
        } catch (Exception e) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error Borrando la planilla", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Borrar Planilla", getStage(), "Ocurrio un error al Borrar la planilla");
        }
    }

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        if (new Mensaje().showConfirmation("Limpiar Planilla", getStage(), "Esta seguro que desea limpiar el registro?")) {
            cargarValoresDefecto();
        }
    }

    private class ButtonCell extends TableCell<EmpleadoDto, Boolean> {

        final Button cellButton = new Button();

        public ButtonCell() {
            cellButton.setPrefWidth(500);
            cellButton.getStyleClass().add("jfx-btnimg-tbveliminar");
            cellButton.setOnAction(event -> {
                EmpleadoDto emp = ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                tipoPlanilla.getEmpleadosEliminados().add(emp);
                tbvEmpleados.getItems().remove(emp);
                tbvEmpleados.refresh();
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }

    }
}
