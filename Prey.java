import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of an abstract prey.
 */
public abstract class Prey extends Animal
{
    // Characteristics shared by all preys (class variables).

    // The age at which a prey can start to breed.
    protected int BREEDING_AGE;
    // The age to which a prey can live.
    protected int MAX_AGE = 1;
    // The likelihood of a prey breeding.
    protected double BREEDING_PROBABILITY;//.12
    // The maximum number of births.
    protected int MAX_LITTER_SIZE;
    // A shared random number generator to control breeding.
    protected Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The prey's age.
    protected int age;
    
    protected List<Animal> newPreys;
    
    protected double DISEASE_PROBABILITY = 0.01 ;
    
    protected boolean isInfected;
    
    protected int daysInfected;
    
    protected int foodLevel;
    
    protected int GRASS_FOOD_VALUE = 15;
    
    protected int PARSLEY_FOOD_VALUE = 15;
    
    /**
     * Create a new prey. A prey may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the prey will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Prey(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        isInfected = false;
        daysInfected = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(GRASS_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = GRASS_FOOD_VALUE;
            
        }
    }
    
    /**
     * This is what the prey does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPreys A list to return newly born preys.
     */
    abstract public void act(List<Animal> newPreys);
    
    
    protected void infection()
    {
         if(!isInfected && rand.nextDouble() <= DISEASE_PROBABILITY){
            isInfected = true;
            daysInfected = 0;
        }
        
        else if(isInfected && daysInfected == 3){
            isInfected = false;
            daysInfected = 0;
        }
        
        else if(isInfected){
            incrementHunger();
            daysInfected++;
            if(isAlive()){
                spreadDisease();
            }
        }
    }
    abstract protected boolean spreadDisease();
    /**
     * Increase the age.
     * This could result in the Prey's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPreys A list to return newly born preys.
     */
    abstract protected void giveBirth(List<Animal> newPreys);
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A prey can breed if it has reached the breeding age.
     * @return true if the prey can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Prey) {
                Prey prey = (Prey) animal;
                if(prey.getGender() != this.getGender() && age >= BREEDING_AGE && prey.getAge() >= BREEDING_AGE){
                    return true;
                }
            }
        }
        return false;
    }
    
    public int getAge(){
        return  age;
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    protected void infect(){
        isInfected = true;
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plants = field.getObjectAt(where);
            if(plants instanceof Grass) {
                Grass grass = (Grass) plants;
                if(grass.isAlive()) { 
                    grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE;
                    return where;
                }
            }
            else if (plants instanceof Parsley) {
                Parsley parsley = (Parsley) plants;
                if(parsley.isAlive()) { 
                    parsley.setDead();
                    foodLevel = PARSLEY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
}
