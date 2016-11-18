package segmentgenerator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SegmentGenerator {

    private final static int SEGMENT_SIZE = 1000;
    
    private final static String[] header = {
        "First Name", "Last Name", "Middle Name", "Address Line 1", "City", "State", "Country", "Zip Code", "Email", "Ok To Contact", "Sales Account Name", "Role", "storeNumber", "okToEmail"
    };
    
    private final static List<String> data = new ArrayList<>(SEGMENT_SIZE + 1);
    
    private final static String[] states = { "California", "Oregon", "Washington", "Missouri", "Texas", "Arizona", "New York", "Illinois", "Ohio", "Nevada"  };
    
    private static void writeOut(PrintStream out) {
        for (String line : data) {
            out.println(line);
        }
    }
    
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        sb.append(header[0]); 
        do {
            sb.append(',');
            sb.append(header[i++]);
        } while (i < header.length);
        data.add(sb.toString());
        for (i = 0; i < SEGMENT_SIZE; i++) {
            sb = new StringBuilder();
            sb.append("fname").append(i).append(',');
            sb.append("lname").append(i).append(',');
            sb.append("mname").append(i).append(',');
            sb.append("addr").append(i).append(',');
            sb.append("city").append(i).append(',');
            sb.append(states[RandUtils.uniform(states.length)]).append(',');
            sb.append("United States").append(',');
            sb.append(RandUtils.uniform(20200, 98720)).append(',');
            sb.append("email").append(i).append("@thingy.com").append(',');
            sb.append(RandUtils.uniformBool() ? 'Y' : 'N').append(',');
            sb.append("salesacc").append(i).append(',');
            sb.append("role").append(i).append(',');
            sb.append("R00").append(i).append(',');
            sb.append(RandUtils.uniformBool() ? 'Y' : 'N');
            data.add(sb.toString());
        }
        writeOut(System.out);
    }

    
    
}
