package main;

import java.util.Random;

public class ParticleFilter {
	
	Particle[] particles;
	int count = 0;
	Point[] sensors;
	double[] measurements;
	double sigma;
	Random random = new Random();
	double radius;
	
	// initialize a series of particles around a given center and radius
	public ParticleFilter(int count, Point center, double radius, Point[] sensors,
			double[] measurements, double sigma) throws IllegalArgumentException {
		if (sensors.length != measurements.length) {
			throw new IllegalArgumentException("Inconsistent length between sensors and measurements.");
		}
		this.count = count;
		this.sensors = sensors.clone();
		this.radius = radius;
		this.particles = new Particle[this.count];
		this.measurements = measurements.clone();
		this.sigma = sigma;
		for (int i = 0; i < this.count; ++i) {
			// new particles have uniform weight
			Particle particle = new Particle(center, 1.0/count);
			// randomly move the particle
			particle.randomMove(this.radius);
			this.particles[i] = particle;
		}
	}
	
	// get a particle
	public Particle getParticle(int num) {
		return this.particles[num];
	}
	
	// get all particles
	public Particle[] getParticles() {
		return this.particles.clone();
	}
	
	// reassign normalized weights
	public void reassignWeights() {
		double[] probs = calProbability();
		double sum = 0.0;
		for (int i = 0; i < count; ++i) {
			sum += probs[i];
		}
		for (int i = 0; i < count; ++i) {
			this.particles[i].setWeight(probs[i] / sum);
		}
	}
	// get estimated location
	// best be done after weights are reassigned
	public Point estimate() {
		double x = 0, y =0;
		for (int i = 0; i < count; ++i) {
			Particle particle = particles[i];
			x += particle.getPos().x * particle.getWeight();
			y += particle.getPos().y * particle.getWeight();
		}
		return new Point(x, y);
	}
	
	// resample based on the weights
	// the new particles 
	// arrange the candidates in the range of [0, 1]
	// with accumulative probabilities
	// select a random number in between
	// and check which sub-range it belongs to (binary search)
	public void resample() {
		// build up probability ranges for each particle [0, 1]
		double[] ranges = new double[count + 1];
		ranges[0] = 0;
		for (int i = 0; i < count; ++i) {
			ranges[i + 1] = ranges[i] + particles[i].getWeight();
		}
		Particle[] newParticles = new Particle[count];
		// resample
		for (int i = 0; i < count; ++i) {
			// randomly choose a number
			double ran = random.nextDouble();
			// binary search for the sample particle
			int l = 0, r = count;
			while (l + 1 < r) {
				int mid = (l + r) / 2;
				if (ran > ranges[mid]) {
					l = mid;
				} else {
					r = mid;
				}
			}
			Particle sample = particles[l];
			Particle newParticle = new Particle(sample.pos, 1.0 / count);
			newParticle.randomMove(radius);
			newParticles[i] = newParticle;
		}
		this.particles = newParticles;
		
	}
	
	// calculate probabilities of all particles with regards of sensors
	// measurements size of (sensorCount)
	private double[] calProbability() {
		double[] probs = new double[count];
		for (int i = 0; i < count; ++i) {
			probs[i] = this.particles[i].probability(sensors, sigma, measurements);
		}
		return probs;
	}
	
}
