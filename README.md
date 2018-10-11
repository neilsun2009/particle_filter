# Particle Filter
A simple implementation of particle filter.

## Classes, members & methods: 
* **Particle**: Representation of one particle.
  + Point pos: position
  + double weight: weight
  + Particle(Point pos, double weight): initialize a particle.
  + void randomMove(double radius): move the particle randomly within a given radius.
  + void setWeight(double weight): set a new weight.
  + double probability(Point[] sensors,double sigma, double[] measurements): calculate the posterior probability
* **ParticleFilter**: A set of particles combining into one filter.
  + Particle[] particles: set of particles.
  + int count: number of particles.
  + Point[] sensors: locations of sensors.
  + int sensorCount: number of sensors.
  + double radius: radius for the particles to be randomly placed.
  + ParticleFilter(int count, Point center, double radius, Point[] sensors): initialize a set of particles.
  + void reassignWeights(double[][] measurements, double sigma): // reassign normalized weights
  + Point estimate(): get estimated location.
  + void resample(): resample based on the weights.
  + *private* double[] calProbability(double[][] measurements, double sigma): calculate probabilities of all particles with regards of sensors.
* **Utils**: A class with static math methods.
  + *static* double distance(Point p1, Point p2): Euclidean distance.
  + *static* double gaussian(double mu, double sigma, double x): Gaussian probability.
* **Point**: Point in a 2-d coordinate.
  + double x, y: co-ordinates.
  + Point(double x, double y), Point(Point p): construct function.
