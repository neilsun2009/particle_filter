package main;

import java.util.Random;

public class ParticleFilter {
	
	public Particle[] particles;
	int count = 0;
	Point[] sensors;
	int sensorCount = 0;
	Random random = new Random();
	double radius;
	
	// initialize a series of particles around a given center and radius
	public ParticleFilter(int count, Point center, double radius, Point[] sensors) {
		this.count = count;
		this.sensors = sensors;
		this.radius = radius;
		this.sensorCount = this.sensors.length;
		this.particles = new Particle[this.count];
		for (int i = 0; i < this.count; ++i) {
			// new particles have uniform weight
			Particle particle = new Particle(center, 1.0/count);
			// randomly move the particle
			particle.randomMove(this.radius);
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
	
	// get estimated location
	// best be done after weights are reassigned
	public Point estimate() {
		double x = 0, y =0;
		for (int i = 0; i < count; ++i) {
			Particle particle = particles[i];
			x += particle.pos.x * particle.weight;
			y += particle.pos.y * particle.weight;
		}
		return new Point(x, y);
	}
	
	// resample based on the weights
	// the new particles 
	// arrange the candidates in the range of [0, 1]
	// select a random number in between
	// and check which sub-range it belongs to (binary search)
	public void resample() {
		// build up probability ranges for each particle [0, 1]
		double[] ranges = new double[count + 1];
		ranges[0] = 0;
		for (int i = 0; i < count; ++i) {
			ranges[i + 1] = ranges[i] + particles[i].weight;
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
			Particle sample = particles[r];
			Particle newParticle = new Particle(sample.pos, 1.0 / count);
			newParticle.randomMove(radius);
			newParticles[i] = newParticle;
		}
		this.particles = newParticles;
		
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
