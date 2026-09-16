package edu.westga.comp4420.grocery_list.model;

/**
 * Stores basic information for a single item in a grocery list.
 *
 * @author	Comp 4420
 * @version Spring 2025
 */
public final class GroceryItem {   

	private final String name;
	private final int amountInCart;
	private final int amountNeeded;
	
	/**
	 * Create and initialize a new grocery item with the provided information.
	 * 
	 * @precondition name != null &&
	 *				 !name.isEmpty() &&
	 * 				 amountNeeded > 0 &&
	 * 				 amountInCart >= 0
	 * @postcondition getName() == name &&
	 * 				  getAmountNeeded() == amountNeeded &&
	 * 				  getAmountInCart() == amountInCart
	 * 
	 * @param name the name for the new grocery item
	 * @param amountNeeded the amount needed to be purchased for the new grocery item
	 * @param amountInCart the amount currently in the cart for the new grocery item
	 */
	public GroceryItem(String name, int amountNeeded, int amountInCart) {
		if (name == null) {
			throw new IllegalArgumentException("Must provide a name, but name was null.");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Must provide a name with at least one character.");
		}
		if (amountNeeded <= 0) {
			throw new IllegalArgumentException("Must provide a positive value for amount needed.");
		}
		if (amountInCart < 0) {
			throw new IllegalArgumentException("Must provide a non-negative value for amount in cart.");
		}
		this.name = name;
		this.amountNeeded = amountNeeded;
		this.amountInCart = amountInCart;
	}
	
	/**
	 * Return the name for the grocery item.
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the name for the new grocery item
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Return the amount needed to be purchased for the new grocery item
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the amount needed to be purchased for the new grocery item
	 */
	public int getAmountNeeded() {
		return this.amountNeeded;
	}
	
	/**
	 * Return the amount currently in the cart for the new grocery item
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the amount currently in the cart for the new grocery item
	 */
	public int getAmountInCart() {
		return this.amountInCart;
	}
	
	@Override
	public String toString() {
		return this.name + "(" + this.amountInCart + "/" + this.amountNeeded + ")";
	}
}