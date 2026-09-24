import model.*;
import view.AppWindow;
import controller.BookController;
import javax.swing.*;

public class MainApp {
	public static void main(String[] args) {

		// Initialize the Book Data Engine instance using BookDataEngine
		BookDAO model = new BookDataEngine();


		SwingUtilities.invokeLater(() -> {

			//App UI layout
			AppWindow view = new AppWindow();

			//Initialize App Controller
			new BookController(model, view);

			// Deleting button logic
			view.addDeleteOption(e -> {

				JTable table = null;

				for (java.awt.Component comp : view.getContentPane().getComponents()) {

					if (comp instanceof JScrollPane) {
						table = (JTable) ((JScrollPane) comp).getViewport().getView();
						break;
					}
				}

				if (table == null || table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(view, "Please select a row first to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int selectedRow = table.getSelectedRow();
				int modelRow = table.convertRowIndexToModel(selectedRow);
				int targetId = (int) table.getModel().getValueAt(modelRow, 0);

				int option = JOptionPane.showConfirmDialog(view, "Delete Book ID " + targetId + "?", "Confirm", JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION) {

					boolean deleted = model.deleteBookById(targetId);

					if (deleted) {

						view.getTableModel().setBooks(model.getTopBooks());
						JOptionPane.showMessageDialog(view, "Record deleted successfully.");

					}
				}
			});

			//Add Book button logic
			view.addAddBookOption(e -> {

				JTextField idField = new JTextField();
				JTextField isbnField = new JTextField();
				JTextField authorField = new JTextField();
				JTextField yearField = new JTextField();
				JTextField titleField = new JTextField();
				JTextField ratingField = new JTextField();

				Object[] form = {
						"ID:", idField,
						"ISBN:", isbnField,
						"Author:", authorField,
						"Year:", yearField,
						"Title:", titleField,
						"Rating:", ratingField
				};

				int result = JOptionPane.showConfirmDialog(
						view,
						form,
						"Add New Book",
						JOptionPane.OK_CANCEL_OPTION
				);

				if (result == JOptionPane.OK_OPTION) {

					try {
						int id = Integer.parseInt(idField.getText());
						String isbn = isbnField.getText();
						String author = authorField.getText();
						int year = Integer.parseInt(yearField.getText());
						String title = titleField.getText();
						double rating = Double.parseDouble(ratingField.getText());

						Book newBook = new Book(id, isbn, author, year, title, rating);

						model.addBook(newBook);

						view.getTableModel().setBooks(model.getTopBooks());
						JOptionPane.showMessageDialog(view, "Book added successfully!");

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(view, "Invalid input. Please try again.");
					}
				}
			});


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
