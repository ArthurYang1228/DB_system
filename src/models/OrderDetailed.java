package models;

public class OrderDetailed {
	public int id;
	public String sugar;
	public String ice;
	public Product product;
	
	public OrderDetailed(int id, String sugar, String ice, Product product) {
		this.id = id;
		this.sugar = sugar;
		this.ice = ice;
		this.product = product;
	}
	
	public OrderDetailed(String sugar, String ice, Product product) {
		this.sugar = sugar;
		this.ice = ice;
		this.product = product;
	}
	
	@Override
	public String toString() {
		return String.format("id: %d, sugar: %s, ice: %s, product: %s", 
				id, sugar, ice, product.toString());
	}
	
	public boolean equals(OrderDetailed o) {
		if(id == o.id)
			return true;
		else
			return false;
	}
}
