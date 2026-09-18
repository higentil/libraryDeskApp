package model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;

// App's Engine setup
public class BookDataEngine implements BookDOA{

	// Pointer switch btn arrayStorage or linkedStorage
	private List<Book> activeList;

	//Main data structures setup
	private List<Book> arrayStorage = new ArrayList<>();
	private List<Book> linkedStorage = new LinkedList<>();

	public BookDataEngine() {

		//Default start with ArrayList data structure
		this.activeList = arrayStorage;
	}

	@Override public void loadCSV(String filePath) {
		//Regular Expression setup
	}
	@Override public void useArrayList() { 
		//Switch to ArrayList
		this.activeList = arrayStorage; 

	}
	@Override public void useLinkedList() { 

		// Switch to LinkedList
		this.activeList = linkedStorage; 
	}
	@Override public List<Book> getTopBooks() { 

		//Returns the first n books
		return activeList.subList(0, Math.min(10, activeList.size())); 

	}
	@Override public List<Book> getAllBooks() { 

		//Returns active data list
		return activeList; 

	}

	@Override public Book search(String query) { 

		//Scan activeList for matching ID or ISBN

		return null; 

	}
	@Override public void sort(Comparator<Book> comp, boolean asc) { 

		//Sort activeList 
	}
	@Override public long runBaseline(int sampleSize) { 

		//Search for random books over n iterations for final results
		return 0; 

	}
}
