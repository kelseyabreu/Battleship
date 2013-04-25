package com.ywdac.battleship.utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Stack;

import android.content.Context;

/**
 * Global logging system. Implemented as a singleton.
 * @author ssell
 */
public class Logger 
{
	public enum VERBOSE
	{
		DEBUG,
		HIGH,
		NORMAL,
		SILENT
	}
	
	public enum CHANNEL
	{
		DEBUG,
		RUNTIME,
		ERROR_NON_CRITICAL,
		ERROR_CRITICAL
	}
	
	private static Logger m_Instance = null;
	
	private static boolean m_TrackExternal      = true;
	private static boolean m_TrackInternal      = true;
	private static boolean m_AllowDuplicates    = false;
	private static boolean m_AllowDebugMessages = true;
	private static boolean m_RewriteOnLaunch    = false;
	
	private static VERBOSE m_Level = VERBOSE.DEBUG;
	
	private final static String m_Path        = "";
	private final static String m_RuntimeLog  = "runtime.log";
	private final static String m_ErrorLog    = "error.log";
	private final static String m_StampFormat = "dd-MM-yyyy HH:mm:ss";
	
	private static Stack< String > m_ErrorStack   = new Stack< String >( );
	private static Stack< String > m_RuntimeStack = new Stack< String >( );
	private static Stack< String > m_DebugStack   = new Stack< String >( );
	
	private static Context m_AndroidContext;
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Private constructor due to singleton implementation.
	 */
	private Logger( )
	{
		
	}
	
	/**
	 * @return The current instance of the Logger.
	 */
	public static Logger get( )
	{
		if( m_Instance == null )
			m_Instance = new Logger( );
		
		return m_Instance;
	}
	
	/**
	 * Initializes the Logger with settings from the configuration file.\n
	 * If the configuration file for some reason is unaccessible then default
	 * settings are loaded in.
	 */
	public void initialize( Context context )
	{
		m_AndroidContext = context;
		
		if( m_RewriteOnLaunch )
		{
			try
			{
				FileOutputStream fOut = m_AndroidContext.openFileOutput( m_Path + m_RuntimeLog, Context.MODE_PRIVATE );
				OutputStreamWriter stream = new OutputStreamWriter( fOut );
				
				stream.write( "" );
				stream.flush( );
				stream.close( );
				
				fOut = m_AndroidContext.openFileOutput( m_Path + m_ErrorLog, Context.MODE_PRIVATE );
				
				stream.write( "" );
				stream.flush( );
				stream.close( );
			}
			catch( IOException error )
			{
				// Something went wrong. Track the error in the internal stack and attempt to write to error log.
				trackInternal( error.toString( ), CHANNEL.ERROR_CRITICAL );	// Considering critical error even though it does not cause program
				                                                            // crash since the absence of logging is pretty important.
				
				trackLoggerError( error.toString( ) );
			}
		}
	}
	
	//--------------------------------------------------------------------------------------
	// Option Setters
	
	/**
	 * Set whether duplicate messages are allowed to be tracked. Disabling may increase performance.
	 * @param set
	 */
	public void setAllowDuplicates( boolean set )
	{
		m_AllowDuplicates = set;
	}
	
	/**
	 * Set the severity level of messages to be tracked. Tighter restriction may increase performance.
	 * @param level
	 */
	public void setVerboseLevel( VERBOSE level )
	{
		m_Level = level;
	}
	
	/**
	 * Set whether to track messages inside of an external log file. Disabling may increase performance.
	 * @param set
	 */
	public void setTrackExternal( boolean set )	
	{
		m_TrackExternal = set;
	}
	
	/**
	 * Set whether to track messages inside of the internal stack.
	 * @param set
	 */
	public void setTrackInternal( boolean set )
	{
		m_TrackInternal = set;
	}
	
