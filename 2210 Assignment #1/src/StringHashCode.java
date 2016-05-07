public class StringHashCode implements HashCode<String>{
	
	public int giveCode(String b){
		
		int hashNumber = 0;
		for (int i=0; i<b.length(); i++){	//takes each character, finds its ascii value and applies polynomial accumulation technique to find hashcode.
			char temp = b.charAt(i);
			int asciiTemp = (int) temp;		//casting from character to string.
			int powerOf37 = 1;
			for (int j=1;j<=i;j++){	//loop is never entered if the first character is being looked at.
				powerOf37 *= 37;
			}
			hashNumber += asciiTemp*(powerOf37);
		}//end of for loop
		return hashNumber;	//returns the object's hashNumber.
	}
	
}
