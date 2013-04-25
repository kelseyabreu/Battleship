package com.ywdac.battleship.renderer;

import javax.microedition.khronos.opengles.GL10;
import com.ywdac.battleship.BattleshipActivity;
import android.widget.TextView;

public class DeviceInfo 
{
	private static float m_ScreenWidth;
	private static float m_ScreenHeight;
	private static float m_Near;
	private static float m_Far;
	private static TextView m_DebugText;
	private static BattleshipActivity m_Activity;
	
	private static GL10 m_GL;
	
	public static float getScreenWidth( )
	{
		return m_ScreenWidth;
	}
	
	public static void setScreenWidth( float x )
	{
		m_ScreenWidth = x;
	}
	
	public static float getScreenHeight( )
	{
		return m_ScreenHeight;
	}
	
	public static void setScreenHeight( float x )
	{
		m_ScreenHeight = x;
	}
	
	public static float getNear( )
	{
		return m_Near;
	}
	
	public static void setNear( float x )
	{
		m_Near = x;
	}
	
	public static float getFar( )
	{
		return m_Far;
	}
	
	public static void setFar( float x )
	{
		m_Far = x;
	}
	
	public static GL10 getGL( )
	{
		return m_GL;
	}
	
	public static void setGL( GL10 gl )
	{
		m_GL = gl;
	}
	
	public static TextView getDebugText( )
	{
		return m_DebugText;
	}
	
	public static void setDebugText( TextView debug )
	{
		m_DebugText = debug;
	}
	
	public static BattleshipActivity getActivity( )
	{
		return m_Activity;
	}
	
	public static void setActivity( BattleshipActivity activity )
	{
		m_Activity = activity;
	}
}
