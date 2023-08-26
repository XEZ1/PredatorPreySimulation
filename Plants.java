import java.util.Random;
import java.util.List;

/**
 * A simple model of a plant.
 */
public abstract class Plants extends LivingThing
{
    // instance variables - replace the example below with your own
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    

    /**
     * Constructor for objects of class Plants
     */
    public Plants(Field field, Location location)
    {
        super(field,location);
        alive = true;
        this.field = field;
        Random rand = new Random();
    }
    
    /**
     * Makes this plant act - that is: make it do
     * whatever it wants/needs to do.
     * @param newPlants A list to receive newly "born" plants.
     */
    abstract public void act(List<Plants> newPlants);

    
    
  
}