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

public class BackwardCompatibleExample {

    /**
     * Write and read data using Avro, data is written using schema V1 and read using schema V2.
     */
    public static void main(String[] args) throws IOException {
        final CustomerV1 johnSmith = CustomerV1.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(42)
                .setWeight(40.5f)
                .setEmail("john.doe@acme.com")
                .setPhoneNumber("+1 345 9078908")
                .build();

        final File file = new File("./target/customer1.avro");

        writeToFile(file, johnSmith);

        final List<CustomerV2> readCustomers = readFromFile(file);

        System.out.println("\nThe data is:\n" + readCustomers);

        System.out.println("\nThe schema is:\n" + ReflectData.get().getSchema(CustomerV2.class).toString(true));
    }

    private static void writeToFile(File file, CustomerV1... customers) {
        final DatumWriter<CustomerV1> datumWriter = new SpecificDatumWriter<>(CustomerV1.class);
        try (DataFileWriter<CustomerV1> fileWriter = new DataFileWriter<>(datumWriter)) {
            fileWriter.create(CustomerV1.SCHEMA$, file);

            for (CustomerV1 record : customers) {
                fileWriter.append(record);
            }

            System.out.println("Record written " + Arrays.toString(customers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<CustomerV2> readFromFile(File file) throws IOException {
        final List<CustomerV2> records = new ArrayList<>();

        final DatumReader<CustomerV2> datumReader = new SpecificDatumReader<>(CustomerV2.SCHEMA$);
        try (DataFileReader<CustomerV2> fileReader = new DataFileReader<>(file, datumReader)) {
            while (fileReader.hasNext()) {
                records.add(fileReader.next());
            }
        }

        return records;
    }
}
