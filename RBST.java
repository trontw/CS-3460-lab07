import java.util.Random;

import javax.lang.model.util.ElementScanner6;

import org.omg.IOP.TAG_ORB_TYPE;

public class RBST {
	
	private Node root;		// Head node of the tree.
	private Random rand;	// A random object - required to randomly insert nodes into the tree.
	
	// Constructors
	public RBST() { 
		root = null;
		rand = new Random();
	}
	public RBST(Node _root) {
		root = _root;
		rand = new Random();
	}
	//Requests [] reqs = new Requests[n];
	/**
		Wraper print method to print the contents of the tree. Calls the private print method.
	 */
	public void print() {
		print(root);
		System.out.println();
	}
	/**
		Print method to print the contents of the tree.
	*/
	private void print(Node T) {
		// An inorder traversal to print the sequence.
			if (T == null) {
				return;
			} else {
				print(T.getLeft());
				System.out.print(T.getTeam() + " ");
				print(T.getRight());
			}
	}

	/**
		Wrapper for insertNormal method.
	*/
	public void insertNormal(int team, int rank) {
		root = insertNormal(root, team, rank);
	}
	/**
		Insert the data team at position rank into node T. 
		This is the normal insert routine without any balancing.
	*/
	int r = 0;
	private Node insertNormal(Node T, int team, int rank) {
		// Base cases here. Inserting into a null tree.
		// RBST tree = new RBST();
		assert (rank >= 1 && rank <= T.getSize() + 1) : "rank should be between 1 and size of the tree <" + (T.getSize()+1) + ">";
		// Create root node (tree.root == null increments rank, 
		// T == null only increments rank by 1???)
		if (T == null) {
			//System.out.println("Rank is "+rank);
			//System.out.println("Adding "+team+" to the Tree");
			return new Node(team);
		}
		// Determine the rank of the root by using the left node size
		if (T.getLeft() != null) 
			r =T.getLeft().getSize() + 1;
		 else if (T.getLeft() == null) 
			r = 1;
		
		// Recursive case. Recursively insert into left tree if rank <= rank of root. Otherwise insert into right tree.
		if (rank <= r) {			
			T.setLeft(insertNormal(T.getLeft(), team, rank));
			//System.out.println("Adding "+team+" to the Left Tree, and Rank = "+rank);
			T.incSize();
		} else {
			T.setRight(insertNormal(T.getRight(), team, rank - r));
			//System.out.println("Adding "+team+" to the Right Tree");
			T.incSize();
		}
		return T;	// Need to return the actual tree. 
	}
	
	/**
		Split the tree at position rank. It returns RET, a RBST 
		array of length two. RET[0] is the left side of the split,
		and RET[1] is the right side of the split. This is a 
		wrapper method that calls the private split method.
	*/
	public RBST[] split(int rank) {
		Node [] ret = split(root, rank);
		RBST [] RET = {null, null};
		RET[0] = new RBST(ret[0]);
		RET[1] = new RBST(ret[1]);
		return RET;
	}
	/**
		The private split method that splits tree T at position rank. 
		It returns an array ret, of two nodes -- ret[0] is the root of the left tree, and
		ret[1] is the root of the right tree of the split.
	*/
	private Node[] split(Node T, int rank) {
		// ret[0] is the root node to the left side of the split, ret[1] is the right side.	
		Node [] ret = {null, null};	
		// Determine the rank of the root by using the left node size
		if (T == null)
			r = 0;
		else if (T.getLeft() != null) 
			r =T.getLeft().getSize() + 1;
		 else if (T.getLeft() == null) 
			r = 1;
		// rank of root = rank
		Node [] R1 = ret;
		Node [] L1 = ret;
		Node [] ret_ = ret;
		if (T == null) {
			ret[0] = ret[1] = null;
		}
		else if (rank == r) {
			ret[1] = T.getRight();
			ret[0] = T;//L = T (from the slides)
			T.setRight(null);
			//ret[0].setRight(null);
			if (ret[0] != null)
				ret[0].updateSize();
			if (ret[1] != null)
				ret[1].updateSize();
			return ret;
		// rank < rank of the root
		} else if (rank < r) {
			ret_ = split(T.getLeft(), rank);//just the L (<= k) node (which is now ret_[0])
			//ret[0] = ret_[0];
			ret[0] = ret_[0];
			ret[1] = T;// R = T (from the slides), connecting ret[1] back to T
			T.setLeft(ret_[1]);//connect ret_[1] back to the original T
			//if (ret[0] != null)
			//	ret[0].updateSize();
			if (ret[1] != null)
				ret[1].updateSize();
		}
		// rank > rank of the root
		if (rank > r){
			ret_ = split(T.getRight(), rank -r);
			//ret[1] = ret_[1];
			ret[1] = ret_[1];
			ret[0] = T;// L = T, connecting ret[0] back to T
			T.setRight(ret_[0]);
			//System.out.println("We found two children (RANK > r)");
			if (ret[0] != null)
				ret[0].updateSize();
			//if (ret[1] != null)
			//	ret[1].updateSize();
			} 
		return ret;
	}
	
