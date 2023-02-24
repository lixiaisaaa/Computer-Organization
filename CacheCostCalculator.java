import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

/**
 * This class is a cache simulator -- it allows a user to select a cache architecture and
 * input a list of memory addresses.  The simulator will simulate accessing the memory
 * addresses twice.  The first pass fills the cache.  The second pass through the memory
 * addresses will have hits and misses within the cache, and the program will keep track
 * of these hits and misses.  Results will be output to the user.
 * 
 * See CS 3810, Assignment #8 for more details.
 * 
 * @author Peter Jensen (starting code)
 * @author Robert Li (implementing code)
 * @version Fall 2021
 */
public class CacheCostCalculator
{
	// Students are welcome to add constants, fields, or helper methods to their class.  If you want additional
	// classes, feel free to put additional private classes at the end of this file.  (One .java file only.)
	
	/**
	 * This helper method computes the ceiling of log base 2 of some number n.  (Any fractional
	 * log is rounded up.)  For example:  logBase2(8) returns 3, logBase2(9) returns 4.
	 * 
	 * This method is being provided to help students when they need to solve for x in 
	 * 2^x = n.  
	 * 
	 * @param n any positive integer
	 * @return the ceiling of log_2(n)
	 */
	public int logBase2 (int n)
	{
		int x = 0;
		long twoToTheXth = 1;
		while (twoToTheXth < n)
		{
			x++;
			twoToTheXth *= 2;
		}
		return x;
	}
	
	
	/**
	 * Application entry point.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args)
	{
		// Working in main is not great.  Instead, let's just create an object
		// and use the run method below to do all the work.  This way, we can
		// create fields and helper methods without them having to be static.  :)
		// (The work really begins in 'run', below.)
		
		
		
		new CacheCostCalculator().run();
	}
	
	/**
	 * Empty constructor -- feel free to add code if needed.
	 */
	public CacheCostCalculator()
	{
		// Constructor does nothing.
	}
	
