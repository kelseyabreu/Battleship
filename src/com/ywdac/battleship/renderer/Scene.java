package com.ywdac.battleship.renderer;

import com.ywdac.battleship.events.EventListener;

/**
 * The base scene class. Once registered with the SceneManager they can be
 * loaded and unloaded as necessary.
 * 
 * A scene can be the start menu, main game scene, credits, etc.
 * 
 * @author ssell
 *
 */
public abstract class Scene
	extends EventListener
{
	protected String m_ID      = null;
	protected SceneTree m_Tree = null;
	
	//--------------------------------------------------------------------------------------
	
	public Scene( String id )
	{
		m_ID = id;
	}
	
	public abstract void load( );
	public abstract void unload( );
	public abstract void update( );
	public abstract void draw( );
	
	public String getID( )
	{
		return m_ID;
	}
}
