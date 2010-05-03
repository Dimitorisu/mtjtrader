package forex.auto.trade.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import forex.auto.trade.lib.Candle;

public class FilePriceProvider implements PriceProvider {
	private static Log log = LogFactory.getLog(FilePriceProvider.class);
	BufferedReader reader = null;

	public FilePriceProvider() {
		// TODO Auto-generated constructor stub
	}

	public void init() {
		File f = new File("d:\\EURUSD1.csv");
		try {
			reader = new BufferedReader(new FileReader(f));

		} catch (FileNotFoundException e) {
			if (log.isErrorEnabled()) {
				log.error("Read file error,file:" + f, e);
			}

		}
	}

	public Candle read() {

		if (reader != null) {
			try {
				String line = reader.readLine();
				if (line != null) {
					
					StringTokenizer tokens = new StringTokenizer(line, ",");
					while (tokens.hasMoreTokens()) {
						String date = tokens.nextToken();
					
						String time = tokens.nextToken();
						String open = tokens.nextToken();
						String high = tokens.nextToken();
						String low = tokens.nextToken();
						String close = tokens.nextToken();
						// String volumn = tokens.nextToken();
						Candle c = new Candle();
						int year = Integer.parseInt(date.substring(0, 4));
						int month = Integer.parseInt(date.substring(5, 7)) -1;
						int ddate = Integer.parseInt(date.substring(8, 10));
						int hrs = Integer.parseInt(time.substring(0, 2));
						int min = Integer.parseInt(time.substring(3, 5));
						
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, ddate, hrs, min,0);

						c.setTime(cal.getTimeInMillis());
						c.setOpen(Double.parseDouble(open));
						c.setHigh(Double.parseDouble(high));
						c.setLow(Double.parseDouble(low));
						c.setClose(Double.parseDouble(close));
						return c;
					}
				}

			} catch (IOException e) {
				if (log.isErrorEnabled()) {
					log.error("Read file error", e);
				}
			}
		}

		return null;
	}

	public static void main(String[] args) {
		FilePriceProvider p = new FilePriceProvider();
		p.init();
		Candle c = null;
		while ((c = p.read()) != null) {
			System.out.println("c:" + c);
		}
	}

}
