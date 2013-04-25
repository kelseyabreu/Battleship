package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_FireUpon 
	extends BaseEvent
{
	
	private String m_Coordinate;
	
	public Event_FireUpon(String coordinates)
	{
		this.m_Type 			= EVENT.FIRE_UPON;
		this.m_PriorityLevel 	= BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor 		= "";
		this.m_Timestamp 		= System.currentTimeMillis();
		
		m_Coordinate = coordinates;
	}
	
	public String getCoords( )
	{
		return m_Coordinate;
	}
}
