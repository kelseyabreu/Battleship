package com.ywdac.battleship.events;

public class Events 
{
	public static enum EVENT
	{
		NONE,
		APPLICATION_START,
		APPLICATION_END,
		SCENE_LOAD,
		SCENE_LOADED,
		TURN_BEGIN,
		TURN_READY,
		TURN_END,
		USER_IO,
		USER_IOSpecial,
		SHIP_HIT,
		SHIP_SUNK,
		SHIP_ADD,
		GAME_END,
		GAME_ANOTHER,
		GUI_UPDATE,
		FIRE_RESPONSE,
		FIRE_UPON
	}
}
