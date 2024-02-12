import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    static final int MONTHS_IN_YEAR = 12;
    static final int PERCENT = 100;


    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        CsvWriter csvWriter;

        try {
            // Instantiem resursele necesare
            FileWriter writer = new FileWriter(FileProvider.getFile());
            csvWriter = new CsvWriter(writer);

            // Scrie header-ul tabelului
            csvWriter.writeHeader();
        } catch (IOException e) {
            System.out.println("Some error occurred when initializing the CsvWriter: " + e.getMessage());
            return;
        }

        System.out.println("Please enter the amount: ");

        int amount;
        int period;
        double interestRate;


        try{
            amount = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println("The amount is mandatory to be numeric!");
            return;
        }

        System.out.println("Please enter the loan period in years: ");


        try{
            period = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println("The period is mandatory to be numeric!");
            return;
        }

        System.out.println("Please enter the annual interest rate");


        try{
            interestRate = Double.parseDouble(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println("The period is mandatory to be numeric!");
            return;
        }


        double balance = amount;
        for(int month=1; month<=period * MONTHS_IN_YEAR; month++){
            double lastMonthBalance = balance;
            double monthlyMortgage = calculateMortgage(amount, period, interestRate);
            double monthlyInterest = calculateInterest(lastMonthBalance, interestRate);
            double paidAmount = monthlyMortgage - monthlyInterest;

            balance = (lastMonthBalance - paidAmount) < 0 ? 0 : (lastMonthBalance - paidAmount);

            try {
                csvWriter.writeRecord(month, monthlyMortgage, balance, monthlyInterest, paidAmount);
            } catch (IOException e) {
                System.out.println("Error while writing the csv file: " + e.getMessage());
            }
        }

        try{
            csvWriter.closeFile();
        }catch (IOException e){
            System.out.println("Something went wrong when trying to close the csv file: " + e.getMessage());
        }
    }


    private static double calculateMortgage(int amount, int period, double interestRate){
        double monthlyRate = interestRate / PERCENT / MONTHS_IN_YEAR;
        return (monthlyRate * amount) / (1 - Math.pow(1 + monthlyRate, (-period * MONTHS_IN_YEAR)));
    }


    private static double calculateInterest(double balance, double interestRate){
        double interestPerYear = balance * interestRate / PERCENT;
        return interestPerYear / MONTHS_IN_YEAR;
    }
}