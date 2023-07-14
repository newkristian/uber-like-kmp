# Uber-like Application - Mobile App

## Tech Stack

- Android (Java)
- Google Maps API

## Description

The project is a mobile application that aims to provide users with a transportation service similar to Uber. It addresses the limitations of traditional taxi services by streamlining the transportation process and reducing interaction between users and drivers, resulting in faster, more consistent, and safer transportation experiences.

The mobile application caters to two types of users:

1. Driver:
   - View basic information about the application, including real-time vehicle tracking on a map.
   - Automatically receive ride assignments from the system, which updates their navigation.
   - Edit their profile (changes require approval from the administrator).
   - Access ride history and generate reports within a specified date range.
   - Have access to a PANIC button to indicate any issues with assigned rides.
   - Accept or decline ride assignments with a mandatory reason for refusal.
   - Toggle their availability for rides manually and become unavailable after exceeding 8 working hours in a day.

2. Passenger:
   - View basic information about the application, including estimated time and cost for transportation between selected destinations.
   - Request a ride and receive constant notifications about the status of the requested ride.
   - Track all available vehicles on a map in real-time.
   - Make payment at the time of booking and rate the driver after the ride.
   - Select multiple destinations for a ride, either in a preferred order or based on the system's recommended route.
   - Schedule rides for the future to prioritize assignment during peak hours.
   - Access complete ride history, view reports within a specified date range, define favorite routes for quick selection, and request assistance through a PANIC button in case of emergencies.
   - Update profile information and contact support for inquiries.

## How to run

1. Clone the repository.
2. Set up the [backend](https://github.com/VukRadmilovic/uber-like-backend) 
3. Open the project in Android Studio.
4. Configure Google Maps API credentials.
5. Build and run the application on an Android device or emulator.

## Related repositories
1. [Backend](https://github.com/VukRadmilovic/uber-like-backend)
2. [Frontend](https://github.com/VukRadmilovic/uber-like-frontend)
