package controller;


import model.BookDOA;
import view.AppWindow;
import java.util.Comparator;
import model.Book;

// Integration Layer
public class BookController {

	private final BookDOA model;
	private final AppWindow view;

	public BookController(BookDOA model, AppWindow view) {

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

			//Give JTable a list containing the single match, or an empty list if nothing found
			view.getTableModel().setBooks(found != null ? java.util.List.of(found) : java.util.List.of());
		}
	}

	private void handleBookSort() {

		boolean asc = !view.isDescending();

		//Automatic sorting method
		Comparator<Book> comp = view.getSelectedSort().equals("Authors") ?
				Comparator.comparing(Book::authors) : Comparator.comparingInt(Book::year);

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
		view.getTableModel().setBooks(model.getAllBooks());
	}
}
