/**
 * Name: Elaine Vizcarra
 * Class: CS2050 MW 12:00
 * Description: Project 01 iteration 02
 * 
 * This programs creates a vending machine for cars utilizing user input. 
 * Allows user to populate vending machine, sort car inventory, 
 * display car invetory, and sell cars through options listed in a menu
 */

/**
 * 
 */
import java.util.*;
import java.util.Scanner;
import java.util.LinkedList;

import java.io.File;
import java.io.FileNotFoundException;


public class CarvanaCarVendingMachineIteration02 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		//inputed numbers are used to initialize Vending Machine tower
		System.out.println("Enter the number of floors for the car vending machine: ");
		int floors = scanner.nextInt();
		System.out.println("Enter the number of spaces for the car vending machiine: ");
		int spaces = scanner.nextInt();
		VM02 newVM = new VM02(floors, spaces);
		
		Queue<Car02> carWashQueue = new LinkedList<>();
	
		//starts menu loop
		boolean running = true;
		while (running) {
			displayMenu();
			if (scanner.hasNextInt()) {
				int choice = scanner.nextInt();
				switch (choice) {
					
					//for menu option: Load Car Data
					case 1 :{
						System.out.println("Enter the file name: ");
						String fileName = scanner.next();
						readFromFileSetup(fileName, newVM);
						break;
					}
					
					//for menu option: Display Vending Machine
					case 2 :{
						VM02.displayInventory(newVM.getCarList());
						break;
					}

					//for menu option: Retrieve a car
					case 3 :{						
						
						System.out.println("Enter floor to retrieve car: ");
						int floor = scanner.nextInt();
						System.out.println("Enter space to retrieve car: ");
						int space = scanner.nextInt();
						newVM.retrieveCar(floor, space);
						
						break;
					}
					
					//for menu option: Print Sorted Inventory (Price)
					case 4 :{
						String attribute = "Price";
						newVM.displayByAttribute(attribute);
						break;
						}
					
					//for menu option: Print Sorted Inventory (Year)
					case 5 :{
						String attribute = "Year";
				        newVM.displayByAttribute(attribute);
						break;
					}
					
					//for menu option: Search for Cars (Manufacturer & Type)
					case 6 :{				
						List<Car02> foundCars = carSearcher(newVM.getCarList(), scanner);
							VM02.displayInventory(foundCars);							
						break;
					}
					
					//for menu option: Add car to Wash Queue
					case 7 :{
						System.out.println();
						System.out.println("Enter floor: ");
						int floor = scanner.nextInt();
						System.out.println("Enter space: ");
						int space = scanner.nextInt();
						Car02 carToWash = newVM.retrieveCar(floor, space);
						
						if (carToWash != null) {
							carWashQueue.add(carToWash);
							System.out.println("Car added to wash queue.");
						}
						break;
					}
					
					//Process Car Wash Queue
					case 8 :{
						System.out.println();
						carWasher(carWashQueue);


						break;
					}
					
					//for menu option: Sell a Car
					case 9 :{
						System.out.println();
						System.out.println("Enter floor of the car to sell: ");
						int floor = scanner.nextInt();
						System.out.println("Enter space of the car to sell: ");
						int space = scanner.nextInt();
						Car02 carToSell = newVM.retrieveCar(floor, space);
						
						if (carToSell != null) {
							System.out.println();
							newVM.sellCar(carToSell, floor, space);
							System.out.println("Car Sold: " + carToSell);
							
						}
						else {
							System.out.printf("\nNo car found at floor: %d space: %d\n", floor, space );
						}
						break;
					}
					
					//for menu option: Exit
					case 10 :{
						running = false;
						System.out.println();
						System.out.println("Exiting program. Goodbye!");
						break;
					}
					
					//handles invalid input if user chooses an integer not between 1 and 10
					default:{	
						System.out.println();
						System.out.println("Invalid choice! Please input an integer between 1 and 10.");
						break;
					}

				}
			}
			else {
				System.out.println();
				System.out.println("Invalid input. Please input an integer!");
				
				scanner.nextLine(); //clears scanner object if an integer is not inputed
			}
			
		}
		scanner.close();
	} //end of Main method
	
	public static void carWasher(Queue<Car02> queue) {
		if (queue.size() == 0) return;
		
		System.out.println("Washing: " + queue.peek());
		queue.remove();
		carWasher(queue);
	}
	public static void displayMenu() {
		
		System.out.println();
		System.out.println("=== Car Vending Machine Menu ===");
		System.out.println("1. Load Car Data");
		System.out.println("2. Display Vending Machine");
		System.out.println("3. Retrieve a Car by Location (Floor & Space)");
		System.out.println("4. Print Sorted Inventory (Price)");
		System.out.println("5. Print Sorted Inventory (Year)");
		System.out.println("6. Search for Cars(Manufacturer & Type");
		System.out.println("7. Add Car to Wash Queue");
		System.out.println("8. Process Car Wash Queue");
		System.out.println("9. Sell a Car");
		System.out.println("10. Exit");
		System.out.println();
		System.out.println("Enter your choice: ");
	} //end of displayMenu method

	
	public static void readFromFileSetup(String fileName, VM02 newVM)
	{
		File inputFileName = new File(fileName);
		Scanner newFile = null;
		System.out.println();
		
		try
		{
			newFile = new Scanner(inputFileName);
			while (newFile.hasNextLine()) {
				char type = newFile.next().toLowerCase().charAt(0);
				int floor = newFile.nextInt();
				int space  = newFile.nextInt(); 
				int year = newFile.nextInt(); 
				double price = newFile.nextDouble();
				String make = newFile.next();
				String model = newFile.next();
				
				Car02 newCar = carCreator(type, make, model, price, year, floor, space);
				newVM.addCar(floor, space, newCar);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found! Please check file path and try again!");	
		}finally { 
			if (newFile != null) {
				newFile.close();
			}
		}
	}// end of readFromFileSetup method
	
	public static Car02 carCreator(char type, String make, String model, double price, int year, int floor, int space) {
		Car02 newCar = null;
		switch (type) {
		
			case 'b' :{
				String carType = "Basic";
				newCar = new BasicCar02(carType, make, model, price, year, floor, space);
				break;
			}
			case 'p' :{
				String carType = "Premium";
				newCar = new PremiumCar02(carType, make, model, price, year, floor, space);
				break;
			}
		}
		return newCar;
	}
	
	// Allows user to search for car based on user input
	public static List<Car02> carSearcher(LinkedList<Car02> carList, Scanner scanner) {
		System.out.println("Enter manufacturer: ");
		String manufacturer = scanner.next();
		System.out.println("Enter car type (Basic/Premium): ");
		String type = scanner.next();
		List<Car02> results = new ArrayList<>(); //creates ArrayList to store matching results
		
		List<Car02> carArray = new ArrayList<>(carList); 
		
		System.out.println();
		for (Car02 car:carList) {  //maybe iterate through carArray instead?
			if (car.getManufacturer().equalsIgnoreCase(manufacturer) && car.getType().equalsIgnoreCase(type)) {
				results.add(car);
			}
		}
		return results;
	}
}// end of driver class

