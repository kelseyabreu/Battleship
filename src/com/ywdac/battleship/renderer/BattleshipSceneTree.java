package com.ywdac.battleship.renderer;

import java.util.ArrayList;

import com.ywdac.battleship.renderer.SceneNode.RENDER_TYPE;

/**
 * SceneTree for displaying the actual game (board, ships, etc.).
 * 
 * Due to the nature of the game, there are aspects of the scene that can be used
 * to the advantage of the SceneTree. For example, we know that the scene, in its
 * entirety, will always be on display. Because of this, the BattleshipSceneTree
 * will not be implemented as a more traditional Quad/Oct/BSP Tree. 
 * 
 * Instead, there are 6 layers of objects that will exist, and these all will 
 * always be on display. From top to bottom they go:
 * 
 * 	1.	Skybox
 *  2.	Tiles
 *  3. 	Grid 
 *  4. 	Ships
 *  5.	GUI
 *  6. 	Wildcard
 *  
 *  Again, due to the known nature of the game, we can have the rendering order
 *  of nodes be based on which layer they belong to, and not necessarily on their
 *  spatial coordinates.
 *  
 *  The only exception to this is the Wildcard node type. These are nodes that may
 *  be present at any level visually and must be rendered based on this location.
 *  
 *  A Wildcard node will be the missiles launched from a ship, a stray asteroid, etc.
 *  
 *  Finally, again thanks to the known nature of the scene, transparency ordering will
 *  be based on the layer. We know the Skybox and Grid will never have transparency
 *  (whether colors or textures) and thus will not have to undergo sorting based on that.
 *  But we do know that any of the other layers may feature transparent textures, etc.
 * 
 * @author ssell
 *
 */
