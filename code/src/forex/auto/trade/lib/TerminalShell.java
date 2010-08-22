package forex.auto.trade.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import net.wimpi.telnetd.TelnetD;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class TerminalShell implements Shell {

	private Connection m_Connection;
	private BasicTerminalIO m_IO;

	public void run(Connection con) {
		m_Connection = con;
		m_IO = m_Connection.getTerminalIO();
		// register the connection listener
		m_Connection.addConnectionListener(this);

		try {
			m_IO.write("Simple2Shell" + BasicTerminalIO.CRLF);
			// output stored environment variables
			ConnectionData cd = m_Connection.getConnectionData();
			HashMap env = cd.getEnvironment();
			Iterator it = env.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				m_IO.write(key + "=" + env.get(key) + BasicTerminalIO.CRLF);
			}
			m_IO.write("Goodbye!" + BasicTerminalIO.CRLF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// run

	public void connectionTimedOut(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_TIMEDOUT");
			m_IO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// close connection
		m_Connection.close();
	}// connectionTimedOut

	public void connectionIdle(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_IDLE");
			m_IO.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}// connectionIdle

	public void connectionLogoutRequest(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_LOGOUTREQUEST");
			m_IO.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// connectionLogout

	public void connectionSentBreak(ConnectionEvent ce) {
		try {
			m_IO.write("CONNECTION_BREAK");
			m_IO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// connectionSentBreak

	public static Shell createShell() {
		return new TerminalShell();
	}// createShell

	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			props.load(TerminalShell.class
					.getResourceAsStream("/telnetd.properties"));
			// 1. create singleton instance
			TelnetD daemon = TelnetD.createTelnetD(props);
			// 2.start serving
			daemon.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
