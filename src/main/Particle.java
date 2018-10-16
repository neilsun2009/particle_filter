package main;

import java.util.Random;


public class Particle {
	
	Point pos;
	double weight = 0;
	Random random = new Random();
	
    // generate a new particle
	public Particle(Point pos, double weight) {
		this.weight = weight;
		this.pos = new Point(pos);
	}
	
	// move the particle randomly within a given radius
	public void randomMove(double radius) {
		double randRadius = random.nextDouble() * radius;
		double randAngle = random.nextDouble() * 2.0 * Math.PI;
		double dx = Math.cos(randAngle) * randRadius;
		double dy = Math.sin(randAngle) * randRadius;
		this.pos = new Point(this.pos.x + dx, this.pos.y + dy);
	}
	
	// get position
	public Point getPos() {
		return pos;
	}
	
	// get weight
	public double getWeight() {
		return weight;
	}
	
	// set the new weight
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	// calculate the posterior probability
	public double probability(Point[] sensors, double sigma, double[] measurements) {
		double prob = 1.0;
		for (int i = 0, len = measurements.length; i < len; ++i) {
			double dist = Utils.distance(this.pos, sensors[i]);
			prob *= Utils.gaussian(dist, sigma, measurements[i]);;
		}
		return prob;
	}
	
}
