package main;

public class ParticleFilter {
	
	public Particle[] particles;
	int count = 0;
	private Point[] sensors;
	int sensorCount = 0;
	
	// initialize a series of particles around a given center and radius
	public ParticleFilter(int count, Point center, double radius, Point[] sensors) {
		this.count = count;
		this.sensors = sensors;
		this.sensorCount = this.sensors.length;
		this.particles = new Particle[this.count];
		for (int i = 0; i < this.count; ++i) {
			// new particles have uniform weight
			Particle particle = new Particle(center, 1.0/count);
			// randomly move the particle
			particle.randomMove(radius);
			this.particles[i] = particle;
		}
	}
	
	// reassign normalized weights
	public void reassignWeights(double[][] measurements, double sigma) {
		double[] probs = calProbability(measurements, sigma);
		double sum = 0.0;
		for (int i = 0; i < count; ++i) {
			sum += probs[i];
		}
		for (int i = 0; i < count; ++i) {
			this.particles[i].setWeight(probs[i] / sum);
		}
	}
	
	// calculate probabilities of all particles with regards of sensors
	// measurements size of (particleCount, sensorCount)
	private double[] calProbability(double[][] measurements, double sigma) {
		double[] probs = new double[count];
		for (int i = 0; i < count; ++i) {
			probs[i] = this.particles[i].probability(this.sensors, sigma, measurements[i]);
		}
		return probs;
	}
	
}
