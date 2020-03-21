import java.util.Scanner;

public class p1 {

    public static String loginId;
	private static int ans;
	private static Scanner in = new Scanner(System.in);
	private static Scanner input = new Scanner(System.in);

    private static void println(String str) {
		System.out.println(str);
	}

	private static void print(String str){
		System.out.print(str);
	}

	private static void intInput() {
		while (!in.hasNextInt()){
			in.next();
			print("Please enter an integer: ");
		}
	}

	private static void createNewCustomer() {
		try {
			println("New Customer Being created");
			print("Name: ");
			String name = input.nextLine();
			boolean finished = false;
			String gender = "";
			while (!finished) {
				print("Gender (M/F): ");
				gender = input.nextLine();
				if ("mMfF".contains(gender))
					finished = true;
			}
			print("Age: ");
			String age = input.nextLine();
			print("Pin: ");
			String pin = input.nextLine();
			if (BankingSystem.newCustomer(name, gender, age, pin)) {
				println("Pin: " + pin + "\nSuccess in creating new customer");
			} else {
				println("Failure: wrong information submitted");
			}
		} catch (Exception e) {
			println("Error in creating customer");
			e.printStackTrace();
		}
	}

	private static void customerLogin() {
		try {
			println("Customer Login");
			print("ID: ");
			loginId = input.nextLine();
			print("Pin: ");
			String pin = input.nextLine();
			if (loginId.equals("0") && pin.equals("0")) {
				adminLogin();
			}
			else if (BankingSystem.login(loginId, pin)) {
				customerScreen(loginId);
			} else {
				println("WRONG ID OR PIN.");
			}
		} catch (Exception e) {
			println("Error in loging in");
			e.printStackTrace();
		}
	}

	private static void openAccount(String id, String balance, String accType)  {
		try {
			println("Open Account");
			print("Enter an ID: ");
			id = input.nextLine();
			boolean finished = false;
			while (!finished) {
				print("What type of account (C)hecking, or (S)avings: ");
				accType = input.nextLine();
				if ("cCsS".contains(accType))
					finished = true;
			}
			print("Enter your initial balance: ");
			balance = input.nextLine();
			println(loginId);
			if (loginId.equals(id)) {
				BankingSystem.openAccount(id, accType, balance);
				println("Bank Account opened");
			}
		} catch (Exception e) {
			println("Error in opening account");
		}
	}

	private static void closeAccountforCust(String accNum)  {
		try {
			println("Close Account");
			print("Enter an account number: ");
			accNum = input.nextLine();
			if (BankingSystem.isOwner(accNum)) {
				BankingSystem.closeAccount(accNum);
			}
			else {
				println("Error");
			}
		} catch (Exception e) {
			println("Error in closing account");
		}
	}

	private static void depositMoney(String accNum, String amount) {
		try {
			println("Deposit");
			print("Enter an account number: ");
			accNum = input.nextLine();
			print("Enter the amount to deposit: ");
			amount = input.nextLine();
			BankingSystem.deposit(accNum, amount);
		} catch (Exception e) {
			println("Error in deposit of money");
		}
	}

	private static void withdrawMoney(String accNum, String amount)  {
		try {
			println("Withdraw");
			print("Enter your account number: ");
			accNum = input.nextLine();
			print("Enter the amount to withdraw: ");
			amount = input.nextLine();
			if (BankingSystem.isOwner(accNum)) {
				BankingSystem.withdraw(accNum, amount);
			} else {
				println("Cannot withdraw from another account");
			}
		} catch (Exception e) {
			println("Error in withdrawing money");
		}
	}

	private static void transferMoney(String accNum, String accNum2, String amount)  {
		try {
			println("Transfer");
			print("Enter source account number: ");
			accNum = input.nextLine();
			print("Enter destination account number: ");
			accNum2 = input.nextLine();
			print("Enter transfer amount: ");
			amount = input.nextLine();
			if (BankingSystem.isOwner(accNum)) {
				BankingSystem.transfer(accNum, accNum2, amount);
			} else {
				println("Invalid account number(s).");
			}
		} catch (Exception e) {
			println("Error in transferring money");
		}
	}

