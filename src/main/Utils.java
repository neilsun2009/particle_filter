package main;

public class Utils {
	// Euclidean distance
	public static double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	// Gaussian probability
	public static double gaussian(double mu, double sigma, double x) {
		return Math.exp(-Math.pow(mu-x, 2) / (2 * sigma * sigma)) / (Math.sqrt(2 * Math.PI) * sigma);
	}
}
