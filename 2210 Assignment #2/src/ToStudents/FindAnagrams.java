import java.io.*;
import java.util.Iterator;

public class FindAnagrams {
	
	public static void main(String[] args){
		
		StringComparator stringComp = new StringComparator();
		AVLTree<String,String> dictTree = new AVLTree<String,String>(stringComp);
		String location = System.getProperty("user.dir");		//find the location of the current directory.
		
		if(args.length!=2){
			System.out.println("Error: incorrect number of arguments entered.\nPlease recheck that a dictionary file along with an input file are specified in the arguments (respectively).");
			System.exit(-1);
		}
		
		String dictionaryFile = args[0]; //puts first argument into string for dictionary file's name.	
		String wordsFile = args[1];	//puts seconds argument into string for input file's name.	
	
		try{
			
			FileReader dictFileReader = new FileReader(location + "\\" + dictionaryFile); //create file readers for the dictionary file.
			BufferedReader dictBuffReader = new BufferedReader(dictFileReader);
			
			for(int i=0;;i++){
				
				try{
					
					String entry = dictBuffReader.readLine();	//adds all lines in dictionary file to dictTree until reaching a null line.
					char[] letterArray = entry.toCharArray();	//get a character array of the current word.
					
					AVLTree<String,Integer> tempTree = new AVLTree<String,Integer>(stringComp);
					
					for(int j=0; j<letterArray.length;j++){
						String charLetter = Character.toString(letterArray[j]);	//Insert a string of single character into tempTree to be sorted alphabetically.
						tempTree.insert(charLetter, new Integer(0));
					}
					
					Iterator<DictEntry<String,Integer>> iterString = tempTree.inOrder();	//tempTree.inOrder() returns an alphabetized version of the string in iterator form.
					String alphabetizedString = "";
					
					while(iterString.hasNext()){
						DictEntry<String,Integer> it = iterString.next();	//convert the iterator to a string.
						alphabetizedString += it.key();
					}
					
					dictTree.insert(alphabetizedString, entry);	//insert a node with the alphabetizedString as the key and the actual word as the entry.
				}
				
				catch(NullPointerException exception){	//exits for above loop when dictBuffReader.readLine() return null.
						break;
				}
		}
			
		FileReader wordsFileReader = new FileReader(location + "\\" + wordsFile);	//create file readers for the input file.
		BufferedReader wordsBuffReader = new BufferedReader(wordsFileReader);
		
		PrintWriter outputFile = new PrintWriter(location + "\\" + "output.txt"); //creates an object which will write an output file.
		
		for(int i=0;; i++){	//this segment is essentially the same as the one above until........
			
			try{
				
				String entry = wordsBuffReader.readLine();
				char[] letterArray = entry.toCharArray();
				
				AVLTree<String,Integer> tempTree = new AVLTree<String,Integer>(stringComp);
				
				for(int j=0; j<letterArray.length;j++){
					String charLetter = Character.toString(letterArray[j]);
					tempTree.insert(charLetter, new Integer(0));
				}
				
				Iterator<DictEntry<String,Integer>> iterString = tempTree.inOrder();
				String alphabetizedString = "";
				
				while(iterString.hasNext()){
					DictEntry<String,Integer> it = iterString.next();
					alphabetizedString += it.key();
				}
				
				Iterator<DictEntry<String,String>> iterator = dictTree.findAll(alphabetizedString);	//....here where an iterator of all instances of an anagram is found.
				
				while(iterator.hasNext()){
					DictEntry<String,String> dictEntry = iterator.next();
					if (dictEntry.value().compareTo(entry)!=0){
						outputFile.println(dictEntry.value());	//consequently, anagrams are written to the output file.	
					}
					
				}
				
				outputFile.println();	//have new line to distinguish between different words.
			}
			
			catch(NullPointerException exception){
				break;
			}
		}
		
		//closing all buffered readers
		outputFile.close();
		wordsBuffReader.close();
		dictBuffReader.close();
	
		}
		
		catch(IOException e){
			System.out.println("Error: input/output file exception.\nPlease ensure that both the dictionary file and the input file are in the same directory as this class, and that their names are correctly entered in the arguments.\nOtherwise, check that the current folder has write permissions in order to create the \"output.txt\" file.");
			System.exit(-1);
		}
	
	}
	
}