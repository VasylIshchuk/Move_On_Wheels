package org.umcs.services;


import org.umcs.models.User;


public interface IRentalService {
    void showListRentalVehicles();

    void rentVehicle(User user);

    void returnVehicle(User user);
}
