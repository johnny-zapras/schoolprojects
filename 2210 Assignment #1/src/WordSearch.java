import java.io.*;

public class WordSearch {
	public static void main(String[] args){
		
		
		//////////////////setting up all appropriate variables that don't need to be in the try block//////////////
		
		//the first four lines set up all dictionaries to be used in the program
		StringHashCode hC =  new StringHashCode();
		HashDictionary<String,Integer> Dict = new HashDictionary<String,Integer>(hC,(float)0.5);	//creates a dictionary to store all valid words
		HashDictionary<String,Integer> OutDict = new HashDictionary<String,Integer>(hC,(float)0.5);	//creates a dictionary to keep track of duplicate words found in puzzle.
		String location = System.getProperty("user.dir"); //finding the location of the current workspace (NOTE: assumes that both files are in the current directory)
		if (args.length!=2){ //checks to see if appropriate amount of arguments are entered
			System.out.println("Incorrect number of arguments entered. Please try the program again.");
			System.exit(0);
		}
		String puzzleFile = args[0];	//puts first argument into string for puzzle file's name.
		String dictionaryFile = args[1]; //puts second argument into string for dictionary file's name.
		
		
		///////////////////setting up dictionary of valid words////////////////////////////
		
		try{	//catch all errors related to external files
		FileReader dictionaryFileReader = new FileReader(location + "\\src\\" + dictionaryFile);
		BufferedReader dictionaryBufferReader = new BufferedReader(dictionaryFileReader);
		for(int i=0;;i++){
			try{
				String entry = dictionaryBufferReader.readLine();	//adds all lines in dictionary file to hashdictionary until reaching a null line.
				if(entry==null){break;}
				Dict.insert(new String(entry),new Integer(0));
				}
				catch(IOException exception){
					break;
				}
		}
		
		
		///////////////////setting up 2d character array from puzzle file////////////////////
		
		FileReader puzzleFileReader = new FileReader(location + "\\src\\" + puzzleFile);	//first file reader is used to determine the size of the character array while the second is actually used for its creation.
		BufferedReader bufferReader = new BufferedReader(puzzleFileReader);
		FileReader puzzleFileReader2 = new FileReader(location + "\\src\\" + puzzleFile);
		BufferedReader bufferReader2 = new BufferedReader(puzzleFileReader2);
		int lineCounter=0; //finds amount of rows in 2d character array
		int stringLength=0;	//finds amount of columns in 2d character array
		for (int i=0;;i++){
			try{
			String checker = bufferReader.readLine();
			if (checker==null){break;} //if null, all rows have been found
				if(i==0){	//checks only the first line for number of columns (assumes square or rectangular puzzle)
					stringLength = checker.length();
				}
				lineCounter++;
			}
			catch(IOException exception){
				break;
			}
		}
		
		char [][] puzzleArray = new char[stringLength][lineCounter];	//creation and insertion of all elements into 2d character array.
		for (int i=0; i<lineCounter; i++){
			String checker = bufferReader2.readLine();
			puzzleArray[i] = checker.toCharArray();
		}

		
		//////////////checking for duplicates and printing all valid words to text file///////////////////
		
		PrintWriter output = new PrintWriter(location + "\\src\\output.txt"); //creation of text file
		
		for(int i=0; i<stringLength; i++){	//accesses every character in array.
			for (int j=0; j<lineCounter; j++){
				
				String s = "";						//the string from array to be checked is initialized here
				for (int y=0; j+y<lineCounter; y++){//this particular section checks the inputed puzzle row by row in left-to-right fashion.
					s += puzzleArray[i][j+y];		//
					if(s.length()>3){				//making sure no strings of length 3 or under are checked
						if(Dict.find(s)!=null){		//checking to see if word is the string is valid in our dictionary
							try{					//try to insert the word into another dictionary. This ensures that no duplicate words are printed.
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}	//do nothing if string already exists within dictionary
						}
					}
				}
				
				s = "";								//setting a null string for a different operation
				for (int y=0; j-y>=0; y++){			//this particular section checks the inputed puzzle row by row in right-to-left fashion.
					s += puzzleArray[i][j-y];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}

				s = "";
				for (int x=0; i+x<stringLength; x++){	//this particular section checks the inputed puzzle column by column in a top-down fashion.
					s += puzzleArray[i+x][j];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
				s = "";
				for (int x=0; i-x>=0; x++){ //this particular section checks the inputed puzzle column by column in a bottom-up fashion.
					s += puzzleArray[i-x][j];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
				s = "";
				for (int y=0, x=0; i+x<lineCounter && j+y<stringLength; y++,x++){ //moves though the character array from top left to bottom right
					s += puzzleArray[i+x][j+y];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
				s = "";
				for (int y=0, x=0; i-x>=0 && j+y<stringLength; y++,x++){ //moves though the character array from bottom left to top right
					s += puzzleArray[i-x][j+y];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
				s = "";
				for (int y=0, x=0; i+x<lineCounter && j-y>=0; y++,x++){ //moves though the character array from top right to bottom left
					s += puzzleArray[i+x][j-y];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
				s = "";
				for (int y=0, x=0; i-x>=0 && j-y>=0; y++,x++){	//moves though the character array from bottom right to top left
					s += puzzleArray[i-x][j-y];
					if(s.length()>3){
						if(Dict.find(s)!=null){
							try{
								OutDict.insert(s, 0);
								output.println(s);
								}
							catch(DictionaryException e){;}
						}
					}
				}
				
			} //end of j loop
		} //end of i loop
		
		dictionaryBufferReader.close();	//closing all buffer readers.
		bufferReader.close();
		bufferReader2.close();
		output.close();	//closing our output text file to ensure the information added is not flushed.
	
	} //end of try block
	catch(IOException e){
			System.out.println("Either puzzle or dictionary file was unable to open. Please recheck arguments and ensure that files are in current working directory. If neither of these work, ensure that the current directory has writing permissions.");
	}
		
	catch(ArrayIndexOutOfBoundsException e){
		System.out.println("The program has gone out of bounds. Please check that dictionary and puzzle files are entered into their appropriate command spaces.");
	}
		
   }//end of test harness
	
}//end of file
