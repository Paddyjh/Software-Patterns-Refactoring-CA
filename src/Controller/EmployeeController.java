package Controller;

import Model.Employee;
import Model.RandomFile;
import View.AddRecordDialog;
import View.EmployeeDetails;

import javax.swing.*;
import java.io.File;
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

    public EmployeeController(EmployeeDetails employeeDetailsView){
        this.employeeDetailsView = employeeDetailsView;
        createRandomFile();// create random file name
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
                ppsExist = application.isPpsExist(pps, currentByte);
                application.closeReadFile();// close file for reading
            } // end if
            else
                ppsExist = true;
        } // end if
        else
            ppsExist = true;

        return ppsExist;
    }// end correctPPS



}
