package forex.auto.trade.core;

import forex.auto.trade.lib.Candle;

public interface PriceProvider {
	public Candle read();
}