class VM02{
	private int floors;
	private int spaces;
	private LinkedList<Car02> carList;
	private HashMap<String, Car02> carMap;
	
	public VM02(int floors, int spaces) {
		this.floors = floors;
		this.spaces = spaces;
		carList = new LinkedList<>();
		carMap = new HashMap<>();
	}
	
	public LinkedList<Car02> getCarList() {
		return carList;
	}
	
	public void addCar(int floor, int space, Car02 car) {
				
		//handles if spot doesn't exist in VM
		if (!checkBounds(floor, space)) {
			System.out.println();	
			System.out.printf("Error: Invalid position at Floor: %d Space: %d\n", floor, space);
			System.out.println("Cannot place " + car.toString());
		}
		
		//handles if spot is occupied
		else if (!isEmpty(floor, space)) {
			System.out.println();
			System.out.printf("Error: Slot at Floor: %d Space: %d is already occupied.\n", floor, space);
			System.out.println(car + " cannot be placed.");
		}
		
		//adds car to VM if spot exists and is not occupied
		else {
			carMap.put(floor+"-"+space, car);
			carList.add(car);
		}
	}
	
	private boolean checkBounds(int proposedFloor, int proposedSpace) {
		boolean inBounds = false;
		if (proposedFloor > 0 && proposedFloor <= floors) {
			if (proposedSpace > 0 && proposedSpace <= spaces) {
				inBounds = true;
			}
		}
		return inBounds;
	}
	
	private boolean isEmpty(int floor, int space) {
		boolean vacancy = true;
		if (carMap.containsKey(floor + "-" + space)) {
			vacancy = false;
		}
		return vacancy;
	}
	
	public Car02 retrieveCar(int floor, int space) {	
		Car02 retrievedCar = null;
		
		System.out.println();
		if (!checkBounds(floor, space)) {
			System.out.printf("Invalid location! Floor %d Space %d does not exist!\n",floor, space);
		}
		else if (isEmpty(floor, space)) {
				System.out.printf("No car found at Floor %d Space %d\n", floor, space);
		}
		else {
		retrievedCar = carMap.get(floor + "-" + space);
		System.out.println("Car retrieved: " + retrievedCar);
		}
		
		return retrievedCar;
	} // end of retrieveCar method
	
	public void displayByAttribute(String attribute) {
		
		//converts car list to an arraylist for more efficient sorting
		List<Car02> carArrayList = new ArrayList<>(carList); 
		
		switch (attribute) {
			case "Price" :{
				System.out.printf("Sorted Inventory by %s:\n", attribute);
				carArrayList.sort(Comparator.comparing(Car02::getPrice));
				break;
			}
			case "Year" :{
				System.out.printf("Sorted Inventory by %s:\n", attribute);
				carArrayList.sort(Comparator.comparing(Car02::getYear));
				break;
			}		
		}
		
		System.out.println();
		displayInventory(carArrayList);
	} //end displayByAttribute method
	
	public static void displayInventory(List<Car02> carList) {
		if (carList.isEmpty()) {
			System.out.println("No cars available;");
		}
		else {
			System.out.println();
			for (Car02 car : carList) {
				System.out.println(car);
			}
		}
	}
	
	
	public void sellCar(Car02 car, int floor, int space) {
		carList.remove(car);
		carMap.remove(floor + "-" + space);
	}
}

abstract class Car02 {
	private String manufacturer;
	private String model;
	private double price;
	private int year;	
	private String type;
	private String location;
	
	public Car02(String type, String manufacturer, String model, double price, int year, int floor, int space) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.price = price;
		this.year = year;
		this.type = type;
		location = "Floor: " + floor + ", Space: " + space;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		return String.format("%s Car: %s %s %d - $%.2f (%s)", type, manufacturer, model, year, price, location);
	}
}

class BasicCar02 extends Car02 {
	
	public BasicCar02(String type, String manufacturer, String model, double price, int year, int floor, int space) {
		super(type, manufacturer, model, price, year, floor, space);
	}
}

class PremiumCar02 extends Car02 {
	public PremiumCar02(String type, String manufacturer, String model, double price, int year, int floor, int space) {
		super(type, manufacturer, model, price, year, floor, space);
	}
}
