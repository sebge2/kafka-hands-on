package be.sgerard.kafka;

import be.sgerard.kafkahandson.Customer;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectedExample {

    /**
     * Write and read data. The Avro schema will be reflected (inferred) from an existing Java POJO.
     */
    public static void main(String[] args) throws IOException {
        final Customer johnSmith = Customer.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setLastLogin(0L)
                .setAdAllowed(true)
                .build();

        final File file = new File("./target/customer-reflected.avro");

        final Schema schema = ReflectData.get().getSchema(Customer.class);
        System.out.println("Reflected/Inferred schema:\n" + schema.toString(true));

        writeToFile(schema, file, johnSmith);
        final List<Customer> readCustomers = readFromFile(file);

        System.out.println("\nThe data is:\n" + readCustomers);

        System.out.println("\nThe schema is:\n" + ReflectData.get().getSchema(Customer.class).toString(true));
    }

    private static void writeToFile(Schema schema, File file, Customer... customers) {
        final DatumWriter<Customer> datumWriter = new ReflectDatumWriter<>(schema);
        try {
            try (DataFileWriter<Customer> fileWriter = new DataFileWriter<>(datumWriter)) {
                fileWriter.create(schema, file);

                for (Customer record : customers) {
                    fileWriter.append(record);
                }

                System.out.println("Record written:\n" + Arrays.toString(customers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Customer> readFromFile(File file) throws IOException {
        final List<Customer> records = new ArrayList<>();

        final DatumReader<Customer> datumReader = new ReflectDatumReader<>(Customer.class);
        try (DataFileReader<Customer> fileReader = new DataFileReader<>(file, datumReader)) {
            while (fileReader.hasNext()) {
                records.add(fileReader.next());
            }
        }

        return records;
    }

}
