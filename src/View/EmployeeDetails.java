package View;
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */


import Controller.EmployeeController;
import Model.Employee;
import Model.ValidationFields;
import Utility.ValidationUtil;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import static Constants.UiConstants.*;


public class EmployeeDetails extends JFrame implements ItemListener, DocumentListener, WindowListener {
	//Controller class that will handle business logic and delegate data handling to the model
	private EmployeeController controller;

	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField;
	private JTextField ppsField;
	private JTextField surnameField;
	private JTextField firstNameField;
	private JTextField salaryField;
//	private static EmployeeDetails frame = new EmployeeDetails();
	// font for labels, text fields and combo boxes
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	private JTextField searchByIdField;
	private JTextField searchBySurnameField;
	// gender combo box values
	String[] gender = {"", "M", "F"};
	// department combo box values
	String[] department = {"", "Administration", "Production", "Transport", "Management"};
	// full time combo box values
	String[] fullTime = {"", "Yes", "No"};

	public EmployeeDetails(){
		createAndShowGUI();
	}

	public JTextField getIdField() {
		return idField;
	}

	public JTextField getSalaryField() {
		return salaryField;
	}

	public static DecimalFormat getFieldFormat() {
		return fieldFormat;
	}

	public JTextField getSearchByIdField(){
		return this.searchByIdField;
	}

	public JTextField getSearchBySurnameField(){
		return this.searchBySurnameField;
	}

