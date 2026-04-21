import java.io.*;

public class ReportManager {

    public void saveReport(DisasterEvent disaster) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("reports.txt", true)
            );

            writer.write(disaster.getReport());
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving report");
        }
    }

    public void readReports() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("reports.txt")
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading reports");
        }
    }
}