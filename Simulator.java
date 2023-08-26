import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A predator-prey simulator, based on a rectangular field
 * containing foxes,wolves,hasmters,rabbits,parrots,grass,and parsley.
 */



public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;//0.02
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.02;//0.02
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.09; //.075   
    // The probability that a hamster will be created in any given grid position.
    private static final double HAMSTER_CREATION_PROBABILITY = 0.08;  //.065
    // The probability that a parrot will be created in any given grid position.
    private static final double PARROT_CREATION_PROBABILITY = 0.1; //.07
    // The probability that grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.03; //.03
    // The probability that a parsley will be created in any given grid position.
    private static final double PARSLEY_CREATION_PROBABILITY = 0.03; //.03
    
    // List of animals in the field.
    private List<Animal> animals;
    // List of plants in the field.
    private List<Plants> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size, while setting the graphical view colour of all species.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        plants = new ArrayList<>();
        
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Wolf.class, Color.BLACK);
        view.setColor(Hamster.class, Color.PINK);
        view.setColor(Parrot.class, Color.MAGENTA);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Parsley.class, Color.GREEN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            //delay(1000);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();  
        List<Plants> newPlants = new ArrayList<>();
        //Weather weather = Weather.values()[new Random().nextInt(Weather.values().length)];
        // Let all animals act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.setTime(step);
            //animal.setWeather(weather);
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }
        //Let all plants act
        for(Iterator<Plants> it = plants.iterator(); it.hasNext(); ) {
            Plants plants = it.next();
            //plants.setWeather(weather);
            plants.act(newPlants);
            if(!plants.isAlive()) {
                it.remove();
            }
        }
        
        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        // Add the newly born plants to the main lists.
        plants.addAll(newPlants);

        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        plants.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes,rabbits,wolves,hamsters,parrots,grass,and parsley.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(false, field, location);
                    animals.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                }
                else if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(false, field, location);
                    animals.add(wolf);
                }
                else if(rand.nextDouble() <= HAMSTER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hamster hamster = new Hamster(true, field, location);
                    animals.add(hamster);
                }
                else if(rand.nextDouble() <= PARROT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Parrot parrot = new Parrot(true, field, location);
                    animals.add(parrot);
                }

                
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(field, location);
                    plants.add(grass);
                }
                
                else if(rand.nextDouble() <= PARSLEY_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Parsley parsley = new Parsley(field, location);
                    plants.add(parsley);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
