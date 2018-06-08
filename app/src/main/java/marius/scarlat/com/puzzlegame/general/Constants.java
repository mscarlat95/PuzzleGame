package marius.scarlat.com.puzzlegame.general;


public class Constants {

    public static final String UNSET = "Unset";

    /* Game Status */
    public static final boolean ACTIVE = true;
    public static final boolean INACTIVE = false;

    /* RecyclerView Type Item */
    public static final int TYPE_GAME_ITEM = 8;
    public static final int TYPE_SCORE_ITEM = 9;

    /* Fragments Information */
    public static final String[] FRAGMENT_TITLES= { "Menu", "Game", "Score"};
    public static final int MENU_FRAGMENT_POSITION = 0;
    public static final int GAME_FRAGMENT_POSITION = 1;
    public static final int SCORE_FRAGMENT_POSITION = 2;

    /* Web Service */
    public static final String WEB_SERVICE_ADDR_GET = "https://development.m75.ro/test_mts/public/highscore/";
    public static final String WEB_SERVICE_ADDR_POST = "https://development.m75.ro/test_mts/public/highscore/public/highscore/";
    public static final int LIMIT_COUNT = 5;


    /* Shared Preferences */
    public static final String SCORE_INFO = "score_info";
    public static final String PLAYER_SCORE = "user_score";
    public static final String PLAYER_NAME = "player_name";


}
