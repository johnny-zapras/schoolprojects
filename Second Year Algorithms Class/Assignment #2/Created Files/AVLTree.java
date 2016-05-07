import java.util.LinkedList;
import java.util.Iterator;

public class AVLTree<K,V> implements AVLTreeInterface<K,V> {

	//////////////member variables///////////////////////////////
	private AVLnode<K,V> root;
	private Comparator<K> treeComparator;

	/////////////constructor for AVLTree object//////////////////
	public AVLTree(Comparator<K> inputComparator){
		this.treeComparator = inputComparator;
		this.root = new AVLnode<K,V>(null, null, null, null);	//creates an "empty" tree wherein the root node is a leaf node.
	}

	/////////////member functions////////////////////////////////
	
	////accessor methods
	
	public Position<K,V> giveRoot(){		//returns a position object of the root
		
		return (Position<K,V>)root;	
		
	}
	
	
	public boolean external(Position<K,V> p){		//returns a position object of the root
		
		AVLnode<K,V> e = (AVLnode<K,V>)p;
		return (e.left()==null && e.right()==null);
		
	}
	
	
	public Position<K,V> left(Position<K,V> p){		//returns a the left node (as a position objection) of the current position object
		
		AVLnode<K,V> e=(AVLnode<K,V>)p;
		e = e.left();
		return (Position<K,V>)e;
		
	}
	
	
	public Position <K,V> right(Position<K,V> p){	//returns a the left node (as a position objection) of the current position object
		
		AVLnode<K,V> e=(AVLnode<K,V>)p;
		e = e.right();
		return (Position<K,V>)e;
		
	}
	
	
	////////////other member functions/////////////////////
	
	//////public methods/////////////
	
	public DictEntry<K,V> find(K key){	//finds once instance of the node containing the entered key within the AVLTree.
		
		AVLnode<K,V> found = treeSearch(key, this.root);
		
		if (found.left()==null && found.right()==null){	//if treeSearch returned an external node, return null.
			return null;
		}
		
		return found.getEntry();						//else, return the node itself.
		
	}
	
	
	public Iterator<DictEntry<K,V>> findAll(K key){	//finds all instances of the node containing the entered key within the AVLTree.
		
		AVLnode<K,V> found = treeSearch(key, this.root);
		
		if (found.left()==null && found.right()==null){
			return null;
		}
		
		LinkedList<DictEntry<K,V>> returnList = new LinkedList<DictEntry<K,V>>();
		inOrderTraversalAltered(found, returnList, key);
		
		return returnList.listIterator();	//return an iterator of the list of entries.
	}
	
	
	public void insert(K key,V value){
		
		AVLnode<K,V> insert = treeInsert(this.root,key,value);	//insert a node with the entered key and value.
		
		while (insert!=null){	//keeps rebalancing the tree until the node to reconfigure is the parent of the root.
			insert.resetHeight();	//get the new height of insert.
			
			if (Math.abs(insert.left().getHeight()-insert.right().getHeight())>1){
				insert = triNodeRestructure(tallerChild(tallerChild(insert)), tallerChild(insert), insert);	//restructure the subtree with root node insert.
				insert.left().resetHeight();
				insert.right().resetHeight();
				insert.resetHeight();
				break;
			}
			
			insert = insert.parent();	//make insert its own parent.
		}
		
	}
	
	
	public DictEntry<K,V> remove(K key) throws AVLTreeException{
		
		AVLnode<K,V> removedNode = treeRemove(key);
		
		if (removedNode==null){		//returns null if treeRemove is unsuccessful.
			return null;
		}
		
		AVLnode<K,V> removedParent = removedNode.parent();
		
		while (removedParent!=null){	//see insert for description of this section's functionality.
			removedParent.resetHeight();
			
			if (Math.abs(removedParent.left().getHeight()-removedParent.right().getHeight())>1){
				removedParent = triNodeRestructure(tallerChild(tallerChild(removedParent)), tallerChild(removedParent), removedParent);
				removedParent.left().resetHeight();
				removedParent.right().resetHeight();
				removedParent.resetHeight();
				break;
			}
			
			removedParent = removedParent.parent();
		}
		
		return removedNode.getEntry();
	
	}
	
	
	public boolean isEmpty(){
		
		return (root.left()==null && root.right()==null);	//checks to see if root is an external node.
	
	}
	
	
	public int treeHeight(){
		
		return(this.root.getHeight());	//returns the height of the tree.
	
	}
	
	
	@SuppressWarnings("unused")
	public void modifyValue(K key,V newValue) throws AVLTreeException{
		
		DictEntry<K,V> entry = new DictEntry<K,V>(key,newValue);
		entry = find(key);
		
		if(entry==null){	//if find return nothing, then the desired node is not within the tree.
			throw new AVLTreeException("Error: entry not found.");
		}
		
		entry.changeValue(newValue);	//change the value of the found node to have the new value.
	}
	
	
	public int size(){
		
		return entryCount(this.root);
	
	}
	
	
	public Iterator<DictEntry<K,V>> inOrder(){
		
		LinkedList<DictEntry<K,V>> returnList = new LinkedList<DictEntry<K,V>>();
		inOrderTraversal(this.root, returnList);
		
		return returnList.listIterator();	//return an iterator of the linked list of DictEntry objects.
		
	}
	
	
	/////////private (helper) methods////////////
	
