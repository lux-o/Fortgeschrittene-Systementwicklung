package dbfileorga;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.hamcrest.core.IsNull;

public class MitgliederDB implements Iterable<Record>
{
	
	protected DBBlock db[] = new DBBlock[8];
	
	
	public MitgliederDB(boolean ordered){
		this();
		insertMitgliederIntoDB(ordered);
		
	}
	public MitgliederDB(){
		initDB();
	}
	
	private void initDB() {
		for (int i = 0; i<db.length; ++i){
			db[i]= new DBBlock();
		}
		
	}
	private void insertMitgliederIntoDB(boolean ordered) {
		MitgliederTableAsArray mitglieder = new MitgliederTableAsArray();
		String mitgliederDatasets[];
		if (ordered){
			mitgliederDatasets = mitglieder.recordsOrdered;
		}else{
			mitgliederDatasets = mitglieder.records;
		}
		for (String currRecord : mitgliederDatasets ){
			appendRecord(new Record(currRecord));
		}	
	}

		
	protected int appendRecord(Record record){
		//search for block where the record should be appended
		int currBlock = getBlockNumOfRecord(getNumberOfRecords());
		int result = db[currBlock].insertRecordAtTheEnd(record);
		if (result != -1 ){ //insert was successful
			return result;
		}else if (currBlock < db.length) { // overflow => insert the record into the next block
			return db[currBlock+1].insertRecordAtTheEnd(record);
		}
		return -1;
	}
	

	@Override
	public String toString(){
		String result = new String();
		for (int i = 0; i< db.length ; ++i){
			result += "Block "+i+"\n";
			result += db[i].toString();
			result += "-------------------------------------------------------------------------------------\n";
		}
		return result;
	}
	
	/**
	 * Returns the number of Records in the Database
	 * @return number of records stored in the database
	 */
	public int getNumberOfRecords(){
		int result = 0;
		for (DBBlock currBlock: db){
			result += currBlock.getNumberOfRecords();
		}
		return result;
	}
	
	/**
	 * Returns the block number of the given record number 
	 * @param recNum the record number to search for
	 * @return the block number or -1 if record is not found
	 */
	public int getBlockNumOfRecord(int recNum){
		int recCounter = 0;
		for (int i = 0; i< db.length; ++i){
			if (recNum <= (recCounter+db[i].getNumberOfRecords())){
				return i ;
			}else{
				recCounter += db[i].getNumberOfRecords();
			}
		}
		return -1;
	}
		
	public DBBlock getBlock(int i){
		return db[i];
	}
	
	
	/**
	 * Returns the record matching the record number
	 * @param recNum the term to search for
	 * @return the record matching the search term
	 */
	public Record read(int recNum){
		//TODO implement - done
		// find the block of record
		int blockNum = getBlockNumOfRecord(recNum);
		if (blockNum >= 0){
			// get number of record in refernce to the block
			int blockRecNum = getRecNumInBlock(recNum, blockNum);
			// return the record
			return
				getBlock(blockNum).getRecord(blockRecNum);
		}
		return null;
	}
	
	/**
	 * Returns the number of the first record that matches the search term
	 * @param searchTerm the term to search for
	 * @return the number of the record in the DB -1 if not found
	 */
	public int findPos(String searchTerm){
		//TODO implement
		int recCounter = 1;
		for (int i = 0; i < db.length; i++){
			for (int j = 1; j <= db[i].getNumberOfRecords(); j++){
				// iterate through all blocks and its records
				if(searchTerm.equals(db[i].getRecord(j).getAttribute(1))){
					// found searched record and return postion
					return recCounter;
				}
				// not found --> increase postion
				else recCounter++;
			}
		}
		return -1;
	}

