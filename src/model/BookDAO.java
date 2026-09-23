package model;

import java.util.List;
import java.util.Comparator;

public interface BookDAO {

	//Read the pre-loaded file records using Regular Expressions
	void loadCSV(String filePath);
	void useArrayList();
	void useLinkedList();
	List<Book> getTopBooks();
	List<Book> getAllBooks();
	Book search(String query);
	void sort(Comparator<Book> comparator, boolean ascending);
	boolean deleteBookById(int id);
}

