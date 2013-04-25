package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_TurnBegin 
	extends BaseEvent
{
	private String m_Who;
	
	public Event_TurnBegin( String who )
	{
		this.m_Type          = EVENT.TURN_BEGIN;
		this.m_PriorityLevel = BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor    = "";
		this.m_Timestamp     = System.currentTimeMillis( );
		
		m_Who = who;
	}
	
	public String getWho( )
	{
		return m_Who;
	}
}
