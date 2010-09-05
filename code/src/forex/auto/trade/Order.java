package forex.auto.trade;

public class Order {
	
	int orderTicket;
	int type;
	double volume;
	double price; 
	double stoploss;
	double profit;
	
	
	public Order(int _orderTicket, int _type, double _volume,
			double _price, double _stoploss, double _profit) {
		
		this.orderTicket = _orderTicket;
		this.type = _type;
		this.volume = _volume;
		this.price = _price;
		this.stoploss = _stoploss;
		this.profit = _profit;
		
	}


	public int getOrderTicket() {
		return orderTicket;
	}


	public void setOrderTicket(int orderTicket) {
		this.orderTicket = orderTicket;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getStoploss() {
		return stoploss;
	}


	public void setStoploss(double stoploss) {
		this.stoploss = stoploss;
	}


	public double getProfit() {
		return profit;
	}


	public void setProfit(double profit) {
		this.profit = profit;
	}
	
	

}
