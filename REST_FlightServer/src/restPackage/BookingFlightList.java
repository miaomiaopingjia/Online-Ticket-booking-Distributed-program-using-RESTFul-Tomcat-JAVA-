package restPackage;
import java.util.ArrayList;
import java.util.List;

public class BookingFlightList {
	private List<BookingFlight> bookedFlightsList = new ArrayList<BookingFlight>();
	
	public List<BookingFlight> getAllBookedFlights(){
		return bookedFlightsList;
	}
	   
	public BookingFlight getBookedFlight(long PNR){
		for(BookingFlight bookingFlight: bookedFlightsList){
			if(bookingFlight.getPNR() == PNR){
				return bookingFlight;
			}
		}
		return null;
	}
	public BookingFlight getBookedFlight(long PNR, String PassengerName){
		for(BookingFlight bookingFlight: bookedFlightsList){
			if(bookingFlight.getPNR() == PNR && PassengerName.equals(bookingFlight.getPassengerName())){
				return bookingFlight;
			}
		}
		return null;
	}
	
	public int addBooking(BookingFlight pBookingFlight){
		boolean userExists = false;
		for(BookingFlight bookingFlight: bookedFlightsList){
			if(bookingFlight.getPNR() == pBookingFlight.getPNR()){
				userExists = true;
				break;
			}
		}		
		      
		if(!userExists){
			bookedFlightsList.add(pBookingFlight);
			return 1;
		}
		return 0;
	}
	
	public int updateBooking(BookingFlight pBookingFlight){
		for(BookingFlight bookingFlight: bookedFlightsList){
			if(bookingFlight.getPNR() == pBookingFlight.getPNR()){
				int index = bookedFlightsList.indexOf(bookingFlight);			
				bookedFlightsList.set(index, pBookingFlight);
				return 1;
			}
		}		
		return 0;
	}
	
	public int deleteBooking(long PNR, String PassengerName){
		for(BookingFlight bookingFlight: bookedFlightsList){
			if(bookingFlight.getPNR() == PNR && bookingFlight.getPassengerName().equals(PassengerName)){
				int index = bookedFlightsList.indexOf(bookingFlight);			
				bookedFlightsList.remove(index);
				return 1;   
			}
		}		
		return 0;
	}
}
