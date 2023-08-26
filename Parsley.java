import java.util.Random;
import java.util.List;

/**
 * A simple model of a parsley.
 */
public class Parsley extends Plants
{
    // instance variables - replace the example below with your own
    private static final double BREEDING_PROBABILITY = 0.5;
    private static final int MAX_LITTER_SIZE = 2;
    private int age;
    private static final int MAX_AGE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class grass
     */
    public Parsley(Field field, Location location)
    {
        super(field, location);
        age = 0;
        
    }
    
    /**
     * @param newPlants A list to return newly "born" plants.
     */
    public void act(List<Plants> newPlants)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newPlants);            
        }     
    }
    
    /**
     * Check whether or not this parsley is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newParsley A list to return newly "born" parsley.
     */
    private void giveBirth(List<Plants> newParsley)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Parsley young = new Parsley(field, loc);
            newParsley.add(young);
        }
    }    
    
    private int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= BREEDING_PROBABILITY) { 
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    private boolean canBreed(){
        return this.getWeather() == Weather.RAINY;
    
    }
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
}