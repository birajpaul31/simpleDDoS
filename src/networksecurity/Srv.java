package networksecurity;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

class Srv extends Frame implements ActionListener, WindowListener
{
        TextArea ta;
        Button b_start, b_dos;
        DOSGui dg;

        public Srv()
        {
                super("Demo Server");
                dg=new DOSGui(this);
                dg.setVisible(false);
                setupGui();
        }
        private void setupGui()
        {
                ta=new TextArea();

                b_start=new Button("Start Server");
                b_start.addActionListener(this);


                b_dos=new Button("DOSWall");
                b_dos.addActionListener(this);

                add(ta);
                //add(b_start, BorderLayout.NORTH);
                add(b_dos, BorderLayout.SOUTH);
			addWindowListener(this);
                setSize(400,400);
                setVisible(true);
        }

        public void connect(Socket s)
        {
                InetAddress ia=s.getInetAddress();
                String ipadd=ia.toString();
                ta.append("New Connection established with-> "+ipadd);

        }

        public void actionPerformed(ActionEvent ae)
        {
                if(ae.getSource()==b_start)
                {
                }
                else
                {
                        dg.setVisible(true);
                }
        }

        public static void main(String args[]) throws Exception
        {
                new Srv();
        }

	  public void windowOpened (WindowEvent we)
	  {

	  }
	  public void windowClosed (WindowEvent we)
	  {

	  }
	  public void windowClosing (WindowEvent we)
	  {
		System.exit(0);
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


}