	/**
	 * Gathers input, runs the simulation, and produces output.
	 */
	public void run ()
	{
		// Scan keyboard input from the console.
		
		Scanner userInput = new Scanner(System.in);
		
		// Determine which cache architecture is to be used.
		// Caution:  Do not change!!!  My autograder will expect these
		// exact prompts / responses.
		
		System.out.println ("Cache simulator Fall 2021");
		System.out.println ("  (D)irect-mapped");
		System.out.println ("  (S)et associative");
		System.out.println ("  (F)ully associative");
		System.out.print ("Enter a letter to select a cache and press enter: ");
		
		String choice = userInput.next();    // Get the first 'word' typed by the user.		
		choice = choice.toUpperCase();		 // Make it uppercase for consistency.
		
		boolean simulateDirectMapped     = choice.startsWith("D");
		boolean simulateSetAssociative   = choice.startsWith("S");
		boolean simulateFullyAssociative = choice.startsWith("F");
		
		// Each cache type has different customizations.  Get these inputs from the user.
		// Note:  All these variables are not needed.  You may rename them, but you
		// MUST NOT CHANGE THE ORDER OF INPUTS.  The autograder will give the inputs
		// in the order coded below.
		
		int entryDataBytes = 0;		
		int directRows = 0;
		int setWays = 0;
		int fullEntries = 0;
		
		// In all caches, we need to know how many data bytes are cached in each entry.
		// Note that we are counting on this being a power of two.  (required)
		
		System.out.println();
		System.out.print("How many data bytes will be in each cache entry? ");
		entryDataBytes = userInput.nextInt();  // Must be a power of two
		
		// Each cache will require different parameters...
		
		if (simulateDirectMapped)
		{
			System.out.print("How many direct-mapped rows will there be? ");
			directRows = userInput.nextInt();  // Must be a power of two			
		}
		else if (simulateSetAssociative)
		{
			System.out.print("How many direct-mapped rows will there be? ");
			directRows = userInput.nextInt();  // Must be a power of two			
			
			System.out.print("How many 'ways' will there be for each row? ");
			setWays = userInput.nextInt();  // Any positive integer is OK	
		}
		else if (simulateFullyAssociative)
		{
			System.out.print("How many entries will be in this fully associative cache? ");
			fullEntries = userInput.nextInt();  // Any positive integer is OK				
		}
		
		// The last step is to gather the addresses.  We will allow an unlimited number of addresses.
		// Each address represents a memory request (a read from memory).  
		
		List<Integer> addressList = new ArrayList<Integer>();  // Some students may prefer a list.
		int[]         addresses;                               // Some students may prefer an array.

		System.out.println("Enter a whitespace-separated list of addresses, type 'done' followed by enter at the end:");
		while (userInput.hasNextInt())
			addressList.add(userInput.nextInt());
		
		userInput.close();
		
		// The input was gathered in a list.  Make an array from it.  Students may use the array and/or the list
		// for their own purposes.
		
		addresses = new int[addressList.size()];
		for (int i = 0; i < addressList.size(); i++)
			addresses[i] = addressList.get(i);
		
		// Done gathering inputs.  Simulation code should be added below.
		
		// Step #1 - students should complete a few computations and update the output
		// statements below.  Do not change the text, only add integer answers to the output.  (No floating point results.)
		
		/* TODO - Compute the total storage size of the cache for 32 bit addresses. */
		int totalStorageSizeBits = 0;

		if (simulateDirectMapped)
		{
			totalStorageSizeBits = directRows * (1 + (32 - (logBase2(entryDataBytes) + (logBase2(directRows)))) + (entryDataBytes * 8) + (logBase2(setWays)));
		}
		else if (simulateSetAssociative)
		{
			totalStorageSizeBits = directRows * setWays * (1 + (32 - (logBase2(entryDataBytes) + (logBase2(directRows)))) + (entryDataBytes * 8) + (logBase2(setWays)));
		}
		else if (simulateFullyAssociative)
		{
			totalStorageSizeBits = fullEntries * (1 + (32 - (logBase2(entryDataBytes))) + (entryDataBytes * 8) + (logBase2(fullEntries)));
		}
				
		System.out.println();
		System.out.println("Number of address bits used as offset bits:        " + logBase2(entryDataBytes));
		System.out.println("Number of address bits used as row index bits:     " + logBase2(directRows)); 
		System.out.println("Number of address bits used as tag bits:           " + (32 - (logBase2(entryDataBytes) + logBase2(directRows))));  
		System.out.println();

		System.out.println("Number of valid bits needed in each cache entry:   " + 1); 
		System.out.println("Number of tag bits stored in each cache entry:     " + (32 - (logBase2(entryDataBytes) + (logBase2(directRows))))) ; 
		System.out.println("Number of data bits stored in each cache entry:    " + (entryDataBytes * 8)); 
		System.out.println("Number of LRU bits needed in each cache entry:     " + (simulateFullyAssociative ? logBase2(fullEntries): logBase2(setWays))); 
		System.out.println("Total number of storage bits needed in each entry: " + (1 + (32 - (logBase2(entryDataBytes) + (logBase2(directRows)))) + (entryDataBytes * 8) + (simulateFullyAssociative ? logBase2(fullEntries): logBase2(setWays))));
		System.out.println();

		System.out.println("Total number of entries in the cache:              " + (simulateDirectMapped ? directRows : (simulateSetAssociative ? (directRows * setWays) : fullEntries)));
		System.out.println("Total number of storage bits needed for the cache: " + totalStorageSizeBits);
		System.out.println();
		
		// simulate the cache

		// Step #2 - students should complete the code below to simulate the cache.
		// Do not change the text, only add integer answers to the output.  (No floating point results.)

		int[] cache = new int[(simulateDirectMapped ? directRows : fullEntries)];
		int hits = 0;
		int misses = 0;
		int tag = 0;
		int row = 0;
		int offsetBit = logBase2(entryDataBytes);
		int rowBits = logBase2(directRows);
		
		
		//This is the case for set associative.
		if(simulateSetAssociative) {
			
			//For this one, create a arraylist
			ArrayList<int[]> SAcache = new ArrayList<>();
			
			//Add setways accordingly
			for (int i = 0; i < directRows; i++) {
				SAcache.add(new int[setWays]);
			}

			// active the cache before analysis
			for (int i = 0; i < addresses.length; i++) {
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit + rowBits));
				 row = (addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit))) % directRows;

				 //Get rows
				int[] cacheRow = SAcache.get(row);
				boolean check = false;
				for (int j = 0; j < cacheRow.length; j++) {

					//check and swap
					if (cacheRow[j] == tag) {					
						for (int k = j; k < cacheRow.length - 1; k++) {
							swapWhenHit(cacheRow, tag,k);
						}		
						check = true;
						break;
					}
					
				}
				//missed, swap and count
				if (check == false) {
					
					for (int m = 0; m < cacheRow.length - 1; m++) {
						swapWhenMiss(cacheRow,m);
					}
					cacheRow[cacheRow.length - 1] = tag;
				}
			}
			
			// Real pass with counting hit and miss 
			for (int i = 0; i < addresses.length; i++) {
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit + rowBits));
				 row = (addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit))) % directRows;
				int[] ways = SAcache.get(row);
				boolean check = false;
				for (int j = 0; j < ways.length; j++) {
					if (ways[j] == tag) {
						//check and swap
						for (int h = j; h < ways.length - 1; h++) {
							swapWhenHit(ways, tag,h);
							
						}
						check = true;
						hits++;
						break;
					}
					
				}
				//missed, swap and count
				if (check == false) {								
					for (int c = 0; c < ways.length - 1; c++) {
						swapWhenMiss(ways,c);
					}
					ways[ways.length - 1] = tag;
				
					misses++;
				}

			}
		}
	// This is the directmapped case
		else if(simulateDirectMapped) {
			//Get value from cache, active
			for (int i = 0; i < addresses.length; i++) {
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit + rowBits));
				 row = (addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit))) % directRows;

				cache[row] = tag;
			}

			// pass the cache with counting hit and miss
			for (int i = 0; i < addresses.length; i++) {
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit + rowBits));
				 row = (addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit))) % directRows;

				
				if (cache[row] == tag) {
					hits++;
					
				} else {
					misses++;
					cache[row] = tag;
				}	
		}

		// this is the fullyAssociative case
		}else if(simulateFullyAssociative) {
			
			int j;

			//active the cache
			for (int i = 0; i < addresses.length; i++) {
				boolean check = false;
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit));
				for (j = 0; j < cache.length; j++) {
					//check and swap
					if (cache[j] == tag) {
						for(int k = j; k< cache.length -1 ;k++) {
							swapWhenHit(cache,tag,k);
						}
						check = true;
						break;
					}
					
				}
				//missed, swap and count
				if (check == false) {
					for (int c = 0; c < cache.length - 1; c++) {
						swapWhenMiss(cache,c);
					}
					cache[cache.length - 1] = tag;
			}
			}

			//pass the cache with counting his and miss
			for (int i = 0; i < addresses.length; i++) {
				boolean check = false;
				 tag = addresses[i] / (int) Math.ceil(Math.pow(2, offsetBit));
				
				for (j = 0; j < cache.length; j++) {
					if (cache[j] == tag) {
						//check and swap
						for(int h=j;h<cache.length -1; h++) {
							swapWhenHit(cache,tag,h);
							
						}
						check = true;
						hits++;
						break;
					}
					
				}
				//missed, swap and count
				if (check == false) {
					for (int c = 0; c < cache.length - 1; c++) {
						swapWhenMiss(cache,c);
					}
					cache[cache.length - 1] = tag;
					misses++;
					
				}				
			}
		}
		
		// Report the results.  Again, Do not change the messages or printing order.
		// Replace the "fix_me" with a calculation or variable in each case.
		double numCycle = (hits+5*(1+11+misses*entryDataBytes/4));
		System.out.println("Repeatedly accessing the addresses gives the following results (for the common case):");
		System.out.println("Total number of hits:   " + hits);
		System.out.println("Total number of misses: " + misses);
		System.out.println("Total number of cycle: " + numCycle);
		System.out.println("CPI: " + numCycle/32);
		
		// Done -- end of run method.
	}  

	// This is a great place for additional helper methods.  Add any you like.
	
	/*
	 * private helper method to swap bit when in hit condition
	 */
	private void swapWhenHit(int []cache,int tag, int index) {
			int temp = cache[index + 1];
			cache[index + 1] = tag;
			cache[index] = temp;
	
	}
	
	/*
	 * private helper method to swap bit when in miss condition
	 */
	private void swapWhenMiss(int []cache,int index) {
		int temp = cache[index];
		cache[index] = cache[index + 1];
		cache[index + 1] = temp;
	}

}

// If you want any additional classes, put them here.  Here is an example:
// Make sure they are non-public.

class Foo
{
	
}