package edu.westga.comp4420.grocery_list.test.model.grocery_item;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.westga.comp4420.grocery_list.model.GroceryItem;

class TestConstructor {
	
	@ParameterizedTest
	@CsvSource({"bob, 1, 1"})
	void testValidInput(String name, int amountNeeded, int amountInCart) {
		GroceryItem result = new GroceryItem(name, amountNeeded, amountInCart);
		
		assertEquals(name, result.getName(), "checking name of grocery item");
		assertEquals(amountNeeded, result.getAmountNeeded(), "checking amount needed of grocery item");
		assertEquals(amountInCart, result.getAmountInCart(), "checking amount in cart of grocery item");
	}
	
	@ParameterizedTest
	@CsvSource({", 1, 1", "'', 1, 1", "bob, 0, 0", "bob, 1, -1"})
	void testInvalidInput(String name, int amountNeeded, int amountInCart) {
		assertThrows(IllegalArgumentException.class, ()->{new GroceryItem(name, amountNeeded, amountInCart);});
	}
}
