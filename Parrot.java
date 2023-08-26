import java.util.List;  
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of a parrot.
 * Parrots age, move, breed, and die.
 */
public class Parrot extends Prey
{
    // Characteristics shared by all parrots (class variables).

    // The age at which a parrot can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a parrot can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a parrot breeding.
    private static final double BREEDING_PROBABILITY = 0.5;//.1 ezzat//.12
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 7;  
    // number of steps a grass can go before it has to eat again.
    private static final int GRASS_FOOD_VALUE = 10;
    // number of steps a parsley can go before it has to eat again.
    private static final int PARSLEY_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The parrot's age.
    private int age;
    
    private ArrayList<Integer> timeList; 
    
    private List<Animal> newParrots;
    
    private boolean isInfected;
    
    private int daysInfected;
    
    private double DISEASE_PORBABILITY = 0.01;

    /**
     * Create a new parrot. A parrot may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the parrot will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Parrot(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        timeList = new ArrayList<>();
        timeList.add(1);
        timeList.add(2);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            super.foodLevel = rand.nextInt(GRASS_FOOD_VALUE);
        }
        else {
            age = 0;
            super.foodLevel = GRASS_FOOD_VALUE;
        }
        super.BREEDING_AGE = BREEDING_AGE;
        super.MAX_AGE = MAX_AGE;
        super.BREEDING_PROBABILITY = BREEDING_PROBABILITY;
        super.MAX_LITTER_SIZE = MAX_LITTER_SIZE;
        super.rand = rand;
        super.newPreys = newParrots;
        super.isInfected = isInfected;
        super.daysInfected = daysInfected; 
        super.DISEASE_PROBABILITY = DISEASE_PORBABILITY;
        super.GRASS_FOOD_VALUE = GRASS_FOOD_VALUE;
        super.PARSLEY_FOOD_VALUE = PARSLEY_FOOD_VALUE;
    }
    
    /**
     * Check whether or not this parrot is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newParrots A list to return newly born parrots.
     */
    protected void giveBirth(List<Animal> newParrots)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Parrot young = new Parrot(false, field, loc);
            newParrots.add(young);
        }
        
    }
    public void act(List<Animal> newParrots)
    {
        incrementAge();
        incrementHunger();
        infection();
        if(isAlive() && timeList.contains(timeOfDay())){ 
            giveBirth(newParrots);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
}
protected boolean spreadDisease()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Parrot && isInfected) {
                Parrot parrot = (Parrot) animal;
                parrot.infect();
                return true;
            }
        }
        return false;
    }
}
