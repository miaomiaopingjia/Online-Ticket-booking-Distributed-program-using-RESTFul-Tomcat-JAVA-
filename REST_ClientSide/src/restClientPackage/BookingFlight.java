package restClientPackage;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Bookings")
public class BookingFlight implements Serializable {
	
	  private static final long serialVersionUID = 1L;
	  private long PNR;
	  private String PassengerName;
	  private String FlightNumber;
	  private int NoOfSeatsBooked;
	  
	  public BookingFlight(){}
	  
	  public BookingFlight(String PassengerName, String FlightNumber,int NoOfSeatsBooked){
		  this.PNR = -1;
		  this.PassengerName = PassengerName;
		  this.FlightNumber = FlightNumber;
		  this.NoOfSeatsBooked = NoOfSeatsBooked;
	  }
	  
	  public BookingFlight(long PNR, String PassengerName, String FlightNumber,int NoOfSeatsBooked){
		  this.PNR = PNR;
		  this.PassengerName = PassengerName;
		  this.FlightNumber = FlightNumber;
		  this.NoOfSeatsBooked = NoOfSeatsBooked;
	  }
	  
	  public long getPNR() {
		  return PNR;
      }	  
	  @XmlElement
	  public void setPNR(long PNR) {
		  this.PNR = PNR;
	  }
	  
	  public String getPassengerName() {
		  return PassengerName;
      }	  
	  @XmlElement
	  public void setPassengerName(String PassengerName) {
		  this.PassengerName = PassengerName;
	  }

	  public String getFlightNumber() {
		  return FlightNumber;
      }	  
	  @XmlElement
	  public void setFlightNumber(String FlightNumber) {
		  this.FlightNumber = FlightNumber;
	  }

	  public int getNoOfSeatsBooked() {
		  return NoOfSeatsBooked;
      }	  
	  @XmlElement
	  public void setNoOfSeatsBooked(int NoOfSeatsBooked) {
		  this.NoOfSeatsBooked = NoOfSeatsBooked;
	  }
	  
	  @Override
	  public boolean equals(Object object){
		  if(object == null){
			  return false;
		  }else if(!(object instanceof BookingFlight)){
			  return false;
		  }else {
			  BookingFlight bookingFlight = (BookingFlight)object;
			  if(PNR == bookingFlight.getPNR()
					  && PassengerName.equals(bookingFlight.getPassengerName())
					  && FlightNumber.equals(bookingFlight.getFlightNumber())
					  && NoOfSeatsBooked == bookingFlight.getNoOfSeatsBooked()){
				  return true;
			  }
		  }
	      return false;
      }
}
