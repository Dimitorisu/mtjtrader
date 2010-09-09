package forex.auto.trade.lib;

import java.io.IOException;
import java.util.Properties;

import net.wimpi.telnetd.TelnetD;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
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
			m_IO.write("JTrader 1.0" + BasicTerminalIO.CRLF);
			m_IO.write(BasicTerminalIO.CRLF);
			m_IO.write(BasicTerminalIO.CRLF);
			// output stored environment variables
			// ConnectionData cd = m_Connection.getConnectionData();
			String input = readInput(m_IO);
			;
			CommadProcessor cp = CommadProcessor.getInstance();
			while (!"exit".equalsIgnoreCase(input)) {

				String output = cp.executeCommand(input);
				m_IO.write(output + BasicTerminalIO.CRLF);

				input = readInput(m_IO);

			}

			m_IO.write("Goodbye!" + BasicTerminalIO.CRLF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// run

	private String readInput(BasicTerminalIO mIO) throws IOException {
		StringBuffer input = new StringBuffer();

		mIO.write("$");
		int i = 0;

		while ((i = mIO.read()) != 10) {
			char c = (char) i;
			input.append(c);
			mIO.write(c);
		}

		mIO.write(BasicTerminalIO.CRLF);
		return input.toString();
	}

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
