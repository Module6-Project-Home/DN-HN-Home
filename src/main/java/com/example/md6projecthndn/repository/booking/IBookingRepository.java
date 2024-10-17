package com.example.md6projecthndn.repository.booking;



import com.example.md6projecthndn.model.dto.BookingByHostDTO;
import com.example.md6projecthndn.model.dto.BookingByUserDTO;
import com.example.md6projecthndn.model.entity.booking.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAll(Pageable pageable);
    List<Booking> findByCheckOutDateBefore(LocalDate date);

    @Query(nativeQuery = true,  value = "SELECT * FROM bookings b WHERE b.property_id = :propertyId AND (b.check_in_date < :endDate AND b.check_out_date > :startDate)")
    List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    Page<Booking> findBookingByGuestId(Long guestId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings WHERE property_id IN (SELECT id FROM properties WHERE owner_id = :ownerId)")
    Page<Booking> findBookingByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT * FROM bookings WHERE property_id IN (SELECT id FROM properties WHERE owner_id = :ownerId AND property_id = :propertyId)")
    Page<Booking> findBookingByOwnerIdByPropertyId(@Param("ownerId") Long ownerId, @Param("propertyId") Long propertyId, Pageable pageable);

    @Query(nativeQuery = true,value = "select p.name, p.address, b.check_in_date, b.check_out_date, SUM(p.price_per_night * DATEDIFF(b.check_out_date, b.check_in_date)) AS total_spent, bs.description from users u join bookings b on u.id = b.guest_id join properties p on b.property_id = p.id join booking_status bs on b.booking_status_id = bs.id where u.id = :userId group by p.name, p.address, b.check_in_date, b.check_out_date, bs.description;")
   List<Object[]>  bookingByUser(@Param("userId") Long userId);




}
