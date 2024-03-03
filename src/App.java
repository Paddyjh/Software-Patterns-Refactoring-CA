import Controller.EmployeeController;
import Controller.FileHandler;
import View.EmployeeDetails;

public class App {

    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EmployeeDetails employeeDetailsView = new EmployeeDetails();
                FileHandler fileHandler = new FileHandler();
                EmployeeController controller = new EmployeeController(employeeDetailsView,fileHandler);
                employeeDetailsView.setController(controller);
                fileHandler.setController(controller);
            }
        });
    }//
}
