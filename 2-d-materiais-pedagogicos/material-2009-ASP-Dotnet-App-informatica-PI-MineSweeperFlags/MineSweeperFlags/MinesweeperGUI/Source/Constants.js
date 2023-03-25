// Game defaults
// ---------------------------

	var COLS = 20;
	var LINES = 20;

	var WAITING_FOR_PLAYERS = 0;
	var WAITING_FOR_START = 1;
	var STARTED = 2;
	var INVALID_NAME = 3;
	var CROWDED = 4;
	var GAME_OVER = 5;

// Player constants
// ---------------------------

	var PLAYER_STATUS_OFF = "Offline";
	var PLAYER_STATUS_ON = "Online";

// CSS board
// ---------------------------

	var BOARD_CLASS = "divArena";

// CSS cells 
// ---------------------------

	var HIDDEN_CELL = "divCell_Hidden";
	var NUMBER_CELL = "divCell_Number";
	var EMPTY_CELL = "divCell_Number";
	var MINE_CELL = "divCell_Flag_P";
	var BOMB_CELL = "divCell_Bomb";
	
	//Server Enumerate
	var TYPE_MINE = "Mine";
	var TYPE_NUMBER = "Number";
	var TYPE_BOMB = "Bomb";

// CSS score board
// ---------------------------	

	var SCORE_LABEL = "divScoreLabel";
	var SCORE_VALUE = "divScoreValue";
	
// CSS message
// ---------------------------	

	var MSGBOARD_CLASS = "divMessageBoard"
	var MSG_CLASS = "divMessage"

// CSS player
// ---------------------------	

	var IMAGES_PATH = "/Images/"

	var PL_CLASS = "divPlayerBoard"
	var PL_BOARD_CLASS = "divPlayer"
	var PL_PIC_CLASS = "divPlayerPicture"
	var PL_PIC_SRC = IMAGES_PATH + "DefaultPicture_P" //File must be a png and must end it's name with "_Px"
	var PL_FLAG_CLASS = "divPlayerFlag"
	var PL_FLAG_SRC = IMAGES_PATH + "Flag_P" //File must be a png and must end it's name with "_Px"
	var PL_SCORE_CLASS = "divPlayerScore"
	var PL_NAME_CLASS = "divPlayerName"
	var PL_QUIT_CLASS = "divPlayerQuit"
	var BTN_QUIT_CLASS = "btnPlayerQuit"
	var PL_INACTIVE = "divPlayerInactive"
	var PL_ACTIVE = "divPlayerActive"


	// ------------------------------------------------------


	function getRealId(gKey, varId) {
	    return gKey + varId;
	}