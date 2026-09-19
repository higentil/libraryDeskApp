package controller;


import model.BookDAO;
import view.AppWindow;
import java.util.Comparator;
import model.Book;
import javax.swing.JOptionPane;
import java.util.ArrayList;

// Integration Layer
public class BookController {

	private final BookDAO model;
	private final AppWindow view;

	public BookController(BookDAO model, AppWindow view) {

		this.model = model;
		this.view = view;

		// UI background logic methods
		this.view.addSearchListener(e -> handleBookSearch());
		this.view.addSortListener(e -> handleBookSort());
		this.view.addStorageToggleListener(e -> handleStorageToggle());

		// Baseline for data rendering after launch 
		refreshTableView();
	}

	private void handleBookSearch() {

		String query = view.getSearchQuery();

		if(query.isEmpty()) {

			//Reset table back to original form
			refreshTableView();
		} else {
			Book found = model.search(query);
			
			java.util.List<Book> searchResult = new ArrayList<>();
			
			if (found != null) {
				searchResult.add(found);
			}

			//Give JTable a list containing the single match, or an empty list if nothing found
			view.getTableModel().setBooks(searchResult);
		}
	}

	private void handleBookSort() {

		boolean asc = !view.isDescending();

		//Automatic sorting method
		Comparator<Book> comp = view.getSelectedSort().equals("Authors") ?
				Comparator.comparing(Book::authors, String.CASE_INSENSITIVE_ORDER) : Comparator.comparingInt(Book::year);

		//Modify backend array order state
		model.sort(comp, asc);

		//Refresh screen view layout
		refreshTableView();
	}

	private void handleStorageToggle() {

		//Switch methods based on radio toggle settings
		if(view.isArrayListSelected()) model.useArrayList();
		else model.useLinkedList();
		
		refreshTableView();
	}

	
	private void refreshTableView() {

		//Switch original state from model layer to presentation model
		view.getTableModel().setBooks(model.getTopBooks());
	}
}
