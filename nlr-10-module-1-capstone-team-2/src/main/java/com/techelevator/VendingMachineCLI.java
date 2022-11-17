package com.techelevator;
// gabe funk, ryan louie, jacob tate
import com.sun.jdi.VMOutOfMemoryException;
import com.techelevator.view.Menu;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String FEED_MONEY = "Feed Money";
	private static final String SELECT_PRODUCT = "Select Product";
	private static final String FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};
	private static final String[] PURCHASE_OPTIONS = {FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};

	public static Map<String, VendingMachineItems> inventorys = new HashMap<>();
	private Menu menu;
	public static BigDecimal money = new BigDecimal("0.00");
	public static TreeMap<String, VendingMachineItems> inventory = new TreeMap<>(inventorys);

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		loadFile();
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public static void loadFile(){
		File sourceFile = new File("vendingmachine.csv");
		try (Scanner fileInput = new Scanner(sourceFile)) {
			while (fileInput.hasNextLine()) {
				String input = fileInput.nextLine();
				String[] items = input.split("\\|");

				if (items[3].equals("Chip")) {
					Chips chipSnack = new Chips(items[1], new BigDecimal(items[2]));
					inventory.put(items[0], chipSnack);
				} else if (items[3].equals("Candy")) {
					Candy candySnack = new Candy(items[1], new BigDecimal(items[2]));
					inventory.put(items[0], candySnack);
				} else if (items[3].equals("Drink")) {
					Drinks drinkSnack = new Drinks(items[1], new BigDecimal(items[2]));
					inventory.put(items[0], drinkSnack);
				} else if (items[3].equals("Gum")) {
					Gum gumSnack = new Gum(items[1], new BigDecimal(items[2]));
					inventory.put(items[0], gumSnack);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Goodbye.");
		}
	}


	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				for (String item : inventory.keySet()) {
					System.out.print(item + ": " + inventory.get(item));
				}
				System.out.print("===========================");
				System.out.println();

			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				while (true) {
					System.out.println("===========================");
					System.out.println("Current Money Provided: $" + money);
					String secondChoice = (String) menu.getChoiceFromOptions(PURCHASE_OPTIONS);
					if (secondChoice.equals(FEED_MONEY)) {
						Scanner userInput = new Scanner(System.in);
						System.out.print("Please insert whole bill: ");
						String moneyInserted = userInput.nextLine();
						//test if int
						BigDecimal bigMoney = new BigDecimal(moneyInserted);
						//test if 1,5,10,50,100
						//ask if we need to worry about forcing them to insert bills and not change
						money = money.add(bigMoney);
						feedMoneyLog(moneyInserted, money);
					} else if (secondChoice.equals(SELECT_PRODUCT)) {
						for (String item : inventory.keySet()) {
							System.out.print(item + ": " + inventory.get(item));
						}
						System.out.println("===========================");
						System.out.println();
						System.out.print("Please enter the slot value:");
						Scanner userInput = new Scanner(System.in);
						String slot = userInput.nextLine();
						if (inventory.containsKey(slot)) {
							if (money.doubleValue() - inventory.get(slot).price.doubleValue() >= 0) {
								money = money.subtract(inventory.get(slot).price);
								if (inventory.get(slot).quantity > 0) {
									inventory.get(slot).quantity--;
									System.out.println(inventory.get(slot).getSound());
									codeLog(slot);
									changeLog(money);
								} else {
									System.out.println("Out of stock! Please choose another item!");
									System.out.println();
								}
							} else {
								System.out.println("No enough funds, please at more!");
								System.out.println();
							}
						} else {
							System.out.println("Slot does not exist please retry!");
						}

					} else if (secondChoice.equals(FINISH_TRANSACTION)) {
						//receive change
						int quarters = 0;
						int dimes = 0;
						int nickels = 0;
						for (money.doubleValue(); money.doubleValue() > 0; ) {
							if (money.doubleValue() >= 0.25) {
								money = new BigDecimal(money.doubleValue() - 0.25);
								quarters++;
							} else if (money.doubleValue() >= 0.10) {
								money = new BigDecimal(money.doubleValue() - 0.10);
								dimes++;
							} else {
								money = new BigDecimal(money.doubleValue() - 0.05);
								nickels++;
							}
						}
						System.out.println("Your change is " + quarters + " quarters, " + dimes + " dimes, " + nickels + " nickels!");
						break;
					}
				}
			}else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				System.exit(1);
			}
		}

	}
	public static void feedMoneyLog(String deposited, BigDecimal balance) {
		File logFile = new File("src/main/resources/log.txt");
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			LocalDateTime currentDate = LocalDateTime.now();
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
			writer.printf("\n" + dateFormat.format(currentDate) + " FEED MONEY: $" + deposited + ", $" + balance.doubleValue() + "\n");
		} catch (FileNotFoundException e) {
			System.out.println("Log File Not Found");
		}
	}
	public static void codeLog(String product) {
		File logFile = new File("src/main/resources/log.txt");
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			LocalDateTime currentDate = LocalDateTime.now();
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
			writer.printf(dateFormat.format(currentDate) + " " + product +  " ");
		} catch (FileNotFoundException e) {
			System.out.println("Log File Not Found");
		}
	}

	public static void changeLog(BigDecimal balance) {
		File logFile = new File("src/main/resources/log.txt");
		try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			LocalDateTime currentDate = LocalDateTime.now();
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
			writer.printf("\n" + dateFormat.format(currentDate) + " GIVE CHANGE: $" + balance + " $0.00");
		} catch (FileNotFoundException e) {
			System.out.println("Log File Not Found");
		}
	}
}








