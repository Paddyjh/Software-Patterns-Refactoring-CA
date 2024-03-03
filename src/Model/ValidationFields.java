package Model;

import javax.swing.*;

public class ValidationFields {

    JTextField ppsField, surnameField, firstNameField, salaryField;
    JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;

    public ValidationFields(JTextField ppsField, JTextField surnameField, JTextField firstNameField,
                            JTextField salaryField, JComboBox<String> genderCombo, JComboBox<String> departmentCombo,
                            JComboBox<String> fullTimeCombo) {
        this.ppsField = ppsField;
        this.surnameField = surnameField;
        this.firstNameField = firstNameField;
        this.salaryField = salaryField;
        this.genderCombo = genderCombo;
        this.departmentCombo = departmentCombo;
        this.fullTimeCombo = fullTimeCombo;
    }

    public JTextField getPpsField() {
        return ppsField;
    }

    public void setPpsField(JTextField ppsField) {
        this.ppsField = ppsField;
    }

    public JTextField getSurnameField() {
        return surnameField;
    }

    public void setSurnameField(JTextField surnameField) {
        this.surnameField = surnameField;
    }

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public void setFirstNameField(JTextField firstNameField) {
        this.firstNameField = firstNameField;
    }

    public JTextField getSalaryField() {
        return salaryField;
    }

    public void setSalaryField(JTextField salaryField) {
        this.salaryField = salaryField;
    }

    public JComboBox<String> getGenderCombo() {
        return genderCombo;
    }

    public void setGenderCombo(JComboBox<String> genderCombo) {
        this.genderCombo = genderCombo;
    }

    public JComboBox<String> getDepartmentCombo() {
        return departmentCombo;
    }

    public void setDepartmentCombo(JComboBox<String> departmentCombo) {
        this.departmentCombo = departmentCombo;
    }

    public JComboBox<String> getFullTimeCombo() {
        return fullTimeCombo;
    }

    public void setFullTimeCombo(JComboBox<String> fullTimeCombo) {
        this.fullTimeCombo = fullTimeCombo;
    }


}
