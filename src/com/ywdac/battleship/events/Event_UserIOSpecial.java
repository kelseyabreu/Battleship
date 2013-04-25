package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_UserIOSpecial 
extends BaseEvent
{
	private float m_X;
	private float m_Y;
	
	public Event_UserIOSpecial( float x, float y )
	{
		this.m_Type          = EVENT.USER_IOSpecial;
		this.m_PriorityLevel = BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor    = "";
		this.m_Timestamp     = System.currentTimeMillis( );
		
		m_X = x;
		m_Y = y;
	}
	
	public float getX( )
	{
		return m_X;
	}
	
	public float getY( )
	{
		return m_Y;
	}
}
