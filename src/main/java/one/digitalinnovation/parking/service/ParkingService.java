package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.model.Parking;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingService {
    public static final double VALOR_HORA = 10.5;
    public static Map<String, Parking> parkingMap = new HashMap<>();

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public List<Parking> findAll(){
        if (parkingMap != null){
            return new ArrayList<>(parkingMap.values());
        } else {
            return new ArrayList<>();
        }
    }

    public Parking findById(String id){
        Parking parking = parkingMap.get(id);
        if (parking == null){
            throw new ParkingNotFoundException(id);
        }
        return parkingMap.get(id);
    }

    public Parking create(Parking parkingCreate) {
        String uuid = getUUID();
        parkingCreate.setId(uuid);
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingMap.put(uuid, parkingCreate);
        return parkingCreate;
    }

    public void delete(String id) {
        findById(id);
        parkingMap.remove(id);
    }

    public Parking update(String id, Parking parkingCreate) {
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parkingMap.replace(id, parking);
        return parking;
    }

    public Parking exit(String id) {
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(calcExit(parking));
        parkingMap.replace(id, parking);
        return parking;
    }

    private Double calcExit(Parking parking) {
        Duration duration = Duration.between(parking.getEntryDate(),parking.getExitDate());
        double min = (double) duration.toMinutes();
        return (min/60) * VALOR_HORA;
    }
}
