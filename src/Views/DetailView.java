package Views;

import Models.EdbEntry;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DetailView extends JFrame{
    public JToggleButton favbutton;
    public JButton clipboardButton;
    public JButton gotoCodeButton;
    private EdbEntry entry;


    public DetailView(EdbEntry entryobj, boolean isFaved){
        entry = entryobj;
        this.setLocationRelativeTo(null);
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0,1));

        favbutton = new JToggleButton("Fav",isFaved);
        sidebar.add(favbutton);
        sidebar.add(new JLabel("EDB-ID: " + entry.getId()));
        sidebar.add(new JLabel("Description: " + entry.getDescription()));
        sidebar.add(new JLabel("Author: " + entry.getAuthor()));
        sidebar.add(new JLabel("Published:  " + entry.getDate()));
        sidebar.add(new JLabel("Type: " + entry.getType()));
        sidebar.add(new JLabel("Platform: " + entry.getPlatform()));
        sidebar.add(new JLabel("Port: " + entry.getPort()));
        sidebar.add(new JLabel(" "));

        sidebar.add(new JLabel("Exploit-DB Path: https://www.exploit-db.com/exploits/" + entry.getId() + "/"));
        sidebar.add(new JLabel("Local Path: " + entry.getFile()));

        this.add(sidebar, BorderLayout.LINE_START);

        JPanel buttonpanel = new JPanel();
        clipboardButton = new JButton("Copy Link To Clipboard");
        gotoCodeButton = new JButton("Go To Code");
        buttonpanel.add(clipboardButton);
        buttonpanel.add(gotoCodeButton);

        this.add(buttonpanel, BorderLayout.LINE_END);

        File exploitFile = new File(getExploitPath());

        if(!exploitFile.exists()){
            gotoCodeButton.setEnabled(false);
        }

        this.pack();
        this.setVisible(true);
    }

    //Returns edb-id
    public String getId(){
        return entry.getId();
    }

    //Returns relative path to exploit
    public String getFilePath(){
        return entry.getFile();
    }

    //Returns absolute path to exploit
    public String getExploitPath(){
        return System.getProperty("user.dir") + "\\lib\\exploitdb\\" + getFilePath();
    }

}
