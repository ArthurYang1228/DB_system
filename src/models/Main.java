package models;

public class Main {

	public static void main(String[] args) {
		
		for(Purchase p : new PurchaseDAO(Connector.getConn()).search("29")) {
			System.out.print(p.toString());
		}
	}
}
