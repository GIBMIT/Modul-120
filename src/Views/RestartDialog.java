package Views;

import javax.swing.*;

public class RestartDialog extends JOptionPane {
    public RestartDialog(boolean isAdded){
        if(isAdded) {
            this.showConfirmDialog(null, "Added To Favorites - Changes Require Reload","Reloading", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } else{
            this.showConfirmDialog(null, "Removed From Favorites - Changes Require Reload","Restarting", JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        }
    }
}
