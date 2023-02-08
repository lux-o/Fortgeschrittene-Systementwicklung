package dbfileorga;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DBPlayground {
    
    public static void main(String[] args) {
        // use database with user interaction
        System.out.println("Create your database! \nChoose a number:");
        // initilize variabeles
        boolean correctNumber = false;
        int choosenNumber = 0;
        Scanner reader = new Scanner(System.in);
        MitgliederDB db = null;
        // get input, if database should be ordered or unordered
        while (!correctNumber){
            System.out.println("1: unordered \n2: ordered");
            // catch false input types (not an int)
            Boolean choosenNumberConfirm = false;
            while (!choosenNumberConfirm){
                try{
                    choosenNumber = reader.nextInt(); 
                    choosenNumberConfirm = true;
                } 
                catch (InputMismatchException e){
                    choosenNumberConfirm = false;
                    reader.next();
                    System.out.println("Not a number. Try again");
                }
            }
            if (choosenNumber == 1) {
                // create unordered database
                db = new MitgliederDB(false);
                correctNumber = true;
            }
            else if(choosenNumber == 2) {
                // create ordered database
                db = new MitgliederDB(true);
                correctNumber = true;
            }
            // fallback
            else System.out.println("Number is not valid");
        }
        boolean quit = false;
        correctNumber = false;
        // user options for unordered database
        if (choosenNumber == 1){
            while (!quit){
                int option = 0;
                System.out.println("Choose your action:\n" + 
                "1: read database\n" +
                "2: get record by record number\n" +
                "3: find record by Mitgliedsnummer\n" +
                "4: modify record\n" +
                "5: delete a record by Mitgliedsnummer \n" +
                "6: quit");
                // catch false input types (not an int)
                boolean optionconfirm = false;
                while (!optionconfirm){
                    try{
                        option = reader.nextInt();
                        optionconfirm = true;
                    } 
                    catch (InputMismatchException e){
                        optionconfirm = false;
                        reader.next();
                        System.out.println("Not a number. Try again");
                    }
                }
                boolean inputconfirm = false;
                // identify input and continue interaction
                int searchTerm = -1;
                switch (option){
                    case 1:
                        // print database
                        System.out.println(db);
                        break;
                    case 2:
                        // find record by number of record
                        System.out.println("Enter a record number");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        Record rec = db.read(searchTerm);
                        // print found record - null if not found
                        System.out.println(rec);
                        }
                        break;
                    case 3:
                        // find record by Mitgliedsnummer
                        System.out.println("Enter a Mitgliedsnummer");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        Record rec = db.read(db.findPos(Integer.toString(searchTerm)));
                        // print found record - null if there is no record with Mitgliedsnummer
                        System.out.println(rec);
                        break;
                    case 4:
                        // modify a record
                        // get Mitgliedsnummer of record, which shall be modified
                        System.out.println("Enter the Mitgliedsnummer of the record, you want to change.");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        // get modified Record 
                        Scanner reader2 = new Scanner(System.in);
                        System.out.println("Enter the modified record. Format: 95;3;13;Bach;Steffi;04.04.06;01.02.16;;5");
                        String input = reader2.nextLine();
                        // modify Record
                        db.modify(db.findPos(Integer.toString(searchTerm)), new Record(input));
                        // print modified record
                        System.out.println(db.read(db.findPos(Integer.toString(searchTerm))));
                        break;
                    case 5:
                        // delete a record
                        System.out.println("Enter the Mitgliedsnummer of the record, you want to delete.");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                reader.next();
                                inputconfirm = false;
                                System.out.println("Not a number. Try again");
                            }
                        }
                        db.delete(db.findPos(Integer.toString(searchTerm)));
                        // print complete database - deleted record should not be printed
                        System.out.println(db);
                        break;
                    case 6:
                        // User quits program
                        System.out.println("Stop program...");
                        quit = true;
                        break;
                    default:
                        // if no option was selected
                        System.out.println("Not an option. Try again.");
                        break;
                }
            }
        }
        // user options for unordered database
        else if (choosenNumber == 2){
            while (!quit){
                int option = 0;
                System.out.println("Choose your action:\n" + 
                "1: read database\n" +
                "2: get record by record number\n" +
                "3: find record by Mitgliedsnummer - linear search\n" +
                "4: find record by Mitgliedsnummer - binary search\n" +
                "5: modify record\n" +
                "6: delete a record by Mitgliedsnummer \n" +
                "7: quit");
                // catch false input types (not an int)
                boolean optionconfirm = false;
                while (!optionconfirm){
                    try{
                        option = reader.nextInt();
                        optionconfirm = true;
                    } 
                    catch (InputMismatchException e){
                        optionconfirm = false;
                        reader.next();
                        System.out.println("Not a number. Try again");
                    }
                }
                boolean inputconfirm = false;
                int searchTerm = -1;
                // identify input and continue interaction
                switch (option){
                    case 1:
                        // print database
                        System.out.println(db);
                        break;
                    case 2:
                        // find record by number of record
                        System.out.println("Enter a record number");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        Record rec = db.read(searchTerm);
                        // print found record - null if not found
                        System.out.println(rec);
                        }
                        break;
                    case 3:
                        // find record by Mitgliedsnummer
                        System.out.println("Enter a Mitgliedsnummer");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        // track and print duration of search for compairison 
                        long beforeLinear = System.currentTimeMillis();
                        Record recLinear = db.read(db.findPos(Integer.toString(searchTerm)));
                        long afterLinear = System.currentTimeMillis();
                        // print found record - null if there is no record with Mitgliedsnummer
                        System.out.println(recLinear);
                        System.out.println("Duration: " + (afterLinear-beforeLinear) + " ms");
                        break;
                    case 4:
                        // find record by Mitgliedsnummer
                        System.out.println("Enter a Mitgliedsnummer");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        // track and print duration of search for compairison 
                        long beforeBinäry = System.currentTimeMillis();
                        Record recBinary = db.read(db.findPosOrdered(Integer.toString(searchTerm)));
                        long afterBinary = System.currentTimeMillis();
                        // print found record - null if there is no record with Mitgliedsnummer
                        System.out.println(recBinary);
                        System.out.println("Duration: " + (afterBinary-beforeBinäry) + " ms");
                        break;
                    case 5:
                        // modify a record
                        // get Mitgliedsnummer of record, which shall be modified
                        System.out.println("Enter the Mitgliedsnummer of the record, you want to change.");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        // get modified Record 
                        Scanner reader2 = new Scanner(System.in);
                        System.out.println("Enter the modified record. Format: 95;3;13;Bach;Steffi;04.04.06;01.02.16;;5");
                        String input = reader2.nextLine();
                        // modify Record
                        db.modify(db.findPos(Integer.toString(searchTerm)), new Record(input));
                        // print modified record
                        System.out.println(db.read(db.findPos(Integer.toString(searchTerm))));
                        break;
                    case 6:
                        // delete a record
                        System.out.println("Enter the Mitgliedsnummer of the record, you want to delete.");
                        // catch false input types (not an int)
                        while (!inputconfirm){
                            try{
                                searchTerm = reader.nextInt();
                                inputconfirm = true;
                            } 
                            catch (InputMismatchException e){
                                inputconfirm = false;
                                reader.next();
                                System.out.println("Not a number. Try again");
                            }
                        }
                        // print complete database - deleted record should not be printed
                        db.delete(db.findPos(Integer.toString(searchTerm)));
                        System.out.println(db);
                        break;
                    case 7:
                        // User exits the program
                        System.out.println("Stop program...");
                        quit = true;
                        break;
                    default:
                        // fallback if number is no option
                        System.out.println("Not an option. Try again.");
                        break;
                }
            }
        }
        // close input reader
        reader.close();      
    }
}
