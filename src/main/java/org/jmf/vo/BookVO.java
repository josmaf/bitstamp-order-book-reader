package org.jmf.vo;

public class BookVO {
	
	Long timestamp;
	Double[][] bids;
	Double[][]asks;
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Double[][] getBids() {
		return bids;
	}
	public void setBids(Double[][] bids) {
		this.bids = bids;
	}
	public Double[][] getAsks() {
		return asks;
	}
	public void setAsks(Double[][] asks) {
		this.asks = asks;
	}
	
	
}
