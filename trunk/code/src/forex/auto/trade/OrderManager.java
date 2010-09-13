package forex.auto.trade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrderManager {
	
	private static Log log = LogFactory.getLog(OrderManager.class);

	private ArrayList<Order> activeOrders = new ArrayList<Order>();
	
	
	HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
	private static OrderManager instance = new OrderManager();
	private ArrayList<String> orderCMD = new ArrayList<String>();
	

	private OrderManager() {
	}

	public static OrderManager getInstance() {
		return instance;
	}

	public int syncOrder(Order _order) {
		
		activeOrders.add(_order);
		
		
		int id = _order.getOrderTicket();
		Order order = orders.get(id);
		int ret = 0;
		if (order != null) {
			ret = 1;
		}
		orders.put(id, _order);
		return ret;

	}

	public void sendOrder(Order _order) {
		int orderType = _order.getType();

		StringBuffer sb = new StringBuffer();

		if (orderType == Order.OP_BUY) {
			sb.append("B");
			sb.append(_order.getPrice());
			sb.append("|");
			sb.append(_order.getStoploss());
			sb.append("|");
			sb.append(_order.getProfit());
			sb.append("|");
			sb.append(87109493);
		} else if (orderType == Order.OP_SELL) {
			sb.append("S");
			sb.append(_order.getPrice());
			sb.append("|");
			sb.append(_order.getStoploss());
			sb.append("|");
			sb.append(_order.getProfit());
			sb.append("|");
			sb.append(87109493);

		}
		orderCMD.add(sb.toString());
	}

	public Order findOrder(int orderId) {
		Order order = orders.get(orderId);
		return order;

	}

	public void closeOrder(Order _order) {

		StringBuffer sb = new StringBuffer();

		sb.append("C");
		sb.append(_order.getOrderTicket());
		sb.append("|");
		sb.append(_order.getVolume());
		sb.append("|");
		sb.append(_order.getPrice());
		orderCMD.add(sb.toString());

	}

	public void modifyOrder(Order _order) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("T");
		sb.append(_order.getOrderTicket());
		sb.append("|");
		sb.append(_order.getPrice());
		sb.append("|");
		sb.append(_order.getStoploss());
		sb.append("|");
		sb.append(_order.getProfit());
		orderCMD.add(sb.toString());

	}
	
	public ArrayList<String> getOrderCMD() {
		return orderCMD;
	}

	public void clearSyncState() {
		
		if(activeOrders.size()>0) {
			activeOrders.clear();
		}
		
		Collection<Order> orderArrays = orders.values();
		for( Order o : orderArrays) {
			if(o.getState() == Order.UNSYNC) {
				if(log.isInfoEnabled()) {
					log.info("Order:" + o.getOrderTicket()+" is not actived,removed!");
				}
				orders.remove(o.getOrderTicket());
			} else {
				o.unSyncState();
			}
		}
		
		
	}

	public  ArrayList<Order> getActiveOrders() {
		return activeOrders;
		
	}

}
