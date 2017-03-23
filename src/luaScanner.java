import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynx on 3/23/17.
 */
public class luaScanner {

    public luaScanner()
    {
    }

    public void processFile(String fileName) {
        List<String> stringList = new ArrayList<String>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
                if(slices[j].startsWith("(") && slices[j].length() > 1)
                {
                    System.out.println(slices[j].substring(0,1)+" : Open Parentheses");
                    System.out.println(slices[j].substring(1) + " : " +getType(slices[j].substring(1)));
                }
                else if(slices[j].endsWith(")"))
                {
                    if(getType(slices[j]).equals("Function ID"))
                    {

                    }
                    else
                    {
                        System.out.println(slices[j].substring(0,1) + " : " +getType(slices[j].substring(0,1)));
                        System.out.println(slices[j].substring(slices[j].length()-1) + ": Close Parentheses");
                    }

                }
                else
                {
                    System.out.println(slices[j] + " : " +getType(slices[j]));
                }

            }
        }
    }

    public String getType(String input)
    {
        if(input.matches("[A-Za-z]\\(\\)"))
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
        else if(input.equals("("))
        {
            return "Open Parentheses";
        }

        else if(input.equals(")"))
        {
            return "Close Parentheses";
        }

        else if(input.equals("print"))
        {
            return "Print Statement";
        }

        else if(input.equals("if"))
        {
            return "If Statement";
        }

        else if(input.equals("then"))
        {
            return "Then Statement";
        }
        else if(input.equals("else"))
        {
            return "Else Statement";
        }
        else if(input.equals("while"))
        {
            return "While Statement";
        }
        else if(input.equals("do"))
        {
            return "Do Statement";
        }
        else if(input.equals("repeat"))
        {
            return "Repeat Statement";
        }
        else if(input.equals("until"))
        {
            return "Until Statement";
        }
        return "Unknown Token";
    }
}
