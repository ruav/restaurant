package com.restaurant.service;

import com.example.QRCode.Helper.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.restaurant.entity.Desk;
import com.restaurant.entity.Hall;
import com.restaurant.repository.DeskRepository;
import com.restaurant.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QRService {

    private static final String KEY = "db7827cc775511e98f9e2a86e4085a59";

    @Autowired
    HallRepository hallRepository;

    @Autowired
    DeskRepository deskRepository;

    public List<BitMatrix> getBitMatrixList(int restaurantId) throws WriterException, NoSuchAlgorithmException {
        List<BitMatrix> bitMatrices = new ArrayList<>();
        for(Hall hall : hallRepository.findByRestaurantId(restaurantId)){
            for(Desk desk : deskRepository.findByHall(hall.getId())) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                bitMatrices.add(qrCodeWriter.encode(restaurantId + " " + hall.getId() + " " + desk.getId() + " " + getSSH1(restaurantId, hall.getId(), desk.getId()), BarcodeFormat.QR_CODE, 450, 450));
            }
        }
        return bitMatrices;
    }


    public boolean check_ssh(int restaurantId, long hallId, long deskId, String ssh1) throws NoSuchAlgorithmException {
        return getSSH1(restaurantId,hallId,deskId).equals(ssh1);
    }

    private String getSSH1(int restaurantId, long hallId, long deskId) throws NoSuchAlgorithmException {
        MessageDigest msg = MessageDigest.getInstance("SHA-1");
        String str = restaurantId+hallId+deskId+KEY;
        msg.update(str.getBytes(StandardCharsets.UTF_8), 0, str.length());
        return DatatypeConverter.printHexBinary(msg.digest());
    }
}
