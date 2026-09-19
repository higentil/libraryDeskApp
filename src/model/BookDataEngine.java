package model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.Random;

// App's Engine setup
public class BookDataEngine implements BookDAO{

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
		
		arrayStorage.clear();
		linkedStorage.clear();
		
		String regex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		Pattern pattern = Pattern.compile(regex);
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String line;
			boolean isHeader = true;
			
			while((line = br.readLine()) != null) {
				if(isHeader) {
					isHeader = false;
					continue;
				}
				
				String[] tokens = pattern.split(line, -1);
				if (tokens.length >= 13) {
					
					try {
						
							// Extract and strip potential wrapping string literal quotes from data fields
							int id = Integer.parseInt(tokens[0].trim().replaceAll("^\"|\"$", ""));
							String isbn = tokens[5].trim().replaceAll("^\"|\"$", "");
							String authors = tokens[7].trim().replaceAll("^\"|\"$", "");
						
							// Fallback check: if original_publication_year field is blank, default to 0
							String yearRaw = tokens[8].trim().replaceAll("^\"|\"$", "");
							int year = yearRaw.isEmpty() ? 0 : (int) Double.parseDouble(yearRaw);
						
							String title = tokens[10].trim().replaceAll("^\"|\"$", "");
						
							String ratingRaw = tokens[12].trim().replaceAll("^\"|\"$", "");
							double rating = ratingRaw.isEmpty() ? 0.0 : Double.parseDouble(ratingRaw);

							// Construct complete book model record
							Book book = new Book(id, isbn, authors, year, title, rating);
						
							// Synchronize storage backends
							arrayStorage.add(book);
							linkedStorage.add(book);
					} catch (NumberFormatException nfe) {
						// Skip malformed records silently to keep execution flow clean
						continue;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Fatal: Error loading CSV resource payload structural stream: " + e.getMessage());
		}
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
		
		int end = Math.min(10, activeList.size());
		//Returns the first n books
		return new ArrayList<>(activeList.subList(0, end)); 

	}
	@Override public List<Book> getAllBooks() { 

		//Returns active data list
		return activeList; 

	}

	@Override public Book search(String query) { 

		//Scan activeList for matching ID or ISBN
		if (query == null || query.isBlank()) {
			return null;
		}
		
		String cleanQuery = query.trim();
		for(Book book : activeList) {
			if(String.valueOf(book.id()).equals(cleanQuery) || book.isbn().equalsIgnoreCase(cleanQuery)) {
				return book;
			}
		}
		return null; 

	}
	@Override public void sort(Comparator<Book> comp, boolean asc) { 

		//Sort activeList 
		Comparator<Book> strictComp = asc ? comp : comp.reversed();
		
		activeList.sort(strictComp);
		
	}

}
