package View;/*
 * 
 * This is the dialog for Model.Employee search by ID
 * 
 * */

import Controller.EmployeeController;

import java.awt.Container;
import java.awt.GridLayout;

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

public class SearchByIdDialog extends JDialog {
	EmployeeDetails parent;
	EmployeeController controller;
	JButton search, cancel;
	public JTextField searchField;
	// constructor for View.SearchByIdDialog
	public SearchByIdDialog(EmployeeDetails parent, EmployeeController controller) {
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
	}// end View.SearchByIdDialog
	
	// initialize search container
	public Container searchPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3, 1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;

		searchPanel.add(new JLabel("Search by ID"));

		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter ID:"));
		searchLabel.setFont(this.parent.font1);
		textPanel.add(searchField = new JTextField(STANDARD_TEXT_FIELD_LIMIT));
		searchField.setFont(this.parent.font1);
		searchField.setDocument(new JTextFieldLimit(STANDARD_TEXT_FIELD_LIMIT));
		
		buttonPanel.add(search = new JButton("Search"));
		search.addActionListener(e -> controller.searchEmployeeByIdController(this));
		search.requestFocus();
		
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(e -> dispose());

		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);

		return searchPanel;
	}// end searchPane

}// end class searchByIdDialog
