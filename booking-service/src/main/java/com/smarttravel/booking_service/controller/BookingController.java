package com.smarttravel.booking_service.controller;

import com.smarttravel.booking_service.dto.BookingRequestDto;
import com.smarttravel.booking_service.dto.BookingResponseDto;
import com.smarttravel.booking_service.model.BookingStatus;
import com.smarttravel.booking_service.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto requestDto) {
        BookingResponseDto response = bookingService.createBooking(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        BookingResponseDto response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateBookingStatus(@PathVariable Long id,
                                                                  @RequestParam("status") BookingStatus status) {
        BookingResponseDto response = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<BookingResponseDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

}
