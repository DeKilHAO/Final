import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class test {
    @Test
    public void Test() throws IOException {
        RankingSystem rs=new RankingSystem();
        rs.init();
        rs.simulation();
    }
    
    public static void main(String[] args)  
	{
    	RankingSystem rs=new RankingSystem();
        try {
			rs.init();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			rs.simulation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
