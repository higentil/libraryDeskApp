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
						continue;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Fatal: Error loading CSV resource payload structural stream: " + e.getMessage());
		}
	}

	@Override public void useArrayList() {
		this.activeList = arrayStorage;
		arrayStorage.sort(Comparator.comparingInt(Book::id));
	}

	@Override public void useLinkedList() {
		this.activeList = linkedStorage;
	}

	@Override public List<Book> getTopBooks() {
		int end = Math.min(10, activeList.size());
		return new ArrayList<>(activeList.subList(0, end));
	}

	@Override public List<Book> getAllBooks() {
		return activeList;
	}

	private Book binarySearchById(List<Book> list, int targetId) {
		int low = 0;
		int high = list.size() - 1;

		while (low <= high) {
			int mid = (low + high) / 2;
			Book midBook = list.get(mid);

			if (midBook.id() == targetId) return midBook;
			else if (midBook.id() < targetId) low = mid + 1;
			else high = mid - 1;
		}
		return null;
	}

	private Book linearSearch(String query) {
		for (Book book : activeList) {
			if (String.valueOf(book.id()).equals(query)
					|| book.isbn().equalsIgnoreCase(query)) {
				return book;
			}
		}
		return null;
	}

	@Override public Book search(String query) {
		if (query == null || query.isBlank()) return null;

		String clean = query.trim();

		if (activeList == arrayStorage) {
			try {
				int id = Integer.parseInt(clean);
				return binarySearchById(activeList, id);
			} catch (NumberFormatException e) {
				return linearSearch(clean);
			}
		}

		return linearSearch(clean);
	}

	@Override public void sort(Comparator<Book> comp, boolean asc) {
		Comparator<Book> strictComp = asc ? comp : comp.reversed();
		activeList.sort(strictComp);
	}

	// Deleting feature
	@Override public boolean deleteBookById(int id) {
		Book targetBk = this.search(String.valueOf(id));

		if (targetBk == null) {
			return false;
		}

		boolean removedFromArray = arrayStorage.remove(targetBk);
		boolean removedFromLinked = linkedStorage.remove(targetBk);

		return removedFromArray || removedFromLinked;
	}

	//// ⭐ ADDED ⭐ — Add Book feature
	@Override
	public void addBook(Book book) {
		arrayStorage.add(book);
		linkedStorage.add(book);
	}

	// Performance Test**
	public void testSearchPerformance() {

		for (int i = 0; i < 2000; i++) {
			search("1");
		}

		List<Integer> ids = new ArrayList<>();
		for (Book b : arrayStorage) ids.add(b.id());

		List<String> isbns = new ArrayList<>();
		for (Book b : arrayStorage) isbns.add(b.isbn());

		Random rand = new Random();

		int trials = 500;
		long arrayIdTotal = 0;
		long linkedIdTotal = 0;
		long isbnTotal = 0;

		for (int i = 0; i < trials; i++) {

			int randomId = ids.get(rand.nextInt(ids.size()));
			String idQuery = String.valueOf(randomId);

			useArrayList();
			long startArray = System.nanoTime();
			search(idQuery);
			long endArray = System.nanoTime();
			arrayIdTotal += (endArray - startArray);

			useLinkedList();
			long startLinked = System.nanoTime();
			search(idQuery);
			long endLinked = System.nanoTime();
			linkedIdTotal += (endLinked - startLinked);
		}

		for (int i = 0; i < trials; i++) {

			String randomIsbn = isbns.get(rand.nextInt(isbns.size()));

			useArrayList();
			long startIsbn = System.nanoTime();
			search(randomIsbn);
			long endIsbn = System.nanoTime();
			isbnTotal += (endIsbn - startIsbn);
		}

		System.out.println("===== PERFORMANCE RESULTS =====");
		System.out.println("Trials: " + trials);
		System.out.println();
		System.out.println("ArrayList (binary search, ID): " + (arrayIdTotal / trials) + " ns avg");
		System.out.println("LinkedList (linear search, ID): " + (linkedIdTotal / trials) + " ns avg");
		System.out.println("Linear search (ISBN): " + (isbnTotal / trials) + " ns avg");
		System.out.println("================================");
	}
}
