
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{

        LZ77 lz77 = new LZ77();

        ArrayList<Tag> Tags = lz77.CompressData("AABBXXCC");

        String Original = lz77.DeCompressData(Tags);

    }

}
