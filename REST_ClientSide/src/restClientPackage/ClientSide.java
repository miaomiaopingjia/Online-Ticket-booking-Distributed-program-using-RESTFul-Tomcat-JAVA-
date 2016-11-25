package restClientPackage;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

// For all the details and documentation:
// https://jersey.java.net/documentation/2.7/user-guide.html#client

public class ClientSide {
	private Client client;
	private String REST_SERVICE_URL = "http://localhost:8080/REST_FlightServer/rest/FlightBookingService/BookedFlights";
	private static final String PASS = "pass";
	private static final String FAIL = "fail";
	
	private void init(){
		this.client = ClientBuilder.newClient();
	}
	
	public static void main(String[] args){
		List<Long> PNRs = new ArrayList<>();
		ClientSide clientSide = new ClientSide();
		//initialize the client
		clientSide.init();
		
		//add Booked Flight Web Service Method
		Long PNR;
		PNR = clientSide.BookFlight(new BookingFlight("Albert Levi", "Flight - 1", 4));
		PNRs.add(PNR);
		PNR = clientSide.BookFlight(new BookingFlight("Erkay Savas", "Flight - 2", 40));
		PNRs.add(PNR);
		PNR = clientSide.BookFlight(new BookingFlight("Ramin Armanfar", "Flight - 3", 20));
		PNRs.add(PNR);
		PNR = clientSide.BookFlight(new BookingFlight("Atil Utku Ay", "Flight - 3", 20));
		PNRs.add(PNR);
		PNR = clientSide.BookFlight(new BookingFlight("Mina Hanim", "Flight - 1", 40));
		PNRs.add(PNR);
		
		//get all Booked Flights Web Service Method
		List<BookingFlight> bookedFlightsList = clientSide.GetAllBookedFlights();
		ShowAllBookedFlightsInfo(bookedFlightsList);
		
		//get Booked Flight Web Service Method by PNR
		BookingFlight RetbookingFlight = clientSide.GetBookedFlight(PNRs.get(2));
		ShowBookedFlightInfo(RetbookingFlight);
		AddLog("Search for PNR: " + PNRs.get(2), true);
		
		//get Booked Flight Web Service Method by PNR & Passenger Name
		RetbookingFlight = clientSide.GetBookedFlight(PNRs.get(1), "Erkay Savas");
		ShowBookedFlightInfo(RetbookingFlight);
		AddLog("Search for PNR: "+PNRs.get(1)+" , Passenger: Erkay Savas", true);
		
		//update Booked Flight Web Service Method
		clientSide.UpdateBookedFlight(new BookingFlight(PNRs.get(0), "Merve Can kus Khalilov", "Flight - 3", 11));
		
		//update Booked Flight Web Service Method
		clientSide.UpdateBookedFlight(new BookingFlight(PNRs.get(0), "Merve Can kus Khalilov", "Flight - 3", 10));
		
		//delete Booked Flight Web Service Method
		clientSide.DeleteBookedFlight(PNRs.get(2), "Ramin Armanfar");

		bookedFlightsList = clientSide.GetAllBookedFlights();
		ShowAllBookedFlightsInfo(bookedFlightsList);
	}
	
	//Get list of all Booked Flights
	//Check if list is not empty
	private List<BookingFlight> GetAllBookedFlights(){
		GenericType<List<BookingFlight>> list = new GenericType<List<BookingFlight>>(){};
		List<BookingFlight> bookedFlights = client
				.target(REST_SERVICE_URL)
				.request(MediaType.APPLICATION_XML)
				.get(list);
		
		String result = PASS;
		if(bookedFlights.isEmpty()) result = FAIL;
		System.out.println("Get All Booked Flights, Result: " + result);
	    return bookedFlights;
    }
	
	//Get Booked Flight Info By PNR 
	private BookingFlight GetBookedFlight(long PNR){
		BookingFlight sampleBookingFlight = new BookingFlight();
		sampleBookingFlight.setPNR(PNR);
		
		BookingFlight bookingFlight = client
				.target(REST_SERVICE_URL)
				.path("/{PNR}")
				.resolveTemplate("PNR", PNR)
				.request(MediaType.APPLICATION_XML)
				.get(BookingFlight.class);
		
		if(bookingFlight != null && sampleBookingFlight.getPNR() == bookingFlight.getPNR()){
			return bookingFlight;
		}
		return null;
	}
	
