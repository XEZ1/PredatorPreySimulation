import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of a wolf.
 * Wolves age, move, eat rabbits, and die.
 */
public class Wolf extends Predator
{
    // Characteristics shared by all wolves (class variables).
    
    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 2;//8
    // The age to which a wolf can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.1675;//.0.08
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;//2
    // The food value of a single rabbit. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 10;
    // The food value of a single hamster. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int HAMSTER_FOOD_VALUE = 6;
    // The food value of a single parrot. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int PARROT_FOOD_VALUE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The wolf's age.
    private int age;
    // The wolf's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    private ArrayList<Integer> timeList ;
    
    private boolean isInfected;
    
    private int daysInfected;
    
    private double DISEASE_PORBABILITY = 0.001;
    
    

    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        timeList = new ArrayList<Integer>();
        timeList.add(0);
        timeList.add(1);
        timeList.add(3);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            super.foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            super.foodLevel = RABBIT_FOOD_VALUE;
        }
        
        //___________________________________________________
        super.BREEDING_AGE = BREEDING_AGE;
        super.MAX_AGE = MAX_AGE;
        super.BREEDING_PROBABILITY = BREEDING_PROBABILITY;
        super.MAX_LITTER_SIZE = MAX_LITTER_SIZE;
        super.RABBIT_FOOD_VALUE = RABBIT_FOOD_VALUE;
        super.HAMSTER_FOOD_VALUE = HAMSTER_FOOD_VALUE;
        super.PARROT_FOOD_VALUE = PARROT_FOOD_VALUE;
        super.rand = rand;
        super.isInfected = isInfected;
        super.daysInfected = daysInfected; 
        super.DISEASE_PROBABILITY = DISEASE_PORBABILITY;
    }
       public void act(List<Animal> newWolves){ 
    {
        incrementAge();
        incrementHunger();
        infection();
        Field field = getField();
        if(isAlive() && timeList.contains(timeOfDay())){ 
            giveBirth(newWolves);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = field.freeAdjacentLocation(getLocation());
            }
            //add comment here
           
            
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
}
/**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolves A list to return newly born wolves.
     */
protected void giveBirth(List<Animal> newWolves)
{
    // New wolves are born into adjacent locations.
    // Get a list of adjacent free locations.
    Field field = getField();
    List<Location> free = field.getFreeAdjacentLocations(getLocation());
    int births = breed();
    for(int b = 0; b < births && free.size() > 0; b++) {
        Location loc = free.remove(0);
        Wolf young = new Wolf(false, field, loc);
        newWolves.add(young);
        }
}    
 protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            
            else if (animal instanceof Hamster) {
                Hamster hamster = (Hamster) animal;
                if(hamster.isAlive()) { 
                    hamster.setDead();
                    foodLevel = HAMSTER_FOOD_VALUE;
                    return where;
                }
            }
            
           
           
        }
        return null;
    }
    protected boolean spreadDisease()
    {
        Field field = getField();
        
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Wolf && isInfected) {
                Wolf wolf = (Wolf) animal;
                wolf.infect();
                return true;
            }
        }
        return false;
    }
}
