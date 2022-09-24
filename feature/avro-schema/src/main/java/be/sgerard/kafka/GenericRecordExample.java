package be.sgerard.kafka;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenericRecordExample {

    /**
     * Write and read data using Avro, data is initialized in Java with a generic
     * record (i.e., not a specific Java POJO). In this case we have the Avro schema in a file.
     */
    public static void main(String[] args) throws IOException {
        final Schema.Parser parser = new Schema.Parser();

        final Schema schema = parser.parse(GenericRecordExample.class.getResourceAsStream("/avro/customer.avsc"));


        final GenericRecord johnSmith = new GenericRecordBuilder(schema)
                .set("firstName", "John")
                .set("lastName", "Smith")
                .set("adAllowed", true)
                .set("lastLogin", 0L)
                .build();

        final File file = new File("./target/customer.avro");

        writeToFile(schema, file, johnSmith);
        final List<GenericRecord> readRecords = readFromFile(file);

        System.out.println("Records read:\n" +readRecords);
    }

    private static void writeToFile(Schema schema, File file, GenericRecord... records) {
        final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try (DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(datumWriter)) {
            fileWriter.create(schema, file);

            for (GenericRecord record : records) {
                fileWriter.append(record);
            }

            System.out.println("Record written:\n" + records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<GenericRecord> readFromFile(File file) throws IOException {
        final List<GenericRecord> records = new ArrayList<>();

        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try(DataFileReader<GenericRecord> fileReader = new DataFileReader<>(file, datumReader)){
            while (fileReader.hasNext()){
                records.add(fileReader.next());
            }
        }

        return records;
    }
}