	private int entryCount(AVLnode<K,V> node){	//helper method for size(): traverses each node once, increasing the count every time an internal node is found.
		
		if (node.left()==null && node.right()==null){	//return 0 when you reach an external node (as its not considered an entry).
			return 0;
		}
		
		else{
			return entryCount(node.left())+ entryCount(node.right())+1;
		}
		
	}
	
	
	private void inOrderTraversal(AVLnode<K,V> node, LinkedList<DictEntry<K,V>> linkedList){	//helper method for inOrder(): traverses each node once in inorder fashion and adds their entry objects into the passed iterator.
		
		if (node.left()==null &&  node.right()==null){	//return null when you reach an external node.
			return;
		}
		
		inOrderTraversal(node.left(), linkedList);
		linkedList.add(node.getEntry());
		inOrderTraversal(node.right(), linkedList);
		
	}
	
	
	private void inOrderTraversalAltered(AVLnode<K,V> node, LinkedList<DictEntry<K,V>> linkedList, K key){	//helper method for findAll(): traverses each node once in inorder fashion and adds their entry objects into the passed iterator only if their key matches the key which is passed.
		
		if (node.left()==null &&  node.right()==null){
			return;
		}
		
		inOrderTraversalAltered(node.left(), linkedList,key);
		if(treeComparator.compare(node.getEntry().key(), key)==0) {
			linkedList.add(node.getEntry());
		}
		inOrderTraversalAltered(node.right(), linkedList,key);
		
	}
	
	
	private AVLnode<K,V> treeSearch(K key, AVLnode<K,V> checkNode){	//helper method for all find, insert, and remove methods. Finds a node and returns an internal node if found, and an external node if it isn't.
		
		if (checkNode.left()==null && checkNode.right()==null){	//returns a leaf node if the node cannot be found within the tree.
			return checkNode;
		}
		
		if ((treeComparator.compare(checkNode.getEntry().key(), key))>0){	//checks the left subtree of the current node if, lexicographically speaking, the node's entry's key is greater than the key.
			return treeSearch(key, checkNode.left());
		}
		
		else if ((treeComparator.compare(checkNode.getEntry().key(), key)<0)){	//checks the right subtree of the current node if, lexicographically speaking, the node's entry's key is smaller than the key.
			return treeSearch(key, checkNode.right());
		}
		
		return checkNode;	//returns actual found node if the two keys are equal.
		
	}
	
	
	private AVLnode<K,V> triNodeRestructure(AVLnode<K,V> x, AVLnode<K,V> y, AVLnode<K,V> z){	//helper method for both insert and remove: rearranges the tree to maintain height-balance property.
		
		AVLnode<K,V> a = null;	//initializing all nodes.
		AVLnode<K,V> b = null;
		AVLnode<K,V> c = null;
		
		if (z.right()==y && y.left()==x){
			a = z; 
			b = x; 
			c = y;
		}
		
		if (z.right()==y && y.right()==x){
			a = z; 
			b = y; 
			c = x;
		}
		
		if (z.left()==y && y.left()==x){
			a = x; 
			b = y; 
			c = z;
		}
		
		if(z.left()==y && y.right()==x){
			a = y; 
			b = x; 
			c = z;
		}

		if (z==this.root){	//case where z is equal to the root (last triNodeRestructure)
				this.root = b; 				
				b.setParent(null);
		}
		
		else{ 			//connect parent of z to the node which will replace z
			
			if (z.parent().left()== z) {
				makeLeftChild(z.parent(), b);
			}
			
			else {
				makeRightChild(z.parent(), b);
			}
		}
		
		if (b.left()!=x && b.left()!=y){ 	//case that the left node is an orphan
			makeRightChild(a, b.left());
		}
		
		if (b.right()!=x && b.right()!=y){	//case that the right node is an orphan
			makeLeftChild(c, b.right());
		}
		
		makeLeftChild(b,a);
		makeRightChild(b,c);
		
		return b;
		
	}

	
	private void makeLeftChild(AVLnode<K,V> first, AVLnode<K,V> second){	//helper method for triNodeRestructure: makes the first node become the second's left child.
		
		first.setLeft(second);
		second.setParent(first);
		
	}
	
	
	private void makeRightChild(AVLnode<K,V> first, AVLnode<K,V> second){	//helper method for triNodeRestructure: makes the first node become the second's right child.
		
		first.setRight(second);
		second.setParent(first);
		
	}
	
	
	private AVLnode<K,V> tallerChild(AVLnode<K,V> node){	//helper method for insert and remove methods: determines which child has a larger height.
		
		if(node.right().getHeight()>=node.left().getHeight()){	//if the height of the right node is larger than the left, return it.
			return node.right();
		}
		
		else {
			return node.left();
		}
	}
	
	
	private AVLnode<K,V> treeInsert(AVLnode<K,V> node, K key, V value){	//helper method for insert(): actually inserts a node with the passed values. 
		
		AVLnode<K,V> nodeAdded = treeSearch(key, node);
		
		if (nodeAdded.left()!=null || nodeAdded.right()!=null){	//if the node specified is an internal node(meaning it is already in the tree), insert on the left subtree.
			return treeInsert(nodeAdded.left(), key, value);
		}
		
		nodeAdded.setEntry(new DictEntry<K,V>(key,value));
		
		AVLnode<K,V> leftChild = new AVLnode<K,V>(null,nodeAdded,null,null);	//create and link leaf nodes for the inserted node.
		AVLnode<K,V> rightChild = new AVLnode<K,V>(null,nodeAdded,null,null);
		nodeAdded.setLeft(leftChild);
		nodeAdded.setRight(rightChild);
		
		return nodeAdded;
	} 
	

