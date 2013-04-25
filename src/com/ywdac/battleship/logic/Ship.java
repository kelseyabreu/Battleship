package com.ywdac.battleship.logic;

import com.ywdac.battleship.events.*;

public abstract class Ship
{
	protected String m_BelongsTo = "";
	
	protected int m_MaxHealth = 0;		// Max HP of ship (ie number of tiles it occupies maximally)
	protected int m_CurrentHealth = 0;	// Current HP of ship (ie number of tiles it currently occupies)
	
	protected boolean m_IsSunk = false;	// FALSE = occupies at least 1 tile; else TRUE
	
	public Tile m_OccupyTiles[];		// List of tiles (directly corresponding to board) that the ship occupies
	
	//--------------------------------------------------------------------------------------
	
	public Ship( String ownedBy )
	{
		m_BelongsTo = ownedBy;
	}
	
	public void setBoard( String board )
	{
		m_BelongsTo = board;
	}
	
	public String getBoard( )
	{
		return m_BelongsTo;
	}
	
	public int getMaxHealth( )			
	{
		return m_MaxHealth;
	}
	
	public int getCurrentHealth( )
	{
		return m_CurrentHealth;
	}
	
	public void hit( )
	{
		m_CurrentHealth--;
		
		if( m_CurrentHealth == 0 )
		{
			// SUNK
			m_IsSunk = true;
			EventManager.get( ).queue( new Event_ShipSunk( this, m_BelongsTo ) );
		}
		else
		{
			EventManager.get( ).queue( new Event_ShipHit( this ) );
		}
	}
}
