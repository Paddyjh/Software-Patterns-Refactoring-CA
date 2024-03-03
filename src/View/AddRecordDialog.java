package View;/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Controller.EmployeeController;
import Model.Employee;
import Model.ValidationFields;
import Utility.ValidationUtil;
import net.miginfocom.swing.MigLayout;

import static Constants.UiConstants.*;

public class AddRecordDialog extends JDialog {
	//Controller to handle business logic and data handling
	EmployeeController controller;

	JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	JButton save, cancel;
	EmployeeDetails parent;

	// constructor for add record dialog
	public AddRecordDialog(EmployeeDetails parent, EmployeeController controller) {
		setTitle("Add Record");
		setModal(true);
		this.controller = controller;
		this.parent = parent;
		this.parent.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(dialogPane());
		setContentPane(scrollPane);

		getRootPane().setDefaultButton(save);

		setSize(500, 370);
		setLocation(350, 250);
		setVisible(true);
	}// end View.AddRecordDialog

	// initialize dialog container
	public Container dialogPane() {
		JPanel empDetails, buttonPanel;
		empDetails = new JPanel(new MigLayout());
		buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), "growx, pushx");
		empDetails.add(idField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "growx, pushx, wrap");
		idField.setEditable(false);


		empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
		empDetails.add(ppsField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "growx, pushx, wrap");

		empDetails.add(new JLabel("Surname:"), "growx, pushx");
		empDetails.add(surnameField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "growx, pushx, wrap");

		empDetails.add(new JLabel("First Name:"), "growx, pushx");
		empDetails.add(firstNameField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "growx, pushx, wrap");

		empDetails.add(new JLabel("Gender:"), "growx, pushx");
		empDetails.add(genderCombo = new JComboBox<String>(this.parent.gender), "growx, pushx, wrap");

		empDetails.add(new JLabel("Department:"), "growx, pushx");
		empDetails.add(departmentCombo = new JComboBox<String>(this.parent.department), "growx, pushx, wrap");

		empDetails.add(new JLabel("Salary:"), "growx, pushx");
		empDetails.add(salaryField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "growx, pushx, wrap");

		empDetails.add(new JLabel("Full Time:"), "growx, pushx");
		empDetails.add(fullTimeCombo = new JComboBox<String>(this.parent.fullTime), "growx, pushx, wrap");

		buttonPanel.add(save = new JButton("Save"));
		save.addActionListener(e -> controller.saveEmployeeRecord(this));
		save.requestFocus();
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(e -> dispose());

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");
		// loop through all panel components and add fonts and listeners
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(this.parent.font1);
			if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(DEFAULT_BACKGROUND_COLOR);
			}// end if
			else if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(PPS_FIELD_LIMIT));
				else
					field.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));
			}// end else if
		}// end for
		idField.setText(Integer.toString(controller.getNextFreeId()));
		return empDetails;
	}

	public Employee returnEmployee() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(), surnameField.getText().toUpperCase(),
				firstNameField.getText().toUpperCase(), genderCombo.getSelectedItem().toString().charAt(0),
				departmentCombo.getSelectedItem().toString(), Double.parseDouble(salaryField.getText()), fullTime);
		return theEmployee;
	}

	// check for input in text fields
	public boolean checkInput() {
		ValidationFields validationFields = new ValidationFields(this.ppsField, this.surnameField, this.firstNameField,
				this.salaryField, this.genderCombo, this.departmentCombo,
				this.fullTimeCombo);

		ValidationUtil validationUtil = new ValidationUtil();
		return validationUtil.checkInputHelper(validationFields, this.controller, -1);
	}// end checkInput

	
}