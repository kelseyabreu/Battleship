package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.*;

public class Event_ShipHit 
	extends BaseEvent
{
	private Ship m_Ship = null;
	
	public Event_ShipHit( Ship ship )
	{
		this.m_Type          = EVENT.SHIP_HIT;
		this.m_PriorityLevel = BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor    = "";
		this.m_Timestamp     = System.currentTimeMillis( );
		
		m_Ship = ship;
	}
	
	public Ship getShip( )
	{
		return m_Ship;
	}
}
