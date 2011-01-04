package org.jtrader.data.provider;

import org.jtrader.core.Candle;

public interface PriceProvider {
	public Candle read();
}
