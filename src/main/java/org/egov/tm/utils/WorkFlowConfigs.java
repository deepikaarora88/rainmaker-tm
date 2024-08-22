package org.egov.tm.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *  Utility Class to keep all the values related to workflow
 *  
 * @author kavi elrey
 *
 */
public class WorkFlowConfigs {

	public static final String ACTION_OPEN = "OPEN";
	public static final String ACTION_ASSIGN = "ASSIGN";
	public static final String ACTION_CLOSE = "CLOSE";
	public static final String ACTION_REJECT = "REJECT";
	public static final String ACTION_RESOLVE = "RESOLVE";
	public static final String ACTION_REOPEN = "REOPEN";
	public static final String ACTION_REASSIGN = "REASSIGN";

	
	public static final String STATUS_OPENED = "OPENED";
	public static final String STATUS_ASSIGNED = "ASSIGNED";
	public static final String STATUS_CLOSED = "CLOSED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_RESOLVED = "RESOLVED";
	public static final String STATUS_REOPENED = "REOPENED";

	
	private WorkFlowConfigs() {}

	private static Map<String, String> actionStatusMap = prepareStatusMap();

	private static Map<String, List<String>> actionCurrentStatusMap = prepareActionCurrentStatusMap();

	private static Map<String, List<String>> roleActionMap = prepareRoleActionMap();
	

	private static Map<String, String> prepareStatusMap() {

		Map<String, String> map = new HashMap<>();
		map.put(ACTION_OPEN, STATUS_OPENED);
		map.put(ACTION_ASSIGN, STATUS_ASSIGNED);
		map.put(ACTION_REASSIGN, STATUS_ASSIGNED);
		map.put(ACTION_CLOSE, STATUS_CLOSED);
		map.put(ACTION_REJECT, STATUS_REJECTED);
		map.put(ACTION_RESOLVE, STATUS_RESOLVED);
		map.put(ACTION_REOPEN, STATUS_REOPENED);
		return map;
	}

	private static Map<String, List<String>> prepareActionCurrentStatusMap() {

		Map<String, List<String>> map = new HashMap<>();
		map.put(ACTION_ASSIGN, Arrays.asList(STATUS_OPENED,STATUS_REOPENED));
		map.put(ACTION_REJECT, Arrays.asList(STATUS_ASSIGNED));
		map.put(ACTION_REASSIGN, Arrays.asList(STATUS_ASSIGNED));
		map.put(ACTION_REOPEN, Arrays.asList(STATUS_RESOLVED));
		map.put(ACTION_CLOSE, Arrays.asList(STATUS_RESOLVED));
		map.put(ACTION_RESOLVE, Arrays.asList(STATUS_ASSIGNED));
		return map;
	}

	/**
	 * This map has the mapping between Role 'codes' and actions.
	 * @return
	 */
	private static Map<String, List<String>> prepareRoleActionMap() {

		Map<String, List<String>> map = new HashMap<>();
		map.put(PGRConstants.ROLE_CREATOR, Arrays.asList(ACTION_OPEN ,ACTION_CLOSE, ACTION_REOPEN));
		map.put(PGRConstants.ROLE_ADMIN, Arrays.asList(ACTION_ASSIGN, ACTION_REASSIGN));
		map.put(PGRConstants.ROLE_ASSIGNER, Arrays.asList(ACTION_REJECT, ACTION_RESOLVE));
		
		return map;
	}
	
	
	public static Map<String, String> getActionStatusMap(){
		return actionStatusMap;
	}
	
	public static Map<String, List<String>> getActionCurrentStatusMap(){
		return actionCurrentStatusMap;
	}
	
	public static Map<String, List<String>> getRoleActionMap(){
		return roleActionMap;
	}
	

}
