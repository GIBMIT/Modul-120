package Models;

import java.util.*;

public class Filter {
    List<String> enteredIds = new ArrayList<String>();
    List<String> enteredPlatform = new ArrayList<String>();
    List<String> enteredTitle = new ArrayList<String>();
    List<String> enteredType = new ArrayList<String>();

    public List<String> getEnteredIds() { return enteredIds; }

    public void setEnteredIds(List<String> enteredIds) { this.enteredIds = enteredIds; }

    public List<String> getEnteredPlatform() { return enteredPlatform; }

    public void setEnteredPlatform(List<String> enteredPlatform) { this.enteredPlatform = enteredPlatform; }

    public List<String> getEnteredTitle() { return enteredTitle; }

    public void setEnteredTitle(List<String> enteredTitle) { this.enteredTitle = enteredTitle; }

    public List<String> getEnteredType() { return enteredType; }

    public void setEnteredType(List<String> enteredType) { this.enteredType = enteredType; }
}
