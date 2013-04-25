package com.ywdac.battleship.logic;

public class Carrier 
	extends Ship
{
	public Carrier( String ownedBy )
	{
		super( ownedBy );
		
		this.m_MaxHealth = 5;
		this.m_CurrentHealth = 5;
		
		this.m_OccupyTiles = new Tile[ 5 ];
	}
}
