
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LZ77 {


    public ArrayList<Tag> CompressData(String Data) throws FileNotFoundException, IOException{

        ArrayList<Tag> Tags = new ArrayList<>();

        String SearchBuffer = "";
        for(int i=0; i<Data.length(); ++i){

            String Current = "";
            int j = i;

            while(j < Data.length() && SearchBuffer.contains(Current)){
                Current += Data.charAt(j++);
            }

            int Position, Length;
            char Next;

            if(SearchBuffer.contains(Current)){

                Position = i - SearchBuffer.lastIndexOf(Current);
                Length = Current.length();
                Next = '\0';

                Tags.add(new Tag(Position, Length, Next));

            } else {

                Position = i - SearchBuffer.lastIndexOf(Current.substring(0, Current.length()-1));
                Length = Current.length()-1;
                Next = Current.charAt(Current.length()-1);

                Tags.add(new Tag(Position, Length, Next));

                i = j - 1;
                SearchBuffer += Current;
            }

        }

        return Tags;

    }


    public String DeCompressData(ArrayList<Tag> Tags) throws IOException{



        String Original = "";

        for(Tag t: Tags){

            int Position = t.Position;
            int Length = t.Length;
            char Next = t.Next;

            Original += Original.substring(Position, Position+Length) + Next;

        }

        return Original;

    }

}