	public JTextField getSurnameField(){
		return this.surnameField;
	}

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(e -> controller.openFile());
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(e -> controller.saveFileController());
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(e -> controller.saveFileAsController());
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(e -> controller.addRecordController());
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(e -> controller.editDetails());
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(e -> controller.deleteRecord());

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(e -> controller.getFirstRecord());
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(e -> controller.getPreviousRecord());
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(e -> controller.getNextRecord());
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(e -> controller.getLastRecord());
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(e -> controller.searchByIdController());
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(e -> controller.searchBySurnameController());
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(e -> controller.displayEmployeeSummaryDialog());

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(e -> controller.closeApp());
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	//Method for setting the controller
	public void setController(EmployeeController controller) {
		this.controller = controller;
	}

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), LAYOUT_GROW_PUSH);
		searchPanel.add(searchByIdField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(e -> controller.searchEmployeeById());
		searchByIdField.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));
		searchPanel.add(searchId = new JButton("Go"),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(e -> controller.searchEmployeeById());
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), LAYOUT_GROW_PUSH);
		searchPanel.add(searchBySurnameField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(e -> controller.searchEmployeeBySurname());
		searchBySurnameField.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));
		searchPanel.add(
				searchSurname = new JButton("Go"), "width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(e -> controller.searchEmployeeBySurname());
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon(getClass().getResource("/assets/first.png")).getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(e -> controller.getFirstRecord());
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/assets/prev.png")).getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(e -> controller.getNextRecord());
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon(getClass().getResource("/assets/next.png")).getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(e -> controller.getPreviousRecord());
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon(getClass().getResource("/assets/last.png")).getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(e -> controller.getLastRecord());
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), LAYOUT_GROW_PUSH);
		add.addActionListener(e -> controller.addRecordController());
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), LAYOUT_GROW_PUSH);
		edit.addActionListener(e -> controller.editDetails());
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), LAYOUT_GROW_PUSH_WRAP);
		deleteButton.addActionListener(e -> controller.deleteRecord());
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), LAYOUT_GROW_PUSH);
		displayAll.addActionListener(e -> controller.displayEmployeeSummaryDialog());
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel("ID:"), LAYOUT_GROW_PUSH);
		empDetails.add(idField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), LAYOUT_GROW_PUSH_WRAP);
		idField.setEditable(false);

		empDetails.add(new JLabel("PPS Number:"), LAYOUT_GROW_PUSH);
		empDetails.add(ppsField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("Surname:"), LAYOUT_GROW_PUSH);
		empDetails.add(surnameField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("First Name:"), LAYOUT_GROW_PUSH);
		empDetails.add(firstNameField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("Gender:"), LAYOUT_GROW_PUSH);
		empDetails.add(genderCombo = new JComboBox<String>(gender), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("Department:"), LAYOUT_GROW_PUSH);
		empDetails.add(departmentCombo = new JComboBox<String>(department), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("Salary:"), LAYOUT_GROW_PUSH);
		empDetails.add(salaryField = new JTextField(STANDARD_TEXT_FIELD_LIMIT), LAYOUT_GROW_PUSH_WRAP);

		empDetails.add(new JLabel("Full Time:"), LAYOUT_GROW_PUSH);
		empDetails.add(fullTimeCombo = new JComboBox<String>(fullTime), LAYOUT_GROW_PUSH_WRAP);

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(e -> controller.saveEmployeeEdits());
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(e -> controller.cancelChange());
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add listeners and format
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(PPS_FIELD_LIMIT));
				else
					field.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));
				field.getDocument().addDocumentListener(this);
			} // end if
			else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(DEFAULT_BACKGROUND_COLOR);
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<String>) empDetails.getComponent(i)).addItemListener(this);
				((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		return empDetails;
	}// end detailsPanel

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
		int countGender = 0;
		int countDep = 0;
		boolean found = false;

		searchByIdField.setText("");
		searchBySurnameField.setText("");
		// if Model.Employee is null or ID is 0 do nothing else display Model.Employee
		// details
		if (thisEmployee == null) {
		} else if (thisEmployee.getEmployeeId() == 0) {
		} else {
			// find corresponding gender combo box value to current employee
			while (!found && countGender < gender.length - 1) {
				if (Character.toString(thisEmployee.getGender()).equalsIgnoreCase(gender[countGender]))
					found = true;
				else
					countGender++;
			} // end while
			found = false;
			// find corresponding department combo box value to current employee
			while (!found && countDep < department.length - 1) {
				if (thisEmployee.getDepartment().trim().equalsIgnoreCase(department[countDep]))
					found = true;
				else
					countDep++;
			} // end while
			idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
			ppsField.setText(thisEmployee.getPps().trim());
			surnameField.setText(thisEmployee.getSurname().trim());
			firstNameField.setText(thisEmployee.getFirstName());
			genderCombo.setSelectedIndex(countGender);
			departmentCombo.setSelectedIndex(countDep);
			salaryField.setText(format.format(thisEmployee.getSalary()));
			// set corresponding full time combo box value to current employee
			if (thisEmployee.getFullTime() == true)
				fullTimeCombo.setSelectedIndex(1);
			else
				fullTimeCombo.setSelectedIndex(2);
		}
		controller.setChange(false);
	}// end display records


	// display search by ID dialog
	public void displaySearchByIdDialog() {
		if (controller.isSomeoneToDisplay())
			new SearchByIdDialog(EmployeeDetails.this, this.controller);
	}// end displaySearchByIdDialog

	// display search by surname dialog
	public void displaySearchBySurnameDialog() {
		if (controller.isSomeoneToDisplay())
			new SearchBySurnameDialog(EmployeeDetails.this, this.controller);
	}// end displaySearchBySurnameDialog

	// get values from text fields and create Model.Employee object
	//Potentially move elsewhere or adapt
	public Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;

		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				Double.parseDouble(salaryField.getText()), fullTime);

		return theEmployee;
	}// end getChangedDetails



	public void resetFields(){
		idField.setText("");
		ppsField.setText("");
		surnameField.setText("");
		firstNameField.setText("");
		salaryField.setText("");
		genderCombo.setSelectedIndex(0);
		departmentCombo.setSelectedIndex(0);
		fullTimeCombo.setSelectedIndex(0);
		JOptionPane.showMessageDialog(null, "No Employees registered!");
	}


	// check for input in text fields
	public boolean checkInput() {
		ValidationFields validationFields = new ValidationFields(this.ppsField, this.surnameField, this.firstNameField,
				this.salaryField, this.genderCombo, this.departmentCombo,
				this.fullTimeCombo);

		ValidationUtil validationUtil = new ValidationUtil();
		boolean valid = validationUtil.checkInputHelper(validationFields, this.controller, -2);
		// display message if any input or format is wrong
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		// set text field to white colour if text fields are editable
		if (ppsField.isEditable())
			setToWhite();

		return valid;
	}

	// set text field background colour to white
	private void setToWhite() {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	}// end setToWhite

	// enable text fields for editing
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderCombo.setEnabled(booleanValue);
		departmentCombo.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeCombo.setEnabled(booleanValue);
		saveChange.setVisible(booleanValue);
		cancelChange.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchId.setEnabled(search);
		searchSurname.setEnabled(search);
	}// end setEnabled

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
//		createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}// end createContentPane

	// create and show main dialog
	private void createAndShowGUI() {

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.createContentPane();// add content pane to frame
		this.setSize(760, 600);
		this.setLocation(250, 200);
		this.setVisible(true);
	}// end createAndShowGUI

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		controller.setChange(true);
		new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT);
	}

	public void insertUpdate(DocumentEvent d) {
		controller.setChange(true);
		new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT);
	}

	public void removeUpdate(DocumentEvent d) {
		controller.setChange(true);
		new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		controller.setChange(true);
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		controller.exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}// end class View.EmployeeDetails
