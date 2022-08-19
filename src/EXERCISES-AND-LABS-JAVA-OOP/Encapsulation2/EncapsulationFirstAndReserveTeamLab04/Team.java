package EncapsulationFirstAndReserveTeamLab04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private String name;
    private List <EncapsulationFirstAndReserveTeamLab04.Person> firstTeam;
    private List <EncapsulationFirstAndReserveTeamLab04.Person> reserveTeam;

    public Team(String name) {
        this.name = name;
        this.firstTeam = new ArrayList<>();
        this.reserveTeam = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Person> getFirstTeam() {
        return Collections.unmodifiableList(firstTeam);
    }

    public List<Person> getReserveTeam() {
        return Collections.unmodifiableList(reserveTeam);
    }

    public void addPlayer (Person person){
        if (person.getAge() < 40){
            firstTeam.add(person);
        }else {
            reserveTeam.add(person);
        }

    }
}

