package me.Math0424.Withered.Inventory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ClockMaker {

    public static void main(String[] args) {
        int current = 0;
        int toMake = 63;

        for (int i = 0; i < toMake; ++i) {

            String string = "{ \"parent\": \"item/generated\", \"textures\": { \"layer0\": \"item/clock_" + current + "\" } }";

            File f = new File("clock_" + current + ".json");

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(f));
                writer.write(string);

                writer.close();
            } catch (Exception e) {
            }

            current++;

        }

    }

}