	private static void accSummary()  {
		try {
			println("Account Summary");
			BankingSystem.accountSummary(loginId);
		} catch (Exception e) {
			println("Error in creating account");
			e.printStackTrace();
		}
	}

	private static void customerScreen(String custId)  {
		boolean done = false;
		String balance, accNum, accNum2, id, amount;
		balance = accNum = accNum2 = id = amount = "";
		String accType = "";
		try {
			while (!done) {
				println("Customer Main Menu" +
						"\n1. Open Account" +
						"\n2. Close Account" +
						"\n3. Deposit" +
						"\n4. Withdraw" +
						"\n5. Transfer" +
						"\n6. Account Summary" +
						"\n7. Exit");
				print("Please enter a number: ");
				intInput();
				ans = in.nextInt();
				switch (ans) {
				case 1:
					openAccount(id, balance, accType);
					break;
				case 2:
					closeAccountforCust(accNum);
					break;
				case 3:
					depositMoney(accNum, amount);
					break;
				case 4:
					withdrawMoney(accNum, amount);
					break;
				case 5:
					transferMoney(accNum, accNum2, amount);
					break;
				case 6:
					println("Account Summary");
					accSummary();
					break;
				case 7:
					done = true;
					break;
				default:
					println("Please choose a valid option (1, 2, 3, 4, 5, 6, or 7).");
					break;
				}
			} 
		} catch (Exception e) {
			println("Error");
		}
	}

	private static void accountSummary() {
		try {
			println("Account Summary");
			print("ID: ");
			String id = input.nextLine();
			BankingSystem.accountSummary(id);
		} catch (Exception e) {
			println("Error in creating account");
			e.printStackTrace();
		}
	}

	private static void customerInformation() {
		try {
			println("Customer Information");
			BankingSystem.reportA();
		} catch (Exception e){
			println("Error in getting customer information");
			e.printStackTrace();
		}
	}

	private static void getAverageTotalBalance() {
		try {
			println("Average total balance");
			String minAge, maxAge;
			print("Min age: ");
			minAge = input.nextLine();
			print("Max age: ");
			maxAge = input.nextLine();
			BankingSystem.reportB(minAge, maxAge);
		} catch (Exception e){
			println("Error in getting average total balance");
			e.printStackTrace();
		}
	}

	private static void adminLogin()  {
		boolean done = false;
		while (!done) {
			println("Administrator Main Menu " +
					"\n1. Account Summary for a Customer " +
					"\n2. Report A :: Customer Information with Total Balance in Decreasing Order " +
					"\n3. Report B :: Find the Average Total Balance Between Age Groups" +
					"\n4. Exit");
			print("Please enter a number: ");
			while (!in.hasNextInt()) {
				in.next();
				println("Please enter an integer");
			}
			ans = in.nextInt();
			switch (ans) {
			case 1:
				accountSummary();
				break;
			case 2:
				customerInformation();
				break;
			case 3: 
				getAverageTotalBalance();
				break;
			case 4:
				println("Exit");
				done = true;
				break;
			default:
				println("Please choose a valid option (1, 2, 3, or 4).");
			}
		}
	}

	public static void openMainMenu(){
		boolean done = false;
		while (!done) {
			println("Welcome to the Self Service Banking System!" + 
			"\n1. New Customer" + 
			"\n2. Customer Login" + 
			"\n3. Exit");
			print("Please enter a number: ");
			intInput();
			ans = in.nextInt();
			switch (ans) {
			case 1:
				createNewCustomer();
				break;
			case 2:
				customerLogin();
				break;
			case 3:
				done = true;
				break;
			default:
				println("Please choose a valid option (1, 2, or 3).");
				break;
			}
		}
	}
}