package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// App Frontend UI setup
public class AppWindow extends JFrame {
	// Main App UI widgets
	private final BookTableView tableModel = new BookTableView();
	private final JButton searchButton = new JButton("Search");
	private final JTextField searchField = new JTextField(15);
	private final JComboBox<String> sortDropdown = new JComboBox<>(new String[]{"Authors", "Year"});
	private final JCheckBox descCheckbox = new JCheckBox("Descending");
	private final JRadioButton arrayRadio = new JRadioButton("ArrayList", true);
	private final JRadioButton linkedRadio = new JRadioButton("LinkedList");

	public AppWindow() {

		setTitle("MyDesk Library App");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());


		//Top Bar UI
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Query:"));
		northPanel.add(searchField);
		northPanel.add(searchButton);
		northPanel.add(sortDropdown);
		northPanel.add(descCheckbox);

		// Link radio buttons UI
		ButtonGroup group = new ButtonGroup();
		group.add(arrayRadio); group.add(linkedRadio);
		northPanel.add(arrayRadio); northPanel.add(linkedRadio);

		//Components positions UI
		add(northPanel, BorderLayout.NORTH);
		add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
	}

	//App Getters UI
	public BookTableView getTableModel() { return tableModel; }
	public String getSearchQuery() { return searchField.getText(); }
	public String getSelectedSort() { return (String) sortDropdown.getSelectedItem(); }
	public boolean isDescending() { return descCheckbox.isSelected(); }
	public boolean isArrayListSelected() { return arrayRadio.isSelected(); }

	//Listeners UI
	public void addSearchListener(ActionListener l) { searchButton.addActionListener(l); }
	public void addSortListener(ActionListener l) { sortDropdown.addActionListener(l); descCheckbox.addActionListener(l); }
	public void addStorageToggleListener(ActionListener l) { arrayRadio.addActionListener(l); linkedRadio.addActionListener(l); }
}

