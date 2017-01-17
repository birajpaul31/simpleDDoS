package networksecurity;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class DOSGui extends Frame implements ActionListener, WindowListener
{
        Label l_m_ipc, l_m_t_stamp, l_status, l_aip_table;
        TextField tf_m_ipc, tf_m_t_stamp;
        TextArea ta_status, ta_aip_table;
        Button b;
        Panel p_north, p_center, p_north_left, p_center_left, p_center_right;
        DosWall dwall;
        boolean bflag=false; //Button start or stop flag;
        Srv srv; //Reference of Server class

        DOSGui(Srv srv)
        {
                super("DDOS Attack Preventer");
                this.srv=srv;
                dwall=new DosWall(this, srv);
                setupGUI();

        }
        private void setupGUI()
        {
                //INITIALIZE ALL COMPONANTS

                l_m_ipc=new Label("MAX IPC");
                l_m_t_stamp=new Label("Max. Time Stamp (mili. sec.");
                l_status=new Label("Attack Status");
                l_aip_table=new Label("AIP TABLE");

                tf_m_ipc=new TextField("20",20);
                tf_m_t_stamp=new TextField("5000",20);

                ta_status=new TextArea(3,10);
                ta_aip_table=new TextArea(3,20);

                b=new Button("Disable");

                //INITIALISE ALL CONTAINERS

                p_north=new Panel();
                p_center=new Panel();
                p_north_left=new Panel();
                p_center_left=new Panel();
                p_center_right=new Panel();

                p_north.setLayout(new GridLayout(1,2));
                p_north_left.setLayout(new GridLayout(2,2));
                p_center.setLayout(new GridLayout(1,2));
                p_center_left.setLayout(new BorderLayout());
                p_center_right.setLayout(new BorderLayout());

                p_north_left.add(l_m_ipc);
                p_north_left.add(tf_m_ipc);
                p_north_left.add(l_m_t_stamp);
                p_north_left.add(tf_m_t_stamp);

                //REGISTER EVENT
                b.addActionListener(this);

                //SET COLORS
                p_north.setBackground(Color.pink);

                //ADD COMPONANTS IN CONTAINERS

                p_north.add(p_north_left);
                p_north.add(b);

                p_center_left.add(l_status, BorderLayout.NORTH);
                p_center_left.add(ta_status, BorderLayout.CENTER);

                p_center_right.add(l_aip_table, BorderLayout.NORTH);
                p_center_right.add(ta_aip_table, BorderLayout.CENTER);

                p_center.add(p_center_left);
                p_center.add(p_center_right);

                add(p_north, BorderLayout.NORTH);
                add(p_center, BorderLayout.CENTER);

                //SET SIZE AND VISIBLITY
		    addWindowListener(this);
                setSize(700,500);
                setVisible(true);

        } //END OF setupGui method

        public void actionPerformed(ActionEvent ae)
        {
                if(bflag)
                {
                        //Start the code to prevent DDoS Attack
                        b.setBackground(Color.green); //indicates that scanning is on
                        b.setLabel("Disable");
                        bflag=false;
                        int m_ipc=0,m_ts=0; // Max IPC and Max Time stamp
                        int flag=0;

                        try
                        {
                                m_ipc=Integer.parseInt(tf_m_ipc.getText());
                                m_ts=Integer.parseInt(tf_m_t_stamp.getText());
                        }
                        catch(NumberFormatException e)
                        {
                                ta_status.append("Please enter a valid number\n");
                                flag=1;
                        }
                        if(flag==0)
                        {
                                if(dwall.alreadyInitiated())
                                {
                                        //dont initiated
                                        dwall.enableDos();
                                }
                                else
                                {
                                        dwall.initiateDosWall(m_ipc,m_ts);
                                }
                        }
                }
                else
                {
                        dwall.disableDos();
                        bflag=true;
                        b.setBackground(Color.red);
                        b.setLabel("Enable");
                }
        }

	  public void windowOpened (WindowEvent we)
	  {

	  }
	  public void windowClosed (WindowEvent we)
	  {

	  }
	  public void windowClosing (WindowEvent we)
	  {
		dispose();
	  }
	  public void windowActivated (WindowEvent we)
	  {

	  }
	  public void windowDeactivated (WindowEvent we)
	  {

	  }
	  public void windowIconified (WindowEvent we)
	  {

	  }
	  public void windowDeiconified (WindowEvent we)
	  {

	  }


}//end of class