	//Get Booked Flight Info By PNR  & PassengerName
	private BookingFlight GetBookedFlight(long PNR, String PassengerName){
		BookingFlight sampleBookingFlight = new BookingFlight();
		sampleBookingFlight.setPNR(PNR);
		sampleBookingFlight.setPassengerName(PassengerName);
		        
		BookingFlight bookingFlight = client
				.target(REST_SERVICE_URL)
				.path("/{PNR}/{PassengerName}")
				.resolveTemplate("PNR", Long.toString(PNR))
				.resolveTemplate("PassengerName", PassengerName)
				.request(MediaType.APPLICATION_XML)
				.get(BookingFlight.class);
		
		if(bookingFlight != null && sampleBookingFlight.getPNR() == bookingFlight.getPNR()&& sampleBookingFlight.getPassengerName().equals(bookingFlight.getPassengerName())){
			return bookingFlight;
		}
		return null;
	}
	
	//Add Booked Flight
	private long BookFlight(BookingFlight bookingFlight){
		Form form = new Form();
		//form.param("PNR", Long.toString(bookingFlight.getPNR()));
		form.param("PassengerName", bookingFlight.getPassengerName());
		form.param("FlightNumber", bookingFlight.getFlightNumber());
		form.param("NoOfSeatsBooked", Integer.toString(bookingFlight.getNoOfSeatsBooked()));
		
		String callResult = client
				.target(REST_SERVICE_URL)
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		
		long PNR;
		if(callResult.contains("<failure>")){
			PNR = -1;
			callResult = callResult.substring(callResult.indexOf("<failure>") + 9, callResult.indexOf("</failure>"));
			System.out.println("Error, Add Booked Flight was unsuccessfull,\r\nResult: " + callResult);
		}
		else{
			PNR = Long.parseLong(callResult);
			System.out.println("Flight has successully been booked, PNR: " + callResult);
		}
		return PNR;
	}

	   
	   //Update Booked Flight Info
	   private void UpdateBookedFlight(BookingFlight bookingFlight){
	      Form form = new Form();
	      form.param("PNR", Long.toString(bookingFlight.getPNR()));
	      form.param("PassengerName", bookingFlight.getPassengerName());
	      form.param("FlightNumber", bookingFlight.getFlightNumber());
	      form.param("NoOfSeatsBooked", Integer.toString(bookingFlight.getNoOfSeatsBooked()));

	      String callResult = client
	         .target(REST_SERVICE_URL)
	         .request(MediaType.APPLICATION_XML)
	         .put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	      
	      System.out.println("Update Booked Flight, Result: " + callResult);
	   }
	   	   
	   //Delete Booked Flight by PNR && Passenger Name
	   private void DeleteBookedFlight(long PNR, String PassengerName){
		   String callResult = client
				   .target(REST_SERVICE_URL)
				   .path("/{PNR}/{PassengerName}")
				   .resolveTemplate("PNR", PNR)
				   .resolveTemplate("PassengerName", PassengerName)
				   .request(MediaType.APPLICATION_XML)
				   .delete(String.class);
		   
		   System.out.println("Delete Booked Flight, Result: " + callResult);
	   }
	   
	   public static void ShowBookedFlightInfo(BookingFlight bookingFlight){
		   if(bookingFlight == null){
			   System.out.println("No data has been retrieved...");
		   }
			else{
				System.out.println("PNR: " + bookingFlight.getPNR());
				System.out.println("Passenger Name: " + bookingFlight.getPassengerName());
				System.out.println("Flight Number: " + bookingFlight.getFlightNumber());
				System.out.println("Number of Booked Seats: " + bookingFlight.getNoOfSeatsBooked());
			}
	  }
		  
	  public static void ShowAllBookedFlightsInfo(List<BookingFlight> bookedFlightsList){
			for(BookingFlight bookFligt: bookedFlightsList){
				ShowBookedFlightInfo(bookFligt);
				System.out.println("--------------------------------------------------------");
			}
			AddLog("End of booked flights list...", true);
	  }
	  
	  private static void AddLog(String MessageToAdd, boolean AddSeparator){
			System.out.println(">>> " + MessageToAdd);
			if(AddSeparator){
				System.out.println("--------------------------------------------------------");				
			}
	  }
}
