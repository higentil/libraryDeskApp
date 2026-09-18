import model.*;
import view.AppWindow;
import controller.BookController;
import javax.swing.SwingUtilities;

public class MainApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {

			// Initialize the Book Data Engine instance
			BookDOA model = new BookDataEngine();

			//Pre-load the dataset
			model.loadCSV("data/books.csv");

			//App UI layout 
			AppWindow view = new AppWindow();

			//Initialize App Controller 
			new BookController(model, view);

			//Display the App Main Window
			view.setVisible(true);
		});
	}
}
