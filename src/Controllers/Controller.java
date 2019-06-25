package Controllers;

import Helpers.CSV;
import Helpers.Favorites;
import Views.DetailView;
import Views.MainView;
import Models.EdbEntry;
import Views.RestartDialog;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	public boolean restart = false;

	public Controller(){
		CSV h = new CSV();
		MainView g = new MainView();
		Favorites f = new Favorites();

		//Gets checkboxes and input when clicked on "search" button and updates gui with result
		//Removes excluded items
		g.searchButton.addActionListener(e -> {
			boolean includeIsCaseSensitive = false;
			boolean excludeIsCaseSensitive = false;
			boolean includeIsLoose = true;
			boolean excludeIsLoose = true;
			if(g.caseSensitiveIncludeBox.isSelected()) {
				includeIsCaseSensitive = true;
			}
			if(g.exactMatchIncludeBox.isSelected()){
				includeIsLoose = false;
			}
			if(g.caseSensitiveExcludeBox.isSelected()) {
				excludeIsCaseSensitive = true;
			}
			if(g.exactMatchExcludeBox.isSelected()){
				excludeIsLoose = false;
			}
			List[] query = h.parseQuery(g.searchField.getText(),includeIsCaseSensitive, excludeIsCaseSensitive);


			List<EdbEntry> dedup = new ArrayList<>();
			List[] include = new List[2];
			System.arraycopy(query,0,include,0,include.length);
			List<EdbEntry> in = h.search(h.buildQuery(include,includeIsLoose));
			if(query[3].size() > 0){
				List[] exclude = new List[2];
				System.arraycopy(query,include.length,exclude,0,exclude.length);

				List<EdbEntry> ex = h.search(h.buildQuery(exclude,excludeIsLoose));

				for (EdbEntry includeEntry : in) {
					if (!ex.contains(includeEntry)) {
						dedup.add(includeEntry);
					}
				}
			}
			else{
				dedup = in;
			}

			g.clearModel();
			for (EdbEntry entry: dedup) {
				Object[] obj = {entry.getId(), entry.getPlatform(),entry.getType(),entry.getDescription()};
				g.populateModel(obj);
			}
	});




		//Fires "detailview" when clicked on object in table
		//Handles favorites
		g.resultTable.getSelectionModel().addListSelectionListener(e -> {
			if(! e.getValueIsAdjusting()) {
				String id = g.resultTable.getValueAt(g.resultTable.getSelectedRow(), 0).toString();
				EdbEntry selectedEntry = h.searchByID(id);
				DetailView d = new DetailView(selectedEntry,f.isFavorite(selectedEntry.getId()));


				d.favbutton.addActionListener(e1 -> {
					File favs = new File("lib\\favorites.properties");
					if(!favs.exists()){
						f.createFavoritefile(d.getId());
						new RestartDialog(true);
						setRestart(true);
						d.dispose();
					}
					if(!d.favbutton.isSelected()){
						f.removeFavoriteNode(d.getId());
						new RestartDialog(false);
						setRestart(true);
						d.dispose();
					} else{
						f.addFavoriteNode(d.getId());
						new RestartDialog(true);
						setRestart(true);
						d.dispose();
					}
				});


				d.clipboardButton.addActionListener(e2 -> {
					StringSelection clipSelection = new StringSelection("https://www.exploit-db.com/exploits/" + d.getId() + "/");
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(clipSelection, clipSelection);
				});


				d.gotoCodeButton.addActionListener(e3 -> {
					try {
						Runtime.getRuntime().exec("notepad " + d.getExploitPath());
					}
					catch(Exception ex){
						System.out.println("Could not open file" + e);
					}


				});

			}
		});


		//Changes from filter/favorite view
		g.favButton.addActionListener(e -> {

			if(!MainView.FAVTOGGLE){
				MainView.setFAVTOGGLE(true);
				g.favButton.setText("Filter");
				g.favButton.setBackground(Color.LIGHT_GRAY);
				g.hidefilter();
				g.clearModel();
				List<EdbEntry> favlist = new ArrayList<>();
				for (String fav:f.getAllFavoriteNodes()) {
					favlist.add(h.searchByID(fav));
				}
				for (EdbEntry entry: favlist) {
					Object[] obj = {entry.getId(), entry.getPlatform(),entry.getType(),entry.getDescription()};
					g.populateModel(obj);
				}

			}
			else{
				MainView.setFAVTOGGLE(false);
				g.favButton.setText("Favoriten");
				g.favButton.setBackground(null);
				g.showfilter();
				g.clearModel();
			}
		});

	}


	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}
}
