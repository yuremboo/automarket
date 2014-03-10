package com.automarket.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Validator {
	public static boolean textFieldNotEmpty(TextField field) {
		boolean notEmpty = false;
		if (field.getText() != null && !field.getText().isEmpty()) {
			notEmpty = true;
		} 
		return notEmpty;
	}
	
	public static boolean textFieldNotEmpty(TextField field, Label label, String message) {
		boolean notEmpty = true;
		if (!textFieldNotEmpty(field)) {
			notEmpty = false;
			label.setText(message);
		}
		return notEmpty;
	}
}
