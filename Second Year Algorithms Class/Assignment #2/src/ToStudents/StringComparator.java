package ToStudents;
public class StringComparator implements Comparator<String> {
	
	public StringComparator(){}					//default constructor for string comparator (doesn't initialize any variables)
	
	public int compare(String a, String b){		//uses String's default compareTo function to compare two strings.
		
		if (a.compareTo(b)==0){					//if the first string entered is identical to the second, zero is returned.
			return 0;
		}
		
		else if (a.compareTo(b)>0){				//if the two strings are arranged lexicographically and the first string succeeds the second, one is returned.
			return 1;
		}
		
		else{									//if the two strings are arranged lexicographically and the second string succeeds the first, negative one is returned.
			return -1; 				
		}
		
	}
	
}