package View;/*
 * 
 * This is a dialog for searching Employees by their surname.
 * 
 * */

import Controller.EmployeeController;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import static Constants.UiConstants.STANDARD_TEXT_FIELD_LIMIT;

public class SearchBySurnameDialog extends JDialog {
	EmployeeDetails parent;
	EmployeeController controller;
	JButton search, cancel;
	public JTextField searchField;
	// constructor for search by surname dialog
	public SearchBySurnameDialog(EmployeeDetails parent, EmployeeController controller) {
		setTitle("Search by Surname");
		setModal(true);
		this.parent = parent;
		this.controller = controller;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(searchPane());
		setContentPane(scrollPane);

		getRootPane().setDefaultButton(search);
		
		setSize(500, 190);
		setLocation(350, 250);
		setVisible(true);
	}// end View.SearchBySurnameDialog
	
	// initialize search container
	public Container searchPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3,1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;

		searchPanel.add(new JLabel("Search by Surname"));
	
		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter Surname:"));
		searchLabel.setFont(this.parent.font1);
		textPanel.add(searchField = new JTextField(STANDARD_TEXT_FIELD_LIMIT));
		searchField.setFont(this.parent.font1);
		searchField.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));

		buttonPanel.add(search = new JButton("Search"));
		search.addActionListener(e -> controller.searchEmployeeBySurnameController(this));
		search.requestFocus();
		
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(e -> dispose());
		
		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);

		return searchPanel;
	}// end searchPane

}// end class View.SearchBySurnameDialog
