package gesaula;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.gesaula.ui.model.Alumno;
import dad.gesaula.ui.model.Grupo;
import dad.gesaula.ui.model.Pesos;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {

	// Controller

	private AlumnoFormController alumnoControler = new AlumnoFormController();

	// modelo grupos

	private StringProperty denominacion = new SimpleStringProperty();
	private ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<>();
	private ObjectProperty<LocalDate> fechaFin = new SimpleObjectProperty<>();
	private DoubleProperty examenes = new SimpleDoubleProperty();
	private DoubleProperty practicas = new SimpleDoubleProperty();
	private DoubleProperty actitud = new SimpleDoubleProperty();

	// modelo alumnos
	private ListProperty<Alumno> listaAlumnos = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<Alumno> seleccionado = new SimpleObjectProperty<>();

	private ObjectProperty<Grupo> grupo = new SimpleObjectProperty<Grupo>();

	// modelo ficheros

	private StringProperty fichero = new SimpleStringProperty();

	// vistas

	@FXML
	private Tab AlumnosTab;

	@FXML
	private Label actLabel;

	@FXML
	private Slider actSlide;

	@FXML
	private BorderPane alumnoPane;

	@FXML
	private Label alumnoVacioLabel;

	@FXML
	private TextField denominText;

	@FXML
	private Button eliminaralumnoButton;

	@FXML
	private Label examenLabel;

	@FXML
	private Slider examenSlide;

	@FXML
	private DatePicker finDate;

	@FXML
	private Tab grupoTab;

	@FXML
	private Button guardarButton;

	@FXML
	private DatePicker inicioDate;

	@FXML
	private TextField ficheroText;

	@FXML
	private Button nuevoAlumnoButton;

	@FXML
	private Button nuevoButton;

	@FXML
	private Label pracLabel;

	@FXML
	private Slider practSlide;

	@FXML
	private TableView<Alumno> tabla;

	@FXML
	private TableColumn<Alumno, LocalDate> fechaColumn;

	@FXML
	private TableColumn<Alumno, String> nombreColumn;

	@FXML
	private TableColumn<Alumno, String> apellidosColumn;

	@FXML
	private BorderPane root;

	public BorderPane getView() {
		return root;
	}

	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// BINDINGS PARA GRUPO
		denominText.textProperty().bindBidirectional(denominacion);
		inicioDate.valueProperty().bindBidirectional(fechaInicio);
		finDate.valueProperty().bindBidirectional(fechaFin);
		examenSlide.valueProperty().bindBidirectional(examenes);
		practSlide.valueProperty().bindBidirectional(practicas);
		actSlide.valueProperty().bindBidirectional(actitud);

		examenLabel.textProperty().bind(examenes.asString("%.0f").concat("%"));
		pracLabel.textProperty().bind(practicas.asString("%.0f").concat("%"));
		actLabel.textProperty().bind(actitud.asString("%.0f").concat("%"));

		grupo.addListener(this::onGrupoChanged);

		// --------------------------

		// BINDINGS PARA ALUMNO
		tabla.itemsProperty().bind(listaAlumnos);

		nombreColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
		apellidosColumn.setCellValueFactory(v -> v.getValue().apellidosProperty());
		fechaColumn.setCellValueFactory(v -> v.getValue().fechaNacimientoProperty());

		// BINDEO A ALUMNO DEL FORMCONTROLLER
		alumnoControler.alumnoProperty().bind(seleccionado);

		// BINDING ITEM SELECCIONADO TABLA
		seleccionado.bind(tabla.getSelectionModel().selectedItemProperty());

		seleccionado.addListener(this::onSeleccionadoChanged);

		// ---------------------------

		// BINDING FICHERO

		fichero.bind(ficheroText.textProperty());

		// ----------------------------

		grupo.set(new Grupo());

	}

	private void onGrupoChanged(ObservableValue<? extends Grupo> o, Grupo ov, Grupo nv) {

		if (ov != null) {

			listaAlumnos.unbind();

			denominacion.unbindBidirectional(ov.denominacionProperty());
			fechaInicio.unbindBidirectional(ov.iniCursoProperty());
			fechaFin.unbindBidirectional(ov.finCursoProperty());
			examenes.unbindBidirectional(ov.getPesos().examenesProperty());
			practicas.unbindBidirectional(ov.getPesos().practicasProperty());
			actitud.unbindBidirectional(ov.getPesos().actitudProperty());

		}

		if (nv != null) {

			listaAlumnos.bind(nv.alumnosProperty());

			denominacion.bindBidirectional(nv.denominacionProperty());
			fechaInicio.bindBidirectional(nv.iniCursoProperty());
			fechaFin.bindBidirectional(nv.finCursoProperty());
			examenes.bindBidirectional(nv.getPesos().examenesProperty());
			practicas.bindBidirectional(nv.getPesos().practicasProperty());
			actitud.bindBidirectional(nv.getPesos().actitudProperty());

		}

	}

	private void onSeleccionadoChanged(ObservableValue<? extends Alumno> o, Alumno ov, Alumno nv) {

		if (nv != null) {

			alumnoPane.setCenter(alumnoControler.getView());
		}

		else {

			alumnoPane.setCenter(alumnoVacioLabel);
		}

	}

	@FXML
	void onEliminarAlumnoButton(ActionEvent event) {

		Alumno seleccionado = tabla.getSelectionModel().getSelectedItem();

		if (seleccionado == null) {

			Alert alertaError = new Alert(AlertType.ERROR);
			alertaError.setTitle("ERROR");
			alertaError.setHeaderText("No hay ningún elemento seleccionado.");
			alertaError.showAndWait();

		}

		else {

			Alert alertaConfirm = new Alert(AlertType.CONFIRMATION);
			alertaConfirm.setTitle("Eliminar Alumno");
			alertaConfirm.setHeaderText(
					"Se va a eliminar el alumno: " + seleccionado.getNombre() + " " + seleccionado.getApellidos());
			Optional<ButtonType> opcion = alertaConfirm.showAndWait();

			if (opcion.get().equals(ButtonType.OK)) {

				listaAlumnos.remove(seleccionado);
			}

		}
	}

	@FXML
	void onGuardarFicheroButton(ActionEvent event) {

		String ruta = fichero.get();
		File file = new File(ruta);
		
		if (ruta != null) {
			
		try {
			
			grupo.get().save(file);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(App.primaryStage);
			alert.setTitle("Guardar grupo");
			alert.setHeaderText("Se ha guardado el grupo correctamente.");
			alert.setContentText("El grupo " + grupo.get().getDenominacion() + " se ha guardado en el fichero '" + ruta + "'.");
			alert.showAndWait();
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(App.primaryStage);
			alert.setTitle("Guardar grupo");
			alert.setHeaderText("Error al guardar el grupo.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			e.printStackTrace();

		}
		}
		else {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(App.primaryStage);
			alert.setTitle("Guardar grupo");
			alert.setHeaderText("Error al guardar el grupo.");
			alert.setContentText("Debe especificar la ruta del fichero donde se guardar� el grupo.");
			alert.showAndWait();
		}

	}

	@FXML
	void onNuevoAlumnoButton(ActionEvent event) {

		Alumno alumno = new Alumno();
		alumno.setNombre("Sin Nombre");
		alumno.setApellidos("Sin apellidos");

		listaAlumnos.add(alumno);

	}

	@FXML
	void onNuevoFicheroButton(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(App.primaryStage);
		alert.setTitle("Nuevo grupo");
		alert.setHeaderText("Va a crear un grupo nuevo.");
		alert.setContentText("¿Está seguro?");
		Optional<ButtonType> opcion = alert.showAndWait();
		if (opcion.get().equals(ButtonType.OK)) {

			grupo.set(new Grupo());

		}

	}
}
