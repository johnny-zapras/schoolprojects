package ToStudents;
public class DictEntry<K,V> {
	
	//member variables
	private K key;
	private V value;
	
	//member functions
	public DictEntry(K key, V value){	//constructor
		this.key = key;
		this.value = value;
	}
	
	//accessor methods
	public K key(){			//returns the key of the entry
		return this.key;
	}
	
	public V value(){		//returns the value of the entry
		return this.value;
	}
	
	//modifier method
	public void changeValue(V newValue){		//modifies the value of the entry
		this.value = newValue;
	}
	
}
