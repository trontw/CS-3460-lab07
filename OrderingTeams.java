import javax.lang.model.util.ElementScanner6;

public class OrderingTeams {
	
	// Returns true if team x defeated team y.
	public static boolean didXBeatY(int x, int y) {
		assert (x != y) : "Teams x and y need to be different.";
		if (x > y) return !didXBeatY(y, x);
		long lx = (long) x;
		long ly = (long) y;
		return (((17 + 8321813 * lx + 1861 * ly) % 1299827) % 2) == 0 ? true : false;
	}
	
	// Return a RBST encoding a valid ordering.
	public static RBST orderTeams(int N) {
		RBST O = new RBST();
		
		O.insert(0, 1);
		
		for (int i = 1; i < N; i++) {	
			if (didXBeatY(i, O.select(1).getTeam())) {
				O.insert(i, 1);
			}
			else if (!didXBeatY(i, O.select(O.getSize()).getTeam())) {
				O.insert(i, O.getSize()+1);
			}
			else {
				//Binary Search the sequence to find the valid position to insert team i.
					int start = 1;
					int end = O.getSize();
					System.out.println("k is = "+end);
					while (start < end - 1){
						int mid = ((start + end)/ 2);
						//if ((!didXBeatY(i, O.select(mid -1).getTeam())) && ((didXBeatY(i, O.select(mid).getTeam())))){
						//if ((!didXBeatY(i, O.select(mid -1).getTeam()))) {
						/* 
						if (start == end){
							System.out.println("Found HAPPY PLACE");
							end = start;
							return O;
							//moves before mid, start going <-- direction
						 } 
						 else if (start == (end - 1)){
						 //if ((didXBeatY(i, O.select(mid).getTeam()))){
							System.out.println(i+" beat "+mid+" <---");
							//start  = end;
							end = start;
							//return O;
							//move after mid, start going ---> direction	
						}
						*/
						if (didXBeatY(i, O.select(mid).getTeam())){
							System.out.println(i+" lost to "+mid+" --->");
							//start = mid;
							end = mid;
						} else {
							//end = mid;
							start = mid;
						}
					}
					System.out.println("inserting " + i + " at position " + end);
					//if (O.select(i) != null || O.select(end) != null)
						O.insert(i, end);
				}
			}
			return O;
		}
		//return O;
	

	public static void main(String [] args) {
		//int N = 100000;
		int N = 100;
		RBST ordering = orderTeams(N);
		if (ordering == null || ordering.getSize() != N) {
			System.out.println("Size of Tree returned by orderTeam(N) is wrong.");
		}
		else {
			for (int i = 1; i <= ordering.getSize() - 1; i++) {
				Node x = ordering.select(i);
				Node y = ordering.select(i+1);
				if (!didXBeatY(x.getTeam(), y.getTeam()))	{
					System.out.println("Invalid sequence: team " + x.getTeam() + " in position " + i + " lost to team " + y.getTeam() + " at position " + (i+1) + ".");
					System.exit(0);	
				}
			}
		}
		System.out.println("Yay! Tree contains a valid ordering.");
		ordering.print(); //You can print the ordering. But realize that N = 100000, so the ordering will fill your entire screen. Try to set N to something more manageable, like N = 100 and then print it out.
	}
}
