package com.ywdac.battleship.renderer;

/**
 * SceneNode parent class.
 * 
 * @author ssell
 *
 */
public abstract class SceneTree 
{
	public abstract void draw( );
	public abstract void addNode( SceneNode node );
	public abstract void deleteNode( String id );
	public abstract SceneNode getNode( String id );
	
	public abstract void load( );
	public abstract void unload( );
}
