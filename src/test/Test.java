package test;

import main.Particle;
import main.ParticleFilter;
import main.Point;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// suppose the map is 80*60
		// four sensors are placed at the corner
		// measurements are given with or without noise
		// the target we are searching for is (60, 40)
		// 50 particles, radius 30
		// starting point at (40, 30)
		int count = 50;
		double radius = 30;
		Point start = new Point(40, 30);
		Point[] sensors = new Point[] {new Point(0, 0), new Point(0, 60), new Point(80, 0), new Point(80, 60)};
		double[] measurements = new double[] {72.11, 63.25, 44.72, 28.28};
		double[] measurements_noise = new double[] {70.11, 65.25, 42, 30};
		double noise = 0.7;
		int iter_num = 50;
		// init filters
		ParticleFilter filter = new ParticleFilter(count, start, radius, sensors);
		// iteration
		for (int j = 0; j < iter_num; ++j) {
			// reassign weights
			filter.reassignWeights(measurements_noise, noise);
			// estimate position
			Point est = filter.estimate();
			for (int i = 0; i < count; ++i) {
				Particle p = filter.particles[i];
				System.out.println("Particle No." + i + ": (" + p.pos.x + ", " + p.pos.y + "), weight:" + p.weight);
			}
			System.out.println("Estimate position: (" + est.x + ", " + est.y + ").");
			// resample
			filter.resample();
		}
	}

}
