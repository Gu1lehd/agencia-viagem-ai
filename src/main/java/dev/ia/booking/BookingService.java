package dev.ia.booking;

import dev.ia.security.SecurityContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookingService {

    private final Map<Long, Booking> bookings = new HashMap<>();
    public BookingService() {

            // Dados de exemplo atualizados com a categoria
            bookings.put(12345L, new Booking(12345L, "John Doe", "Tesouros do Egito",
                    LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(10), BookingStatus.CONFIRMED, Category.TREASURE));
            bookings.put(67890L, new Booking(67890L, "Jane Smith", "Aventura Amazônia",
                    LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(3).plusDays(7), BookingStatus.CONFIRMED, Category.ADVENTURE));
            bookings.put(98765L, new Booking(98765L, "Peter Jones", "Trilha Inca",
                    LocalDate.now().plusMonths(4), LocalDate.now().plusMonths(4).plusDays(8), BookingStatus.CONFIRMED, Category.ADVENTURE));
        }

        public List<Booking> findPackagesByCategory(Category category) {
            return bookings.values().stream()
                    .filter(booking -> category.equals(booking.category()))
                    .toList();
        }

    public Optional<Booking> cancelBooking(long bookingId) {
        String currentUser = SecurityContext.getCurrentUser();
        if (bookings.containsKey(bookingId)) {
            Booking booking = bookings.get(bookingId);
            // Verifica se o usuário atual é o dono da reserva
            if (booking.customerName().equals(currentUser)) {
                Booking cancelledBooking = new Booking(
                        booking.id(),
                        booking.customerName(),
                        booking.destination(),
                        booking.startDate(),
                        booking.endDate(),
                        BookingStatus.CANCELLED, // O novo status
                        booking.category()
                );
                this.bookings.replace(bookingId, cancelledBooking);
                return Optional.of(cancelledBooking);
            }
        }
        return Optional.empty();
    }

}
