package com.trainbooking;

import com.trainbooking.dto.BookingDTO;
import com.trainbooking.dto.TrainDTO;
import com.trainbooking.model.Admin;
import com.trainbooking.model.Passenger;
import com.trainbooking.repository.*;
import com.trainbooking.service.*;
import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Instantiate Repositories
        UserRepository userRepository = new UserRepository();
        PassengerRepository passengerRepository = new PassengerRepository();
        AdminRepository adminRepository = new AdminRepository();
        TrainRepository trainRepository = new TrainRepository();
        CoachRepository coachRepository = new CoachRepository();
        BookingRepository bookingRepository = new BookingRepository();

        // Instantiate Services via Constructor Injection
        UserService userService = new UserService(userRepository, passengerRepository);
        BookingService bookingService = new BookingService(bookingRepository, coachRepository, trainRepository);
        TrainService trainService = new TrainService(trainRepository);
        AdminService adminService = new AdminService(adminRepository, trainRepository);

        Passenger user = null; // Track logged-in passenger
        Admin adminUser = null; // Track logged-in admin

        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("Click 1 to View your credentials");
            System.out.println("Click 2 to Purchase Ticket");
            System.out.println("Click 3 to Register Customer");
            System.out.println("Click 4 to See your Tickets");
            System.out.println("Click 5 to Login");
            System.out.println("Click 6 to Logout");
            System.out.println("Click 7 to Delete Customer");
            System.out.println("Click 8 to Delete Ticket");
            System.out.println("Click 9 for admin Login");
            System.out.println("Click 10 for admin Logout");
            System.out.println("Click 11 for report generation(Admin only)");
            System.out.println("Click 12 for Train Scheduling(Admin only)");
            System.out.println("Click 13 to Exit");
            System.out.print("Enter your choice: ");
            
            int choice = -1;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    if (user != null) {
                        System.out.println("Passenger Credentials:");
                        System.out.println("Name: " + user.getName());
                        System.out.println("Email: " + user.getEmail());
                        System.out.println("Phone: " + user.getPhone());
                        System.out.println("Age: " + user.getAge());
                        System.out.println("Gender: " + user.getGender());
                        System.out.println("User ID: " + user.getUserID());
                        System.out.println("Passenger ID: " + user.getPassengerID());
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 2:
                    if (user != null) {
                        System.out.println("Where to?");
                        try {
                            List<TrainDTO> schedules = trainService.getTrainSchedules();
                            schedules.forEach(t -> System.out.println("Train ID: " + t.getTrainId() + " Route: " + t.getSource() + " -> " + t.getDestination() + " [" + String.join(", ", t.getAvailableClasses()) + "]"));
                            
                            System.out.println("Enter the train ID u want to book?");
                            String trainID = sc.nextLine();
                            String classes = trainService.getAvailableCoachTypes(trainID);
                            System.out.println("Available classes: " + classes);
                            System.out.println("Enter the name of the coach you want");
                            String coachType = sc.nextLine();
                            System.out.println("Enter the number of seats");
                            if (sc.hasNextInt()) {
                                int seats = sc.nextInt();
                                sc.nextLine();
                                System.out.println("Do you confirm booking? Y/N");
                                String confirm = sc.nextLine();
                                if (confirm.equalsIgnoreCase("Y")) {
                                    BookingDTO bookingDto = new BookingDTO(0, trainID, coachType, seats);
                                    bookingService.confirmBooking(bookingDto, user.getPassengerID());
                                    System.out.println("Booking confirmed successfully!");
                                } else {
                                    System.out.println("Booking cancelled");
                                }
                            } else {
                                System.out.println("Invalid seats input.");
                                sc.nextLine();
                            }
                        } catch (Exception e) {
                            System.out.println("Booking failed: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter your email: ");
                        String email = sc.nextLine();
                        System.out.print("Enter your phone: ");
                        String phone = sc.nextLine();
                        System.out.print("Enter your age: ");
                        int age = -1;
                        if (sc.hasNextInt()) {
                            age = sc.nextInt();
                            sc.nextLine();
                        } else {
                            System.out.println("Invalid age input.");
                            sc.nextLine();
                            break;
                        }
                        System.out.print("Enter your gender: ");
                        String gender = sc.nextLine();
                        
                        Console console = System.console();
                        String password;
                        if (console != null) {
                            char[] passwordArray = console.readPassword("Enter your password: ");
                            password = new String(passwordArray);
                        } else {
                            System.out.print("Enter your password: ");
                            password = sc.nextLine();
                        }

                        Passenger passenger = new Passenger(0, 0, name, email, phone, password, age, gender);
                        Passenger registeredPassenger = userService.registerPassenger(passenger);
                        System.out.println("Customer registered successfully with Passenger ID: " + registeredPassenger.getPassengerID());
                    } catch (Exception e) {
                        System.out.println("Error during customer registration: " + e.getMessage());
                    }
                    break;

                case 4:
                    if (user != null) {
                        System.out.println("Your tickets:");
                        try {
                            List<BookingDTO> bookings = bookingService.getBookingsForPassenger(user.getPassengerID());
                            if (bookings.isEmpty()) {
                                System.out.println("No tickets found.");
                            } else {
                                bookings.forEach(booking -> System.out.println("Booking ID: " + booking.getBookingId() + ", Train: " + booking.getTrainId() + ", Coach: " + booking.getCoachType() + ", Seats: " + booking.getNumberOfSeats()));
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to fetch tickets: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 5:
                    if (user != null) {
                        System.out.println("You are already logged in!!");
                    } else {
                        System.out.print("Enter Email: ");
                        String inputEmail = sc.nextLine();
                        System.out.print("Enter Password: ");
                        String inputPassword = sc.nextLine();
                        try {
                            user = userService.loginPassenger(inputEmail, inputPassword);
                            System.out.println("Login successful. Welcome, " + user.getName() + ".");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            user = null;
                        }
                    }
                    break;

                case 6:
                    if (user != null) {
                        try {
                            userService.logoutPassenger(user);
                            System.out.println("Goodbye, " + user.getName());
                            user = null;
                        } catch (Exception ex) {
                            System.out.println("Logout failed: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;

                case 7:
                    System.out.println("Delete customer feature is not implemented yet.");
                    break; // Fix missing break case fallthrough bug

                case 8:
                    if (user != null) {
                        System.out.print("Enter Booking ID to delete: ");
                        if (sc.hasNextInt()) {
                            int ticketId = sc.nextInt();
                            sc.nextLine();
                            try {
                                bookingService.cancelBooking(ticketId);
                                System.out.println("Ticket deleted successfully.");
                            } catch (Exception ex) {
                                System.out.println("Cancellation failed: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Invalid booking ID.");
                            sc.nextLine();
                        }
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;

                case 9:
                    if (user != null) {
                        System.out.println("First logout as user.");
                    } else {
                        System.out.print("Enter Email: ");
                        String inputEmail = sc.nextLine();
                        System.out.print("Enter Password: ");
                        String inputPassword = sc.nextLine();
                        try {
                            adminUser = adminService.loginAdmin(inputEmail, inputPassword);
                            System.out.println("Admin login successful! Welcome, " + adminUser.getName() + ".");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            adminUser = null;
                        }
                    }
                    break;

                case 10:
                    if (adminUser != null) {
                        try {
                            adminService.logoutAdmin(adminUser);
                            System.out.println("Goodbye, " + adminUser.getName());
                            adminUser = null;
                        } catch (Exception ex) {
                            System.out.println("Logout failed: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;

                case 11:
                    if (adminUser != null) {
                        try {
                            adminService.printAdminReport();
                        } catch (Exception ex) {
                            System.out.println("Report generation failed: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("You are not Authorised");
                    }
                    break;

                case 12:
                    if (adminUser != null) {
                        System.out.println("enter Train ID");
                        String trainID = sc.nextLine();
                        System.out.print("Enter new departure time (HH:mm:ss): ");
                        String departureTime = sc.nextLine();
                        System.out.print("Enter new arrival time (HH:mm:ss): ");
                        String arrivalTime = sc.nextLine();
                        System.out.print("Do you want to reset seats? Y/N: ");
                        String yesOrNo = sc.nextLine();
                        boolean reset = yesOrNo.equalsIgnoreCase("Y");
                        try {
                            adminService.editTrainSchedule(trainID, departureTime, arrivalTime, reset);
                            System.out.println("Train schedule updated successfully.");
                        } catch (Exception ex) {
                            System.out.println("Failed to edit schedule: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("You are not Authorised");
                    }
                    break;

                case 13:
                    System.out.println("Exiting program...");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
