package com.ywdac.battleship.logic;

public class Battleship 
	extends Ship
{
	public Battleship( String ownedBy )
	{
		super( ownedBy );
		
		this.m_MaxHealth = 4;
		this.m_CurrentHealth = 4;
		
		this.m_OccupyTiles = new Tile[ 4 ];
	}
}
