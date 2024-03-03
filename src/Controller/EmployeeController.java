package Controller;

import Model.Employee;
import View.*;

import javax.swing.*;
import java.util.Vector;

import static Constants.UiConstants.*;

public class EmployeeController {
    // hold object start position in file
    long currentByteStart = 0;
    boolean change = false;
    Employee currentEmployee;
    EmployeeDetails employeeDetailsView;
    FileHandler fileHandler;

    public EmployeeController(EmployeeDetails employeeDetailsView, FileHandler fileHandler) {
        this.employeeDetailsView = employeeDetailsView;
        this.fileHandler = fileHandler;
        fileHandler.createRandomFile();
//        createRandomFile();// create random file name
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public long getCurrentByte() {
        return this.currentByteStart;
    }


    public void saveEmployeeRecord(AddRecordDialog view) {
        // Validate inputs from the view
        if (view.checkInput() && !checkForChanges()) {
            // Extract data from view and create an Employee object
            // Call model methods to save the Employee object
            addEmployeeRecord(view);
            view.dispose();
//            changesMade = true;
            fileHandler.setChangesMade(true);
        } else {
            // Notify view to display error message or highlight fields
        }

    }

    //Add Employee record to File
    public void addEmployeeRecord(AddRecordDialog view) {
        Employee theEmployee = view.returnEmployee();
        this.currentEmployee = theEmployee;

        fileHandler.application.openWriteFile(fileHandler.file.getAbsolutePath());
        // write into a file
        currentByteStart = fileHandler.application.addRecords(theEmployee);
        fileHandler.application.closeWriteFile();// close file for writing

        this.employeeDetailsView.displayRecords(theEmployee);

    }

    public void addRecordController() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) new AddRecordDialog(employeeDetailsView, this);
    }

    // get next free ID from Employees in the file
    public int getNextFreeId() {
        int nextFreeId = 0;
        // if file is empty or all records are empty start with ID 1 else look
        // for last active record
        if (fileHandler.file.length() == 0 || !isSomeoneToDisplay()) nextFreeId++;
        else {
            lastRecord();// look for last active record
            // add 1 to last active records ID to get next ID
            nextFreeId = currentEmployee.getEmployeeId() + 1;
        }
        return nextFreeId;
    }// end getNextFreeId

    // find byte start in file for first active record
    void firstRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
            // get byte start in file for first record
            currentByteStart = fileHandler.application.getFirst();
            // assign current Model.Employee to first record in file
            currentEmployee = fileHandler.application.readRecords(currentByteStart);
            fileHandler.application.closeReadFile();// close file for reading
            // if first record is inactive look for next record
            if (currentEmployee.getEmployeeId() == 0) nextRecord();// look for next record
        } // end if
    }// end firstRecord

    // find byte start in file for previous active record
    private void previousRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
            // get byte start in file for previous record
            currentByteStart = fileHandler.application.getPrevious(currentByteStart);
            // assign current Model.Employee to previous record in file
            currentEmployee = fileHandler.application.readRecords(currentByteStart);
            // loop to previous record until Model.Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                // get byte start in file for previous record
                currentByteStart = fileHandler.application.getPrevious(currentByteStart);
                // assign current Model.Employee to previous record in file
                currentEmployee = fileHandler.application.readRecords(currentByteStart);
            } // end while
            fileHandler.application.closeReadFile();// close file for reading
        }
    }// end previousRecord

    // find byte start in file for next active record
    private void nextRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
            // get byte start in file for next record
            currentByteStart = fileHandler.application.getNext(currentByteStart);
            // assign current Model.Employee to record in file
            currentEmployee = fileHandler.application.readRecords(currentByteStart);
            // loop to previous next until Model.Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                // get byte start in file for next record
                currentByteStart = fileHandler.application.getNext(currentByteStart);
                // assign current Model.Employee to next record in file
                currentEmployee = fileHandler.application.readRecords(currentByteStart);
            } // end while
            fileHandler.application.closeReadFile();// close file for reading
        } // end if
    }// end nextRecord

    // find byte start in file for last active record
    private void lastRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
            // get byte start in file for last record
            currentByteStart = fileHandler.application.getLast();
            // assign current Model.Employee to first record in file
            currentEmployee = fileHandler.application.readRecords(currentByteStart);
            fileHandler.application.closeReadFile();// close file for reading
            // if last record is inactive look for previous record
            if (currentEmployee.getEmployeeId() == 0) previousRecord();// look for previous record
        } // end if
    }// end lastRecord

    // check if any of records in file is active - ID is not 0
    public boolean isSomeoneToDisplay() {
        boolean someoneToDisplay = false;
        // open file for reading
        fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
        // check if any of records in file is active - ID is not 0
        someoneToDisplay = fileHandler.application.isSomeoneToDisplay();
        fileHandler.application.closeReadFile();// close file for reading
        // if no records found clear all text fields and display message
        if (!someoneToDisplay) {
            currentEmployee = null;
            //reset text fields
            employeeDetailsView.resetFields();
        }
        return someoneToDisplay;
    }// end isSomeoneToDisplay

    // check for correct PPS format and look if PPS already in use
    public boolean correctPps(String pps, long currentByte) {

        boolean ppsExist = false;
        // check for correct PPS format based on assignment description
        if (pps.length() == 8 || pps.length() == PPS_FIELD_LIMIT) {
            if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1)) && Character.isDigit(pps.charAt(2)) && Character.isDigit(pps.charAt(3)) && Character.isDigit(pps.charAt(4)) && Character.isDigit(pps.charAt(5)) && Character.isDigit(pps.charAt(6)) && Character.isLetter(pps.charAt(7)) && (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
                // open file for reading
                fileHandler.application.openReadFile(fileHandler.file.getAbsolutePath());
                // look in file is PPS already in use
                if (currentByte == -2) {
                    ppsExist = fileHandler.application.isPpsExist(pps, currentByteStart);
                } else {
                    ppsExist = fileHandler.application.isPpsExist(pps, currentByte);
                }
                fileHandler.application.closeReadFile();// close file for reading
            } // end if
            else ppsExist = true;
        } // end if
        else ppsExist = true;

        return ppsExist;
    }// end correctPPS

    // open file
    public void openFile() {
        fileHandler.openFile();
    }// end openFile

    public void saveFileController() {
        saveFile();
        change = false;
    }

    public void saveFileAsController() {
        saveFileAs();
        change = false;
    }

    public void saveFile() {
        fileHandler.saveFile();
    }// end saveFile

    // save changes to current Employee
    private void saveChanges() {
        fileHandler.saveChanges();
    }// end saveChanges

    // save file as 'save as'
    //Potentially move elsewhere or adapt
    public void saveFileAs() {
        fileHandler.saveFileAs();
    }// end saveFileAs

    public void getFirstRecord() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            firstRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getPreviousRecord() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            previousRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getNextRecord() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            nextRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getLastRecord() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            lastRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    // delete (make inactive - empty) record from file
    public void deleteRecord() {
        if (isSomeoneToDisplay()) {// if any active record in file display
            // message and delete record
            int returnVal = JOptionPane.showOptionDialog(employeeDetailsView, "Do you want to delete record?", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if answer yes delete (make inactive - empty) record
            if (returnVal == JOptionPane.YES_OPTION) {
                // open file for writing
                fileHandler.application.openWriteFile(fileHandler.file.getAbsolutePath());
                // delete (make inactive - empty) record in file proper position
                fileHandler.application.deleteRecords(currentByteStart);
                fileHandler.application.closeWriteFile();// close file for writing
                // if any active record in file display next record
                if (isSomeoneToDisplay()) {
                    nextRecord();// look for next record
                    employeeDetailsView.displayRecords(currentEmployee);
                } // end if
            } // end if
        } // end if
    }// end deleteDecord

    // activate field for editing
    public void editDetails() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            // activate field for editing if there is records to display
            if (isSomeoneToDisplay()) {
                // remove euro sign from salary text field
                employeeDetailsView.getSalaryField().setText(employeeDetailsView.getFieldFormat().format(currentEmployee.getSalary()));
                change = false;
                employeeDetailsView.setEnabled(true);// enable text fields for editing
            } // end if
        }
    }// end editDetails

    // check if any changes text field where made
    public boolean checkForChanges() {
        boolean anyChanges = false;
        // if changes where made, allow user to save there changes
        if (change) {
            saveChanges();// save changes
            anyChanges = true;
        } // end if
        // if no changes made, set text fields as unenabled and display
        // current Model.Employee
        else {
            employeeDetailsView.setEnabled(false);
            employeeDetailsView.displayRecords(currentEmployee);
        } // end else

        return anyChanges;
    }// end checkForChanges

    public void saveEmployeeEdits() {
        if (employeeDetailsView.checkInput()) checkForChanges();
    }

    public void searchEmployeeBySurnameController(SearchBySurnameDialog view) {
        this.employeeDetailsView.getSearchBySurnameField().setText(view.searchField.getText());
        searchEmployeeBySurname();
        view.dispose();
    }

    public void searchEmployeeByIdController(SearchByIdDialog view) {
        try {
            Double.parseDouble(view.searchField.getText());
            employeeDetailsView.getSearchByIdField().setText(view.searchField.getText());
            searchEmployeeById();
            view.dispose();
        } catch (NumberFormatException num) {
            // display message and set colour to text field if entry is wrong
            view.searchField.setBackground(ERROR_COLOR);
            JOptionPane.showMessageDialog(null, "Wrong ID format!");
        }
    }

    public void searchEmployeeBySurname() {
        boolean found = false;
        // if any active Model.Employee record search for ID else do nothing
        if (isSomeoneToDisplay()) {
            firstRecord();// look for first record
            String firstSurname = currentEmployee.getSurname().trim();
            // if ID to search is already displayed do nothing else loop through
            // records
            if (employeeDetailsView.getSearchBySurnameField().getText().trim().equalsIgnoreCase(employeeDetailsView.getSurnameField().getText().trim()))
                found = true;
            else if (employeeDetailsView.getSearchBySurnameField().getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                found = true;
                employeeDetailsView.displayRecords(currentEmployee);
            } // end else if
            else {
                nextRecord();// look for next record
                // loop until Model.Employee found or until all Employees have been
                // checked
                while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                    // if found break from loop and display Model.Employee details
                    // else look for next record
                    if (employeeDetailsView.getSearchBySurnameField().getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                        found = true;
                        employeeDetailsView.displayRecords(currentEmployee);
                        break;
                    } // end if
                    else nextRecord();// look for next record
                } // end while
            } // end else
            // if Model.Employee not found display message
            if (!found) JOptionPane.showMessageDialog(null, "Employee not found!");
        } // end if
        employeeDetailsView.getSearchBySurnameField().setText("");
    }// end searchEmployeeBySurname

    public void searchEmployeeById() {
        boolean found = false;

        try {// try to read correct correct from input
            // if any active Model.Employee record search for ID else do nothing
            if (isSomeoneToDisplay()) {
                firstRecord();// look for first record
                int firstId = currentEmployee.getEmployeeId();
                // if ID to search is already displayed do nothing else loop
                // through records
                if (employeeDetailsView.getSearchByIdField().getText().trim().equals(employeeDetailsView.getIdField().getText().trim()))
                    found = true;
                else if (employeeDetailsView.getSearchByIdField().getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
                    found = true;
                    employeeDetailsView.displayRecords(currentEmployee);
                } // end else if
                else {
                    nextRecord();// look for next record
                    // loop until Model.Employee found or until all Employees have
                    // been checked
                    while (firstId != currentEmployee.getEmployeeId()) {
                        // if found break from loop and display Model.Employee details
                        // else look for next record
                        if (Integer.parseInt(employeeDetailsView.getSearchByIdField().getText().trim()) == currentEmployee.getEmployeeId()) {
                            found = true;
                            employeeDetailsView.displayRecords(currentEmployee);
                            break;
                        } else nextRecord();// look for next record
                    } // end while
                } // end else
                // if Model.Employee not found display message
                if (!found) JOptionPane.showMessageDialog(null, "Employee not found!");
            } // end if
        } // end try
        catch (NumberFormatException e) {
            employeeDetailsView.getSearchByIdField().setBackground(ERROR_COLOR);
            JOptionPane.showMessageDialog(null, "Wrong ID format!");
        } // end catch
        employeeDetailsView.getSearchByIdField().setBackground(DEFAULT_BACKGROUND_COLOR);
        employeeDetailsView.getSearchByIdField().setText("");
    }// end searchEmployeeByID

    // create vector of vectors with all Model.Employee details
    private Vector<Object> getAllEmloyees() {
        // vector of Model.Employee objects
        Vector<Object> allEmployee = new Vector<Object>();
        Vector<Object> empDetails;// vector of each employee details
        long byteStart = currentByteStart;
        int firstId;

        firstRecord();// look for first record
        firstId = currentEmployee.getEmployeeId();
        // loop until all Employees are added to vector
        do {
            empDetails = new Vector<Object>();
            empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
            empDetails.addElement(currentEmployee.getPps());
            empDetails.addElement(currentEmployee.getSurname());
            empDetails.addElement(currentEmployee.getFirstName());
            empDetails.addElement(new Character(currentEmployee.getGender()));
            empDetails.addElement(currentEmployee.getDepartment());
            empDetails.addElement(new Double(currentEmployee.getSalary()));
            empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

            allEmployee.addElement(empDetails);
            nextRecord();// look for next record
        } while (firstId != currentEmployee.getEmployeeId());// end do - while
        currentByteStart = byteStart;

        return allEmployee;
    }// end getAllEmployees

    public void displayEmployeeSummaryDialog() {
        if (isSomeoneToDisplay()) new EmployeeSummaryDialog(getAllEmloyees());
    }

    public void closeApp() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) exitApp();
    }

    // allow to save changes to file when exiting the application
    public void exitApp() {
        fileHandler.exitApp();
    }// end exitApp

    // ignore changes and set text field unenabled
    public void cancelChange() {
        employeeDetailsView.setEnabled(false);
        employeeDetailsView.displayRecords(currentEmployee);
    }

    public void searchByIdController() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) employeeDetailsView.displaySearchByIdDialog();
    }

    public void searchBySurnameController() {
        if (employeeDetailsView.checkInput() && !checkForChanges()) employeeDetailsView.displaySearchBySurnameDialog();
    }


}
