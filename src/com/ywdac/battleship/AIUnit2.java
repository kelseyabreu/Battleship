package com.ywdac.battleship;

import java.util.*;
import com.ywdac.battleship.events.*;
import com.ywdac.battleship.events.EventListener;
import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.*;

public class AIUnit2 
	extends EventListener
{
	
	private Vector OpenMoves = new Vector(100);
	private Vector OrientGuesses;
	private String InitialHit;
	private String RecentHit;
	private String NextAttack;
	private int OrientIndex;
	private int AttackLine;
	private char AtkAdjustVal;
	
	/////////////Some Testing Values/////////
	private Board AI_Board;
	private Carrier AI_Carrier;
	private Battleship AI_BShip;
	private Cruiser AI_Cruiser;
	private Destroyer AI_Destroyer;
	private Submarine AI_Sub;
	
	private Ship[] AI_Ships = {AI_Carrier, AI_BShip, AI_Cruiser, AI_Destroyer, AI_Sub};
	/*
	 * Testing the placement algorithm
	 */
	public void AI_SHIP_PLACEMENT_TEST()
	{				
		boolean validPlacement = false;
		int orientation = -1;
		String location = null;
		int xVal = -1;
		int yVal = -1;
		
		for(int shipIndex = 0; shipIndex < 5; shipIndex++)
		{
			int length = AI_Ships[shipIndex].getMaxHealth();
			validPlacement = false;
			orientation = -1;
			
			while(!validPlacement)
			{
				orientation = (orientation + 1) % 5; 
				
				while(orientation == 0)
				{
					orientation = (int) ((Math.random()*10)%5);
					int boardIndex = (int) ((Math.random()*100)%OpenMoves.size());
					location = (String) OpenMoves.get(boardIndex);
				}
				
				xVal = (location.charAt(0) - 'A');
				yVal = (location.charAt(1) - '0');
				
				validPlacement = CHECK_PLACEMENT_VALIDITY(xVal, yVal, orientation, length);
				
			}
			
			AI_Board.placeShip(xVal, yVal, orientation, AI_Ships[shipIndex]);
		}
		
		EventManager.get( ).queue( new Event_TurnEnd( ) );
	}
	
	/**
	 * 
	 * @param xCord
	 * @param yCord
	 * @param direction 1 = north, 2 = south, 3 = east, 4 = west
	 * @param numSpaces Number of occupying tiles
	 * @return TRUE if no conflicts, FALSE otherwise
	 */
	private boolean CHECK_PLACEMENT_VALIDITY(int xCord, int yCord, int direction, int numSpaces)
	{
		switch(direction)
		{
		case 1:
			if((xCord - numSpaces) < 0)
				return false;
			
			for(int check = xCord; check > (xCord - numSpaces); check--)
			{
				if(AI_Board.isTileOccupied(check, yCord))
					return false;
			}
			break;
		case 2:
			if((xCord + numSpaces) > 9)
				return false;
			
			for(int check = xCord; check < (xCord + numSpaces); check++)
			{
				if(AI_Board.isTileOccupied(check, yCord))
					return false;
			}
			break;
		case 3:
			if((yCord + numSpaces) > 9)
				return false;
			
			for(int check = yCord; check < (yCord + numSpaces); check++)
			{
				if(AI_Board.isTileOccupied(xCord, check))
					return false;
			}
			break;
		case 4:
			if((yCord - numSpaces) < 0)
				return false;
			
			for(int check = yCord; check > (yCord - numSpaces); check--)
			{
				if(AI_Board.isTileOccupied(xCord, check))
					return false;
			}
			break;
		}
		
		return true;
	}
	/////////////////////////////////////////////////////////////////////////////
	
	enum AI_STATE
	{
		GUESSING,
		ORIENTING,
		SINKING
	}
	
	private AI_STATE currState;
	
	//Initialize Vector of move selections and set event listeners
	public AIUnit2()
	{
		char initChars = 'A';
		String initCoordinate;
		
		for(int letrCord = 0; letrCord < 10; letrCord++)
		{
			initCoordinate = Character.toString(initChars);
			
			for(int numCord = 0; numCord < 10; numCord++)
			{
				OpenMoves.add(initCoordinate.concat(Integer.toString(numCord)));
			}
			
			initChars = (char) (initChars + 1);
		}
		
		InitialHit = null;
		RecentHit = null;
		NextAttack = null;
		currState = AI_STATE.GUESSING;
		
		/*
		 * Event Listener initializations should go here
		 */
		
		AI_Board     = GameState.get( ).getBoard( "opponent" );
		AI_Carrier   = AI_Board.m_Carrier;
		AI_BShip     = AI_Board.m_Battleship;
		AI_Cruiser   = AI_Board.m_Cruiser;
		AI_Sub       = AI_Board.m_Submarine;
		AI_Destroyer = AI_Board.m_Destroyer;
		
		EventManager.get( ).addListener( this, EVENT.FIRE_RESPONSE );
	}
	
	/*
	 * Set up Random ship locations on board
	 */
	
	public void TakeTurn()
	{
		switch(currState)
		{
		case GUESSING:
			AttackGuess();
			break;
		case ORIENTING:
			FindOrientation();
			break;
		case SINKING:
			SinkShip();
			break;
		}
	}
	
	private void AttackGuess()
	{
		int boardIndex = (int) ((Math.random()*100) % OpenMoves.size());
		InitialHit = (String) OpenMoves.remove(boardIndex);
		
		/*
		 * Call Event Manager
		 */
		//EventTestHandler(InitialHit);
		EventManager.get().queue(new Event_FireUpon(InitialHit));
		
	}
	
	private void FindOrientation()
	{
		int guessIndex;
		String guessTile;
		
		if(!OrientGuesses.isEmpty())
		{
			
			guessIndex = (int) ((Math.random()*10) % OrientGuesses.size());
			guessTile = (String) OrientGuesses.remove(guessIndex);
			
			//Remove guess from available moves
			OpenMoves.remove(guessTile);
			RecentHit = guessTile;
			
			/*
			 * Make Call to event manager
			 */
			//EventTestHandler(RecentHit);
			EventManager.get().queue(new Event_FireUpon(RecentHit));
			
			
		}
	}
	
	private void SinkShip()
	{
		AtkAdjustVal = RecentHit.charAt(OrientIndex);
		
		//Edge of the board, adjust shot choices
		if((AtkAdjustVal == 'A') || (AtkAdjustVal == 'J') || (AtkAdjustVal == '0') || (AtkAdjustVal == '9'))
		{
			AtkAdjustVal = InitialHit.charAt(OrientIndex);
			AttackLine = (-1)*(AttackLine);
		}
		
		//Set next shot location
		AtkAdjustVal = (char) (AtkAdjustVal + AttackLine);
		
		if(OrientIndex == 0)
		{
			NextAttack = Character.toString(AtkAdjustVal).concat( Character.toString(RecentHit.charAt(1)) );
		}
		else if(OrientIndex == 1)
		{
			NextAttack = Character.toString(RecentHit.charAt(0)).concat( Character.toString(AtkAdjustVal) );
		}
		
		
		//Remove tile from available moves if it was not a old miss location
		if(OpenMoves.contains(NextAttack))
		{
			OpenMoves.remove(NextAttack);
		}
		
		//Compensate for next location being a used tile
		else
		{
			AtkAdjustVal = InitialHit.charAt(OrientIndex);
			AttackLine = (-1)*(AttackLine);
			
			AtkAdjustVal = (char) (AtkAdjustVal + AttackLine);
			
			if(OrientIndex == 0)
			{
				NextAttack = Character.toString(AtkAdjustVal).concat( Character.toString(RecentHit.charAt(1)) );
			}
			else if(OrientIndex == 1)
			{
				NextAttack = Character.toString(RecentHit.charAt(0)).concat( Character.toString(AtkAdjustVal) );
			}
		}
		
		/*
		 * Make call to Event Manager
		 */
		//EventTestHandler(NextAttack);
		EventManager.get().queue(new Event_FireUpon(NextAttack));
	}
	
	private void SetSinkValues()
	{
		if(RecentHit.charAt(0) == InitialHit.charAt(0))
		{
			if(RecentHit.charAt(1) > InitialHit.charAt(1))
			{
				OrientIndex = 1;
				AttackLine = 1;
			}
			else
			{
				OrientIndex = 1;
				AttackLine = -1;
			}
		}
		
		else if(RecentHit.charAt(1) == InitialHit.charAt(1))
		{
			if(RecentHit.charAt(0) > InitialHit.charAt(0))
			{
				OrientIndex = 0;
				AttackLine = 1;
			}
			else
			{
				OrientIndex = 0;
				AttackLine = -1;
			}
		}
	}
	
	private void SetOrientGuesses()
	{
		if(InitialHit != null)
		{
			OrientGuesses = new Vector(4);
			char letrVal = InitialHit.charAt(0);
			char numVal = InitialHit.charAt(1);
			String temp;
			
			//Value Below InitialHit is a valid guess
			if((letrVal >= 'A') && (letrVal < 'J'))
			{
				temp = Character.toString((char) (letrVal + 1));
				OrientGuesses.add( temp.concat(Character.toString(numVal)) );
			}
			
			//Value Above InitialHit is a valid guess
			if((letrVal > 'A') && (letrVal <= 'J'))
			{
				temp = Character.toString((char) (letrVal - 1));
				OrientGuesses.add( temp.concat(Character.toString(numVal)) );
			}
			
			//Value to the Right of InitialHit is a valid guess
			if((numVal >= '0') && (numVal < '9'))
			{
				temp = Character.toString((char) (numVal + 1));
				OrientGuesses.add( Character.toString(letrVal).concat(temp) );
			}
			
			//Value to the Left of InitialHit is a valid guess
			if((numVal > '0') && (numVal <= '9'))
			{
				temp = Character.toString((char) (numVal - 1));
				OrientGuesses.add( Character.toString(letrVal).concat(temp) );
			}
			
			//Remove guesses that have already been selected on the board
			for(int move = (OrientGuesses.size() - 1); move >= 0; move--)
			{
				if( !OpenMoves.contains(OrientGuesses.get(move)) )
				{
					OrientGuesses.remove(move);
				}
			}
		}
	}
	
//////////DESIGNATED EVENT TESTING AREA, SHOULD BE DELETED LATER/////////////
/*
 *private EventStub EM = new EventStub();
 *
 *private void EventTestHandler(String s)
 *{
 *	int response = EM.RequestReply(s);
 *	
 *	if(response == 0)
 *	{
 *		if(currState == AI_STATE.SINKING)
 *		{
 *			//AtkAdjustVal = InitialHit.charAt(OrientIndex);
 *			RecentHit = InitialHit;
 *			AttackLine = (-1)*(AttackLine);
 *		}
 *	}
 *	else if(response == 1)
 *	{
 *		if(currState == AI_STATE.GUESSING)
 *		{
 *			SetOrientGuesses();
 *			currState = AI_STATE.ORIENTING;
 *		}
 *		else if(currState == AI_STATE.ORIENTING)
 *		{
 *			SetSinkValues();
 *			currState = AI_STATE.SINKING;
 *		}
 *		else if(currState == AI_STATE.SINKING)
 *		{
 *			RecentHit = NextAttack;
 *		}
 *	}
 *	else if(response == 2)
 *	{
 *		currState = AI_STATE.GUESSING;
 *	}
 *}
 *
 */
/////////////////////////////////////////////////////////////////////////////
	
	public boolean handleEvent(BaseEvent e)
	{
		if(e instanceof Event_FireResponse)
		{
			Event_FireResponse event = (Event_FireResponse) e;
			int response = event.getResponse();
			
			// Make sure it is the AI turn
			if( GameState.get( ).getState( ) != GameState.STATE.OPPONENT_TURN )
				return false;
			
			if(response == 0)
			{
				if(currState == AI_STATE.SINKING)
				{
					RecentHit = InitialHit;
					AttackLine = (-1)*(AttackLine);
				}
				
				EventManager.get( ).queue( new Event_TurnEnd( ) );
			}
			else if(response == 1)
			{
				if(currState == AI_STATE.GUESSING)
				{
					SetOrientGuesses();
					currState = AI_STATE.ORIENTING;
				}
				else if(currState == AI_STATE.ORIENTING)
				{
					SetSinkValues();
					currState = AI_STATE.SINKING;
				}
				else if(currState == AI_STATE.SINKING)
				{
					RecentHit = NextAttack;
				}
			}
			else if(response == 2)
			{
				currState = AI_STATE.GUESSING;
			}
			
		}
		
		return false;
	}
	
	

}
