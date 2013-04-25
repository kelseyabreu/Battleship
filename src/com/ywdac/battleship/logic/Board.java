package com.ywdac.battleship.logic;

import com.ywdac.battleship.events.*;

public class Board 
{
	public Tile m_Tiles[ ][ ] = new Tile[ 10 ][ 10 ];
	
	public Carrier    m_Carrier    = null;
	public Battleship m_Battleship = null;
	public Submarine  m_Submarine  = null;
	public Cruiser    m_Cruiser    = null;
	public Destroyer  m_Destroyer  = null;
	
	public String     m_ID = "";
	
	public Board( String id )
	{
		
		/*
		 * Initialize a set of ships unique for individual boards
		 */
		m_Carrier = new Carrier( id );
		m_Battleship = new Battleship( id );
		m_Submarine = new Submarine( id );
		m_Cruiser = new Cruiser( id );
		m_Destroyer = new Destroyer( id );
		
		/*
		 * Initialize all board tiles; prevents null pointer errors
		 */
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				m_Tiles[i][j] = new Tile(i,j);
			}
		}
	}
	
	public void BOARD_TEST_PLACEMENT( )
	{		
		for( int i = 0; i < 5; i++ )
		{
			m_Carrier.m_OccupyTiles[ i ] = m_Tiles[ i ][ 0 ];
			m_Tiles[ i ][ 0 ].m_OccupiedBy = m_Carrier;
		}
		
		for( int i = 0; i < 4; i++ )
		{
			m_Battleship.m_OccupyTiles[ i ] = m_Tiles[ i + 1 ][ 2 ];
			m_Tiles[ i + 1 ][ 2 ].m_OccupiedBy = m_Battleship;
		}
		
		for( int i = 0; i < 3; i++ )
		{
			m_Submarine.m_OccupyTiles[ i ] = m_Tiles[ i + 2 ][ 4 ];
			m_Tiles[ i + 2 ][ 4 ].m_OccupiedBy = m_Submarine;
		}
		
		for( int i = 0; i < 3; i++ )
		{
			m_Cruiser.m_OccupyTiles[ i ] = m_Tiles[ i + 3 ][ 6 ];
			m_Tiles[ i + 3 ][ 6 ].m_OccupiedBy = m_Cruiser;
		}
		
		for( int i = 0; i < 2; i++ )
		{
			m_Destroyer.m_OccupyTiles[ i ] = m_Tiles[ i + 4 ][ 8 ];
			m_Tiles[ i + 4 ][ 8 ].m_OccupiedBy = m_Destroyer;
		}
		
		EventManager.get( ).queue( new Event_ShipAdd( m_Carrier ) );
		EventManager.get( ).queue( new Event_ShipAdd( m_Battleship ) );
		EventManager.get( ).queue( new Event_ShipAdd( m_Submarine ) );
		EventManager.get( ).queue( new Event_ShipAdd( m_Cruiser ) );
		EventManager.get( ).queue( new Event_ShipAdd( m_Destroyer ) );
		
		EventManager.get( ).queue( new Event_TurnEnd( ) );
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return -1 = oob/repeat, 0 = miss, 1 = hit, 2 = sunk
	 */
	public int fireUpon( int x, int y )
	{
		// Make sure we are in the bounds of the board
		if( x >= 10 || y >= 10 || x < 0 || y < 0 )
			return -1; // Out of bounds
		
		int response = -1;
		
		// Make sure desired target has not already been hit
		if( m_Tiles[ x ][ y ].beenHit( ) == Tile.HIT_STATUS.NOT_HIT )
		{
			m_Tiles[ x ][ y ].hit( );
			
			Ship ship = m_Tiles[ x ][ y ].m_OccupiedBy;
			
			if( ship == null )
				response = 0; // Miss
			else if( ship.m_IsSunk == true )
				response = 2;
			else // Hit but not sunk
				response = 1;
		}
		
		// Ship has already been hit
		return response;
	}
	
	/*
	 * Alternate method that could work well for placing ships
	 * Not sure how to handle the passing of a generic ship
	 */
	public boolean AltplaceShip(int x, int y, int angle, Ship ship)
	{
		
		int length = ship.getMaxHealth();
		int i = 0;
		
		switch( angle )
		{
		case 1:
			for(int align = x; align > (x-length); align--)
			{
				m_Tiles[align][y].attachShip(ship);
				ship.m_OccupyTiles[ i ] = m_Tiles[ align ][ y ];
				
				i++;
			}
			break;
		case 2:
			for(int align = x; align < (x+length); align++)
			{
				m_Tiles[align][y].attachShip(ship);
				ship.m_OccupyTiles[ i ] = m_Tiles[ align ][ y ];
				i++;
			}
			break;
		case 3:
			for(int align = y ; align < (y+length); align++)
			{
				m_Tiles[x][align].attachShip(ship);
				ship.m_OccupyTiles[ i ] = m_Tiles[ x ][ align ];
				i++;
			}
			break;
		case 4:
			for(int align = y ; align > (y-length); align--)
			{
				m_Tiles[x][align].attachShip(ship);
				ship.m_OccupyTiles[ i ] = m_Tiles[ x ][ align ];
				i++;
			}
			break;
		}
		
		return true;
	}
	
	public boolean placeShip( int x, int y, int angle, Ship ship )
	{
		if( ship instanceof Carrier )
		{
			switch( angle )
			{
			}
		}
		else if( ship instanceof Battleship )
		{
			switch( angle )
			{
			
			}
		}
		else if( ship instanceof Submarine )
		{
			switch( angle )
			{
			
			}
		}
		else if( ship instanceof Cruiser )
		{
			switch( angle )
			{
			
			}
		}
		else if( ship instanceof Destroyer )
		{
			switch( angle )
			{
			
			}
		}
		else
		{
			return false;	// A generic Ship was passed. Not allowed
		}
		
		return true;
	}
	
	public boolean isTileOccupied( int x, int y )
	{
		return m_Tiles[ x ][ y ].isOccupied( );
	}
}
