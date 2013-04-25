package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

import com.ywdac.battleship.R;
import com.ywdac.battleship.events.BaseEvent;
import com.ywdac.battleship.events.EventManager;
import com.ywdac.battleship.events.Event_GameAnother;
import com.ywdac.battleship.events.Event_GameEnd;
import com.ywdac.battleship.events.Event_UserIO;
import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.renderer.SceneNode.RENDER_TYPE;

public class GameOverScene 
	extends Scene
{
	private SkyboxNode m_Skybox;
	private Camera     m_Camera;
	private SceneNode  m_Center;
	private SceneNode  m_Message;
	private boolean    m_Active = false;
	
	public GameOverScene( String id ) 
	{
		super( id );		
		
		m_Tree = new BattleshipSceneTree( );
		EventManager.get( ).addListener( this, EVENT.USER_IO );
	}

	@Override
	public void load( ) 
	{		
		m_Skybox  = new SkyboxNode( "test_sky", RENDER_TYPE.SOLID_TEXTURE, R.drawable.skybox_over );
		m_Center  = new SceneNode( "center_decor", RENDER_TYPE.TRANSPARENT_TEXTURE );
		m_Message = new SceneNode( "message", RENDER_TYPE.TRANSPARENT_TEXTURE );
		m_Camera  = new Camera( "camera", RENDER_TYPE.NONE );
		
		ModelPrimitiveQuad centerModel = new ModelPrimitiveQuad( 50, 50 );
		ModelPrimitiveQuad centerMessage = new ModelPrimitiveQuad( 60, 30 );
		
		if( this.m_ID.compareToIgnoreCase( "game_over_win" ) == 0 )
		{
			centerModel.setTexture( R.drawable.game_over_win );
			centerMessage.setTexture( R.drawable.game_over_win_message );
			
		}
		else
		{
			centerModel.setTexture( R.drawable.game_over_loss );
			centerMessage.setTexture( R.drawable.game_over_loss_message );
		}
		
		m_Center.attachModel( centerModel );
		m_Center.setPosition( -25.f, -10.f, 0.f );
		
		m_Message.attachModel( centerMessage );
		m_Message.setPosition( -30.f, -40.f, 0.f );
	
		m_Camera.setPosition( 0.f, 0.f, 100.f );
		m_Camera.lookAt( 0.f, 0.f, 0.f );
		
		m_Tree.addNode( m_Skybox );
		m_Tree.addNode( m_Camera );
		m_Tree.addNode( m_Center );
		m_Tree.addNode( m_Message );
		m_Tree.load( );
		
		//--------------------------------------------
		
		m_Active = true;
	}

	@Override
	public void unload( ) 
	{
		m_Tree.unload( );
		m_Active = false;
	}

	@Override
	public void update( ) 
	{
		
	}

	@Override
	public void draw( ) 
	{
		GLES11.glPushMatrix( );
		GLES11.glLoadIdentity( );
		
		m_Tree.draw( );
		
		GLES11.glPopMatrix( );
	}

	@Override
	public boolean handleEvent( BaseEvent e ) 
	{
		if( m_Active )
		{
			if( e instanceof Event_UserIO )
			{
				Event_UserIO event = ( Event_UserIO )e;
				
				float x = event.getX( );
				float y = event.getY( );
				float h = DeviceInfo.getScreenHeight( );
				
				if( x < ( h / 2.f ) )
				{
					// New Game
					EventManager.get( ).queue( new Event_GameAnother( ) );
				}
				else
				{
					// Quit
					if( y > 600.f )
						EventManager.get( ).queue( new Event_GameEnd( ) );
				}
			}
		}
		
		return false;
	}

}
