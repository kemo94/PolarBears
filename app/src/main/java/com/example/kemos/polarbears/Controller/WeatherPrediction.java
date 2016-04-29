package com.example.kemos.polarbears.Controller;

import android.content.Context;

import com.example.kemos.polarbears.Model.StefanEquation;

public class WeatherPrediction {


	private double temp;
	private double hmuity;
	private double windSpeed;
	private double windDirc;
	private Context cont;
    private double thickness ;


    public WeatherPrediction(double t, double h, double ws, double wd ,double thickness , Context x)
	{
		temp = t;
		hmuity = h;
		windSpeed = ws;
		windDirc = wd;
		cont = x;
        this.thickness =thickness ;
	}

	public double PredicitThickness(double Lon , double Lat)
	{

		StefanEquation formula = new StefanEquation(thickness , windSpeed , temp);
		return formula.getTicknessConverge();
	}
	void pridictStorms()
	{
		
	}
	
}
