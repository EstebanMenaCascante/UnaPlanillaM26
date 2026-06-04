/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.unaplanillam26.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.unaplanillam26.model.EmpleadoDto;
import cr.ac.una.unaplanillam26.service.EmpleadoService;
import cr.ac.una.unaplanillam26.util.BindingUtils;
import cr.ac.una.unaplanillam26.util.Formato;
import cr.ac.una.unaplanillam26.util.Mensaje;
import cr.ac.una.unaplanillam26.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author emena
 */
public class EmpleadosController extends Controller implements Initializable {

    @FXML
    private MFXTextField txfId;
    @FXML
    private MFXTextField txfNombre;
    @FXML
    private MFXTextField txfPrimerApellido;
    @FXML
    private MFXTextField txfSegundoApellido;
    @FXML
    private MFXTextField txfCedula;
    @FXML
    private ToggleGroup tggGenero;
    @FXML
    private MFXCheckbox chkAdministrador;
    @FXML
    private MFXCheckbox chkActivo;
    @FXML
    private MFXDatePicker dtpFechaIngreso;
    @FXML
    private MFXDatePicker dtpFechaSalida;
    @FXML
    private MFXTextField txfCorreo;
    @FXML
    private MFXTextField txfUsuario;
    @FXML
    private MFXPasswordField pswClave;
    @FXML
    private MFXButton btnNuevo;
    @FXML
    private MFXButton btnBuscar;
    @FXML
    private MFXButton btnEliminar;
    @FXML
    private MFXButton btnGuardar;

    @FXML
    private AnchorPane root;
    @FXML
    private RadioButton rdbMasculino;
    @FXML
    private RadioButton rdbFemenino;

