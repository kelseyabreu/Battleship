package com.ywdac.battleship.renderer;

public abstract class Model 
{
	protected int m_TextureIDs[ ] = null;
	protected int m_GLIDs[ ]      = null;
	
	public Model( )
	{
		
	}
	
	public abstract void render( );
	
	public int[ ] getTextureIDs( )
	{
		return m_TextureIDs;
	}
	
	public int[ ] getGLIDs( )
	{
		return m_GLIDs;
	}
}
