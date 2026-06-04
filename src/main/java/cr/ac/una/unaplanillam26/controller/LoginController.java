package cr.ac.una.unaplanillam26.controller;

import cr.ac.una.unaplanillam26.model.EmpleadoDto;
import cr.ac.una.unaplanillam26.service.EmpleadoService;
import cr.ac.una.unaplanillam26.util.AppContext;
import cr.ac.una.unaplanillam26.util.FlowController;
import cr.ac.una.unaplanillam26.util.Mensaje;
import cr.ac.una.unaplanillam26.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emena
 */
public class LoginController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imvFondo;
    @FXML
    private MFXButton btnCancelar;
    @FXML
    private MFXTextField txfUsuario;
    @FXML
    private MFXPasswordField pswClave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }

    @Override
    public void initialize() {
        txfUsuario.clear();
        pswClave.clear();
    }

    private void comprobarUsuario(){
        try {
            Respuesta respuesta = new EmpleadoService().getEmpleadoPorUsuario(txfUsuario.getText(), pswClave.getText());
            if (respuesta.getEstado()) {
                EmpleadoDto usuario = (EmpleadoDto) respuesta.getResultado("Empleado");
                AppContext.getInstance().set("UsuarioActual", usuario);
                getStage().close();
                FlowController.getInstance().goMain();
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Iniciar Sesion", getStage(), respuesta.getMensaje());
            }
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Error Consultando el usuario", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar Usuario", getStage(), "Ocurrio un error consultando el Usuario");
        }    
    }
    
    @FXML
    private void onActionBtnCancelar(ActionEvent event) {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnIngresar(ActionEvent event) {
        if (!txfUsuario.getText().isBlank() && !pswClave.getText().isBlank()) {
            comprobarUsuario();
        }
    }

    @FXML
    private void onKeyPressedTxfUser(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !txfUsuario.getText().isBlank()) {
            pswClave.requestFocus();
        }
    }

    @FXML
    private void onKeyPressedPswUser(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !pswClave.getText().isBlank()) {
            comprobarUsuario();
        }
    }

}
