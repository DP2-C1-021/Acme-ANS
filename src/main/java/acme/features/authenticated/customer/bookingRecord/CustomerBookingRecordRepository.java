
package acme.features.authenticated.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingRecord;
import acme.entities.S2.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking getBookingById(Integer bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.id=:passengerId")
	Passenger getPassengerById(Integer passengerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId")
	Collection<Passenger> getAllPassengersByCustomerId(Integer customerId);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id=:bookingId")
	Collection<Passenger> getPassengersInBooking(Integer bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingRecord findBookingRecordBybookingIdpassengerId(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.id = :bookingRecordId")
	BookingRecord findBookingRecordById(@Param("bookingRecordId") Integer bookingRecordId);
}
