package com.ywdac.battleship.events;

public abstract class BaseEvent 
{
	static enum PRIORITY
	{
		LOW,
		NORMAL,
		HIGH,
		CRITICAL
	}
	
	protected Events.EVENT m_Type;
	protected String m_Descriptor;
	protected long m_Timestamp;
	protected PRIORITY m_PriorityLevel;
	
	public Events.EVENT getType( )
	{
		return m_Type;
	}
	
	public String getDescriptor( )
	{
		return m_Descriptor;
	}
	
	public long getTimestamp( )
	{
		return m_Timestamp;
	}
	
	public PRIORITY getPriorityLevel( )
	{
		return m_PriorityLevel;
	}
}
