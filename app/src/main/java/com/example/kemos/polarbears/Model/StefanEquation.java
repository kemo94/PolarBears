package com.example.kemos.polarbears.Model;

public class StefanEquation {

	private double  icethickness  ;
	private  double heattransfercoefficient ; 
	private final double thermalconductivity = 2.24 ; 
	private final double latentheatoffusion = 3.3400000 ;
	private  double airtemperature  ;
	private final double densityofice =  917 ; 
	private  double HScoveespectivel ; 
	
	public StefanEquation(double thick , double winds , double temp) {
		
		icethickness = thick;
		airtemperature = temp;
		heattransfercoefficient = (winds*25)/4;
		HScoveespectivel = (1000/200 )*(1-917/1000)*icethickness;
		  
	}
	
	
	public double getTicknessConverge ()
	{
    double h  = (HScoveespectivel/0.0000032 * 917*917) + (icethickness/thermalconductivity) + (1/heattransfercoefficient) ; 
	       h  = h * densityofice * latentheatoffusion;
	       h = -1*airtemperature/h;
	     return h;  
    }
	 
}
	

