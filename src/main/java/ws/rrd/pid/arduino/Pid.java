package ws.rrd.pid.arduino;

import java.util.Properties;
 
/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  09.02.2012::20:29:02<br> 
 */
public class Pid {
	
	public static final int REVERSE = 1;
	public static final int AUTOMATIC = 1;
	public static final int MANUAL = 0;
	public static final int DIRECT = 0;
	
	private double dispKp;				// * we'll hold on to the tuning parameters in user-entered 
	private double dispKi;				//   format for display purposes
	private double dispKd;				//
    
	double kp;                  // * (P)roportional Tuning Parameter
    double ki;                  // * (I)ntegral Tuning Parameter
    double kd;                  // * (D)erivative Tuning Parameter

	int controllerDirection;

    double myInput;              // * Pointers to the Input, Output, and Setpoint variables
    double myOutput;             //   This creates a hard link between the variables and the 
    double mySetpoint;           //   PID, freeing the user from having to constantly tell us
                                  //   what these values are.  with pointers we'll just know.
			  
	long lastTime;
	double ITerm, lastInput;

	int SampleTime;
	double outMin, outMax;
	boolean inAuto;

	/** 
	 * Constructor (...)*********************************************************
	 *    The parameters specified here are those for for which we can't set up 
	 *    reliable defaults, so we need to have the user set them.
	 ***************************************************************************/
	public Pid(double Input, double Output, double Setpoint,
	        double Kp, double Ki, double Kd, int ControllerDirection)
	{
		this.setOutputLimits(-255, 255);				//default output limit corresponds to 
													//the arduino pwm limits

	    long SampleTime = 100;							//default Controller Sample Time is 0.1 seconds

	    this.setControllerDirection(ControllerDirection);
	    this.setTunings(Kp, Ki, Kd);

	    lastTime = millis()-SampleTime;				
	    inAuto = false;
	    myOutput = Output;
	    myInput = Input;
	    mySetpoint = Setpoint;
			
	} 

    private Properties double2prop(Properties retval, String name, double value)
    {
        retval.setProperty(name, (new StringBuilder()).append(value).toString());
        return retval;
    }
    private static double toDouble(String valAsString)
    {
        return Double.parseDouble(valAsString);
    }

    public static double prop2Double(Properties pVpid, String namePar)
    {
        return toDouble(pVpid.getProperty(namePar, "0.0"));
    }
    
    public static int  prop2Integer(Properties pVpid, String namePar)
    {
        return toInteger(pVpid.getProperty(namePar, "0.0"));
    } 

	private static int toInteger(String property) { 
		String s =""+(int)Double.parseDouble( property);
		return Integer.parseInt(s );
	}

	public Pid(Properties pVpid) {
		// TODO refactor to reflection-way
        this.myInput = prop2Double(pVpid, "myInput");
        this.mySetpoint = prop2Double(pVpid, "mySetpoint");
        this.myOutput = prop2Double(pVpid, "myOutput"); 
        this.controllerDirection = prop2Integer(pVpid, "controllerDirection");
        this.kd = prop2Double(pVpid, "kd");
        this.ki = prop2Double(pVpid, "ki"); 
        this.kp = prop2Double(pVpid, "kp");
        this.outMax = prop2Double(pVpid, "outMax");
        this.outMin = prop2Double(pVpid, "outMin"); 
        

	    this.setControllerDirection(controllerDirection);
	    this.setTunings(kp, ki, kd);
	    
	    lastTime = millis()-SampleTime;				
	    inAuto = true;
	    //myOutput = Output;
	    //myInput = Input;
	    //mySetpoint = Setpoint;
	}

	private long millis() { 
		return System.currentTimeMillis(); 
	} 
 

	
	/**
	 *  Compute() **********************************************************************
	 *     This, as they say, is where the magic happens.  this function should be called
	 *   every time "void loop()" executes.  the function will decide for itself whether a new
	 *   pid Output needs to be computed
	 **********************************************************************************/ 
	void Compute()
	//void PID::Compute()
	{
	   if(!inAuto) return;
	   long now = millis();
	   long timeChange = (now - lastTime);
	   if(timeChange>=SampleTime)
	   {
	      /*Compute all the working error variables*/
		  double input = myInput;
	      double error = mySetpoint - input;
	      ITerm+= (ki * error);
	      if(ITerm > outMax) ITerm= outMax;
	      else if(ITerm < outMin) ITerm= outMin;
	      double dInput = (input - lastInput);
	 
	      /*Compute PID Output*/
	      double output = kp * error + ITerm- kd * dInput;
	      
		  if(output > outMax) output = outMax;
	      else if(output < outMin) output = outMin;
		  myOutput = output;
		  
	      /*Remember some variables for next time*/
	      lastInput = input;
	      lastTime = now;
	   }
	}


