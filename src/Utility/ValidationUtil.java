package Utility;

import Controller.EmployeeController;
import Model.ValidationFields;


import static Constants.UiConstants.ERROR_COLOR;

public class ValidationUtil {


    public boolean checkInputHelper(ValidationFields validationFields, EmployeeController controller, long currentByte) {
        boolean valid = true;
        // if any of inputs are in wrong format, colour text field and display
        // message
        if (validationFields.getPpsField().isEditable() && validationFields.getPpsField().getText().trim().isEmpty()) {
            validationFields.getPpsField().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        if (validationFields.getPpsField().isEditable() && controller.correctPps(validationFields.getPpsField().getText().trim(), currentByte)) {
            validationFields.getPpsField().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        if (validationFields.getSurnameField().isEditable() && validationFields.getSurnameField().getText().trim().isEmpty()) {
            validationFields.getSurnameField().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        if (validationFields.getFirstNameField().isEditable() && validationFields.getFirstNameField().getText().trim().isEmpty()) {
            validationFields.getFirstNameField().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        if (validationFields.getGenderCombo().getSelectedIndex() == 0 && validationFields.getGenderCombo().isEnabled()) {
            validationFields.getGenderCombo().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        if (validationFields.getDepartmentCombo().getSelectedIndex() == 0 && validationFields.getDepartmentCombo().isEnabled()) {
            validationFields.getDepartmentCombo().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        try {// try to get values from text field
            Double.parseDouble(validationFields.getSalaryField().getText());
            // check if salary is greater than 0
            if (Double.parseDouble(validationFields.getSalaryField().getText()) < 0) {
                validationFields.getSalaryField().setBackground(ERROR_COLOR);
                valid = false;
            } // end if
        } // end try
        catch (NumberFormatException num) {
            if (validationFields.getSalaryField().isEditable()) {
                validationFields.getSalaryField().setBackground(ERROR_COLOR);
                valid = false;
            } // end if
        } // end catch
        if (validationFields.getFullTimeCombo().getSelectedIndex() == 0 && validationFields.getFullTimeCombo().isEnabled()) {
            validationFields.getFullTimeCombo().setBackground(ERROR_COLOR);
            valid = false;
        } // end if
        return valid;
    }
}
