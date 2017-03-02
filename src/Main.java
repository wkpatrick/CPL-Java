import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<String>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("test.lua"));
            while((line = reader.readLine()) != null)
            {
                stringList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < stringList.size(); i++)
        {
            String[] slices = stringList.get(i).split(" ");
            for(int j = 0; j < slices.length; j++)
            {
                System.out.println(slices[j] + " : " +getType(slices[j]));

            }
        }
    }

    public static String getType(String input)
    {
        if(input.contains("()"))
        {
            return "Function ID";
        }
        else if(input.equals("function"))
        {
            return "Function Keyword";
        }
        else if(input.equals("end"))
        {
            return "End Keyword";
        }
        else if(input.equals("="))
        {
            return "Assignment Operator";
        }
        else if(input.equals("<="))
        {
            return "LE Operator";
        }
        else if(input.equals("<"))
        {
            return "LT Operator";
        }
        else if(input.equals(">="))
        {
            return "GE Operator";
        }
        else if(input.equals(">"))
        {
            return "GT Operator";
        }
        else if(input.equals("=="))
        {
            return "EQ Operator";
        }
        else if(input.equals("~="))
        {
            return "NE Operator";
        }
        else if(input.equals("+"))
        {
            return "Add Operator";
        }
        else if(input.equals("-"))
        {
            return "Sub Operator";
        }
        else if(input.equals("*"))
        {
            return "Mult Operator";
        }
        else if(input.equals("/"))
        {
            return "Div Operator";
        }
        else if(input.matches("\\d+"))
        {
            return "Int Literal";
        }
        else if(input.matches("[A-Za-z]"))
        {
            return "String literal";
        }
        return "Test";
    }
}
