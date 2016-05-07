import java.io.*;

public class TestWordOpener {
public static void main(String[] args){
	try{
	FileReader puzzleFileReader = new FileReader("C:\\Users\\Johnny\\workspace\\2210 Assignment #1\\src\\puzzleSmall.txt");
	FileReader puzzleFileReader2 = new FileReader("C:\\Users\\Johnny\\workspace\\2210 Assignment #1\\src\\puzzleSmall.txt");
	BufferedReader bufferReader = new BufferedReader(puzzleFileReader);
	BufferedReader bufferReader2 = new BufferedReader(puzzleFileReader2);
	int lineCounter=0;
	int stringLength=0;
	for (int i=0;;i++){
		try{
		String checker = bufferReader.readLine();
		if (checker==null){break;}
			if(i==0){
				stringLength = checker.length()-1;	//accounts for null spacing
				System.out.println(stringLength);
			}
			lineCounter++;
		}
		catch(IOException exception){
			break;
		}
	}
	char [][] puzzleArray = new char[stringLength][lineCounter];
	for (int i=0; i<lineCounter; i++){
		String checker = bufferReader2.readLine();
		checker = checker.substring(0,checker.length()-1);
		puzzleArray[i] = checker.toCharArray();
	}
	System.out.println(puzzleArray[4][4]);
	}
	catch(IOException e){
		System.out.println("WHAT THE FUCK?");
	}
}
}
