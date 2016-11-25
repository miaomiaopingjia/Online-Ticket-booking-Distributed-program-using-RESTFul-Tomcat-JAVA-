package restPackage;

import java.util.ArrayList;
import java.util.List;

public class FlightList {
	private List<Flight> FlightsList = new ArrayList<Flight>();
	
	public FlightList(){
		for(int i = 1; i <= 10; i++){
			addNewFlight("Flight - " + Integer.toString(i), 189);
		}
	}
	
	public List<Flight> getAllFlightsInfo(){
		return FlightsList;
	}
	
	public Flight getFlightInfoByFlightNo(String FlightNumber){
		for(Flight flight: FlightsList){
			if(flight.getFlightNumber().equals(FlightNumber)){
				return flight;
			}
		}
		return null;
	}
	
	public int getFlightAvailableSeatsByFlightNo(String FlightNumber){
		for(Flight flight: FlightsList){
			if(flight.getFlightNumber().equals(FlightNumber)){
				return flight.getAvailableSeat();
			}
		}
		return -1;
	}
	
	public void addNewFlight(String FlightNumber, int AvailableSeats){
		Flight flight = new Flight(FlightNumber, AvailableSeats);
		FlightsList.add(flight);
	}
	
	public boolean updateFlightAvailableSeatsByFlightNo(String FlightNumber, int AvailableSeats){
		for(Flight flight: FlightsList){
			if(flight.getFlightNumber().equals(FlightNumber)){
				flight.setAvailableSeat(AvailableSeats);
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteFlight(String FlightNumber){
		for(Flight flight: FlightsList){
			if(flight.getFlightNumber().equals(FlightNumber)){
				int index = FlightsList.indexOf(flight);
				FlightsList.remove(index);
				return true;
			}
		}
		return false;
	}
}
