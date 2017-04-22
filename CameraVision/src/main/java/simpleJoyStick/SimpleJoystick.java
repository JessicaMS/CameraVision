package simpleJoyStick;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.games.input.Component;
import simpleJoyStick.ButtonData;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class SimpleJoystick implements Runnable {
	private ButtonData buttonData;

	private Controller controller;
	private Thread joystickThread;
    
    private boolean runThread;
    
    private ArrayList<Controller> foundControllers;

    public SimpleJoystick() {
    	buttonData = null;
    	foundControllers = this.getUsableControllers();
    	
    	if (foundControllers.size() > 0) {
            controller = foundControllers.get(0);
        	int componentCount = controller.getComponents().length;
    		buttonData = new ButtonData(componentCount);
    	}  else {
    		System.out.println("No joystick found.");
    	}
        joystickThread = new Thread(this);
        runThread = false;
    }
    
	public ButtonData getButtonData() {
		return buttonData;
	}


   public void StartThread() {
       // If at least one controller was found we start showing controller data on window.
       if(!foundControllers.isEmpty()) {
    	   if (runThread == false) {
    		   runThread = true;   
    		   joystickThread.start();
    	   }
       }
   }
   
   public void StopThread() {
	   if (runThread == true) {
		   runThread = false;   
		   joystickThread.interrupt();
	   }
   }
    
    public ArrayList<Controller> getUsableControllers() {
    	 Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    	 ArrayList<Controller> usableControllers = new ArrayList<Controller>();
    	 
    	 for(Controller controller : controllers) {
    		 if (
                     controller.getType() == Controller.Type.STICK || 
                     controller.getType() == Controller.Type.GAMEPAD || 
                     controller.getType() == Controller.Type.WHEEL ||
                     controller.getType() == Controller.Type.FINGERSTICK
                )
             {
    			 usableControllers.add(controller);
             }
    	 }
    	 
    	 return usableControllers;
    }
    
    /**
     * Starts showing controller data on the window.
     */
    @Override
    public void run(){
        while(runThread)
        {
            // Check whether controller is live
            if( !controller.poll() ) {
            	System.out.println("Controller disconnected!");
            	StopThread();
                break;
            }
            
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;
            
            //For each component, loop by index
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                
                // If the component identifier name contains only numbers, then this is a button.
                if(component.getIdentifier().getName().matches("^[0-9]*$")){ 
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    
                    buttonData.setButton(i, buttonIndex, component.getPollData());
                    
                    continue;
                }
                
                // Hat switch
                if(component.getIdentifier() == Component.Identifier.Axis.POV){
                    float hatSwitchPosition = component.getPollData();
                    
                    hatSwitchPosition *= 360.0;
                    buttonData.setHatPosition(i, hatSwitchPosition);
                    
                    continue;
                }
                
                // Axes
                if(component.isAnalog()){
                    float axisValue = component.getPollData();
                    int axisValueInPercentage = getAxisValueInPercentage(axisValue);
                    String axisName = component.getIdentifier().getName();
                    
                    if(component.getIdentifier() == Component.Identifier.Axis.X){
//                        btn.setAxisPosition(i, axisName, axisValueInPercentage);
                        continue; // Go to next component.
                    }
                    
                    if(component.getIdentifier() == Component.Identifier.Axis.Y){
//                        btn.setAxisPosition(i, axisName, axisValueInPercentage);
                        continue; // Go to next component.
                    }
                    if (axisName == "rx" || axisName == "ry") {
                    	//btn.setAxisPosition(i, axisName, axisValueInPercentage);
                    } else {
                    	buttonData.setAxisPosition(i, axisName, axisValue);
                    }
                }
            }
            
            // We have to give processor some rest.
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SimpleJoystick.class.getName()).log(Level.SEVERE, null, ex);
            	Logger.getLogger(SimpleJoystick.class.getName()).log(Level.INFO, "Joystick thread stopped");
            }
        }
    }
    
    
    
    /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the left/top 
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     * 
     * @return value of axis in percentage.
     */
    public int getAxisValueInPercentage(float axisValue)
    {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }


}
