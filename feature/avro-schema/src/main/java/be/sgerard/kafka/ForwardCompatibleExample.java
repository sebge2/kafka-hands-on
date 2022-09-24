package be.sgerard.kafka;

import be.sgerard.kafkahandson.CustomerV1;
import be.sgerard.kafkahandson.CustomerV2;
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

public class ForwardCompatibleExample {

    /**
     * Write and read data using Avro, data is written using schema V2 and read using schema V1.
     */
    public static void main(String[] args) throws IOException {
        final CustomerV2 johnSmith = CustomerV2.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(42)
                .setWeight(40.5f)
                .setAutomatedEmail(true)
                .build();

        final File file = new File("./target/customer1.avro");

        writeToFile(file, johnSmith);

        final List<CustomerV1> readCustomers = readFromFile(file);

        System.out.println("\nThe data is:\n" + readCustomers);

        System.out.println("\nThe schema is:\n" + ReflectData.get().getSchema(CustomerV1.class).toString(true));
    }

    private static void writeToFile(File file, CustomerV2... customers) {
        final DatumWriter<CustomerV2> datumWriter = new SpecificDatumWriter<>(CustomerV2.class);
        try (DataFileWriter<CustomerV2> fileWriter = new DataFileWriter<>(datumWriter)) {
            fileWriter.create(CustomerV2.SCHEMA$, file);

            for (CustomerV2 record : customers) {
                fileWriter.append(record);
            }

            System.out.println("Record written " + Arrays.toString(customers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<CustomerV1> readFromFile(File file) throws IOException {
        final List<CustomerV1> records = new ArrayList<>();

        final DatumReader<CustomerV1> datumReader = new SpecificDatumReader<>(CustomerV1.SCHEMA$);
        try (DataFileReader<CustomerV1> fileReader = new DataFileReader<>(file, datumReader)) {
            while (fileReader.hasNext()) {
                records.add(fileReader.next());
            }
        }

        return records;
    }
}