	/**
	 * Returns the number of the first record that matches the search term
	 * @param searchTerm the term to search for
	 * @param left left border of search area
	 * @param right right border of search area
	 * @return the number of the record in the DB -1 if not found
	 */
	private int binarySearch(String searchTerm, int left, int right){
		int pivot = left + (right - left) / 2;
        if ((right < left) || (db[pivot].getNumberOfRecords() == 0)) {
			// break-up if pivot block is empty 
			System.out.println("Bin??ry search failed. Maybe try linear search.");
            return -1;
		}
        if (Integer.parseInt(db[pivot].getRecord(1).getAttribute(1)) > Integer.parseInt(searchTerm))
			// searcht record hast to be in the blocks before
            return
                binarySearch(searchTerm, left, pivot-1);
        else if (Integer.parseInt(db[pivot].getRecord(db[pivot].getNumberOfRecords()).getAttribute(1)) < Integer.parseInt(searchTerm))
			// record has to be in the blocks after
            return
                binarySearch(searchTerm, pivot + 1, right);
		else{
			// record hast to be in the pivot block
			int recCounter = 1;
			for (int i = 0; i < pivot; i++){
				recCounter+= db[i].getNumberOfRecords();
			}
			for (int i = 1; i <= db[pivot].getNumberOfRecords(); i++){
				if(searchTerm.equals(db[pivot].getRecord(i).getAttribute(1))){
					// found searched record and return postion
					return recCounter;
				}
				// not found --> increase postion
				else recCounter++;
			} 
			// if search is not successful --> record to searchterm not existing
			return -1;
		}
    }

	/**
	 * Returns the number of the first record that matches the search term - binary search
	 * @param searchTeram the term to search for
	 * @return the number of the record in the DB -1 if not found
	 */
    public int findPosOrdered(String searchTeram) {
        return
                binarySearch(searchTeram, 0, db.length - 1);
    }
	
	/**
	 * Inserts the record into the file and returns the record number
	 * @param record
	 * @return the record number of the inserted record
	 */
	public int insert(Record record){
		//TODO implement - geht nur f??r unordered
		// search for block where the record should be appended
		int currBlock = getBlockNumOfRecord(getNumberOfRecords());
		int result = db[currBlock].insertRecordAtTheEnd(record);
		if (result != -1 ){ //insert was successful
			return getNumberOfRecords();
		}else if (currBlock < db.length) { // overflow => insert the record into the next block
			db[currBlock+1].insertRecordAtTheEnd(record);
			return getNumberOfRecords();
		}
		return -1;
	}
	
	/**
	 * Deletes the record specified 
	 * @param numRecord number of the record to be deleted
	 */
	public void delete(int numRecord){
		//TODO implement - done
		// get block of the record
		int blockNum = getBlockNumOfRecord(numRecord);
		if (blockNum >= 0){
			// get record number in refernce to the block it is in
			int blockRecNum = getRecNumInBlock(numRecord, blockNum);
			db[blockNum].deleteRecord(blockRecNum);
		}
	}
	
	/**
	 * Replaces the record at the specified position with the given one.
	 * @param numRecord the position of the old record in the db
	 * @param record the new record
	 * 
	 */
	public void modify(int numRecord, Record record){
		//TODO
		// get block of the record
		int blockNum = getBlockNumOfRecord(numRecord);
		if (blockNum >= 0){
			// get record number in refernce to the block it is in
			int blockRecNum = getRecNumInBlock(numRecord, blockNum);
			db[blockNum].modifyRecord(blockRecNum, record);
		}
	}

	
	private int getRecNumInBlock(int numRecord, int blockNum){
		int recCounter = 0;
		for (int i = 0; i < blockNum; i++){
			recCounter += getBlock(i).getNumberOfRecords();
		}
		return numRecord - recCounter;
	}


	@Override
	public Iterator<Record> iterator() {
		return new DBIterator();
	}
 
	private class DBIterator implements Iterator<Record> {

		    private int currBlock = 0;
		    private Iterator<Record> currBlockIter= db[currBlock].iterator();
	 
	        public boolean hasNext() {
	            if (currBlockIter.hasNext()){
	                return true; 
	            } else if (currBlock < db.length) { //continue search in the next block
	                return db[currBlock+1].iterator().hasNext();
	            }else{ 
	                return false;
	            }
	        }
	 
	        public Record next() {	        	
	        	if (currBlockIter.hasNext()){
	        		return currBlockIter.next();
	        	}else if (currBlock < db.length){ //continue search in the next block
	        		currBlockIter= db[++currBlock].iterator();
	        		return currBlockIter.next();
	        	}else{
	        		throw new NoSuchElementException();
	        	}
	        }
	 
	        @Override
	        public void remove() {
	        	throw new UnsupportedOperationException();
	        }
	    } 
	 

}