	/**
	 * Set whether to allow debug messages to be tracked. Suggested to disable under production runs to prevent
	 * any debug messages that may have been forgotten about to not be tracked.
	 * @param set
	 */
	public void setAllowDebug( boolean set )
	{
		m_AllowDebugMessages = false;
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Writes the specified string to the appropriate stack and log file if allowed.
	 * @param message
	 * @param channel
	 */
	public void write( String message, CHANNEL channel )
	{
		// Check for message restrictions
		
		if( m_Level == VERBOSE.SILENT )
			return;
		
		if( m_Level == VERBOSE.NORMAL && channel == CHANNEL.ERROR_NON_CRITICAL )
			return;
		
		if( m_Level != VERBOSE.DEBUG && channel == CHANNEL.DEBUG )
			return;
		
		if( !m_AllowDebugMessages && channel == CHANNEL.DEBUG )
			return;
		
		//--------------------------------------------
		
		System.out.println( message );
		trackInternal( message, channel );
		trackExternal( message, channel );
	}
	
	/**
	 * Generates a time stamp for the external log messaging.
	 * @return
	 */
	private String generateTimeStamp( )
	{
		Calendar cal = Calendar.getInstance( );
		SimpleDateFormat format = new SimpleDateFormat( m_StampFormat );
		
		return "\r\n" + format.format( cal.getTime( ) ) + "]\t";
	}
	
	/**
	 * Writes the message to an external log file.
	 * @param message
	 * @param channel
	 * @return
	 */
	private boolean trackExternal( String message, CHANNEL channel )
	{
		if( !m_TrackExternal )
			return true; // Not allowed to track externally
		
		String path = ( channel == CHANNEL.RUNTIME || channel == CHANNEL.DEBUG ? m_RuntimeLog : m_ErrorLog );
		
		try
		{
			FileOutputStream fOut = m_AndroidContext.openFileOutput( path, Context.MODE_APPEND );
			OutputStreamWriter stream = new OutputStreamWriter( fOut );
			
			stream.write( generateTimeStamp( ) + message );
			stream.flush( );
			stream.close( );
		}
		catch( IOException error )
		{
			// Something went wrong. Track the error in the internal stack and attempt to write to error log.
			trackInternal( error.toString( ), CHANNEL.ERROR_CRITICAL );	// Considering critical error even though it does not cause program
			                                                            // crash since the absence of logging is pretty important.
			
			trackLoggerError( error.toString( ) );
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Used to track externally of a crash in the Logger to prevent recursive calls to trackExternal.
	 * @param message
	 */
	private void trackLoggerError( String message )
	{
		if( m_TrackExternal )
		{
			String path = m_Path + m_ErrorLog;
			
			try
			{
				FileOutputStream fOut = m_AndroidContext.openFileOutput( path, Context.MODE_APPEND );
				OutputStreamWriter stream = new OutputStreamWriter( fOut );
				
				stream.write( generateTimeStamp( ) + message );
				stream.flush( );
				stream.close( );
			}
			catch( IOException error )
			{
				// Just failed twice to record to external logs. Nothing more we can do here.
				// Gotta hope the error is picked up and noticed in the internal stack.
			}
		}
	}
	
	/**
	 * Adds the message to the specified channel's stack if allowed.
	 * @param message
	 * @param channel
	 */
	private void trackInternal( String message, CHANNEL channel )
	{
		if( !m_TrackInternal )
			return;	// Not allowed to track internally
		
		Stack< String > stack = ( channel == CHANNEL.RUNTIME ? m_RuntimeStack : channel == CHANNEL.DEBUG ? m_DebugStack : m_ErrorStack );
		
		if( !m_AllowDuplicates && stack.search( message ) != -1 )
			return;	// Already an instance of this exact message and duplicates not allowed
		
		stack.push( message );
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Returns the top message on either the debug, runtime, or error stack. Note that there is no distinction between critical and non-critical errors.
	 * @param channel
	 * @return
	 */
	public String getLastMessage( CHANNEL channel )
	{
		Stack< String > stack = ( channel == CHANNEL.DEBUG ? m_DebugStack : channel == CHANNEL.RUNTIME ? m_RuntimeStack : m_ErrorStack );
		
		return stack.peek( );
	}
	
	/**
	 * Pops and returns the top message on either the debug, runtime, or error stack. Note that there is no distinction between critical and non-critical errors.
	 * @param channel
	 * @return
	 */
	public String popStack( CHANNEL channel )
	{
		Stack< String > stack = ( channel == CHANNEL.DEBUG ? m_DebugStack : channel == CHANNEL.RUNTIME ? m_RuntimeStack : m_ErrorStack );
		
		return stack.pop( );
	}
	
}
