import java.util.ArrayList;
import java.util.HashMap;

public class Team  {
    String TeamName;
    int totalGoalDifference;//Goal difference.
    int totalpoint;//3 points if win, 1 point if duel, 0 point if lose.
    HashMap<Team, ArrayList<Integer>> matches;

    public Team(String teamName) {
        this.TeamName = teamName;
        this.totalGoalDifference =0;
        this.totalpoint=0;
        matches=new HashMap<Team, ArrayList<Integer>>();
    }

}
