import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 */
public class Rabbit extends Prey
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.35 ;//.12 
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 12;   
    // number of steps a grass can go before it has to eat again.
    private static final int GRASS_FOOD_VALUE = 10; //new stuff
    // number of steps a parsley can go before it has to eat again.
    private static final int PARSLEY_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;
    
    private ArrayList<Integer> timeList;
    
    private List<Animal> newRabbits;
    
    private boolean isInfected;
    
    private int daysInfected;
    
    private double DISEASE_PORBABILITY = 0.01;
    
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        timeList = new ArrayList<Integer>();
        timeList.add(0);
        timeList.add(1);
        timeList.add(2);
        timeList.add(3);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            super.foodLevel = rand.nextInt(GRASS_FOOD_VALUE);
        }
        else {
            age = 0;
            super.foodLevel = GRASS_FOOD_VALUE;
        }
        
        //___________________________________________________
        super.BREEDING_AGE = BREEDING_AGE;
        super.MAX_AGE = MAX_AGE;
        super.BREEDING_PROBABILITY = BREEDING_PROBABILITY;
        super.MAX_LITTER_SIZE = MAX_LITTER_SIZE;
        super.rand = rand;
        super.newPreys = newRabbits;
        super.isInfected = isInfected;
        super.daysInfected = daysInfected; 
        super.DISEASE_PROBABILITY = DISEASE_PORBABILITY;
        super.GRASS_FOOD_VALUE = GRASS_FOOD_VALUE;
        super.PARSLEY_FOOD_VALUE = PARSLEY_FOOD_VALUE;
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    protected void giveBirth(List<Animal> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }
    public void act(List<Animal> newRabbits)
    {
        incrementAge();
        incrementHunger();
        infection();
        if(isAlive() && timeList.contains(timeOfDay())) {
            giveBirth(newRabbits);            
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
            if(animal instanceof Rabbit && isInfected) {
                Rabbit rabbit = (Rabbit) animal;
                rabbit.infect();
                return true;
            }
        }
        return false;
    }
    
}
