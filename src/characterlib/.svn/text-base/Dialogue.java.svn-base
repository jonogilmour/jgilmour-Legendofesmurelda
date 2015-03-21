package characterlib;

import java.util.Scanner;
import static config.GlobalConfiguration.*;
/**
 * Currently unused, but may be implemented in future
 * 
 * @author Jono
 *
 */
public class Dialogue {
	private String fileName;
	private String[] lines;
	private int currentLine;
	
	public Dialogue() {
		this.fileName = DEFAULT_DIALOGUE;
		this.lines = new String[1];
		this.currentLine = 0;
		
		this.lines[0] = "";
	}
	
	public Dialogue(String fn) {
		this.fileName = fn;
		this.lines = new String[MAX_DIALOGUE_LENGTH];
		this.currentLine = 0;
		
		Scanner parser = new Scanner(SCRIPT_FOLDER + this.getFileName());
		int x = NIL;
		while(parser.hasNextLine()) {
			if(x == MAX_DIALOGUE_LENGTH) break;
			
			this.lines[x] = parser.nextLine();
			x++;
		}
	}
	
	public void nextLine() {
		this.getLine();
		this.setCurrentLine(this.getCurrentLine() + 1);
	}
	
	private String getLine() {
		return this.lines[this.getCurrentLine()];
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}

	public int getCurrentLine() {
		return currentLine;
	}
}
