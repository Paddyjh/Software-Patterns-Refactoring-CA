package Controller;

import Model.Employee;
import Model.RandomFile;
import View.AddRecordDialog;
import View.EmployeeDetails;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class EmployeeController {

    private long currentByteStart = 0;
    private RandomFile application = new RandomFile();
    private File file;
    private boolean change = false;
    // holds true or false if any changes are made for file content
    boolean changesMade = false;
    String generatedFileName;
    // holds current Model.Employee object
    Employee currentEmployee;

    private EmployeeDetails employeeDetailsView;
    // display files in File Chooser only with extension .dat
    private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");

    public EmployeeController(EmployeeDetails employeeDetailsView){
        this.employeeDetailsView = employeeDetailsView;
        createRandomFile();// create random file name
    }

    public void setChange(boolean change){
        this.change = change;
    }


    private void createRandomFile() {
        generatedFileName = getFileName() + ".dat";
        // assign generated file name to file
        file = new File(generatedFileName);
        // create file
        application.createFile(file.getName());
    }// end createRandomFile

    private String getFileName() {
        String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
        StringBuilder fileName = new StringBuilder();
        Random rnd = new Random();
        // loop until 20 character long file name is generated
        while (fileName.length() < 20) {
            int index = (int) (rnd.nextFloat() * fileNameChars.length());
            fileName.append(fileNameChars.charAt(index));
        }
        String generatedfileName = fileName.toString();
        return generatedfileName;
    }// end getFileName

    //don't forget about updating changes made
    public void saveEmployeeRecord(AddRecordDialog view) {
        // Validate inputs from the view
        if (view.checkInput()) {
            // Extract data from view and create an Employee object
            // Call model methods to save the Employee object
            addEmployeeRecord(view);
            view.dispose();
            changesMade = true;
        } else {
            // Notify view to display error message or highlight fields
        }

    }

    public void addEmployeeRecord(AddRecordDialog view) {
        Employee theEmployee = view.returnEmployee();
        this.currentEmployee = theEmployee;
//        this.parent.addRecord(theEmployee);

        application.openWriteFile(file.getAbsolutePath());
        // write into a file
        currentByteStart = application.addRecords(theEmployee);
        application.closeWriteFile();// close file for writing

        this.employeeDetailsView.displayRecords(theEmployee);

    }

    public int getNextFreeId() {
        int nextFreeId = 0;
        // if file is empty or all records are empty start with ID 1 else look
        // for last active record
        if (file.length() == 0 || !isSomeoneToDisplay())
            nextFreeId++;
        else {
            lastRecord();// look for last active record
            // add 1 to last active records ID to get next ID
            nextFreeId = currentEmployee.getEmployeeId() + 1;
        }
        return nextFreeId;
    }// end getNextFreeId

    private void firstRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            // get byte start in file for first record
            currentByteStart = application.getFirst();
            // assign current Model.Employee to first record in file
            currentEmployee = application.readRecords(currentByteStart);
            application.closeReadFile();// close file for reading
            // if first record is inactive look for next record
            if (currentEmployee.getEmployeeId() == 0)
                nextRecord();// look for next record
        } // end if
    }// end firstRecord

    // find byte start in file for previous active record
    //Potentially move elsewhere or adapt
    private void previousRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            // get byte start in file for previous record
            currentByteStart = application.getPrevious(currentByteStart);
            // assign current Model.Employee to previous record in file
            currentEmployee = application.readRecords(currentByteStart);
            // loop to previous record until Model.Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                // get byte start in file for previous record
                currentByteStart = application.getPrevious(currentByteStart);
                // assign current Model.Employee to previous record in file
                currentEmployee = application.readRecords(currentByteStart);
            } // end while
            application.closeReadFile();// close file for reading
        }
    }// end previousRecord

    // find byte start in file for next active record
    //Potentially move elsewhere or adapt
    private void nextRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            // get byte start in file for next record
            currentByteStart = application.getNext(currentByteStart);
            // assign current Model.Employee to record in file
            currentEmployee = application.readRecords(currentByteStart);
            // loop to previous next until Model.Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                // get byte start in file for next record
                currentByteStart = application.getNext(currentByteStart);
                // assign current Model.Employee to next record in file
                currentEmployee = application.readRecords(currentByteStart);
            } // end while
            application.closeReadFile();// close file for reading
        } // end if
    }// end nextRecord

    // find byte start in file for last active record
    //Potentially move elsewhere or adapt
    private void lastRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            // get byte start in file for last record
            currentByteStart = application.getLast();
            // assign current Model.Employee to first record in file
            currentEmployee = application.readRecords(currentByteStart);
            application.closeReadFile();// close file for reading
            // if last record is inactive look for previous record
            if (currentEmployee.getEmployeeId() == 0)
                previousRecord();// look for previous record
        } // end if
    }// end lastRecord

    private boolean isSomeoneToDisplay() {
        boolean someoneToDisplay = false;
        // open file for reading
        application.openReadFile(file.getAbsolutePath());
        // check if any of records in file is active - ID is not 0
        someoneToDisplay = application.isSomeoneToDisplay();
        application.closeReadFile();// close file for reading
        // if no records found clear all text fields and display message
        if (!someoneToDisplay) {
            currentEmployee = null;
            //reset text fields
            employeeDetailsView.resetFields();
        }
        return someoneToDisplay;
    }// end isSomeoneToDisplay

    public boolean correctPps(String pps, long currentByte) {
        System.out.println("Current Byte : " + currentByte);
        System.out.println("Current Byte Start : " + currentByteStart);

        boolean ppsExist = false;
        // check for correct PPS format based on assignment description
        if (pps.length() == 8 || pps.length() == 9) {
            if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
                    && Character.isDigit(pps.charAt(2)) && Character.isDigit(pps.charAt(3))
                    && Character.isDigit(pps.charAt(4)) && Character.isDigit(pps.charAt(5))
                    && Character.isDigit(pps.charAt(6)) && Character.isLetter(pps.charAt(7))
                    && (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
                // open file for reading
                application.openReadFile(file.getAbsolutePath());
                // look in file is PPS already in use
                if(currentByte == -2) {
                    ppsExist = application.isPpsExist(pps, currentByteStart);
                } else {
                    ppsExist = application.isPpsExist(pps, currentByte);
                }
                application.closeReadFile();// close file for reading
            } // end if
            else
                ppsExist = true;
        } // end if
        else
            ppsExist = true;

        return ppsExist;
    }// end correctPPS

