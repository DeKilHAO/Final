import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TeamList {
    ArrayList<Team> teamList;

    public TeamList() {
        teamList=new ArrayList<Team>();
    }

    public ArrayList<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(ArrayList<Team> teamList) {
        this.teamList = teamList;
    }

    public boolean existTeam(String teamName)
    {
        for (Team t:teamList)
        {
            if (t.TeamName.equals(teamName))
                return true;
        }
            return false;
    }

    public Team findTeam(String teamName)
    {
        for (Team t:teamList)
        {
            if (t.TeamName.equals(teamName))
                return t;
        }
        return null;
    }
    public void sort()
    {
        Collections.sort(teamList, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                if (o1.totalpoint-o2.totalpoint!=0)
                    return o2.totalpoint-o1.totalpoint;
                else
                    return o2.totalWin-o1.totalWin;
            }
        });
    }
}
