package com.pmsadmin.sharedhandler;


/**
 *
 *
 * It is used to declare different purpose of Shared types and their shared keys.<p>
 * Here two level of index is maintained. First in the variable in shared variable to recognize the shared type for each key used.
 * Second used in each key values to avoid duplicate value, with a combination of both index level.<p>
 * <b>Calling Type:</b><ol><p>
 * SharedPreferences sharedPreferences = getSharedPreferences(SHORT_TIME_SHARED_INDEX_0, MODE_PRIVATE);
 *
 *
 *
 */
public class SharedUtils {

	/**
	 * DEAD_ON_LOGOUT_SHARED_INDEX_2<P>
	 * This is used to save data for the SESSION of an application.<p>
	 * Ex. login status, user id etc.
	 */

	public static final String TYPE_DEAD_ON_LOGOUT_SHARED = "TYPE_DEAD_ON_LOGOUT_SHARED";
	public static final String KEY_SHARED_USER_DATA_MODEL = "KEY_SHARED_USER_DATA_MODEL";
	public static final String KEY_SHARED_LOGIN_TOKEN= "KEY_SHARED_LOGIN_TOKEN";
	public static final String KEY_SHARED_NO_DATA = "";


	/*******************************************************************************/
}