public class BattleshipSceneTree 
	extends SceneTree
{
	private Layer m_Tiles = new Layer( );
	private Layer m_Ships = new Layer( );
	private Layer m_WildCard = new Layer( );
	
	private SkyboxNode m_Skybox     = null;		// Not implemented as a Layer object since
	private GridNode m_GridPlayer   = null;
	private GridNode m_GridOpponent = null;
	
	private Camera m_Camera = null;
	
	private boolean m_Initialized = false;

	@Override
	public void draw( ) 
	{
		if( !m_Initialized )
			return;
		
		/**
		 * For solid objects, render Front to Back with skybox last.
		 * For transparent object, render Back to Front.
		 * 
		 * Following those two rules, the render order will be as
		 * follows (ignoring wildcard for now):
		 * 
		 * 		1. Ship Solids
		 * 		2. Grid
		 * 		3. Tile Solids
		 * 		4. Skybox
		 * 		5. Tile Transparents
		 * 		6. Ship Transparents
		 * 
		 * Again, due to the nature of the scene, we do not have to worry
		 * about spatial sorting.
		 */
		
		if( m_Camera == null )
			return;
		
		m_Camera.push( false );
		
		renderList( m_Ships.m_Solids );		
		
		if( m_GridPlayer != null )
			m_GridPlayer.draw( );
		
		if( m_GridOpponent != null )
			m_GridOpponent.draw( );
		
		renderList( m_Tiles.m_Solids );
		renderList( m_WildCard.m_Solids );
		
		if( m_Skybox != null )
			m_Skybox.draw( );
		
		renderList( m_Tiles.m_Transparencies );
		renderList( m_Ships.m_Transparencies );
		renderList( m_WildCard.m_Transparencies );
		
		m_Camera.pop( );
	}
	
	private void renderList( ArrayList< SceneNode > list )
	{
		SceneNode node;
		
		for( int i = 0; i < list.size( ); i++ )
		{
			node = list.get( i );
			
			if( node == null )
				continue;
			
			if( node.getRenderType( ) != RENDER_TYPE.NONE )
				node.draw( );
		}
	}

	@Override
	public void addNode( SceneNode node ) 
	{
		if( node == null )
			return;
		
		RENDER_TYPE type = node.getRenderType( );
		
		if( node instanceof ShipNode )
		{
			if( type == RENDER_TYPE.TRANSPARENT_COLOR || type == RENDER_TYPE.TRANSPARENT_TEXTURE )
				m_Ships.m_Transparencies.add( node );
			else
				m_Ships.m_Solids.add( node );
		}
		else if( node instanceof TileNode )
		{
			if( type == RENDER_TYPE.TRANSPARENT_COLOR || type == RENDER_TYPE.TRANSPARENT_TEXTURE )
				m_Tiles.m_Transparencies.add( node );
			else
				m_Tiles.m_Solids.add( node );
		}
		else if( node instanceof SkyboxNode )
		{
			SceneNode oldSky = m_Skybox;
			m_Skybox = ( SkyboxNode )node;
		}
		else if( node instanceof GridNode )
		{
			if( m_GridPlayer == null )
				m_GridPlayer = ( GridNode )node;
			else
				m_GridOpponent = ( GridNode )node;
		}
		else if( node instanceof Camera )
		{
			m_Camera = ( Camera )node;
		}
		else
		{
			// Wildcard
			if( type == RENDER_TYPE.TRANSPARENT_COLOR || type == RENDER_TYPE.TRANSPARENT_TEXTURE )
				m_WildCard.m_Transparencies.add( node );
			else
				m_WildCard.m_Solids.add( node );
		}
		
		/**
		 * If we have not yet been initialized, then all texture loading
		 * will be done in a single batch via the load call.
		 * 
		 * But if we are already loaded, then we must handle the texture
		 * right now. This may (probably) cause a noticeable frame dip.
		 */
		if( m_Initialized )
			handleNodeTextureLoad( node );
		
	}

	@Override
	public void deleteNode( String id ) 
	{
		m_Ships.m_Transparencies.clear( );
		
		for( int i = 0; i < m_WildCard.m_Transparencies.size( ); i++ )
		{
			if( m_WildCard.m_Transparencies.get( i ).m_ID.compareToIgnoreCase( "center_decor" ) != 0 )
			{
				m_WildCard.m_Transparencies.remove( i );
				i = 0;
			}
		}
	}

	@Override
	public SceneNode getNode( String id ) 
	{
		if( m_Skybox.getID( ).compareTo( id ) == 0 )
			return m_Skybox;
		else if( m_GridPlayer.getID( ).compareTo( id ) == 0 )
			return m_GridPlayer;
		else if( m_GridOpponent.getID( ).compareTo( id ) == 0 )
			return m_GridOpponent;
		
		for( int i = 0; i < m_Tiles.m_Transparencies.size( ); i++ )
		{
			if( m_Tiles.m_Transparencies.get( i ).getID( ).compareTo( id ) == 0 )
				return m_Tiles.m_Transparencies.get( i );
		}
		
		for( int i = 0; i < m_Tiles.m_Transparencies.size( ); i++ )
		{
			if( m_Tiles.m_Solids.get( i ).getID( ).compareTo( id ) == 0 )
				return m_Tiles.m_Solids.get( i );
		}
		
		for( int i = 0; i < m_Ships.m_Transparencies.size( ); i++ )
		{
			if( m_Ships.m_Transparencies.get( i ).getID( ).compareTo( id ) == 0 )
				return m_Ships.m_Transparencies.get( i );
		}
		
		for( int i = 0; i < m_Ships.m_Transparencies.size( ); i++ )
		{
			if( m_Ships.m_Solids.get( i ).getID( ).compareTo( id ) == 0 )
				return m_Ships.m_Solids.get( i );
		}
		
		for( int i = 0; i < m_WildCard.m_Transparencies.size( ); i++ )
		{
			if( m_WildCard.m_Transparencies.get( i ).getID( ).compareTo( id ) == 0 )
				return m_WildCard.m_Transparencies.get( i );
		}
		
		for( int i = 0; i < m_WildCard.m_Transparencies.size( ); i++ )
		{
			if( m_WildCard.m_Solids.get( i ).getID( ).compareTo( id ) == 0 )
				return m_WildCard.m_Solids.get( i );
		}
		
		return null;
	}
	
	public ShipNode getShipNode( String type )
	{
		ShipNode active;
		
		for( int i = 0; i < m_Ships.m_Transparencies.size( ); i++ )
		{
			active = ( ShipNode )m_Ships.m_Transparencies.get( i );
			
			if( active.m_Board.compareToIgnoreCase( "player" ) == 0 && active.m_ID.compareToIgnoreCase( type ) == 0 )
			{
				return active;
			}
		}
		
		return null;
	}

	@Override
	public void load( ) 
	{
		/**
		 * The primary purpose of load is to ensure that the textures for
		 * all of our nodes are loaded into memory.
		 */
		
		if( m_Initialized )
			return;
		
		// Simply cycle through all of our nodes and load their textures
		
		ArrayList< SceneNode > list;
		
		handleNodeTextureLoad( m_Skybox );
		
		list = m_Ships.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		list = m_Ships.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		list = m_Tiles.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		list = m_Tiles.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		list = m_WildCard.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		list = m_WildCard.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureLoad( list.get( i ) );
		
		m_Initialized = true;
	}

	@Override
	public void unload( ) 
	{
		/**
		 * The primary purpose of unload is to remove the textures for our
		 * nodes from memory.
		 */
		
		if( !m_Initialized )
			return;
		
		// Simply cycle through all of our nodes and unload their textures
		
		ArrayList< SceneNode > list;
		
		handleNodeTextureUnload( m_Skybox );
		
		list = m_Ships.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		list = m_Ships.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		list = m_Tiles.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		list = m_Tiles.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		list = m_WildCard.m_Solids;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		list = m_WildCard.m_Transparencies;
		
		for( int i = 0; i < list.size( ); i++ )
			handleNodeTextureUnload( list.get( i ) );
		
		m_Initialized = false;
	}
	
	/**
	 * Loads the texture(s) for the node into memory.
	 * @param node
	 */
	private void handleNodeTextureLoad( SceneNode node )
	{
		// First, we know that the Grid node will never be textured, so
		// skip out if we were passed a grid.
		
		if( node instanceof GridNode )
			return;
		
		// Now, a node may have multiple textures attached to it.
		// Simply run through them and load them via the TextureManager.
		// The TextureManager will handle any cases of textures already being
		// present so we can lean on it.
		
		Model model = node.getModel( );
		
		if( model == null )
			return;
		
		int textures[ ] = model.getTextureIDs( );
		
		if( textures == null )
			return;
		
		TextureManager TM = TextureManager.get( );
		
		for( int i = 0; i < textures.length; i++ )
			TM.loadTexture( textures[ i ] );
	}
	
	private void handleNodeTextureUnload( SceneNode node )
	{
		// First, we know that the Grid node will never be textured, so
		// skip out if we were passed a grid.
				
		if( node instanceof GridNode )
			return;
				
		// Now, a node may have multiple textures attached to it.
		// Simply run through them and unload them via the TextureManager.
		
		Model model = node.getModel( );
		
		if( model == null || model.m_GLIDs == null || model.m_TextureIDs == null )
			return;
		
		
		int textures[ ] = model.getTextureIDs( );
		TextureManager TM = TextureManager.get( );
		
		for( int i = 0; i < textures.length; i++ )
			TM.unloadTexture( textures[ i ] );
	}
	
	class Layer
	{
		ArrayList< SceneNode > m_Solids = new ArrayList< SceneNode >( );
		ArrayList< SceneNode > m_Transparencies = new ArrayList< SceneNode >( );
	}

}
