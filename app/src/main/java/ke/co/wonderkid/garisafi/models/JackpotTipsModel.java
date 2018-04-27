package ke.co.wonderkid.garisafi.models;

/**
 * Created by Beni on 4/2/2017.
 */

public class JackpotTipsModel {
    public JackpotTipsModel(String team1, String team2, String Odd, String play_time) {

        this.setTeam1(team1);
        this.setTeam2(team2);
        this.setOdd(Odd);
        this.setPlay_time(play_time);


    }

    String team1;

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getOdd() {
        return Odd;
    }

    public String getPlay_time() {
        return play_time;
    }

    String team2;
    String Odd;
    String play_time;



    public void setTeam1(String team1) {
        this.team1 = team1;
    }


    public void setTeam2(String team2) {
        this.team2 = team2;
    }


    public void setOdd(String odd) {
        Odd = odd;
    }


    public void setPlay_time(String play_time) {
        this.play_time = play_time;
    }

}
