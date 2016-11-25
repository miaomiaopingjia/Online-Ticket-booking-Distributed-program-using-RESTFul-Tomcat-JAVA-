package restPackage;

public class Flight {
	private String FlightNumber;
	private int AvilableSeats;
	
	public Flight(String FlightNumber, int AvilableSeats){
		this.FlightNumber = FlightNumber;
		this.AvilableSeats = AvilableSeats;
	}
	
	public String getFlightNumber(){
		return FlightNumber;
	}
	
	public int getAvailableSeat(){
		return AvilableSeats;
	}
	
	public void setFlightNumber(String FlightNumber){
		this.FlightNumber = FlightNumber;
	}
	
	public void setAvailableSeat(int AvailableSeat){
		this.AvilableSeats = AvailableSeat;
	}
	
	public void subAvailableSeatsBy(int NoOfSeatsToSubstract){
		this.AvilableSeats -= NoOfSeatsToSubstract;
	}
	public void addAvailableSeatsBy(int NoOfSeatsToAdd){
		this.AvilableSeats += NoOfSeatsToAdd;
	}

}