    private EmpleadoDto empleado;
    private ObjectProperty<EmpleadoDto> empleadoProperty = new SimpleObjectProperty<>();
    private List<Node> requeridos = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rdbFemenino.setUserData("F");
        rdbMasculino.setUserData("M");
        txfId.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txfNombre.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));
        txfPrimerApellido.delegateSetTextFormatter(Formato.getInstance().letrasFormat(15));
        txfSegundoApellido.delegateSetTextFormatter(Formato.getInstance().letrasFormat(15));
        txfCedula.delegateSetTextFormatter(Formato.getInstance().cedulaFormat(40));
        txfCorreo.delegateSetTextFormatter(Formato.getInstance().maxLengthFormat(80));
        txfUsuario.delegateSetTextFormatter(Formato.getInstance().letrasFormat(15));
        pswClave.delegateSetTextFormatter(Formato.getInstance().maxLengthFormat(8));
        empleado = new EmpleadoDto();

        bindEmpleado();
        cargarValoresDefecto();
        indicarRequeridos();
    }

    @Override
    public void initialize() {
    }

    private void bindEmpleado() {
        try {
            empleadoProperty.addListener((ov, oldVal, newVal) -> {
                if (oldVal != null) {
                    txfId.textProperty().unbind();
                    txfCedula.textProperty().unbindBidirectional(oldVal.getCedulaProperty());
                    txfNombre.textProperty().unbindBidirectional(oldVal.getNombreProperty());
                    txfPrimerApellido.textProperty().unbindBidirectional(oldVal.getPrimerApellidoProperty());
                    txfSegundoApellido.textProperty().unbindBidirectional(oldVal.getSegundoApellidoProperty());
                    txfCorreo.textProperty().unbindBidirectional(oldVal.getCorreoProperty());
                    txfUsuario.textProperty().unbindBidirectional(oldVal.getUsuarioProperty());
                    pswClave.textProperty().unbindBidirectional(oldVal.getClaveProperty());
                    chkAdministrador.selectedProperty().unbindBidirectional(oldVal.getAdministradorProperty());
                    chkActivo.selectedProperty().unbindBidirectional(oldVal.getActivoProperty());
                    dtpFechaIngreso.valueProperty().unbindBidirectional(oldVal.getFechaIngresoProperty());
                    dtpFechaSalida.valueProperty().unbindBidirectional(oldVal.getFechaSalidaProperty());
                    BindingUtils.unbindToggleGroupToProperty(tggGenero, oldVal.getGeneroProperty());
                }
                if (newVal != null) {
                    if (newVal.getIdProperty().get() != null
                            && !newVal.getIdProperty().get().isBlank()) {
                        txfId.textProperty().bind(newVal.getIdProperty());
                    }
                    txfCedula.textProperty().bindBidirectional(newVal.getCedulaProperty());
                    txfNombre.textProperty().bindBidirectional(newVal.getNombreProperty());
                    txfPrimerApellido.textProperty().bindBidirectional(newVal.getPrimerApellidoProperty());
                    txfSegundoApellido.textProperty().bindBidirectional(newVal.getSegundoApellidoProperty());
                    txfCorreo.textProperty().bindBidirectional(newVal.getCorreoProperty());
                    txfUsuario.textProperty().bindBidirectional(newVal.getUsuarioProperty());
                    pswClave.textProperty().bindBidirectional(newVal.getClaveProperty());
                    chkAdministrador.selectedProperty().bindBidirectional(newVal.getAdministradorProperty());
                    chkActivo.selectedProperty().bindBidirectional(newVal.getActivoProperty());
                    dtpFechaIngreso.valueProperty().bindBidirectional(newVal.getFechaIngresoProperty());
                    dtpFechaSalida.valueProperty().bindBidirectional(newVal.getFechaSalidaProperty());
                    BindingUtils.bindToggleGroupToProperty(tggGenero, newVal.getGeneroProperty());
                }
            });
        } catch (Exception e) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error al realizar el bindeo", getStage(),
                    "Ocurrió un error al realizar el bindeo.");
        }
    }

    private void cargarValoresDefecto() {
        this.empleado = new EmpleadoDto();
        this.empleado.setActivo(Boolean.TRUE);
        this.empleado.setAdministrador(Boolean.FALSE);
        this.empleado.setFechaIngreso(LocalDate.now());
        this.empleado.setGenero("M");
        empleadoProperty.setValue(this.empleado);
        validarAdministrador();
        txfId.clear();
        txfId.requestFocus();
    }

    private void validarAdministrador() {
        if (chkAdministrador.isSelected()) {
            this.requeridos.addAll(Arrays.asList(txfUsuario, pswClave));
            txfUsuario.setDisable(false);
            pswClave.setDisable(false);
        } else {
            this.requeridos.removeAll(Arrays.asList(txfUsuario, pswClave));
            txfUsuario.clear();
            txfUsuario.setDisable(true);
            pswClave.clear();
            pswClave.setDisable(true);
        }
    }

    private void indicarRequeridos() {
        this.requeridos.clear();
        this.requeridos.addAll(Arrays.asList(txfCedula, txfNombre, txfPrimerApellido, dtpFechaIngreso));

    }

    public String validarRequeridos() {
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
            } else if (node instanceof MFXPasswordField && (((MFXPasswordField) node).getText() == null || ((MFXPasswordField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXPasswordField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXPasswordField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
                if (validos) {
                    invalidos += ((MFXDatePicker) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXDatePicker) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXComboBox && ((MFXComboBox) node).getSelectionModel().getSelectedIndex() < 0) {
                if (validos) {
                    invalidos += ((MFXComboBox) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXComboBox) node).getFloatingText();
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

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        if (new Mensaje().showConfirmation("Limpiar Empleado", getStage(),
                "Está seguro de que desea limpiar el registro??")) {
            cargarValoresDefecto();
        }
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        try {
            if (this.empleado.getId() == null) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Eliminar Empleados", getStage(), "Favor consultar el empleado a eliminar");
            } else {
                EmpleadoService empleadoService = new EmpleadoService();
                Respuesta respuesta = empleadoService.eliminarEmpleado(this.empleado.getId());
                if (respuesta.getEstado()) {
                    cargarValoresDefecto();
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Empleado", getStage(), "El empleado se eliminó correctamente");
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Empleado", getStage(), respuesta.getMensaje());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error eliminando el empldeado", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Empldeado",
                    getStage(), "Ocurrio un error al eliminar el empleado");
        }
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isBlank()) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Guardar Empleados", getStage(), invalidos);
            } else {
                EmpleadoService empleadoService = new EmpleadoService();
                Respuesta respuesta = empleadoService.guardarEmpleado(this.empleado);
                if (respuesta.getEstado()) {
                    this.empleado = (EmpleadoDto) respuesta.getResultado("Empleado");
                    this.empleadoProperty.set(this.empleado);
                    validarAdministrador();
                    validarRequeridos();
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar Empleados", getStage(), "El empleado se guardó correctamente");
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Empleado", getStage(), respuesta.getMensaje());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error guardadno el empldeado", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Empldeado",
                    getStage(), "Ocurrio un error al guardar el empleado");
        }
    }

    @FXML
    private void onActionChkAdministrador(ActionEvent event) {
        validarAdministrador();
    }

    private void cargarEmpleado(Long id) {
        try {
            EmpleadoService empleadoService = new EmpleadoService();
            Respuesta respuesta = empleadoService.getEmpleado(id);
            if (respuesta.getEstado()) {
                this.empleado = (EmpleadoDto) respuesta.getResultado("Empleado");
                this.empleadoProperty.set(this.empleado);
                validarAdministrador();
                validarRequeridos();
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Empleado", getStage(), respuesta.getMensaje());
            }

        } catch (Exception ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, "Error cargando el empldeado", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar Empldeado",
                    getStage(), "Ocurrio un error al cargar el empleado");
        }
    }

    @FXML
    private void onKeyPressedTxfId(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfId.getText().isBlank()) {
            cargarEmpleado(Long.valueOf(txfId.getText()));

        }
    }

}
