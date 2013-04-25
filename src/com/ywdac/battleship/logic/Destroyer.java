package com.ywdac.battleship.logic;

public class Destroyer 
	extends Ship
{
	public Destroyer( String ownedBy )
	{
		super( ownedBy );
		
		this.m_MaxHealth = 3;
		this.m_CurrentHealth = 3;
		
		this.m_OccupyTiles = new Tile[ 3 ];
	}
}
