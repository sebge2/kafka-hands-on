package be.sgerard.kafka;

import be.sgerard.kafkahandson.Customer;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratedRecordExample {

    /**
     * Write and read data using Avro, data is initialized in Java POJO generated with an Avro schema file.
     * In this case we have the Avro schema in a file.
     */
    public static void main(String[] args) throws IOException {
        final Customer johnSmith = Customer.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setLastLogin(0L)
                .setAdAllowed(true)
                .build();

        final File file = new File("./target/customer.avro");

        writeToFile(file, johnSmith);

        final List<Customer> readCustomers = readFromFile(file);

        System.out.println("\nThe data is:\n" + readCustomers);

        System.out.println("\nThe schema is:\n" + ReflectData.get().getSchema(Customer.class).toString(true));
    }

    private static void writeToFile(File file, Customer... customers) {
        final DatumWriter<Customer> datumWriter = new SpecificDatumWriter<>(Customer.class);
        try (DataFileWriter<Customer> fileWriter = new DataFileWriter<>(datumWriter)) {
            fileWriter.create(Customer.SCHEMA$, file);

            for (Customer record : customers) {
                fileWriter.append(record);
            }

            System.out.println("Record written " + Arrays.toString(customers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Customer> readFromFile(File file) throws IOException {
        final List<Customer> records = new ArrayList<>();

        final DatumReader<Customer> datumReader = new SpecificDatumReader<>(Customer.SCHEMA$);
        try (DataFileReader<Customer> fileReader = new DataFileReader<>(file, datumReader)) {
            while (fileReader.hasNext()) {
                records.add(fileReader.next());
            }
        }

        return records;
    }
}
