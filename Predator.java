import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A simple model of an abstract predator animal.
 */
public abstract class Predator extends Animal
{
    // Characteristics shared by all predators (class variables).
    
    // The age at which a predator can start to breed.
    protected int BREEDING_AGE;//10
    // The age to which a predator can live.
    protected int MAX_AGE = 1 ;
    // The likelihood of a predator breeding.
    protected double BREEDING_PROBABILITY;//0.08
    // The maximum number of births.
    protected int MAX_LITTER_SIZE;//2
    // The food value of a single rabbit. In effect, this is the
    // number of steps a predator can go before it has to eat again.
    protected int RABBIT_FOOD_VALUE = 10 ;
    // The food value of a single hamster. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    protected int HAMSTER_FOOD_VALUE;
    // The food value of a single parrot. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    protected int PARROT_FOOD_VALUE;
    // A shared random number generator to control breeding.
    protected Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The predator's age.
    protected int age;
    // The predator's food level, which is increased by eating rabbits.
    protected int foodLevel;
    
    protected double DISEASE_PROBABILITY = 0.01 ;
    
    protected boolean isInfected;
    
    protected int daysInfected;

    /**
     * Create a predator. A fpredatorox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Predator(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        isInfected = false;
        daysInfected = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
            
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
            
        }
    }
    
    /**
     * This is what the predator does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newPredators A list to return newly born predators.
     */
    abstract public void act(List<Animal> newPredators);

    /**
     * Increase the age. This could result in the predator's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this predator more hungry. This could result in the predator's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    abstract protected Location findFood();
    
    /**
     * Check whether or not this predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPredators A list to return newly born predators.
     */
    abstract protected void giveBirth(List<Animal> newPredators);
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
     * A predator can breed if it has reached the breeding age, while both predators are opposite sexes.
     */
    protected boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Predator) {
                Predator predator = (Predator) animal;
                if(predator.getGender() != this.getGender() && age >= BREEDING_AGE ){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public int getAge(){
        return age;
    }
    
    protected void infection(){
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
   
     protected void infect(){
        isInfected = true;
    
    }
}
