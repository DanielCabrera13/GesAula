package gesaula;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Observable;
import java.util.ResourceBundle;

import dad.gesaula.ui.model.Alumno;
import dad.gesaula.ui.model.Grupo;
import dad.gesaula.ui.model.Sexo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AlumnoFormController implements Initializable {
	
	//modelo
	
	private StringProperty nombre = new SimpleStringProperty();
	private StringProperty apellidos = new SimpleStringProperty();
	private ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();
	private ObjectProperty<Sexo> sexo = new SimpleObjectProperty<>();
	private BooleanProperty repite = new SimpleBooleanProperty();
	
	private ObjectProperty<Alumno> alumno = new SimpleObjectProperty<>();
	
	
	//vista
	
    @FXML
    private TextField apellidosText;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private TextField nombreText;

    @FXML
    private CheckBox repiteCheck;

    @FXML
    private GridPane root;

    @FXML
    private ComboBox<Sexo> sexoCombo;
    
    
    public GridPane getView() {
    	return root;
    }
    
    public AlumnoFormController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AlumnoAddView.fxml"));
		loader.setController(this);
		loader.load();
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		nombreText.textProperty().bindBidirectional(nombre);
		apellidosText.textProperty().bindBidirectional(apellidos);
		fechaPicker.valueProperty().bindBidirectional(fecha);
		sexoCombo.valueProperty().bindBidirectional(sexo);
		repiteCheck.selectedProperty().bindBidirectional(repite);
		
		sexoCombo.getItems().addAll(Sexo.values());
		
		alumno.addListener(this::onAlumnoChanged);
		

	}
	
	
	public void onAlumnoChanged(ObservableValue<? extends Alumno> o, Alumno ov, Alumno nv) {
		
		if (ov != null) {
			
			nombre.unbindBidirectional(ov.nombreProperty());
			apellidos.unbindBidirectional(ov.apellidosProperty());
			fecha.unbindBidirectional(ov.fechaNacimientoProperty());
			sexo.unbindBidirectional(ov.sexoProperty());
			repite.unbindBidirectional(ov.repiteProperty());
		}
		
		if (nv != null) {
			
			nombre.bindBidirectional(nv.nombreProperty());
			apellidos.bindBidirectional(nv.apellidosProperty());
			fecha.bindBidirectional(nv.fechaNacimientoProperty());
			sexo.bindBidirectional(nv.sexoProperty());
			repite.bindBidirectional(nv.repiteProperty());
		}
		
	}

	public final ObjectProperty<Alumno> alumnoProperty() {
		return this.alumno;
	}
	

	public final Alumno getAlumno() {
		return this.alumnoProperty().get();
	}
	

	public final void setAlumno(final Alumno alumno) {
		this.alumnoProperty().set(alumno);
	}
	
	

}
