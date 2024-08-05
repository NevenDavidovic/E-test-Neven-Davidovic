import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 // Using default path
        MoneyCounter counter = new MoneyCounter();
        DatabaseOperations database=new DatabaseOperations("evolva_db.db");
        boolean exit = true;

        do {
        	
        	    	
        	System.out.println("Testiranje spojivosti baze i programa");
        	       	
        	database.testConnection();
        	
        	System.out.println("_______________________________________________");
            System.out.println("CSV Evolva Test CLI program ");
            counter.displayPath();
            System.out.println("Odaberi opciju:");
            System.out.println("1. Promijeni folder Path ");
            System.out.println("2. Statistika/prvi zadatak  ");
            System.out.println("3. Dodavanje podataka iz foldera u bazu ");
            System.out.println("4. Kreiranje izvještaja ");
            System.out.println("5. Izlaz ");
        	System.out.println("_______________________________________________");
            System.out.println("Unesi odabir: ");
            Scanner skener = new Scanner(System.in);
            String odabir = skener.nextLine();

            switch (odabir) {
                case "1":
                    boolean choice = true;

                    do {
                        System.out.println("Odaberi opciju: ");
                        System.out.println("1.default path-> \"C:\\\\data :");
                        System.out.println("2. Unesi path ");
                        System.out.println("3. Povratak ");

                        String odabirPatha = skener.nextLine();

                        switch (odabirPatha) {
                            case "1":
                                System.out.println("Odabran je path \"C:\\\\data :");
                                counter.setDefaultPath();
                                choice = false;
                                break;
                            case "2":
                                System.out.println("Unesi novi path");
                                String noviPath = skener.nextLine();
                                counter.setPath(noviPath);
                                System.out.println("Namjestili ste novi path");
                                break;
                            case "3":
                                System.out.println("Odabran je path \"C:\\\\data :");
                                choice = false;
                                break;
                        }

                    } while (choice);
                    break;
                case "2":
                    System.out.println("Racunanje... ");
                    counter.addToDatabase();
                    counter.count();
                    break;
                    
                    
                case "3":
                	System.out.println("Dodavanje podataka iz foldera u bazu... ");
                    counter.addToDatabase();
                	break;
                	
                	
                case "4":
                	//prikaz podataka iz tablica
                	
                	 HTMLReportGenerator.createHTMLHeader();
                	    
                	   

                	    System.out.println("Generating report...");
                	    database.getTotalAmountPerCity();
                	    database.getTotalAmountPerCurrency();
                	    database.getNumberOfCitiesPerCountry();
                	    database.getAverageTransactionAmountPerCurrency();
                	    database.generateTransactionReport(); 

                	    HTMLReportGenerator.createHTMLFooter();
                	    
                	    System.out.println("Report generated successfully.");

                    

                  
                	break;
                	
                case "5":
                    System.out.println("Izlazim iz aplikacije ");
                    skener.close();
                    exit = false;
                    break;
                default:
                    System.out.println("Nepravilan unos. Molimo Vas unesite ispravnu opciju! ");
            }

        } while (exit);
	}
}

	