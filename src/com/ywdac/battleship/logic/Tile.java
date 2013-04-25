package com.ywdac.battleship.logic;

public class Tile 
{
	public enum HIT_STATUS
	{
		NOT_HIT,
	    HIT_UNOCCUPIED,
	    HIT_OCCUPIED;
	};
	
	public int m_X;
	public int m_Y;
	
	public Ship m_OccupiedBy = null;
	
	private HIT_STATUS m_BeenHit = HIT_STATUS.NOT_HIT;
	
	//--------------------------------------------------------------------------------------
	
	public Tile( int x, int y )
	{
		m_X = x;
		m_Y = y;
	}
	
	public boolean isOccupied( )
	{
		if( m_OccupiedBy != null )
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return TRUE if space was occupied, else FALSE
	 */
	public boolean hit( )
	{		
		if( m_OccupiedBy != null )
		{
			m_BeenHit = HIT_STATUS.HIT_OCCUPIED;
			m_OccupiedBy.hit( );
			return true;
		}
		
		m_BeenHit = HIT_STATUS.HIT_UNOCCUPIED;		
		return false;
	}
	
	public HIT_STATUS beenHit( )
	{
		return m_BeenHit;
	}
	
	public void attachShip( Ship ship )
	{
		m_OccupiedBy = ship;
	}
}
