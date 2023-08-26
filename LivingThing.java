import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of living creatures.
 */
enum Weather{RAINY,SUNNY,FOG};
public abstract class LivingThing
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    private Weather weather;
  
    /**
     * Create a new living thing at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public LivingThing(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        Random rand = new Random();
        
    }

    /**
     * Check whether the living thing is alive or not.
     * @return true if the living thing is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the living thing is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the living things's location.
     * @return The living things's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the living thing at the new location in the given field.
     * @param newLocation The living things's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the living thing's field.
     * @return The living thing's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    public void setWeather(Weather newWeather){
        weather = newWeather;
    }
    
    protected Weather getWeather(){
        return weather;
    }
    
}