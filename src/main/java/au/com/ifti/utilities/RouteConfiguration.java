package au.com.ifti.utilities;

import java.util.ArrayList;

import au.com.ifti.Route;

public class RouteConfiguration {
	
	/**
	 * The container of routes which the current request will be checked against.
	 */
	private static ArrayList<Route> routeList = new ArrayList<Route>();
	
	public static synchronized void addRoute(Route route) {
		RouteConfiguration.routeList.add(route);
	}
	
	public static synchronized void addRoutes(ArrayList<Route> routes) {
		RouteConfiguration.routeList.addAll(routes);
	}
	
	public static synchronized ArrayList<Route> getRoutes() {
		return RouteConfiguration.routeList;
	}
	
}
