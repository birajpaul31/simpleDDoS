package networksecurity;

//Just a test class for date
import java.util.*;
class Dtest
{
	public static void main(String args[])
	{
			int max_ts=5000;
			long tmp;
			Date c_date=new Date(); //Current date and time
			tmp=c_date.getTime();
			Date exp_date; //Inactive IP Expiry date
			exp_date=new Date(tmp-max_ts);
			System.out.println("c_date: "+c_date+", exp_date: "+exp_date);
	}
}