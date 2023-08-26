import java.util.Random;
import java.util.List;

/**
 * A simple model of a grass.
 */
public class Grass extends Plants
{
    // instance variables - replace the example below with your own
    private static final double BREEDING_PROBABILITY = 0.5;
    private static final int MAX_LITTER_SIZE = 3;
    private int age;
    private static final int MAX_AGE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class grass
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
        age = 0;
        
    }
    
    /**
     * @param newPlants A list to return newly created plants.
     */
    public void act(List<Plants> newPlants)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newPlants);            
        } 
    }
    
    
    /**
     * Check whether or not this grass is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newGrass A list to return newly "born" grass.
     */
    private void giveBirth(List<Plants> newGrass)
    {
        // New grass is "born" at the adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        //List<Location> free = field.getFreeAdjacentLocations(null);
        
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(field, loc);
            newGrass.add(young);
        }
    }    
    
    private int breed()
    {
        int births = 0;
        if( rand.nextDouble() <= BREEDING_PROBABILITY) { 
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