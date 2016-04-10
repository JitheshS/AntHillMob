/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antgame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Parse world files into playable game board objects
 */
public class WorldParser {

    public WorldParser() {

    }

    //add exceptions for all this
    public GameBoard parse(String filename) throws FileNotFoundException, IOException, InvalidMapTokenException {

        Cell[][] board;
        ArrayList<Ant> ants = new ArrayList<Ant>();

        //trim sentence, scanToken(x) into cell, into board, maybe n-1 for size? check
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        
        //add exceptions for all this
        int height = Integer.parseInt(reader.readLine());
        int width = Integer.parseInt(reader.readLine());
        System.out.println(height + " " + width);

        board = new Cell[height][width];

        String line;

        int rowCount = 0;
        while ((line = reader.readLine()) != null) {
            String trim_line = line.replaceAll("\\s+", "");
            for (int i = 0; i < trim_line.length(); i++) {
                Cell cell = scanToken(String.valueOf(trim_line.charAt(i)));
                

                if (cell.isAnthill(Colour.RED)) {
                    Ant ant = new Ant(Colour.RED, 0, rowCount, i); //what direction?
                    cell.setOccupied(ant);
                    ants.add(ant);
                    //System.out.println("red anthill cords: " + rowCount + " " + i);
                } else if (cell.isAnthill(Colour.BLACK)) {
                    Ant ant = new Ant(Colour.BLACK, 0, rowCount, i); //what direction?
                    cell.setOccupied(ant);
                    ants.add(ant);
                }
                
                board[rowCount][i] = cell;
            }
            rowCount++;
        }
        System.out.println("Columns done: " + rowCount);
        
        return new GameBoard(board, ants);
    }

    private Cell scanToken(String token) throws InvalidMapTokenException {

        if (token.equals("#")) { //rocky cell
            Cell cell = new Cell(Terrain.ROCKY, null);
            return cell;
        } else if (token.equals(".")) { //normal clear cell
            Cell cell = new Cell(Terrain.CLEAR, null);
            return cell;
        } else if (token.equals("+")) { //red anthill
            Cell cell = new Cell(Terrain.CLEAR, Colour.RED);
            return cell;
        } else if (token.equals("-")) { //black anthill
            Cell cell = new Cell(Terrain.CLEAR, Colour.BLACK);
            return cell;
        } else if (token.matches("[1-9]")) { //food cell
            Cell cell = new Cell(Terrain.CLEAR, null);
            int foodCount = Integer.parseInt(token);
            cell.setFoodCount(foodCount);
            return cell;
        } else {
            throw new InvalidMapTokenException("Token not found");
        }

    }

}