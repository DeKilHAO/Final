import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class RankingSystem {
    static TeamList teamList;
    Random random = new Random();
    String WritefilePath1;
    String WritefilePath2;
    String Path;

    public RankingSystem() {
        this.teamList=new TeamList();
        this.Path=Paths.get("E0.csv").toAbsolutePath().toString();
        WritefilePath1 =Path.substring(0,Path.length()-6)+"output.csv";
        WritefilePath2=Path.substring(0,Path.length()-6)+"finalrank.csv";
        clearcsv(WritefilePath1);
        clearcsv(WritefilePath2);
    }

    public void init() throws IOException
    {
        try {
            BufferedReader reader=new BufferedReader(new FileReader(Path));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String info[] = line.split(",");
                if(teamList.existTeam(info[0]))
                    continue;
                Team team=new Team(info[0]);
                teamList.getTeamList().add(team);
            }
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //initial Rank after import the file
    public void rank() throws IOException
    {
        try {
            BufferedReader reader=new BufferedReader(new FileReader(Path));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String info[] = line.split(",");
                Team t1=teamList.findTeam(info[0]);
                Team t2=teamList.findTeam(info[1]);
                if (info[4].equals("H"))
                {
                    t1.totalpoint+=3;
                }
                else if (info[4].equals("A"))
                {
                    t2.totalpoint+=3;
                }
                else
                {
                    t1.totalpoint+=1;
                    t2.totalpoint+=1;
                }
                t1.totalGoalDifference +=Integer.valueOf(info[2])-Integer.valueOf(info[3]);
                t2.totalGoalDifference +=Integer.valueOf(info[3])-Integer.valueOf(info[2]);
                
                ArrayList<Integer> t1GoalDifference = new ArrayList<Integer>();
                ArrayList<Integer> t2GoalDifference = new ArrayList<Integer>();
                
                if(!t1.matches.containsKey(t2)) {
                    t1GoalDifference.add(Integer.valueOf(info[2])-Integer.valueOf(info[3]));
                    t2GoalDifference.add(Integer.valueOf(info[3])-Integer.valueOf(info[2]));
                    
                    t1.matches.put(t2, t1GoalDifference);
                	t2.matches.put(t1, t2GoalDifference);
                }else {
                	t1.matches.get(t2).add(Integer.valueOf(info[2])-Integer.valueOf(info[3]));
                	t2.matches.get(t1).add(Integer.valueOf(info[3])-Integer.valueOf(info[2]));
                }
            }
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        teamList.sort();
        System.out.println("Rank-Team-Points-Total Goal Difference");
        for(int i=0;i<teamList.getTeamList().size();i++)
	    	{
	    		System.out.println(i+1+". "+teamList.getTeamList().get(i).TeamName+"  "+teamList.getTeamList().get(i).totalpoint+"  "+teamList.getTeamList().get(i).totalGoalDifference);
	    	}
    }
    
    public void simulation() throws IOException {
    	rank();
        writecsv(WritefilePath1,"Team1,Team2,Goal Difference");
        writecsv(WritefilePath2,"Rank,Team Name,Total point,Total Goal Difference");
    	for(Team team : teamList.getTeamList()) {
    		for(Team opponentTeam : team.matches.keySet()) {
    			ArrayList<Integer> matchResult = new ArrayList<Integer>();
    			matchResult = team.matches.get(opponentTeam);
                //System.out.println(team.TeamName+" "+opponentTeam.TeamName+" "+matchResult.size());
    			while(matchResult.size() < 2) {
    				double totalScore = 0;
    				for(int i = 0; i<matchResult.size(); i++) {
    					totalScore += matchResult.get(i);
    				}
    				double averageScore = totalScore/matchResult.size();

    				int simulationScore = normalDistributionFunction(averageScore);
    				
    				String output=team.TeamName+","+opponentTeam.TeamName+","+simulationScore;
    				writecsv(WritefilePath1,output);
    				if(simulationScore > 0) {
    					team.totalpoint += 3;
    				}else if(simulationScore < 0) {
    					opponentTeam.totalpoint += 3;
    				}else{
    					team.totalpoint += 1;
    					opponentTeam.totalpoint += 1;
    				}
    				
    				team.totalGoalDifference += simulationScore;
    				opponentTeam.totalGoalDifference -= simulationScore;
    				
    				team.matches.get(opponentTeam).add(simulationScore);
    				opponentTeam.matches.get(team).add(-simulationScore);
    			}
    		}
    	}
    	teamList.sort();
        System.out.println("Rank-Team-Points-Total Goal Difference");
        for(int i=0;i<teamList.getTeamList().size();i++)
	    	{
	    		System.out.println(i+1+". "+teamList.getTeamList().get(i).TeamName+"  "+teamList.getTeamList().get(i).totalpoint+"  "+teamList.getTeamList().get(i).totalGoalDifference);
	    		String line=i+1+","+teamList.getTeamList().get(i).TeamName+","+teamList.getTeamList().get(i).totalpoint+","+teamList.getTeamList().get(i).totalGoalDifference;
	    		writecsv(WritefilePath2,line);
	    	}
    }
    
    public int normalDistributionFunction(double averageScore) {
    	int simulationScore = 0;
    	simulationScore = Integer.parseInt(String.valueOf(Math.round(random.nextGaussian()* Math.sqrt(1)+averageScore)));
    	
    	return simulationScore;
    }

    public void writecsv(String filePath, String line) throws IOException
    {
        try {
            FileWriter writer=new FileWriter(filePath,true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Not found");
        }catch (IOException e){
            System.out.println("write wrong");
        }
    }

    public void clearcsv(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
