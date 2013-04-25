package com.ywdac.battleship;

import com.ywdac.battleship.utilities.Logger;
import com.ywdac.battleship.utilities.Logger.CHANNEL;
import com.ywdac.battleship.events.*;
import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.GameState;
import com.ywdac.battleship.renderer.*;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

public class BattleshipActivity 
	extends Activity 
{
	private GLSurfaceView m_GLSurface;
	private TextView m_DebugText;
	private long m_LastInput = 0;
	private Listener m_Listener;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState ) 
    {
        
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        Logger.get( ).initialize( this );
        TextureManager.get( ).initialize( this );
        
        m_GLSurface = new BattleshipSurfaceView( this );
        setContentView( m_GLSurface );
        
        //m_DebugText = new TextView( this );
        //this.addContentView( m_DebugText, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
       // DeviceInfo.setDebugText( m_DebugText );
        //m_DebugText.setText( "debug" );
        
        //--------------------------------------------
        
        final ActivityManager actMgr       = ( ActivityManager ) getSystemService( Context.ACTIVITY_SERVICE );
        final ConfigurationInfo configInfo = actMgr.getDeviceConfigurationInfo( );
        
        DeviceInfo.setActivity( this );
        
        Logger.get( ).write( "OpenGL Version " + configInfo.getGlEsVersion( ) + " detected!", CHANNEL.RUNTIME ); 
        GameState.get( ).startGame( );
        m_Listener = new Listener( this );
    }
    
    public void setDebugText( String str )
    {
    	m_DebugText.setText( str );
    }
    
    @Override
    protected void onPause( )
    {
    	super.onPause( );
    	m_GLSurface.onPause( );
    }
    
    @Override
    protected void onResume( )
    {
    	super.onResume( );
    	m_GLSurface.onResume( );
    }
    
    protected void end( )
    {
    	this.finish( );
    }
    
    @Override
	public boolean onTouchEvent( MotionEvent event )
    {
    	float x = event.getX( );
    	float y = event.getY( );
    	
    	float cellSize = 417 / 10.f;
		
		float xRel = x - 31;
		float yRel = y - 246;
		
		int cellX = ( int )( xRel / cellSize );
		int cellY = ( int )( yRel / cellSize );
		
		String tileID = x + ":" + y + ":player";
    	
    	//m_DebugText.setText( ( CharSequence )tileID );
    	
    	if( System.currentTimeMillis( ) - m_LastInput > 200 )
    	{
    		EventManager.get( ).queue( new Event_UserIO( x-150, y-246 ) );
    		m_LastInput = System.currentTimeMillis( );
    	}
    	
    	return true;
    }
    
    private class Listener
    	extends EventListener
    {
    	private BattleshipActivity m_Parent = null;
    	
    	public Listener( BattleshipActivity parent )
    	{
    		m_Parent = parent;
    		EventManager.get( ).addListener( this, EVENT.GAME_END );
    	}
    	
		@Override
		public boolean handleEvent( BaseEvent e ) 
		{
			if( e instanceof Event_GameEnd )
			{
				GameState.get( ).endGame( );
				m_Parent.end( );
			}
			
			return false;
		}
    	
    }
}

class BattleshipSurfaceView
	extends GLSurfaceView
{
	public BattleshipSurfaceView( Context context )
	{
		super( context );
		
		setRenderer( new BattleshipRenderer( ) );
	}
}