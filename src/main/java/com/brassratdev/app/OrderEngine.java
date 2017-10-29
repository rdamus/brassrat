package com.brassratdev.app;

import java.util.ArrayDeque;
import java.util.Queue;

import com.brassratdev.AbstractEngine;
import com.brassratdev.beeline.model.Order;

public class OrderEngine extends AbstractEngine{
    private static Long orderId = 0L;
    private double orderFrequency = 5;//5Hz
    private double orderPeriod = -1;
    private Queue<Order> orders = new ArrayDeque<Order>(5);
    private OrderProxy orderProxy = new MockOrderProxy();
    
    public OrderEngine(){
	super("OrderEngine");
    }
    boolean shouldOrder(){
	return ( getDeltaTime() > getOrderPeriod() );
    }
    
    public void runEngine() {
	if (calculatePeriod()) {
	    if (shouldOrder()) {
		placeOrder();
	    }
	}
    }
    
    public void init(){
	calculatePeriod();
	System.out.println("OrderEngine - freq: " + getOrderFrequency() + ", period: " + getOrderPeriod());
    }
    
    private boolean calculatePeriod(){
	orderPeriod = ( 1.0 / orderFrequency );// * 1000.0;
	return orderPeriod > 0;
    }
    
    void placeOrder(){
        Integer noOrders = Math.round( (float)deltaTime / (float)orderPeriod );
        orderProxy.placeOrders( noOrders );
    }
    
    private Long incrOrderId(){
	return ++orderId;
    }

    public static Long getOrderId() {
        return orderId;
    }

    public double getOrderFrequency() {
        return orderFrequency;
    }

    public void setOrderFrequency(double orderFrequency) {
        this.orderFrequency = orderFrequency;
    }

    public double getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(double orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public Queue<Order> getOrders() {
        return orders;
    }

    public void setOrders(Queue<Order> orders) {
        this.orders = orders;
    }
    
    class SimulatorOrderProxy extends OrderProxy {
	void placeOrders(Integer noOrders) {
	    for (int i = 0; i < noOrders; i++) {
		System.out.println("placing " + noOrders + " orders");
		getSimulator().addOrder(new Order(incrOrderId()));
	    }
	}
    }
    
    class MockOrderProxy extends OrderProxy {
	void placeOrders(Integer noOrders) {

	    for (int i = 0; i < noOrders; i++) {
		System.out.println("placing order id " + incrOrderId());
	    }
	}
    }

}

abstract class OrderProxy{
    abstract void placeOrders(Integer noOrders);
}



