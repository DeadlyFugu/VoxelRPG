package game.util;

//Stolen from ENIGMA's mathnc.cpp.
//Yeah, I steal code from everyone ^^.
//That's why I like OSS so much ^^.

public class MathEXT {
	public static double sind(double dir) { return Math.sin(dir * Math.PI / 180.0); }
	public static double cosd(double dir) { return Math.cos(dir * Math.PI / 180.0); }
	public static double point_direction(double x1,double y1,double x2,double y2) { return ((Math.atan2(y1-y2,x2-x1)*(180/Math.PI))+360)%360; }
	public static double point_distance(double x1,double y1,double x2,double y2) { return Math.hypot(x2-x1,y2-y1); }
}