package Views;

import Models.Filter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;
import java.awt.event.ItemEvent;

public class MainView extends JFrame{
	private DefaultTableModel model;
	private final static int INITIAL_ROW_NUMBER = 5;
	private final static int INITIAL_COL_NUMBER = 5;

	public static boolean FAVTOGGLE = false;

	private JPanel filterPanel;
	private GridLayout guilayout;
	private JTextField includeField;
	private JComboBox includeCombobox;
	private Filter filterProperties = new Filter();
	private Filter excludeProperties = new Filter();
	private JComboBox excludeCombobox;
	private JTextField excludeField;

	public JTextField searchField;
	public JButton searchButton;
	public JButton favButton;
	public JTable resultTable;
	public JCheckBox caseSensitiveIncludeBox;
	public JCheckBox exactMatchIncludeBox;
	public JCheckBox caseSensitiveExcludeBox;
	public JCheckBox exactMatchExcludeBox;

	
	public MainView() {
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		guilayout = new GridLayout(3,0);
		this.setLayout(guilayout);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setTitle("SearchSploit");
		
		JPanel topBar = new JPanel();
		searchField = new JTextField("Software Name",35);
		searchButton = new JButton("Search");
		favButton = new JButton("Favoriten");
		

		topBar.setLayout(new BorderLayout());
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(searchField, BorderLayout.CENTER);
		topBar.add(searchPanel);

		JPanel topButtonPanel = new JPanel();
		topButtonPanel.add(searchButton);
		topButtonPanel.add(favButton);
		topBar.add(topButtonPanel,BorderLayout.LINE_END);

		this.add(topBar);

		
		filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(0,1));
		
		filterPanel.add(new JLabel("Filter: "));
		
		JPanel includePanel = new JPanel();
		includePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		String[] filterOptions = {"EDB ID", "Platform","Type","Title"};
		includeCombobox = new JComboBox(filterOptions);
		includePanel.add(includeCombobox);
		includeField = new JTextField("EDB ID (Komma seperated)");
		includePanel.add(includeField);
		caseSensitiveIncludeBox = new JCheckBox("Case Sensitive", false);
		includePanel.add(caseSensitiveIncludeBox);
		exactMatchIncludeBox = new JCheckBox("Exact Match", false);
		includePanel.add(exactMatchIncludeBox);
		
		filterPanel.add(includePanel);
		
		filterPanel.add(new JLabel("Exclude: "));

		JPanel excludePanel = new JPanel();
		excludePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		excludeCombobox = new JComboBox(filterOptions);
		excludePanel.add(excludeCombobox);
		excludeField = new JTextField("EDB ID (Komma seperated)");
		excludePanel.add(excludeField);
		caseSensitiveExcludeBox = new JCheckBox("Case Sensitive", false);
		excludePanel.add(caseSensitiveExcludeBox);
		exactMatchExcludeBox = new JCheckBox("Exact Match", false);
		excludePanel.add(exactMatchExcludeBox);
		
		filterPanel.add(excludePanel);
		
		this.add(filterPanel);
		
		
		//Sets jtable read only
		model = new DefaultTableModel(INITIAL_ROW_NUMBER, INITIAL_COL_NUMBER){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};

		model.setColumnIdentifiers(filterOptions);
		resultTable = new JTable(model);
		resultTable.setPreferredScrollableViewportSize(resultTable.getPreferredSize());
		
		JScrollPane resultPanel = new JScrollPane(resultTable);
		resultTable.setFillsViewportHeight(true);
		
		this.add(resultPanel);