	/**
	 *  SetTunings(...)*************************************************************
	 * This function allows the controller's dynamic performance to be adjusted. 
	 * it's called automatically from the constructor, but tunings can also
	 * be adjusted on the fly during normal operation
	 ******************************************************************************/ 
	void setTunings(double Kp, double Ki, double Kd)
	//void PID::SetTunings(double Kp, double Ki, double Kd)
	{
	   if (Kp<0 || Ki<0 || Kd<0) return;
	 
	   dispKp = Kp; dispKi = Ki; dispKd = Kd;
	   
	   double SampleTimeInSec = ((double)SampleTime)/1000;  
	   SampleTimeInSec = SampleTimeInSec==0?0.01:SampleTimeInSec;
	   kp = Kp;
	   ki = Ki * SampleTimeInSec;
	   kd = Kd / SampleTimeInSec;
	 
	  if(controllerDirection ==REVERSE)
	   {
	      kp = (0 - kp);
	      ki = (0 - ki);
	      kd = (0 - kd);
	   }
	}
	  
	/** 
	 * SetSampleTime(...) *********************************************************
	 * sets the period, in Milliseconds, at which the calculation is performed	
	 ******************************************************************************/
	//void PID::SetSampleTime(int NewSampleTime)
	void  setSampleTime(int NewSampleTime)
	{
	   if (NewSampleTime > 0)
	   {
	      double ratio  = (double)NewSampleTime
	                      / (double)SampleTime;
	      ki *= ratio;
	      kd /= ratio;
	      SampleTime =  NewSampleTime;
	   }
	}
	 
	/**
	 * SetOutputLimits(...)****************************************************
	 *     This function will be used far more often than SetInputLimits.  while
	 *  the input to the controller will generally be in the 0-1023 range (which is
	 *  the default already,)  the output will be a little different.  maybe they'll
	 *  be doing a time window and will need 0-8000 or something.  or maybe they'll
	 *  want to clamp it from 0-125.  who knows.  at any rate, that can all be done
	 *  here.
	 **************************************************************************/
	//void PID::SetOutputLimits(double Min, double Max)
	void  setOutputLimits(double Min, double Max)
	{
	   if(Min >= Max) return;
	   outMin = Min;
	   outMax = Max;
	 
	   if(inAuto)
	   {
		   if( myOutput > outMax)  myOutput = outMax;
		   else if( myOutput < outMin)  myOutput = outMin;
		 
		   if(ITerm > outMax) ITerm= outMax;
		   else if(ITerm < outMin) ITerm= outMin;
	   }
	}

	/** 
	 * SetMode(...)****************************************************************
	 * Allows the controller Mode to be set to manual (0) or Automatic (non-zero)
	 * when the transition from manual to auto occurs, the controller is
	 * automatically initialized
	 ******************************************************************************/ 
	//void PID::SetMode(int Mode)
	void setMode(int Mode)
	{
	    boolean newAuto = (Mode == AUTOMATIC);
	    if(newAuto == !inAuto)
	    {  /*we just went from manual to auto*/
	       Initialize();
	    }
	    inAuto = newAuto;
	}
	 
	/**
	 *  Initialize()****************************************************************
	 *	does all the things that need to happen to ensure a bumpless transfer
	 *  from manual to automatic mode.
	 ******************************************************************************/ 
	private void Initialize()  
	//void PID::Initialize()
	{
	   this.ITerm = this.myOutput;
	   this.lastInput = this.myInput;
	   if(ITerm > outMax) ITerm = outMax;
	   else if(ITerm < outMin) ITerm = outMin;
	}

	/**
	 *  SetControllerDirection(...)*************************************************
	 * The PID will either be connected to a DIRECT acting process (+Output leads 
	 * to +Input) or a REVERSE acting process(+Output leads to -Input.)  we need to
	 * know which one, because otherwise we may increase the output when we should
	 * be decreasing.  This is called from the constructor.
	 ******************************************************************************/
	private void setControllerDirection(int Direction)  
//	void PID::SetControllerDirection(int Direction)
	{
	   if(inAuto && Direction !=controllerDirection)
	   {
		  kp = (0 - kp);
	      ki = (0 - ki);
	      kd = (0 - kd);
	   }   
	   this.controllerDirection = Direction;
	}

	/**
	 *  Status Funcions*************************************************************
	 * Just because you set the Kp=-1 doesn't mean it actually happened.  these
	 * functions query the internal state of the PID.  they're here for display 
	 * purposes.  this are the functions the PID Front-end uses for example
	 ******************************************************************************/
	double  getKp(){ return  dispKp; }
	double  getKi(){ return  dispKi;}
	double  getKd(){ return  dispKd;}
	int  getMode(){ return  inAuto ? AUTOMATIC : MANUAL;}
	int  getDirection(){ return controllerDirection;}

	public double Compute(double input) {
		this.myInput = input;
		Compute();
		return this.myOutput;
	}
	
	
	
	/*
	 * 
	 * 
	 */
    public Properties toProperties()
    {
		// TODO refactore to reflection-way
        Properties retval = new Properties();
        double2prop(retval, "kd", this.dispKd); 
        double2prop(retval, "ki", this.dispKi ); 
        double2prop(retval, "kp", this.dispKp); 
        double2prop(retval, "myInput", this.myInput); 
        double2prop(retval, "myOutput", this.myOutput); 
        double2prop(retval, "mySetpoint", this.mySetpoint); 
        double2prop(retval, "outMax", this.outMax ); 
        double2prop(retval, "outMin", this.outMin); 
 
        return retval;
    }
}


 