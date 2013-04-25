package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.Ship;

public class Event_ShipAdd 
	extends BaseEvent
{
	private Ship m_Ship;
	
	public Event_ShipAdd( Ship ship )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.NORMAL;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.SHIP_ADD;
		
		m_Ship = ship;
	}
	
	public Ship getShip( )
	{
		return m_Ship;
	}
}
