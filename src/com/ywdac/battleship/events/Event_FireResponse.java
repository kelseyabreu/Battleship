package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_FireResponse 
	extends BaseEvent
{
	private int m_Response;
	private int m_X;
	private int m_Y;
	
	/// -1 = oob/repeat, 0 - miss, 1 - hit, 2 - sink
	public Event_FireResponse( int response, int x, int y )
	{
		this.m_Type 			= EVENT.FIRE_RESPONSE;
		this.m_PriorityLevel	= BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor		= "";
		this.m_Timestamp		= System.currentTimeMillis();
		
		m_Response = response;
		m_X = x;
		m_Y = y;
	}
	
	public int getResponse()
	{
		return m_Response;
	}
	
	public int[ ] getCoords( )
	{
		int c[ ] = new int[ ]{ m_X, m_Y };
		return c;
	}
}
