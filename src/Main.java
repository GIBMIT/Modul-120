import Controllers.Controller;

public class Main {

	public static void main(String[] args) {

		Controller c = new Controller();
		while(true) {
			if(c.isRestart()){
				c = new Controller();
			}

		}
	}
}
