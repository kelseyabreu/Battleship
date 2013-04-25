package com.ywdac.battleship.utilities;

/**
 * XORShift Random Number Generator.\n
 * Low period (2^64 - 1 in this implementation) but high quality randomness and speed.
 * @author ssell
 *
 */
public class RNG 
{
	private long m_Seed;
	
	public RNG( long l )
	{
		m_Seed = l;
	}
	
	public void seed( int seed )
	{
		m_Seed = seed;
	}
	
	public long next( )
	{
		m_Seed ^= ( m_Seed << 21 );
		m_Seed ^= ( m_Seed >> 35 );
		m_Seed ^= ( m_Seed << 4 );
		
		return m_Seed;
	}
	
	public long next( long max )
	{
		return next( ) % max;
	}
}
