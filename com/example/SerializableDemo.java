package com.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializableDemo implements Serializable {
    private final String value;
    private final SubClass subClass;

    public SerializableDemo(String value, SubClass subClass) {
        this.value = value;
        this.subClass = subClass;
    }

    public String getValue() {
        return this.value;
    }

    public SubClass getSubClass() {
        return this.subClass;
    }

    public static void main(String[] args) {
        SerializableDemo toSerialize = new SerializableDemo("initial value", new SubClass(123));
        String fileName = "test.txt";

        // serialize
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(toSerialize);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deserialize
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SerializableDemo serializableDemo = (SerializableDemo) ois.readObject();
            ois.close();
            fis.close();

            // result
            System.out.println("=====[DESERIALIZED]=====");
            System.out.println("serializableDemo.getValue(): " + serializableDemo.getValue());
            System.out
                    .println("serializableDemo.getSubClass().getValue(): " + serializableDemo.getSubClass().getValue());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // without writing to file
        try {
            // serialize
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(toSerialize);
            String str = Base64.getEncoder().encodeToString(bos.toByteArray());
            oos.close();

            // deserialize
            byte[] bytes = Base64.getDecoder().decode(str);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            SerializableDemo serializableDemo = (SerializableDemo) ois.readObject();
            ois.close();

            // test
            System.out.println();
            System.out.println("=====[DESERIALIZED WITHOUT WRITING TO FILE]====");
            System.out.println("serializableDemo.getValue(): " + serializableDemo.getValue());
            System.out
                    .println("serializableDemo.getSubClass().getValue(): " + serializableDemo.getSubClass().getValue());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class SubClass implements Serializable { // <-- needs to implement here as well ***
    private Integer value;

    public SubClass(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
