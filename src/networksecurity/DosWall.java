package networksecurity;

import java.net.*;
import java.sql.*;
import java.util.*;

public class DosWall implements Runnable
{
        Thread t;
        DOSGui gui;
        ServerSocket ss;
        Socket s;
        int max_ipc, max_ts;
	Connection c;
	Statement stm;
        boolean enable; //true if dos scanner is enabled false if not enabled
        Srv srv; //Server object reference for passing the accepted connection
        boolean start_flag=false;

        public DosWall(DOSGui gui, Srv srv)
        {
                this.gui=gui;
                this.srv=srv;
                enable=true;//By default dos attch scanning is enable
                t=new Thread(this);
        }

        public void enableDos() //to Enable DOS
        {
                enable=true;
        }

        public void disableDos() //to Disable DOS
        {
                enable=false;
        }

        public boolean alreadyInitiated()
        {
                return start_flag;
        }

        public void initiateDosWall(int max_ipc, int max_ts)
        {
                        start_flag=true; //indicate that DOSWall has already been started
				enable=true;
                        this.max_ipc=max_ipc;
			this.max_ts=max_ts;

			//Create Database connection
                        try
                        {
                                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                                c=DriverManager.getConnection("jdbc:mysql://localhost:3306/new_db","root","");
                                stm=c.createStatement();
                        }
                        catch(Exception ex)
                        {
                                System.out.println("Error in initiateDosWall method while establishing Database connection: "+ex);
                        }

			//Start Thread
                	t.start(); //Starting the Dos attack preventor with a new thread
        }

        public void run()
        {
                //Start Listening to the port number 8080
                try
                {
                        ss=new ServerSocket(8080);
                }
                catch(Exception ee)
                {
                }
                gui.ta_status.append("Starting the Dos Wall...\n");
                gui.ta_status.append("Waiting for connection...\nEnable="+enable+", start_flag: "+start_flag);
                while(true)
                {
                        try
                        {
                                s=ss.accept(); //Waiting to accept new connection from client
                                if(enable)
                                {
                                        InetAddress ia=s.getInetAddress();
                                        String ipadd=ia.toString();
							System.out.println("***************IP Address: "+ipadd+"******************************");
                                        if(checkDosAttack(ipadd))
                                        {
                                                //BLOCK
                                                gui.ta_status.append("\nBlocked: "+ipadd);
								updateIPTableList();
                                        }
                                        else
                                        {
                                                //ALLOW
                                                gui.ta_status.append("\nAllowed: "+ipadd);
								updateIPTableList();
                                                srv.connect(s);
                                        }
                                }
                                else
                                {
                                        //allow in any case
                                        InetAddress ia=s.getInetAddress();
                                        String ipadd=ia.toString();
                                        gui.ta_status.append("\nDosWall is disabled, didnt checked: "+ipadd);
                                        srv.connect(s);


                                }
                        }
                        catch(Exception ex)
                        {
                                System.out.println("Error in run method: "+ex);
                        }//end of catch

                }//end of while

        }//end of run method

        private boolean checkDosAttack(String ipadd) //Will return true if its a attack and false to allow
        {
                  removeInactiveIp(); //Remove all inactive IP entries from IP Table

			if(doesExist(ipadd))
			{
				if(checkIPC(ipadd)) //if true that means (ipc of passed ip_add)>max_ipc
				{
					return true; //Block its an attack
				}
				else
				{
					return false;//pass its not an attach yet
				}
			}
			else
			{
				addIP(ipadd);
				return false; //pass; its not an attack
			}
                        //return true; // delete this statement after writing the method

        }// end of checkDosAttack method

