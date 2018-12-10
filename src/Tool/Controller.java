package Tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import RNS.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	MenuItem aboutProgramMenuItem;
	@FXML
	MenuItem saveMenuItem;
	@FXML
	MenuItem saveAsMenuItem;
	@FXML
	private Label sliderLabel;
	@FXML
	private Slider slider;
	@FXML
	private Spinner<Integer> nSpinner;
	@FXML
	private TextField modSystemTextField;
	@FXML
	private TextField maxValueModSystemTextField;
	@FXML
	private TextField expressionTextField;
	@FXML
	private TextField resultRNSTextField;
	@FXML
	private TextField resultPNSTextField;
	@FXML
	private TextField numberPNSTextField;
	@FXML
	private TextField resultPNSToRNSTextField;
	@FXML
	private TextField numberRNSTextField;
	@FXML
	private TextField resultRNSToPNSTextField;
	@FXML
	private TextArea resultTextArea;
	@FXML
	private Button goButton;

	private ModSystem modSystem;
	private int countOfRuns;

	@FXML
	void initialize() {
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() == 1) {
				sliderLabel.setText("Ручной");
				nSpinner.setDisable(true);
				modSystemTextField.setEditable(true);
				modSystemTextField.clear();
				maxValueModSystemTextField.clear();
			}
			if (newValue.intValue() == 0) {
				sliderLabel.setText("Авто");
				nSpinner.setDisable(false);
				modSystemTextField.setEditable(false);
				modSystemTextField.clear();
				maxValueModSystemTextField.clear();
				generateModSystem(nSpinner.getValue());
			}
		});

		final int minValue = 2;
		final int maxValue = 6;
		nSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			generateModSystem(newValue);
		});
		// Value factory.
		SpinnerValueFactory<Integer> valueFactory =
				new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);
		nSpinner.setValueFactory(valueFactory);
		//Добавляем изменения
		textChange(modSystemTextField); //Подписываем modSystemTextField на слушателя события изменения текста.
		//Старый код
		textChange(expressionTextField);
		textChange(numberPNSTextField);
		textChange(numberRNSTextField);
	}

	private void generateModSystem(int number) {
		modSystem = new ModSystem(number);
		modSystemTextField.setText(modSystem.toString());
		maxValueModSystemTextField.setText(modSystem.getRange());
	}

	private void textChange(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			int length = newValue.length();
			if (length > 0) {
				char last = newValue.charAt(length - 1);
				String punctuations = ",(){}+-*/";
				if (!(Character.isDigit(last) || punctuations.contains(String.valueOf(last)))) {
					textField.setText(newValue.substring(0, length - 1));
				}
			}
		});
	}

	private void addInfoToResults(String what) {
		resultTextArea.appendText(what);
	}

	@FXML
	void goButton_onAction(ActionEvent e) {
		countOfRuns++;
		addInfoToResults("##### Запуск №" + countOfRuns + " #####\n");
		addInfoToResults("Система модулей: " + modSystemTextField.getText() + "\n");
		try {
			if (sliderLabel.getText().equals("Ручной")) {
				modSystem = new ModSystem(modSystemTextField.getText());
				maxValueModSystemTextField.setText(modSystem.getRange());
			}
			if (!expressionTextField.getText().isEmpty()) {
				Unit unit = PostfixNotation.resultOfExpression(expressionTextField.getText(), modSystem);
				resultRNSTextField.setText(unit.toString());
				resultPNSTextField.setText(Integer.toString(Convert.toPositionalSystem(unit)));
				addInfoToResults("Результат выражения: " + expressionTextField.getText() +
						"\n\t- в системе остаточных классов равен: " + resultRNSTextField.getText() +
						"\n\t- в позиционной системе счисления равен: " + resultPNSTextField.getText() + "\n");
			}
			if (!numberPNSTextField.getText().isEmpty()) {
				resultPNSToRNSTextField.setText(Convert.toRNS(Integer.valueOf(numberPNSTextField.getText()), modSystem).toString());
				addInfoToResults("Число в позиционной системе счисления: " + numberPNSTextField.getText() +
						"\n\tравно числу в системе остаточных классов: " + resultPNSToRNSTextField.getText() + "\n");
			}
			if (!numberRNSTextField.getText().isEmpty()) {
				Unit unit = new Unit(numberRNSTextField.getText(), modSystem);
				resultRNSToPNSTextField.setText(Integer.toString(Convert.toPositionalSystem(unit)));
				addInfoToResults("Число в системе остаточных классов: " + numberRNSTextField.getText() +
						"\n\tравно числу в позиционной системе счисления: " + resultRNSToPNSTextField.getText() + "\n");
			}
			addInfoToResults("\n");
		} catch (Exception ex) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void aboutProgramMenuItem_onAction(ActionEvent e) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("О программе");
		alert.setHeaderText("RNS Tool");
		alert.setContentText("Программа для работы с числами в системе остаточных классов.\n\n"
				+ "Версия: 1.0\n2018 год\n\nРазработал: Ковязин Александр,\nгруппа ИВТм-1302, кафедра ЭВМ, ВятГУ");
		alert.showAndWait();
	}

	@FXML
	void saveAsMenuItem_onAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName("Результаты");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Текстовые документы (.txt)", "*.txt"),
				new FileChooser.ExtensionFilter("Все файлы", "*.*"));
		File saveFile = fileChooser.showSaveDialog(new Stage());
		if (saveFile != null) {
			try {
				FileWriter fileWriter = new FileWriter(saveFile);
				fileWriter.write(resultTextArea.getText().replaceAll("\n", "\r\n"));
				fileWriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}