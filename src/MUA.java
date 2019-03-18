import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author stuart spiegel Date: 2/14/2019 Email client for sending email to one
 *         recipient
 */
public class MUA {

	public static void main(String argv[]) throws Exception {

		// Scanner input = new Scanner(System.in);
		// //get sender info
		// System.out.println("What is the email address of the sender?
		// (username@domainname): ");
		// String sender = input.nextLine();
		//
		// //get recipient info
		// System.out.println("What is the recipient email address?
		// (username@domainname): ");
		// String receieve = input.nextLine();
		//
		// //Get the subject line from the user
		// System.out.println("What is the subject for the email?: ");
		// String subject = input.nextLine();

		// establish TCP connection with the mail server
	  System.out.println("Enter the mail server you wish to connect to (example:  exmail.dickinson.edu\n");
		String hostName = new String();
		Scanner emailScanner = new Scanner(System.in);
		hostName = emailScanner.next();
		Socket emailSocket = new Socket(hostName, 778);

		// create buffered reader to read one line at a time of user input
		InputStream inStream = emailSocket.getInputStream();
		InputStreamReader toRead = new InputStreamReader(inStream);
		BufferedReader buff = new BufferedReader(toRead);

		// Read the greeting from the server
		String serverResponse = buff.readLine();
		System.out.println(serverResponse);

		// check for successful server connection
		if (!serverResponse.startsWith("220")) {
			throw new Exception("220 reply not received from server.\n");
		}

		// get reference to server OutputStream
		OutputStream outputStream = emailSocket.getOutputStream();

		// Send HELO Command
		String command = "HELO \r\n";
		System.out.print(command); // output hello command

		outputStream.write(command.getBytes("US-ASCII"));
		serverResponse = buff.readLine();
		System.out.println(serverResponse); // output the server response

		// check for successful response from server
		if (!serverResponse.startsWith("250")) {
			throw new Exception("250 reply not received from server.\n");
		}

		// send AUTH LOGIN Command
		String auth_command = "AUTH LOGIN \r\n";
		System.out.println(auth_command);

		outputStream.write(auth_command.getBytes("US-ASCII"));
		serverResponse = buff.readLine();
		System.out.println(serverResponse); // output the server response

		// check for successful response from server
		if (!serverResponse.startsWith("334")) {
			throw new Exception("334 You have provided incorrect credentials for login.\n");
		}

		// send AUTH LOGIN ID
		String logIn_id = emailScanner.next() + "\r\n";
		System.out.println(logIn_id);

		outputStream.write(logIn_id.getBytes("US-ASCII"));
		serverResponse = buff.readLine();
		System.out.println(serverResponse); // output the server response

		// check for successful response from server
		if (!serverResponse.startsWith("334")) {
			throw new Exception("334 You have provided incorrect credentials for login.\n");
		}

		// send AUTH LOGIN Password
		String logIn_pw = emailScanner.next() + "\r\n";
		System.out.println(logIn_pw);

		outputStream.write(logIn_pw.getBytes("US-ASCII"));
		serverResponse = buff.readLine();
		System.out.println(serverResponse); // output the server response


		// Send MAIL FROM Command
		System.out.println("Please enter your (source) e-mail address (example: me@exmail.dickinson.edu:\n");
		String sourceAddress = emailScanner.next();
		String mailFromCommand = "MAIL FROM: <" + sourceAddress + "> \r\n";
		System.out.println(mailFromCommand);
		outputStream.write(mailFromCommand.getBytes("US-ASCII"));

		serverResponse = buff.readLine();
		System.out.println(serverResponse);

		if (!serverResponse.startsWith("250"))
			throw new Exception("250 reply not received from server.\n");

		// send RCPT TO command
		System.out.println("Please type the destination e-mail address (example:exmail@dickinson.edu):\n");
		String destEmailAddress = emailScanner.next();
		String fullAddress = "RCPT TO: <" + destEmailAddress + "> \r\n";
		System.out.println(fullAddress);
		outputStream.write(fullAddress.getBytes("US-ASCII"));

		serverResponse = buff.readLine();
		System.out.println(serverResponse);

		// RCPT check
		if (!serverResponse.startsWith("250")) {
			System.out.println("Did not reach");

			throw new Exception("250 reply not received from server.\n");
		}

		// Send DATA command
		String dataString = new String();
		dataString = "DATA \r\n";
		System.out.println(dataString);
		outputStream.write(dataString.getBytes("US-ASCII"));


		System.out.println("Subject: ");
		String subject_id = emailScanner.next();
		String fullSubject = "SUBJECT: " + subject_id + "\r\n";
		outputStream.write(fullSubject.getBytes("US-ASCII"));

		serverResponse = buff.readLine();
		System.out.println(serverResponse);

		if (!serverResponse.startsWith("354"))
			throw new Exception("354 reply not received from server.\n");


		// Send the message data for the email
		System.out.println("Enter your message, enter '.' on a separate line to end message data entry:\n");
		String input = new String();
		input = emailScanner.nextLine() + "\r\n";
		while (input.charAt(0) != '.') {
			outputStream.write(input.getBytes("US-ASCII"));
			input = emailScanner.nextLine();
			String finalInput = input + "\r\n";
		}

		// end line properly
		input = "\r\n.\r\n";
		outputStream.write(input.getBytes("US-ASCII"));
		serverResponse = buff.readLine();
		System.out.println(serverResponse);
		if (!serverResponse.startsWith("250"))
			throw new Exception("250 reply not received from server\n");

		// send QUIT command
		String quitCommand = new String();
		quitCommand = "QUIT";
		outputStream.write(command.getBytes("US-ASCII"));

	}

}
