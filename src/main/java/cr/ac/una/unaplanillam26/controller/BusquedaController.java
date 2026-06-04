/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.unaplanillam26.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.unaplanillam26.model.TiposPlanillaDto;
import cr.ac.una.unaplanillam26.service.TipoPlanillaService;
import cr.ac.una.unaplanillam26.util.FlowController;
import cr.ac.una.unaplanillam26.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author emena
 */
public class BusquedaController extends Controller implements Initializable {

    @FXML
    private MFXButton btnFiltrar;
    @FXML
    private TableView<TiposPlanillaDto> tbvFiltro;
    @FXML
    private MFXButton btnAceptar;
    @FXML
    private Label lblNombreVentana;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox vboxParametros;

    private final List<MFXTextField> textFieldList = new ArrayList<>();
    private final ObservableList<TiposPlanillaDto> planillasDisponibles = FXCollections.observableArrayList();
    private final TipoPlanillaService tipoPlanillaService = new TipoPlanillaService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNombreVentana.setText("Busqueda Tipos Planilla");
        agregarColumnasTable();
        agregarTextFills();
        tbvFiltro.setItems(planillasDisponibles);
        planillasDisponibles.clear();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnFiltrar(ActionEvent event) {
        cargarPlanillas();
    }

    @FXML
    private void onActionBtnAceptar(ActionEvent event) {
        try {
            TiposPlanillaDto seleccionada = tbvFiltro.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Buscar Planilla", getStage(), "Debe seleccionar una planilla de la tabla antes de aceptar.");
                return;
            }

            if (seleccionada.getId() == null) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Buscar Planilla", getStage(), "La planilla seleccionada no tiene un identificador válido.");
                return;
            }

            TiposPlanillaController controller = (TiposPlanillaController) FlowController.getInstance().getController("TiposPlanillaView");
            controller.cargarPlanilla(seleccionada.getId());
            getStage().close();
        } catch (Exception ex) {
            Logger.getLogger(BusquedaController.class.getName()).log(Level.SEVERE, "Error al aceptar la planilla seleccionada", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Buscar Planilla", getStage(), "Ocurrio un error al cargar la planilla seleccionada.");
        }
    }

    public void agregarColumnasTable() {
        TableColumn<TiposPlanillaDto, String> tbcCodigo = new TableColumn<>("Codigo");
        tbcCodigo.setPrefWidth(100);
        tbcCodigo.setCellValueFactory(cd -> cd.getValue().getCodigoProperty());

        TableColumn<TiposPlanillaDto, String> tbcDescripcion = new TableColumn<>("Descripcion");
        tbcDescripcion.setPrefWidth(100);
        tbcDescripcion.setCellValueFactory(cd -> cd.getValue().getDescripcionProperty());

        TableColumn<TiposPlanillaDto, String> tbcPlanillasXMes = new TableColumn<>("PlanillasXMes");
        tbcPlanillasXMes.setPrefWidth(100);
        tbcPlanillasXMes.setCellValueFactory(cd -> cd.getValue().getPlanillasXMesProperty());

        tbvFiltro.getColumns().add(tbcCodigo);
        tbvFiltro.getColumns().add(tbcDescripcion);
        tbvFiltro.getColumns().add(tbcPlanillasXMes);
    }

    public void agregarTextFills() {
        MFXTextField txfCodigo = new MFXTextField();
        HBox.setHgrow(txfCodigo, Priority.ALWAYS);
        txfCodigo.setMaxWidth(Double.MAX_VALUE);
        txfCodigo.setFloatingText("Codigo");

        MFXTextField txfDescripcion = new MFXTextField();
        HBox.setHgrow(txfDescripcion, Priority.ALWAYS);
        txfDescripcion.setMaxWidth(Double.MAX_VALUE);
        txfDescripcion.setFloatingText("Descripcion");

        MFXTextField txfNombre = new MFXTextField();
        HBox.setHgrow(txfNombre, Priority.ALWAYS);
        txfNombre.setMaxWidth(Double.MAX_VALUE);
        txfNombre.setFloatingText("Nombre Empleado");

        vboxParametros.getChildren().addAll(txfCodigo);
        vboxParametros.getChildren().addAll(txfDescripcion);
        vboxParametros.getChildren().addAll(txfNombre);

        textFieldList.add(txfCodigo);
        textFieldList.add(txfDescripcion);
        textFieldList.add(txfNombre);
    }

    private void cargarPlanillas() {
        String codigo = textFieldList.isEmpty() ? "" : textFieldList.get(0).getText();
        String descripcion = textFieldList.size() > 1 ? textFieldList.get(1).getText() : "";
        String nombreEmpleado = textFieldList.size() > 2 ? textFieldList.get(2).getText() : "";

        var respuesta = tipoPlanillaService.buscarTipoPlanillas(codigo, descripcion, nombreEmpleado);
        if (respuesta.getEstado()) {
            List<TiposPlanillaDto> resultado = (List<TiposPlanillaDto>) respuesta.getResultado("TipoPlanillas");
            planillasDisponibles.setAll(resultado);
        } else {
            planillasDisponibles.clear();
        }

        tbvFiltro.setItems(planillasDisponibles);
    }

}