public void testing(){
        System.out.println("This is a test");
}

    public void openFile() {
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open");
        // display files in File Chooser only with extension .dat
        fc.setFileFilter(datfilter);
        File newFile; // holds opened file name and path
        // if old file is not empty or changes has been made, offer user to save
        // old file
        if (file.length() != 0 || change) {
            int returnVal = JOptionPane.showOptionDialog(employeeDetailsView, "Do you want to save changes?", "Save",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if user wants to save file, save it
            if (returnVal == JOptionPane.YES_OPTION) {
                saveFile();// save file
            } // end if
        } // end if

        int returnVal = fc.showOpenDialog(employeeDetailsView);
        // if file been chosen, open it
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            // if old file wasn't saved and its name is generated file name,
            // delete this file
            if (file.getName().equals(generatedFileName))
                file.delete();// delete file
            file = newFile;// assign opened file to file
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            firstRecord();// look for first record
            employeeDetailsView.displayRecords(currentEmployee);
            application.closeReadFile();// close file for reading
        } // end if
    }// end openFile

    public void saveFileController(){
        saveFile();
        change = false;
    }
    public void saveFileAsController(){
        saveFileAs();
        change = false;
    }

    public void saveFile() {
        // if file name is generated file name, save file as 'save as' else save
        // changes to file
        if (file.getName().equals(generatedFileName))
            saveFileAs();// save file as 'save as'
        else {
            // if changes has been made to text field offer user to save these
            // changes
            if (change) {
                int returnVal = JOptionPane.showOptionDialog(employeeDetailsView, "Do you want to save changes?", "Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                // save changes if user choose this option
                if (returnVal == JOptionPane.YES_OPTION) {
                    // save changes if ID field is not empty
                    if (!employeeDetailsView.getIdField().getText().equals("")) {
                        // open file for writing
                        application.openWriteFile(file.getAbsolutePath());
                        // get changes for current Model.Employee
                        currentEmployee = employeeDetailsView.getChangedDetails(); //May need to modify this method
                        // write changes to file for corresponding Model.Employee
                        // record
                        application.changeRecords(currentEmployee, currentByteStart);
                        application.closeWriteFile();// close file for writing
                    } // end if
                } // end if
            } // end if

            employeeDetailsView.displayRecords(currentEmployee);
            employeeDetailsView.setEnabled(false);
        } // end else
    }// end saveFile

    private void saveChanges() {
        int returnVal = JOptionPane.showOptionDialog(employeeDetailsView, "Do you want to save changes to current Model.Employee?", "Save",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        // if user choose to save changes, save changes
        if (returnVal == JOptionPane.YES_OPTION) {
            // open file for writing
            application.openWriteFile(file.getAbsolutePath());
            // get changes for current Model.Employee
            currentEmployee = employeeDetailsView.getChangedDetails(); //Again may need to modify this method
            // write changes to file for corresponding Model.Employee record
            application.changeRecords(currentEmployee, currentByteStart);
            application.closeWriteFile();// close file for writing
            changesMade = false;// state that all changes has bee saved
        } // end if
        employeeDetailsView.displayRecords(currentEmployee);
        employeeDetailsView.setEnabled(false);
    }// end saveChanges

    // save file as 'save as'
    //Potentially move elsewhere or adapt
    public void saveFileAs() {
        final JFileChooser fc = new JFileChooser();
        File newFile;
        String defaultFileName = "new_Employee.dat";
        fc.setDialogTitle("Save As");
        // display files only with .dat extension
        fc.setFileFilter(datfilter);
        fc.setApproveButtonText("Save");
        fc.setSelectedFile(new File(defaultFileName));

        int returnVal = fc.showSaveDialog(employeeDetailsView);
        // if file has chosen or written, save old file in new file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            // check for file name
            if (!checkFileName(newFile)) {
                // add .dat extension if it was not there
                newFile = new File(newFile.getAbsolutePath() + ".dat");
                // create new file
                application.createFile(newFile.getAbsolutePath());
            } // end id
            else
                // create new file
                application.createFile(newFile.getAbsolutePath());

            try {// try to copy old file to new file
                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // if old file name was generated file name, delete it
                if (file.getName().equals(generatedFileName))
                    file.delete();// delete file
                file = newFile;// assign new file to file
            } // end try
            catch (IOException e) {
            } // end catch
        } // end if
        changesMade = false;
    }// end saveFileAs

    private boolean checkFileName(File fileName) {
        boolean checkFile = false;
        int length = fileName.toString().length();

        // check if last characters in file name is .dat
        if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
                && fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
            checkFile = true;
        return checkFile;
    }// end checkFileName

    public void getFirstRecord(){
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            firstRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getPreviousRecord(){
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            previousRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getNextRecord(){
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            nextRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void getLastRecord(){
        if (employeeDetailsView.checkInput() && !checkForChanges()) {
            lastRecord();
            employeeDetailsView.displayRecords(currentEmployee);
        }
    }

    public void deleteRecord() {
        if (isSomeoneToDisplay()) {// if any active record in file display
            // message and delete record
            int returnVal = JOptionPane.showOptionDialog(employeeDetailsView, "Do you want to delete record?", "Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if answer yes delete (make inactive - empty) record
            if (returnVal == JOptionPane.YES_OPTION) {
                // open file for writing
                application.openWriteFile(file.getAbsolutePath());
                // delete (make inactive - empty) record in file proper position
                application.deleteRecords(currentByteStart);
                application.closeWriteFile();// close file for writing
                // if any active record in file display next record
                if (isSomeoneToDisplay()) {
                    nextRecord();// look for next record
                    employeeDetailsView.displayRecords(currentEmployee);
                } // end if
            } // end if
        } // end if
    }// end deleteDecord

    public void editDetails() {
        // activate field for editing if there is records to display
        if (isSomeoneToDisplay()) {
            // remove euro sign from salary text field
            employeeDetailsView.getSalaryField().setText(employeeDetailsView.getFieldFormat().format(currentEmployee.getSalary()));
            change = false;
            employeeDetailsView.setEnabled(true);// enable text fields for editing
        } // end if
    }// end editDetails

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

    public void saveEmployeeEdits(){
        employeeDetailsView.checkInput();
        checkForChanges();
    }


}
