package com.gdiot.udp;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;

public class UdpPacketMsg {
	private InetSocketAddress serverAddress;
	private InetSocketAddress clientAddress;
	
	private byte[] data;
	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	public InetSocketAddress getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(InetSocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
	
}
