package restPackage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
//Sets the path to base URL + /hello 

@Path("/FlightBookingService")
public class FlightService {
	static FlightList flightList = new FlightList(); 
	static BookingFlightList bookingFlightList = new BookingFlightList();
	static Random randomNo = new Random();

	
	private static final String SUCCESS_RESULT="<success>operation was success</success>";
	private static final String FAILURE_RESULT="<failure>general failure occured</failure>";
	private static final String FAILURE_RESULT_FLIGHT_NOT_EXISTS="<failure>requested flight is not exists</failure>";
	private static final String FAILURE_RESULT_FLIGHT_NOT_ENOUGH_SEATS="<failure>there is not enough seats available</failure>";
	private static final String FAILURE_RESULT_BOOKING_NOT_FOUND="<failure>there is no such booking information with given PNR</failure>";
	
	@GET
	@Path("/BookedFlights")
	@Produces(MediaType.APPLICATION_XML)
	public List<BookingFlight> getBookedFlights(){
		return bookingFlightList.getAllBookedFlights();
	}
	
	@GET
	@Path("/BookedFlights/{PNR}")
	@Produces(MediaType.APPLICATION_XML)
	public BookingFlight getBookedFlightByPNR(@PathParam("PNR") long PNR){
		return bookingFlightList.getBookedFlight(PNR);
	}
	
	@GET
	@Path("/BookedFlights/{PNR}/{PassengerName}")
	@Produces(MediaType.APPLICATION_XML)
	public BookingFlight getBookedFlightByPNR(
			@PathParam("PNR") long PNR, 
			@PathParam("PassengerName") String PassengerName){
		return bookingFlightList.getBookedFlight(PNR, PassengerName);
	}
	
	@POST
	@Path("/BookedFlights")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createBooking(
			@FormParam("PassengerName") String PassengerName,
			@FormParam("FlightNumber") String FlightNumber,
			@FormParam("NoOfSeatsBooked") int NoOfSeatsBooked,
			@Context HttpServletResponse servletResponse) throws IOException{
		//--- check whether the flight number is exists ---
		Flight flight = flightList.getFlightInfoByFlightNo(FlightNumber);
		if(flight == null){
			return FAILURE_RESULT_FLIGHT_NOT_EXISTS;
		}
		else if(flight.getAvailableSeat() < NoOfSeatsBooked){
			return FAILURE_RESULT_FLIGHT_NOT_ENOUGH_SEATS;
		}
		else{
			long PNR;
			do{
				PNR = randomNo.nextLong();
				if(PNR < 0) PNR = -PNR;
			}while(bookingFlightList.getBookedFlight(PNR) != null);

			BookingFlight bookFlight = new BookingFlight(PNR, PassengerName, FlightNumber, NoOfSeatsBooked);
			int result = bookingFlightList.addBooking(bookFlight);
			if(result == 1){
				flight.subAvailableSeatsBy(NoOfSeatsBooked);
				return Long.toString(PNR);
			}
		}
		return FAILURE_RESULT;
	}
	
	@PUT
	@Path("/BookedFlights")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateBookedFlight(@FormParam("PNR") long PNR,
			@FormParam("PassengerName") String PassengerName,
			@FormParam("FlightNumber") String FlightNumber,
			@FormParam("NoOfSeatsBooked") int NoOfSeatsBooked,
			@Context HttpServletResponse servletResponse) throws IOException{
		
		BookingFlight currentBookingFlight = bookingFlightList.getBookedFlight(PNR);
		BookingFlight updatedbookingFlight;
		if(currentBookingFlight == null){
			return FAILURE_RESULT_BOOKING_NOT_FOUND;
		}
		else{
			String NewPassengerName = PassengerName;
			String NewFlightNumber = FlightNumber;
			
			if(PassengerName == null){
				NewPassengerName = currentBookingFlight.getPassengerName();
			}
			if(FlightNumber == null){
				NewFlightNumber = currentBookingFlight.getFlightNumber();
			}
			else if(flightList.getFlightInfoByFlightNo(FlightNumber) == null){
				return FAILURE_RESULT_FLIGHT_NOT_EXISTS;
			}
				
			//BookingFlight newBookingFlight = bookingFlightList.getBookedFlight(PNR);
			if(NoOfSeatsBooked != -1){//---> -1 means no need to change Seats
				
				String currentFlightNumber = currentBookingFlight.getFlightNumber();
				int currentBookedSeats = currentBookingFlight.getNoOfSeatsBooked();
				
				int AvailableSeatsOnNewFlight =  flightList.getFlightAvailableSeatsByFlightNo(FlightNumber);
				int numberOfRemainingSeatsAfterUpdate;
				if(currentFlightNumber == NewFlightNumber){
					numberOfRemainingSeatsAfterUpdate = AvailableSeatsOnNewFlight + currentBookedSeats - NoOfSeatsBooked;
				}
				else{
					numberOfRemainingSeatsAfterUpdate = AvailableSeatsOnNewFlight - NoOfSeatsBooked;
				}
				if(numberOfRemainingSeatsAfterUpdate < 0){
					return FAILURE_RESULT_FLIGHT_NOT_ENOUGH_SEATS;
				}
				else{
					//--- old seats should be delivered back to the old flight number
					//--- and new requested seats should be substracted from new requested flight number
					flightList.getFlightInfoByFlightNo(currentFlightNumber).addAvailableSeatsBy(currentBookedSeats);
					flightList.getFlightInfoByFlightNo(FlightNumber).subAvailableSeatsBy(NoOfSeatsBooked);
					updatedbookingFlight = new BookingFlight(PNR, NewPassengerName, NewFlightNumber, NoOfSeatsBooked);
				}
			}
			else{//--- no need to change number of seats booked
				//--- no need to change number of seats because -1 means don't change seats number (null fo number)
				NoOfSeatsBooked = currentBookingFlight.getNoOfSeatsBooked();
				updatedbookingFlight = new BookingFlight(PNR, NewPassengerName, NewFlightNumber, NoOfSeatsBooked);
			}
		}

		int result = bookingFlightList.updateBooking(updatedbookingFlight);
		if(result == 1)	return SUCCESS_RESULT;
		else return FAILURE_RESULT;
	}
	
	@DELETE
	@Path("/BookedFlights/{PNR}/{PassengerName}")
	@Produces(MediaType.APPLICATION_XML)
	public String deleteUser(
			@PathParam("PNR") long PNR, 
			@PathParam("PassengerName") String PassengerName){

		BookingFlight bookedFlight = bookingFlightList.getBookedFlight(PNR);
		int SeatsToReturnBack = bookedFlight.getNoOfSeatsBooked();
		flightList.getFlightInfoByFlightNo(bookedFlight.getFlightNumber()).addAvailableSeatsBy(SeatsToReturnBack);
		int result = bookingFlightList.deleteBooking(PNR, PassengerName);
		if(result == 1)	return SUCCESS_RESULT;
		return FAILURE_RESULT;
	}
	
	@OPTIONS
	@Path("/BookedFlights")
	@Produces(MediaType.APPLICATION_XML)
	public String getSupportedOperations(){
		return "<operations>GET, PUT, POST, DELETE</operations>";
	}
}