		//Listens for changes in the "include" filter field
		includeField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { updateQuery(); }
			@Override public void removeUpdate(DocumentEvent e) { updateQuery(); }
			@Override public void changedUpdate(DocumentEvent e){ updateQuery(); }
		});

		//Listens for changes in the "exclude" filter field
		excludeField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { updateQuery(); }
			@Override public void removeUpdate(DocumentEvent e) { updateQuery(); }
			@Override public void changedUpdate(DocumentEvent e){ updateQuery(); }
		});

		//Writes back saved searchproperties from "include" filter field when combobox changed
		includeCombobox.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED){
				switch(e.getItem().toString()){
					case "EDB ID":
						StringBuilder savedids = new StringBuilder();
						if(filterProperties.getEnteredIds().size() > 0){
							for (String s: filterProperties.getEnteredIds()) { savedids.append(s).append(","); }
							savedids = new StringBuilder(savedids.substring(0, savedids.length() - 1));
						}
						includeField.setText(savedids.toString());
						break;
					case "Platform":
						StringBuilder savedplatforms = new StringBuilder();
						if(filterProperties.getEnteredPlatform().size() > 0) {
							for (String s : filterProperties.getEnteredPlatform()) { savedplatforms.append(s).append(","); }
							savedplatforms = new StringBuilder(savedplatforms.substring(0, savedplatforms.length() - 1));
						}
						includeField.setText(savedplatforms.toString());
						break;
					case "Type":
						StringBuilder savedtypes = new StringBuilder();
						if(filterProperties.getEnteredType().size() > 0) {
							for (String s : filterProperties.getEnteredType()) { savedtypes.append(s).append(","); }
							savedtypes = new StringBuilder(savedtypes.substring(0, savedtypes.length() - 1));
						}
						includeField.setText(savedtypes.toString());
						break;
					case "Title":
						StringBuilder savedtitles = new StringBuilder();
						if(filterProperties.getEnteredTitle().size() > 0) {
							for (String s : filterProperties.getEnteredTitle()) { savedtitles.append(s).append(","); }
							savedtitles = new StringBuilder(savedtitles.substring(0, savedtitles.length() - 1));
						}
						includeField.setText(savedtitles.toString());
						break;
				}
			}

		});


		//Writes back saved searchproperties from "exclude" filter field when combobox changed
		excludeCombobox.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED){
				switch(e.getItem().toString()){
					case "EDB ID":
						StringBuilder savedids = new StringBuilder();
						if(excludeProperties.getEnteredIds().size() > 0){
							for (String s: excludeProperties.getEnteredIds()) { savedids.append(s).append(","); }
							savedids = new StringBuilder(savedids.substring(0, savedids.length() - 1));
						}
						excludeField.setText(savedids.toString());
						break;
					case "Platform":
						StringBuilder savedplatforms = new StringBuilder();
						if(excludeProperties.getEnteredPlatform().size() > 0) {
							for (String s : excludeProperties.getEnteredPlatform()) { savedplatforms.append(s).append(","); }
							savedplatforms = new StringBuilder(savedplatforms.substring(0, savedplatforms.length() - 1));
						}
						excludeField.setText(savedplatforms.toString());
						break;
					case "Type":
						StringBuilder savedtypes = new StringBuilder();
						if(excludeProperties.getEnteredType().size() > 0) {
							for (String s : excludeProperties.getEnteredType()) { savedtypes.append(s).append(","); }
							savedtypes = new StringBuilder(savedtypes.substring(0, savedtypes.length() - 1));
						}
						excludeField.setText(savedtypes.toString());
						break;
					case "Title":
						StringBuilder savedtitles = new StringBuilder();
						if(excludeProperties.getEnteredTitle().size() > 0) {
							for (String s : excludeProperties.getEnteredTitle()) { savedtitles.append(s).append(","); }
							savedtitles = new StringBuilder(savedtitles.substring(0, savedtitles.length() - 1));
						}
						excludeField.setText(savedtitles.toString());
						break;
				}
			}

		});


		this.setVisible(true);
	}



	//Gets values from filter fields and uses them to call setProperties and store them in objects
	private void setQueryProperties(){
		String includeArgs = includeField.getText();
		String[] includeArglist = includeArgs.split(",");
		String excludeArgs = excludeField.getText();
		String[] excludeArglist = excludeArgs.split(",");
		setProperties(includeArglist, includeCombobox, filterProperties);
		setProperties(excludeArglist, excludeCombobox, excludeProperties);
	}

	//Saves values into filterArgument objects
	private void setProperties(String[] excludeArglist, JComboBox excludeCombobox, Filter excludeProperties) {
		switch(excludeCombobox.getSelectedItem().toString()){
			case "EDB ID":
				List<String> edbli = new ArrayList<>();
				for (String arg:excludeArglist) {if(!arg.equals("EDB ID (Komma seperated)")){edbli.add(arg);} }
				excludeProperties.setEnteredIds(edbli);
				break;
			case "Platform":
				List<String> pli = new ArrayList<>();
				Collections.addAll(pli, excludeArglist);
				excludeProperties.setEnteredPlatform(pli);
				break;
			case "Type":
				List<String> tli = new ArrayList<>();
				Collections.addAll(tli, excludeArglist);
				excludeProperties.setEnteredType(tli);
				break;
			case "Title":
				List<String> tili = new ArrayList<>();
				Collections.addAll(tili, excludeArglist);
				filterProperties.setEnteredTitle(tili);
				break;
		}
	}


	//Updates searchQuery in gui from filterArgument object
	private void updateQuery(){
		setQueryProperties();
		StringBuilder idstring = new StringBuilder();
		StringBuilder platformstring = new StringBuilder();
		StringBuilder typestring = new StringBuilder();
		StringBuilder titlestring = new StringBuilder();
		StringBuilder eIdstring = new StringBuilder();
		StringBuilder ePlatformstring = new StringBuilder();
		StringBuilder eTypestring = new StringBuilder();
		StringBuilder eTitlestring = new StringBuilder();
		for (String s: filterProperties.getEnteredIds()) { idstring.append("id:").append(s).append(" "); }
		for (String s: filterProperties.getEnteredPlatform()) { platformstring.append("platform:").append(s).append(" "); }
		for (String s: filterProperties.getEnteredType()) { typestring.append("type:").append(s).append(" "); }
		for (String s: filterProperties.getEnteredTitle()) { titlestring.append("title:").append(s).append(" "); }
		for (String s: excludeProperties.getEnteredIds()) { eIdstring.append("-id:").append(s).append(" "); }
		for (String s: excludeProperties.getEnteredPlatform()) { ePlatformstring.append("-platform:").append(s).append(" "); }
		for (String s: excludeProperties.getEnteredType()) { eTypestring.append("-type:").append(s).append(" "); }
		for (String s: excludeProperties.getEnteredTitle()) { eTitlestring.append("-title:").append(s).append(" "); }
		String query = idstring + platformstring.toString() + typestring + titlestring + eIdstring + ePlatformstring + eTypestring + eTitlestring;
		searchField.setText(query);
	}



    //Default table model from java native library
	public DefaultTableModel getModel() {
		return model;
	}


	//Fills the table
	public void populateModel(Object[] data){
		if(!modelIsFull()){
			if(modelHasEmptyCell()){
				int fillIndex  = modelGetEmptyCellIndex();
				for(int i = 0; i < (INITIAL_COL_NUMBER-1); i++){
					model.setValueAt(data[i], fillIndex , i);
				}
			}
			
		}
		else{
			model.addRow(data);
		}
	}

	//Removes all entries from table
	public void clearModel(){
		while (model.getRowCount() > 0)
		{
			model.removeRow(0);
		}
		model.setRowCount(INITIAL_ROW_NUMBER);
	}
	
	//Checks if model contains empty cells
	private boolean modelHasEmptyCell(){
		for(int i = 0; i <= model.getColumnCount(); i++){
			if(model.getValueAt(i,0) == null){
				return true;
			}
		}
		return false;
	}
	
	//Gets index of empty cell
	private int modelGetEmptyCellIndex(){
		for(int i = 0; i <= model.getColumnCount(); i++){
			if(model.getValueAt(i,0) == null){
				return i;
			}
		}
		return -1;
	}

	//Checks if table is full and needs more rows
	private boolean modelIsFull(){
		return model.getValueAt(model.getColumnCount(), 0) != null;
	}


	//Adds filtergroup from gui
	public void hidefilter(){
		super.remove(filterPanel);
		super.repaint();
		guilayout.setRows(2);
		super.repaint();
	}

	//Removes filtergroup from gui
	public void showfilter(){
		guilayout.setRows(3);
		super.repaint();
		super.add(filterPanel, 1);
		super.repaint();
	}


	//Getter for favtoggle
	public static boolean isFAVTOGGLE() {
		return FAVTOGGLE;
	}

	//Setter for favtoggle
	public static void setFAVTOGGLE(boolean FAVTOGGLE) {
		MainView.FAVTOGGLE = FAVTOGGLE;
	}
}
