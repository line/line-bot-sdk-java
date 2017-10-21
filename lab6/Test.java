package skeleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
	public static final String MESSAGE_WELCOME = "Welcome to Starbucks!";
	public static final String MESSAGE_SEPERATOR = "===============================================";
	public static final String MESSAGE_SELECT = "Select functions:\n 1) Order\n 2) An order is ready\n 3) Check the queue\n 4) Exit";
	public static final String MESSAGE_SELECT_ONE = "Which drink you would like to order?";
	public static final String MESSAGE_SELECT_TWO = "Which order is ready?";
	public static final String MESSAGE_SELECT_THREE = "The following ids are in the queue:";
	public static final String MESSAGE_SELECT_FOUR = "Goodbye!~";
	public static final String MESSAGE_ERROR = "Please enter the number.";

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// TODO: possible setup
		Subject starbucks = new Subject();
		Adapter adapter = new Adapter();
		int id = 1;
		
		try {
			System.out.println(MESSAGE_WELCOME);
			loop: while(true) {
				System.out.println(MESSAGE_SEPERATOR);
				System.out.println(MESSAGE_SELECT);
				String in = br.readLine();
				switch(in){
				case "1":
					System.out.println(MESSAGE_SELECT_ONE);
					String drink = br.readLine();
					// TODO: give an ordered id
					// order is successful only if edit distance <= 3
					String beverage = adapter.getBeverage(drink); 
					if ( beverage != null ) {
						Observer customer = new Observer(id++);
						starbucks.register(customer);
						customer.subscribe(starbucks);
						System.out.println(
								String.format("%s is ordered and your order id is %d"
										, beverage, customer.getID())); //success
					} else {
						System.out.println(String.format("%s not found.", drink)); //fail
					}
					break;
				case "2":
					System.out.println(MESSAGE_SELECT_TWO);
					String order = br.readLine();
					// TODO: act appropriately according to your design
					starbucks.setMessage(order);
					break;
				case "3":
					System.out.println(MESSAGE_SELECT_THREE);
					// print all id in the queue
					for( Observer obs : starbucks.getQueue() ) System.out.println(obs.getID()); 
					break;
				case "4":
					break loop;
				default:
					System.out.println(MESSAGE_ERROR);
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
