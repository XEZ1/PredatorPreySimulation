import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 */
public abstract class Animal extends LivingThing
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The anmial's gender
    private int gender;
    // time of the day
    private int time;
  
    /**
     * Create a new animal at location in field. 
     * Randomly setting the gender to male or female
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field,location);
        alive = true;
        this.field = field;
        setLocation(location);
        Random rand = new Random();
        gender = rand.nextInt(2);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Return the animals gender
     */
    
    protected int getGender()
    {
        return gender;
    }
    
    /**
     * Callback method that sets the time of the day
     * Each step represents 6 hours of a day, making 4 steps 1 whole day
     * 
     * @param step current step of the simulation
     * 
     */
    public void setTime(int step)
    {
        time = step % 4;
    }
    
    /**
     * Returns time of the day
     */
    protected int timeOfDay()
    {
        return time;
    }
    
}
