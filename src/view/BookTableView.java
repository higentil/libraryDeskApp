package view;

import model.Book;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class BookTableView extends AbstractTableModel {

	// Header labels
	private final String[] columns = {"ID", "ISBN", "Authors", "Year", "Title", "Rating"};
	private List<Book> books = new ArrayList<>();

	public void setBooks(List<Book> books) {

		this.books = books;
		fireTableDataChanged();
	}

	@Override public int getRowCount() { return books.size(); }
	@Override public int getColumnCount() { return columns.length; }
	@Override public String getColumnName(int col) { return columns[col]; }

	// Maps book object to its column index
	@Override public Object getValueAt(int row, int col) {

		Book bk = books.get(row);

		return switch(col) {

			case 0 -> bk.id();
			case 1 -> bk.isbn();
			case 2 -> bk.authors();
			case 3 -> bk.year();
			case 4 -> bk.title();
			case 5 -> bk.rating();
			default -> null;

		};
	}
}