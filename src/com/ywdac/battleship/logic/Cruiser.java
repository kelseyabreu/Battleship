package com.ywdac.battleship.logic;

public class Cruiser 
	extends Ship
{
	public Cruiser( String ownedBy )
	{
		super( ownedBy );
		
		this.m_MaxHealth = 2;
		this.m_CurrentHealth = 2;
		
		this.m_OccupyTiles = new Tile[ 2 ];
	}
}
