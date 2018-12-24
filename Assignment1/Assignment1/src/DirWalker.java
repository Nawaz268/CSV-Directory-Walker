/*
 *This code uses the librarys given by Mr. Daniel Perry 
 * Used the code for printWriter from the following link : https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/
 * 
 * 
 * */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class DirWalker {

	// GLOBAL VARIABLES

	String fileName = "";
	private static final String OutputCSV = "/Users/nawazhussain/Desktop/A00428036_MCDA5510/Assignment1/Output/Output.csv";

	static int skippedRow = 0;
	static int validRow = 0;

	public void walk(String path, CSVPrinter csvPrinter) throws IOException {

		System.setProperty("java.util.logging.config.file", "./logging.properties");

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath(), csvPrinter);
				Logger.getAnonymousLogger().log(Level.INFO, "Dir:" + f.getAbsoluteFile());
				// System.out.println( "Dir:" + f.getAbsoluteFile() );
			} else {
				// System.out.println( "File:" + f.getAbsoluteFile() );
				fileName = f.getAbsoluteFile().toString();
				Logger.getLogger("Main").log(Level.INFO, fileName);
				if (!fileName.endsWith(".csv")) {
					continue;
				}

				// System.out.println(fileName + "\n");

				String line = null;

				try {
					// FileReader reads text files in the default encoding.
					FileReader fileReader = new FileReader(fileName);

					// Always wrap FileReader in BufferedReader.
					BufferedReader bufferedReader = new BufferedReader(fileReader);

					while ((line = bufferedReader.readLine()) != null) {
						// System.out.println(line);
					}

					// Always close files.
					bufferedReader.close();
				} catch (FileNotFoundException ex) {
					Logger.getLogger("Main").log(Level.SEVERE, "Unable to open file '" + fileName + "'");
					// System.out.println("Unable to open file '" + fileName + "'");

				} catch (IOException ex) {
					Logger.getLogger("Main").log(Level.SEVERE, "Error reading file '" + fileName + "'");
					// System.out.println("Error reading file '" + fileName + "'");
					// Or we could just do this:
					// ex.printStackTrace();
				}

				String[] dateString = f.getPath().toString().split(File.separator);
				String date = (dateString[dateString.length - 4]) + "/"
						+ (dateString[dateString.length - 3].length() > 1 ? dateString[dateString.length - 3]
								: "0" + dateString[dateString.length - 3])
						+ "/" + (dateString[dateString.length - 2].length() > 1 ? dateString[dateString.length - 2]
								: "0" + dateString[dateString.length - 2]);

				System.out.println("Date : " + date + "\n");

				// Start of CSV Parser

				Reader in;
				try {
					// System.out.println(fileName);
					in = new FileReader(fileName);
					Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
					for (CSVRecord record : records) {

						if (record.getRecordNumber() == 1)
							continue; // Skip the Headers

						if (record.size() != 10) // THIS IS IF THERE ARE SKIPPED FIELDS CONCURRENTLY IN A ROW
						{
							skippedRow++;
							continue;

						}

						String FirstName = record.get(0);
						String LastName = record.get(1);
						String StreetNumber = record.get(2);
						String Street = record.get(3);
						String City = record.get(4);
						String Province = record.get(5);
						String PostalCode = record.get(6);
						String Country = record.get(7);
						String PhoneNumber = record.get(8);
						String EmailAddress = record.get(9);

						if (FirstName.length() == 0 || LastName.length() == 0 || StreetNumber.length() == 0
								|| Street.length() == 0 || City.length() == 0 || Province.length() == 0
								|| PostalCode.length() == 0 || Country.length() == 0 || PhoneNumber.length() == 0
								|| EmailAddress.length() == 0) {

							skippedRow++;
						}

						else {

							csvPrinter.printRecord(FirstName, LastName, StreetNumber, Street, City, Province,
									PostalCode, Country, PhoneNumber, EmailAddress, date);

							validRow++;
							csvPrinter.flush();
						}

					}

				} catch (IOException e) {
					e.printStackTrace();
					Logger.getAnonymousLogger().log(Level.SEVERE, e.getLocalizedMessage().toString());
					e.printStackTrace();

				}

			}
		}
	}

	public static void main(String[] args) throws IOException {

		final long startTime = System.currentTimeMillis(); // START TIMER

		DirWalker fw = new DirWalker();

		BufferedWriter writer;
		writer = Files.newBufferedWriter(Paths.get(OutputCSV));

		try {
			CSVPrinter csvPrinter = new CSVPrinter(writer,
					CSVFormat.DEFAULT.withHeader("First Name", "Last Name", "Street Number", "Street", "City",
							"Province", "Postal Code", "Country", "Phone Number", "Email Address", "Date"));

			fw.walk("/Users/nawazhussain/Desktop/A00428036_MCDA5510" + "/Assignment1/SampleData", csvPrinter);

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getLocalizedMessage().toString());
		}
		System.out.println("Skipped Rows : " + skippedRow + " Valid Rows : " + validRow);

		Logger.getLogger("Main").log(Level.INFO, "Skipped Rows : " + skippedRow + " Valid Rows : " + validRow);

		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + " ms"); // END TIMER
		Logger.getLogger("Main").log(Level.INFO, "Total execution time: " + (endTime - startTime) + " ms");

	}

}