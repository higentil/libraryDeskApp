import model.*;
import view.AppWindow;
import controller.BookController;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        
        // Initialize the Book Data Engine instance using BookDataEngine
        BookDAO model = new BookDataEngine();

        
        SwingUtilities.invokeLater(() -> {

            //App UI layout 
            AppWindow view = new AppWindow();

            //Initialize App Controller 
            new BookController(model, view);
            
            //Display the App Main Window
            view.setVisible(true);
            
            new Thread(() -> {
                model.loadCSV("data/books.csv");

                // Performance results
                ((BookDataEngine) model).testSearchPerformance();
                
                SwingUtilities.invokeLater(() -> {
                    if (view.isArrayListSelected()) model.useArrayList();
                    else model.useLinkedList();
					
                    view.getTableModel().setBooks(model.getTopBooks());
                });
            }).start();
        });
    }
}
