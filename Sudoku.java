import java.util.*;
import java.io.*;
class Sudoku{
	public static Scanner sc = new Scanner(System.in);
	public static int[][][] board = new int[9][9][10];	
	public static boolean added = false;
	public static int i;
	public static FileInputStream fin;

	//public static void main(String args[])
	//args: the file name where the sudoku is stored
	//rets: none
	//purpose: runs all functions in the correct order
	public static void main(String args[]){
		
		
		if(args.length != 1){
			System.out.println("No file specified.\nPlease input sudoku manually");
			input();
		}
		else input(args[0]);		
		
		setPossibleNumber();

		printBoard();

		while(inComplete()){
			do{
				added = false;
				
				check();
				add();
				
				if(added)printBoard();
				if(!inComplete())return;
			}while(added);	
	
			uniqueCandidate();			
			add();
			

			if(!added){
				System.out.println("Not possible for me to solve.");
				return;
			}
			

			printBoard();			
		}
		
	}


	//public static void input()
	//args: none
	//rets: none
	//purpose: manually inputs the value of each point on the sudoku board into the array
	public static void input(){
		System.out.println("Input sudoku values from top left to bottom right");
		for(int y = 0; y < 9; y++){
			for(int x = 0; x < 9; x++){
				System.out.println("Input value at (" + (x+1) + "," + (y+1) + ")");
				board[x][y][0] = sc.nextInt();
				
			}
		}
	}

	//public static void input(String file)
	//args: the name of the file where the sudoku board is stored
	//rets: none
	//purpose: reads the file and stores the values in the arrays
	public static void input(String file){
		try{
			fin = new FileInputStream(file);
		}catch(FileNotFoundException e){
			System.out.println("Cannot Open File");
		}

		try{
			int x = 0, y = 0;
			do{
				i = fin.read();
				
				if(i != -1){
					if(i == 13){
						fin.skip(1);
						y++;	
						x = 0;
					}
					else{ 
						board[x][y][0] = Character.getNumericValue(i);
						x++;
					}				
				}

			}while(i != -1);
		}catch(IOException e){
			System.out.println("Error Reading File");
		}



		try{
			fin.close();
		}catch(IOException e){
			System.out.println("Error Closing File");
		}
	}

