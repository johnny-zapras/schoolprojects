
public class Entry<K,V> {

	//member variables
	private K key;
	private V value;
	
	public Entry(K k, V v){	//constructor for entry type
		this.key = k;
		this.value = v;
	}
	
	public K Key(){	//accessor method for key
		return key;
	}
	
	public V Value(){	//accessor method for value	
		return value;
	}
	
	public void modifyValue(V v){	//modifier method for value
		this.value = v;
	}
}