	//Check IPC method to checck the existing IP address for its last request time
	private boolean checkIPC(String ipadd) //will return true if ipc>max_ipc
	{
		String sqlstr="Select ipc from AIPT where ip='"+ipadd+"'";
                int ipc=0;
                try
                {
                        ResultSet rs=stm.executeQuery(sqlstr);
				rs.next();
                        ipc=rs.getInt("ipc");
                }
                catch(Exception ex2)
                {
                        System.out.println("Error in checkIPC method: "+ex2);
                }
		if(ipc>max_ipc)
		{
			updateTimestamp(ipadd);
			return true; //its an attack
		}
		else
		{
			ipc++;
                        //java.util.Date t=new java.util.Date();
				long t=new java.util.Date().getTime();
                        sqlstr="Update AIPT set ipc="+ipc+", t="+t+" where ip='"+ipadd+"'";
				//System.out.println(sqlstr);
                        try
                        {
                                int i=stm.executeUpdate(sqlstr);
                        }
                        catch(Exception ex)
                        {
                                System.out.println("Error in checkIPC method: "+ex);
                        }

			return false; //Its not a attach yet
		}

	}

	//Update the timestamp of an IP address
	private void updateTimestamp(String ipadd)
	{
		long t=new java.util.Date().getTime();
		String sqlstr="Update AIPT set t="+t+" where ip='"+ipadd+"'";
		System.out.println(sqlstr);
                try
                {
                        int i=stm.executeUpdate(sqlstr);
                }
                catch(Exception ex)
                {
                        System.out.println("Error in updateTimeStamp method: "+ex);
                }

	}

	//This method will add the passed IP address in AIPTable
	private void addIP(String ipadd)
	{
		long t=new java.util.Date().getTime();
		String sqlstr="Insert into AIPT values('"+ipadd+"',1,"+t+")";
		System.out.println(sqlstr);
                try
                {
                        int i=stm.executeUpdate(sqlstr);
                }
                catch(Exception ex)
                {
                        System.out.println("Error in addIP method: "+ex);
                }
		    updateIPTableList();
	}


	//This method check whether the passed ip address exists in AIPTable or not
	private boolean doesExist(String ipadd)
	{
		String sqlstr="Select * from AIPT where ip='"+ipadd+"'";
                try
                {
                        ResultSet rs=stm.executeQuery(sqlstr);
                        if(rs.next())
                        {
                                System.out.println("doesExist(): Ip address found in AIPTABLe");
                                return true;
                        }
                        else
                        {
                                System.out.println("Ip address not found in AIPTABLe");
                                return false;
                        }
                }
                catch(Exception ex)
                {
                        System.out.println("Error in doesExist method: "+ex);
                }
                return true;
	}

        private void removeInactiveIp()
        {
		try
		{
			//Find the current date and time
			//long tmp;
			//java.util.Date c_date=new java.util.Date(java.util.Date().getTime()); //Current date and time
			//tmp=c_date.getTime();
			//java.util.Date exp_date; //Inactive IP Expiry date
			//exp_date=new java.util.Date(tmp-max_ts);
			//System.out.println("c_date: "+c_date+", exp_date: "+exp_date);
			//java.sql.Date sqlDate = new java.sql.Date(exp_date.getTime());
			long t=new java.util.Date().getTime()-max_ts;
			String sqlstr="delete from AIPT where (t<"+t+")"; //delete all the entries from table whose time stamp is before the expire date&time
			System.out.println(sqlstr);
			stm.executeUpdate(sqlstr);
			System.out.println("**********Records Removed*********");
		}
		catch(Exception e)
		{
			System.out.println("Error in RemoveIP method(): "+e);
		}
        }//end of removeInactiveip() method
	  private void updateIPTableList()
	  {
		    String sqlstr="Select *  from AIPT";
		    String ip;
		    int ipc;
		    Double t;
		    gui.ta_aip_table.setText("");
		    gui.ta_aip_table.setText("IP Add \t\tIPC \tTimeStamp");
                try
                {
                        ResultSet rs=stm.executeQuery(sqlstr);
				while(rs.next())
				{
					ip=rs.getString("ip");
                        	ipc=rs.getInt("ipc");
                        	t=rs.getDouble("t");
					gui.ta_aip_table.append("\n"+ip+"\t"+ipc+"\t"+t);
				}
                }
                catch(Exception ex2)
                {
                        System.out.println("Error in checkIPC method: "+ex2);
                }

	  }
}//end of class