	//public static void setPossibleNumber()
	//args: none
	//rets: none
	//purpose: resets possible numbers for each position on the board
	//			if the position has a value --> it sets all possible numbers to 0(false) except for the value of that position
	//			if the position does not have a value( a value of 0) ---> it sets all possible numbers to 1(true)
	public static void setPossibleNumber(){
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				if(board[x][y][0] != 0){
					for(int z = 1; z < 10; z++)board[x][y][z] = 0;
					board[x][y][(board[x][y][0])] = 1;
				}
				else{
					for(int z = 1; z < 10; z++)board[x][y][z] = 1;					
				}
			}
		}
	}

	
	//public static void check()
	//args: none
	//rets: none
	//purpose: incorporates all check functions and runs them at the same time
	public static void check(){
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				if(board[x][y][0] == 0){
					checkRow(x,y);
					checkColumn(x,y);
					checkBox(x,y);
					
				}
			}
		}
	}


	//public static void checkRow(int x, int y)
	//args: the x- and y- postion of the place to be checked
	//rets: none
	//purpose: checks all values in the row and removes them from the possible numbers
	public static void checkRow(int x, int y){
		for(int i = 0; i < 9; i++){
			if(i != x && board[i][y][0] != 0)board[x][y][(board[i][y][0])] = 0;
		}
	}

	//public static void main checkColumn(int x, int y)
	//args: the x- and y- postion of the place to be checked
	//rets: none
	//purpose: checks all values in the column and removes them from possible numbers
	public static void checkColumn(int x, int y){
		for(int i = 0; i < 9; i++){
			if(i != y && board[x][i][0] != 0)board[x][y][(board[x][i][0])] = 0; 
		}
	}
	
	//public static void checkBox(int x, int y)
	//args: the x- and y- position of the place to be checked
	//rets: none
	//purpose: checks all values in the same box and removes them from possible numbers
	public static void checkBox(int x, int y){
		int[] a = boxCoordinates(y);
		int[] b = boxCoordinates(x);


		for(int z = 0; z < 3; z++){
			for(int k = 0; k < 3; k++){
				if(!(x == b[k] && y == a[z]) && board[(b[k])][(a[z])][0] != 0)board[x][y][(board[(b[k])][(a[z])][0])] = 0;
			}
		}
	}
	
	//public static void uniqueCandidate()
	//args: none
	//rets: none
	//purpose: incorporates all uniqueCandidate functions to be run at the same time
	public static void uniqueCandidate(){
		for(int y = 0; y < 9; y++){
			for(int x = 0; x < 9; x++){
				if(board[x][y][0] == 0){
					uniqueCandidateBox(x,y);
					uniqueCandidateRow(x,y);
					uniqueCandidateColumn(x,y);
				}
			}
		}				
	}

	//public static void uniqueCandidateBox(int x, int y)
	//args: the x- and y- position of the place to be checked
	//rets: none
	//purpose: decides if a certain number is only possible in (x,y) in its box	
	public static void uniqueCandidateBox(int x, int y){
		int[] a = boxCoordinates(y);		
		int[] b = boxCoordinates(x);		

		boolean unique;
		for(int z = 1; z < 10; z++){
			unique = true;
			if(board[x][y][z] == 1){
				for(int k = 0; k < 3; k++){
					for(int j = 0; j < 3; j++){
						if(!(x == b[k] && y == a[j]) && board[(b[k])][(a[j])][0] == 0 && board[x][y][z] == board[(b[k])][(a[j])][z]){						
							unique = false;
							break;
						}
					}
					if(!unique)break;
				}
				if(unique){
					for(int k = 1; k < 10; k++)board[x][y][k] = 0;				
					board[x][y][z] = 1;
					return;
				}

			}

		}
	}

	//public static void uniqueCandidateRow(int x, int y)
	//args: the x- and y- position of the place to be checked
	//rets: none
	//purpose: decides if a certain number is only possible in (x,y) in its row	
	public static void uniqueCandidateRow(int x, int y){
		
		boolean unique;
		for(int z = 1; z < 10; z++){
			unique = true;
			if(board[x][y][z] == 1){	
				for(int k = 0; k < 9; k++){
					if(x != k && board[x][y][z] == board[k][y][z]){
						unique = false;
						break;
					}				
				}
				if(unique){
					for(int k = 1; k < 10; k++)board[x][y][k] = 0;
					board[x][y][z] = 1;	
					break;
				}
			}
		}
	}

	//public static void uniqueCandidateColumn(int x, int y)
	//args: the x- and y- position of the place to be checked
	//rets: none
	//purpose: decides if a certain number is only possible in (x,y) in its column

	public static void uniqueCandidateColumn(int x, int y){
		
		boolean unique;
		for(int z = 1; z < 10; z++){
			unique = true;
			if(board[x][y][z] == 1){	
				for(int k = 0; k < 9; k++){
					if(y != k && board[x][y][z] == board[x][k][z]){
						unique = false;
						break;
					}				
				}
				if(unique){
					for(int k = 1; k < 10; k++)board[x][y][k] = 0;
					board[x][y][z] = 1;	
					break;
				}
			}
		}
	}
	

	//public static void add()
	//args: none
	//rets: none
	//purpose: adds numbers to the solution iff (x,y) has only one possible number
	public static void add(){
		int k = 0;
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				if(board[x][y][0] == 0){
					k = count(x,y);
					if(k == 1){
						board[x][y][0] = possibleNumber(x,y);
						added = true;
					}	
					k = 0;
				}
			}
		}		
	}

	//public static int count(int x, int y)
	//args: the x- and y- position of the place to be checked
	//rets: the count of possible numbers 
	//purpose: counts the number of possible numbers for a given position(x,y)
	public static int count(int x, int y){
		int c = 0;
		for(int z = 1; z < 10; z++){
			if(board[x][y][z] == 1)c++;
		}
		return c;
	}

	//public static int possibleNumber(int x, int y)
	//args: x- and y- position of the place to be checked
	//rets: the first possible number
	//purpose: finds the first possible number for a given position(x,y)
	public static int possibleNumber(int x, int y){
		for(int z = 1; z < 10; z++){
			if(board[x][y][z] == 1)return z;
		}
		return 0;
	}


	//public static boolean inComplete()
	//args: none
	//rets: sudoku is not solved ---> true
	//		sudoku is solced     ---> false
	//purpose: checks if the sudoku has been solved or not 
	public static boolean inComplete(){
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				if(board[x][y][0] == 0)return true;
			}
		}
		return false;
	}

	//public static int[] boxCoordinates(int x)
	//args: the row/column number 
	//rets: an array containing the row/column values 
	//purpose: finds the positions of each place in a box
	public static int[] boxCoordinates(int x){
		int[] coordinates = new int[3];
		int k = x - (x%3);
		for(int y = 0; y < 3; y++){
			coordinates[y] = k + y;
		}
		return coordinates;
	}

	//public static void printBoard()
	//args: none
	//rets: none
	//purpose: prints the current state of the sudoku to the console
	public static void printBoard(){
		for(int y = 0; y < 9; y++){
			for(int x = 0; x < 9; x++){
				if(board[x][y][0] == 0)System.out.print("- ");
				else System.out.print(board[x][y][0] + " ");
			}
			System.out.println();
		}
		System.out.println("\n \n \n");
	}
}
