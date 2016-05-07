import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;

public class HashDictionary<K,V> implements Dictionary<K,V> 
{
	
	/////////////////////member variables////////////////////////////////////
	
    private HashCode<K> hCode;	
    private Entry<K,V> Avaliable;
    private float maxLoadFactor;
    private int numOps, numProbes;
    private Entry<K,V>[] hashTable;		
    private int a, b=-1;	//a and b for secondary hash function (the reason for b=-1 is explained below)
    private int numOfEntries = 0;	//stores the number of occupied space within Hash Table.
    private int prevPrime=5, currentPrime=11;	//used for q for secondary hash function and Hash Table length, respectively
    
    
    /////////////////////constructor////////////////////////////
    
    public HashDictionary(HashCode<K> inputCode, float maxLFactor){
         hCode = inputCode;         // input hash code
         maxLoadFactor = maxLFactor;	//setting load factor
         hashTable = (Entry<K,V>[]) new Entry[currentPrime]; 
         Avaliable = new Entry("Generic Key", "Generic Value"); //instantiating Available marker
         Random randomFunction = new Random(System.currentTimeMillis());	//setting a and b variables
         a = randomFunction.nextInt();
         while(a<=0 || (a%(hashTable.length))==0){
        	 a = randomFunction.nextInt();
         }
         while(b<0){	//b is set to negative 1 to enter this loop
        	 b = randomFunction.nextInt();
         }
        }
    
    
    /////////////////////////////member functions///////////////////////////
    
    
    ///////Insert, Find, and Remove for HashDictionary/////////
    
    public void insert(K key, V value) throws DictionaryException{
    	//rehashing function
    	if (((float)(numOfEntries+1)/(float)hashTable.length)>maxLoadFactor)	//works even if insert function is unsuccessful
    	{
    		prevPrime = currentPrime;	//setting prevPrime to previous hashTable length
    		Entry<K,V>[] tempTable = hashTable;		//renaming hashTable to tempTable
    		currentPrime *= 2;	//sets currentPrime to double its value so that the next prime used is substantially larger  
    		this.getNextPrime();
    		hashTable = (Entry<K,V>[]) new Entry[currentPrime];	//instantiating new hashTable under hashTable name
    		Random randomFunction = new Random(System.currentTimeMillis());	//finding new a and b values
    		b=-1;
    		a = randomFunction.nextInt();
    		while(a<=0 || (a%(hashTable.length))==0){
           	 a = randomFunction.nextInt();
            }
            while(b<0){
           	 b = randomFunction.nextInt();
            }
           for (int j=0; j<hashTable.length; j++){		//clears the hashTable of any garbage(precautionary measure).
            	hashTable[j]=null;	
           }
            numOfEntries=0;	//sets number of entries to appropriate values
            for (int j=0; j<tempTable.length; j++) //finds all valid entries in old hashTable and inserts them into the new one.
    		{
    			if(tempTable[j]!=null && tempTable[j].Key()!=Avaliable) 
    			{
    				this.insert(tempTable[j].Key(), tempTable[j].Value());
    			}
    		}
    	}
    	////altered find function(almost the opposite of normal find function except it returns position instead of actual entry)
    	int h = Math.abs((a*hCode.giveCode(key)+b))%hashTable.length;	//abs ensures that h is positive (consequently ensuring the position selected is valid)
    	int h2 = prevPrime - (a*hCode.giveCode(key)+b)%prevPrime;	
    	int p = 0;
    	int pos = 0;
    	while(p<hashTable.length){
    		numProbes++;	//increases the number of probes
    		pos = (h + p*h2)%hashTable.length;	//gives a valid position in hashTable
    		Entry<K,V> checker;	//pulls the corresponding entries values and puts it into checker variable
    		checker = hashTable[pos];		
    		if(checker==null || checker==Avaliable){	//finds a position in which we can insert a new element
    			break;
    		}
    		if(checker.Key().equals(key)){	//throws an exception if element entered is already in hashTable.
    			numOps++;
    			throw new DictionaryException("InsertException");
    		}
    		p++;	//checks next position
    	}
    	Entry<K,V> insertedObject = new Entry<K,V>(key,value);	//creates the item to be inserted.
    	hashTable[pos] = insertedObject;	//adds the item to the appropriate index
    	numOps++;	//increases the number of operations	
    	numOfEntries++;	//increases the number of occupied spaces
    	
    }//end of insert function
    
    
    // Returns true if there is  an entry with specified key. Returns null otherwise     
    public Entry<K,V> find(K key){
    	int h = Math.abs((a*hCode.giveCode(key)+b))%hashTable.length;
    	int h2 = prevPrime - (a*hCode.giveCode(key)+b)%prevPrime;
    	int p = 0;
    	while(p<hashTable.length){
    		numProbes++;
    		int i = (h + p*h2)%hashTable.length;
    		Entry<K,V> checker;
    		checker = hashTable[i];		
    			if(checker==null){	//checks to ensure that space being checked is not null (if it is the entry is not in the hashTable)
    				numOps++;
    				return null;
    				}
    			if(checker!=Avaliable){	//ignores entries with Available maker
    				if (checker.Key().equals(key)){
    					numOps++;
    					return checker;
    					}
    				}
    			p++;
    		}
    	numOps++;
    	return null;
    }

    
    public void remove(K key) throws DictionaryException{
    //altered find function (identical to find function except it gives the position instead of the object. 
    	int h = Math.abs((a*hCode.giveCode(key)+b))%hashTable.length;
    	int h2 = prevPrime - (a*hCode.giveCode(key)+b)%prevPrime;
    	int p = 0;
    	int pos = 0;
    	while(p<hashTable.length){
    		numProbes++;
    		pos = (h + p*h2)%hashTable.length;
    		Entry<K,V> checker;
    		checker = hashTable[pos];		
    		if(checker==null){
    			numOps++;
    			throw new DictionaryException("RemoveException");
    		}
    		if(checker!=Avaliable){
    			if (checker.Key().equals(key)){
    				break;
    			}
    		}
    		p++;
    	}
    	hashTable[pos] = Avaliable; //change the found element to Available to indicate index was previous occupied
    	numOps++;
    	numOfEntries--;	//decreases the number of valid entries in array
    }
    
    /////Miscellaneous HashDictionary functions//////
    
    //Iterator method loops through hashTable to find valid entries and adds them to a linkedlist whose iterator is returned to the user.
    Iterator <Entry<K,V>> elements(){
    	LinkedList<Entry<K,V>> returner = new LinkedList<Entry<K,V>>();
    	for (int i=0; i<hashTable.length; i++){
    		if(this.hashTable[i]!=null && this.hashTable[i]!=Avaliable){
    		returner.add(this.hashTable[i]);
    		}
    	}
    	return returner.listIterator();
    }
    
    //returns the number of valid entries within hashTable
    public int size(){
    	return numOfEntries;
    }  
    
    //returns the average number of probes in float format
    public float averNumProbes(){
    	return ((float) numProbes)/numOps; 
    }
    
    //changes currentPrime to the closest greater prime
    private void getNextPrime(){
    	int tempNumber = currentPrime + 1; //checking next number
    	int i = 2;						   //start with first possible divisor
    	while (i<tempNumber-1){			   //exit checker loop when next prime number is found
    		if (tempNumber%i==0){		   //if tempNumber is divided then it is obviously not a prime number, thus we start checking again for divisibility with a greater number.
    			tempNumber++;
    			i=2;
    		}
    		else{
    			i++;					   //incrementing the divisor
    		}
    	}
    	currentPrime = tempNumber;		   //set currentPrime to checked number
    }
    
    
} //end of file