package ReinforcementLearning;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;



/**
 *
 * @author Group 22
 */
public class Track {
    // holds the character array representing the track
    private char[][] track;
    // holds the row and column sizes y and x respectively
    private int y, x;
    private String temp;
    // starting coordinates for the track
    private int start_x, start_y;
    
    /**
     * Track takes a file name for a file located in the project directory
     * and parses it into a character array. File must start with array dimensions
     * as "##,##". Array is row major.
     * 
     * @param file String of the file name as "<filename>.txt"
     */
    public Track (String file) {
        trackbuilder(file);
    }
    
    
    public char[][] getTrack() {
        return track;
    }
    
    private void trackbuilder(String s) {
        try {
            FileReader parser = new FileReader(s);
            Scanner scan = new Scanner(parser);
            scan.useDelimiter("\n");
            // first line is the dimensions for the track
            temp = scan.next();
            // split the first line on the comma
            String[] coords = temp.split("[,\n\r]");
            
            // extract x and y values (row major)
            y = Integer.parseInt(coords[0]);
            x = Integer.parseInt(coords[1]);
            
            // initialize the character array size
            track = new char[y][x];
            
            // scan the rest of the track file as a character array
            for (int i = 0; i < y; i++) {
                temp = scan.next();
                for (int j = 0; j < x; j++) {
                    track[i][j] = temp.charAt(j);
                }
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
    
}