	private AVLnode<K,V> treeRemove(K key){			//helper method for remove(): removes the key passed if found within the tree. 
		
		AVLnode<K,V> removeNode = treeSearch(key, this.root);		//check for returned node.
		
		if (removeNode.left()==null && removeNode.right()==null){ 	//case such that the node found is an external node: return null as the key is not found. 
			return null;
		}
		
		if (removeNode.left()!=null && removeNode.right()!=null){	//case such that the node found is an internal node whose children are both internal nodes.
			AVLnode<K,V> temp = removeNode;
			removeNode = removeNode.right();						//find the leftmost node in the right subtree of the entry				
			while (removeNode.left()!=null){
				removeNode = removeNode.left();						
			}
			temp.setEntry(removeNode.parent().getEntry());			//copy the contents of the parent of leftmost node (the leftmost internal node) in the right subtree  to the internal node's position--node to be removed is now the leaf node.
		}
		
		else{	//case such that one child is an orphan.
			
			if(removeNode.left().left()==null & removeNode.left().right()==null){	//if the left node is the orphan.
				removeNode = removeNode.left();	//removeNode now becomes the leaf node.
			}
			
			else{
				removeNode = removeNode.left();
			}
			
		}
		
		AVLnode<K,V> parentNode = removeNode.parent();	
		AVLnode<K,V> nodeToRelink;						
		
		if (parentNode.left()==removeNode){				
			nodeToRelink = parentNode.right();
		}
		
		else{
			nodeToRelink = parentNode.left();
			}
		
		if (parentNode==this.root){
			this.root = nodeToRelink;
			this.root.setParent(null);
		}
		
		else{
			
			if (parentNode==(parentNode.parent().left())){
				parentNode.parent().setLeft(nodeToRelink);
			}
			
			else {
				parentNode.parent().setRight(nodeToRelink);
				}
			
			nodeToRelink.setParent(parentNode.parent());
		
		}
		
		return nodeToRelink;
	}
	
}