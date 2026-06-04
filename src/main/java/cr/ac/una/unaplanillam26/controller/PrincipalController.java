/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.unaplanillam26.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cr.ac.una.unaplanillam26.model.EmpleadoDto;
import cr.ac.una.unaplanillam26.util.AppContext;
import cr.ac.una.unaplanillam26.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emena
 */
public class PrincipalController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnEmpleados;
    @FXML
    private MFXButton btnTiposPlanilla;
    @FXML
    private MFXButton btnCerrarSesion;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblEmpleado;
    
    private EmpleadoDto empleado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      empleado = (EmpleadoDto) AppContext.getInstance().get("UsuarioActual");
        lblUsuario.setText(empleado.getUsuario());
        lblEmpleado.setText(empleado.getNombre() + empleado.getPrimerApellido());
    }    

    @Override
    public void initialize() {
    }
    
    public void setEmpleado(EmpleadoDto empleado) {
    this.empleado = empleado;
    System.out.println("setEmpleado llamado: " + empleado.getNombre()); // ← verificar
    System.out.println("labelUsuario es null? " + (lblEmpleado == null)); // ← verificar
    if (empleado != null) {
        lblEmpleado.setText(empleado.getNombre() + " " + empleado.getPrimerApellido());
    }
}

    @FXML
    private void onActionBtnEmpleados(ActionEvent event) {
        FlowController.getInstance().goView("EmpleadosView");
    }

    @FXML
    private void onActionBtnTiposPlanilla(ActionEvent event) {
        FlowController.getInstance().goView("TiposPlanillaView");
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().salir();
    }

    @FXML
    private void onActionBtnCerrarSesion(ActionEvent event) {
        this.empleado = new EmpleadoDto();
        AppContext.getInstance().delete("UsuarioActual");
        ((Stage) btnCerrarSesion.getScene().getWindow()).close();
        FlowController.getInstance().goViewInWindow("LoginView");
    }
    
}