	/**
		Insert the data team at position rank in the tree. 
		This is a wrapper method that calls the private insert method.
	*/
	public void insert(int team, int rank) {
		root = insert(root, team, rank);
	}
	/**
		The private insert method, that inserts the data team at position 
		rank in the tree rooted at node T. team is inserted at the root 
		with probability 1/(T.getSize()+1). This is done by splitting the tree T
		at position rank-1, creating a new node for team, and attaching 
		the left and right sides of the split as the two subtrees of the new node. 
		Otherwise, with probability 1 - 1/(T.getSize()+1), insert recursively
		at either the left tree (rank <= rank of root) or at the right tree 
		(rank > rank of root).
	*/
	private Node insert(Node T, int team, int rank) {
		// ret[0] is the root node to the left side of the split, ret[1] is the right side.	
		Node [] ret = {null, null};	
		double pb = rand.nextDouble();
		// rank of root = rank
		Node [] R1 = ret;
		Node [] L1 = ret;
		Node [] ret_ = ret;
		assert (rank >= 1 && rank <= T.getSize() + 1) : "rank should be between 1 and size of the tree <" + (T.getSize()+1) + ">";
		/** Base case here.// Inserting into a null tree.//
		 * First, we want to split based on the median n/2, then
		 * use getSize of RBST to compare the left and right 
		 * sub-trees. The larger of the two will become the root 
		 * of the tree.
		 *----------------------------------------------------
		 * Below is the initial root node if none exists
		 *----------------------------------------------------
		 */
		if (T == null) {
			//System.out.println("Rank is "+rank);
			//System.out.println("Adding "+team+" to the Tree");
			return new Node(team);
		}
		
		// Determine the rank of the root by using the left node size
		if (T == null)
			r = 0;
		else if (T.getLeft() != null) 
			r =T.getLeft().getSize() + 1;
		else if (T.getLeft() == null) 
			r = 1;
		
		/** Recursive case. 
		 * With probability 1 / (T.getSize() + 1), the new node 
		 * becomes the root. Otherwise recursively insert into left or right subtrees 
		 * depending upon the rank.
		**/
		if (pb < 1.0 / (T.getSize() + 1)){
			//Split the node, making the rank equal to the left node
			ret = split(T, (rank - 1));
			//Create the new node, setting both the left and right nodes
			T = new Node(team, ret[0], ret[1]);
			return T;
			//Recursively insert into the left or right subtree depending on the rank
		} else if ( rank <= r) {
			T.setLeft(insert(T.getLeft(), team, rank));
			T.incSize();
		} else {
			T.setRight(insert(T.getRight(), team, rank - r));
			T.incSize();
		}
		return T;	// Need to return the actual tree. 
	}

	/**
		Return the node at position rank in the tree. 
		This is a wrapper method that calls the private select method.
	*/	
	public Node select(int rank) {
		return select(root, rank);
	}
	/**
		The select method that returns the node in the tree at position rank. 
	*/
	private Node select(Node T, int rank) {
		// Base case. Return null if the tree is empty.

		assert (rank >= 1 && rank <= T.getSize()) : "rank should be between 1 and size of the tree <" + T.getSize() + "> ";
		
		/**Recursive case. 
		 * Return T if rank is equal to the rank of the root. 
		 * Else, revursively select in either the left tree 
		 * (rank < rank of root) or the right tree (rank > rank of the root).
		**/ 
		// Determine the rank of the root by using the left node size
		if (T == null)
			r = 0;
		else if (T.getLeft() == null) 
			r = 1;
		else //(T.getLeft() != null) 
			r =T.getLeft().getSize() + 1;

		//Compare rank to root's rank: Return T if equal
		if (rank == r)
			return T;
		else if (rank < r)
			return select(T.getLeft(), rank);
		else if (rank > r)
			return select(T.getRight(), rank - r);
		return T;
	}

	/**
		Returns the size of the tree.
	*/
	public int getSize() {
		if (root == null) return 0;
		return root.getSize();
	}
}
